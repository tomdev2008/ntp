package cn.me.xdf.action.passThrough;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.hibernate4.Value;
import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.page.SimplePage;
import cn.me.xdf.common.utils.array.ArrayUtils;
import cn.me.xdf.common.utils.array.SortType;
import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseContent;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.message.Message;
import cn.me.xdf.model.message.MessageReply;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.SysOrgPersonService;
import cn.me.xdf.service.bam.BamCourseService;
import cn.me.xdf.service.bam.BamMaterialService;
import cn.me.xdf.service.bam.process.SourceNodeService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.course.CourseParticipateAuthService;
import cn.me.xdf.service.course.CourseService;
import cn.me.xdf.service.log.LogLoginService;
import cn.me.xdf.service.log.LogOnlineService;
import cn.me.xdf.service.material.ExamQuestionService;
import cn.me.xdf.service.material.MaterialService;
import cn.me.xdf.service.message.MessageReplyService;
import cn.me.xdf.service.message.MessageService;
import cn.me.xdf.service.studyTack.StudyTrackService;
import cn.me.xdf.utils.DateUtil;
import cn.me.xdf.utils.ShiroUtils;


@Controller
@RequestMapping(value = "/ajax/passThrough")
@Scope("request")
public class PassThroughAjaxController {
	
	@Autowired
	private AttMainService attMainService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private BamCourseService bamCourseService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private BamMaterialService bamMaterialService;
	
	@Autowired
	private SourceNodeService sourceNodeService;
	
	@Autowired
	private MaterialService materialService;

	@Autowired
	private ExamQuestionService examQuestionService;
	
	@Autowired
	private CourseParticipateAuthService courseParticipateAuthService;
	
	@Autowired
	private SysOrgPersonService sysOrgPersonService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private MessageReplyService messageReplyService;
	
	
	/**
	 * 检查当前课程 当前登录是否有权限进入
	 * @param courseId
	 * @return
	 */
	@RequestMapping(value = "checkCoursePwd/{courseId}")
	@ResponseBody
	public String checkCoursePwd(@PathVariable("courseId") String courseId){
		Map<String,String> map = new HashMap<String,String>();
		CourseInfo course = courseService.get(courseId);
		if(!course.getIsPublish()){
			if(StringUtil.isNotBlank(course.getFdPassword())){
				map.put("flag", "0");//代表是有密码的加密课程
			} else {
				boolean canStudy= courseParticipateAuthService
						.findCouseParticipateAuthById(courseId,ShiroUtils.getUser().getId());
				if(canStudy){//无权学习的
					map.put("flag", "0");//无权学习
				} else {
					map.put("flag", "1");//有权学习
				}
			}
		} else {
			map.put("flag", "1");//有权学习
		}
		
		return JsonUtils.writeObjectToJson(map);
	}
	
	@RequestMapping(value = "verifyPwd")
	@ResponseBody
	public String verifyPwd(HttpServletRequest request){
		String courseId = request.getParameter("courseId");
		String userPwd = request.getParameter("userPwd");
		Map<String,String> map = new HashMap<String,String>();
		CourseInfo course = courseService.get(courseId);
		if(StringUtil.isNotBlank(userPwd)&&userPwd.equals(course.getFdPassword())){
			map.put("flag", "1");
		}else{
			map.put("flag", "0");
		}
		return JsonUtils.writeObjectToJson(map);
	}
	
	
	/**
	 * 点击学习通过，更改节的学习状态
	 */
	@RequestMapping(value = "updateCatalogThrough")
	@ResponseBody
	public void updateCatalogThrough(HttpServletRequest request){
		String catalogId = request.getParameter("catalogId");
		String bamId = request.getParameter("bamId");
		BamCourse bamCourse = bamCourseService.get(BamCourse.class, bamId);
		bamCourseService.updateCatalogThrough(bamCourse, catalogId);
	}
	
	/**
	 * 最新课程列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getNewCourseList")
	@ResponseBody
	public String getNewCourseList(HttpServletRequest request) {
		List result = new ArrayList();
		Pagination page = courseService.discoverCourses(1,5);
		if(page.getTotalCount()>0){
			List list = page.getList();
			for(int i=0;i<list.size();i++){
				Map course = new HashMap();
				course = (Map)list.get(i);
				AttMain attMain = attMainService.getByModelIdAndModelName((String)course.get("FDID"), CourseInfo.class.getName());
				course.put("ATTID", attMain==null?"":attMain.getFdId());
				result.add(course);
			}
		}
		return JsonUtils.writeObjectToJson(result);
	}
	
	/**
	 * 正在学习课程的教师列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getPageByLeaningTeacher")
	@ResponseBody
	public String getPageByLeaningTeacher(HttpServletRequest request) {
		String pageNoStr = request.getParameter("pageNo");
		String courseId = request.getParameter("courseId");
		int pageNo;
		if (StringUtil.isNotBlank(pageNoStr)) {
			pageNo = Integer.parseInt(pageNoStr);
		} else {
			pageNo = 1;
		}
		List result = new ArrayList();
		Finder finder = Finder.create(" select preTeachId  from ixdf_ntp_bam_score b where b.courseId = :courseId order by dbms_random.value ");
		finder.setParam("courseId", courseId);
		Pagination page = bamCourseService.getPageBySql(finder, pageNo, 15);
		if(page.getTotalCount()>0){
			List list = page.getList();
			for(int i=0;i<list.size();i++){
				Map bam = (Map)list.get(i);
				SysOrgPerson person = accountService.load((String)bam.get("PRETEACHID"));
				Map people = new HashMap();
				people.put("id", person.getFdId());
				people.put("name", person.getRealName());
				people.put("photoUrl", person.getPoto());
				result.add(people);
			}
		}
		Map map = new HashMap();
		map.put("totalPage", page.getTotalPage());
		map.put("currentPage", pageNo);
		map.put("list", result);
		map.put("totalCount", page.getTotalCount());
		return JsonUtils.writeObjectToJson(map);
	}
	
	/**
	 * 根据课程学习进程获取章节目录树
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getBamCatalogTree")
	@ResponseBody
	public String getBamCatalogTree(HttpServletRequest request) {
		//获取进程ID
		String bamId = request.getParameter("bamId");
		Map map = new HashMap();
		if(StringUtil.isNotBlank(bamId)){
			BamCourse bamCourse = bamCourseService.get(BamCourse.class, bamId);
			if(bamCourse!=null && bamCourse.getCatalogs()!=null){
				Boolean isOrder=bamCourse.getCourseInfo().getIsOrder();
				List<CourseCatalog> catalogs = bamCourse.getCatalogs();
				if(catalogs!=null){
					ArrayUtils.sortListByProperty(catalogs, "fdTotalNo", SortType.HIGHT);
				}
				Map catalogMap = new HashMap();
				List<Map> chapter = new ArrayList();
				List<Map> lecture = new ArrayList();
				Boolean currentCatalog = true;//设置标识，记录上一节是否通过
				for(CourseCatalog catalog : catalogs){
					Map tmp = new HashMap();
					if(Constant.CATALOG_TYPE_CHAPTER==catalog.getFdType()){
						tmp.put("index", catalog.getFdTotalNo());
						tmp.put("num", catalog.getFdNo());
						tmp.put("name", catalog.getFdName());
						chapter.add(tmp);
					}else{
						tmp.put("id", catalog.getFdId());
						tmp.put("index", catalog.getFdTotalNo());
						tmp.put("num", catalog.getFdNo());
						tmp.put("type", catalog.getMaterialType());
						tmp.put("baseType", catalog.getFdMaterialType());
						tmp.put("name", catalog.getFdName());
						tmp.put("intro", catalog.getFdDescription());
						if(!isOrder){//无序
							tmp.put("fdStatus", "true");
						}else{//有序
							if(currentCatalog!=null){
								if(currentCatalog == true){
									tmp.put("fdStatus", "true");
								}else{
									tmp.put("fdStatus", "false");
								}
							}else{
								tmp.put("fdStatus", "false");
							}
						}
						if(catalog.getThrough()==null){
								tmp.put("status", "untreated");
						}else if(catalog.getThrough()==false){
								tmp.put("status", "doing");
						}else if(catalog.getThrough()==true){
								tmp.put("status", "pass");
						}
						currentCatalog = catalog.getThrough();
						lecture.add(tmp);
					}
				}
				catalogMap.put("chapter", chapter);
				catalogMap.put("lecture", lecture);
				catalogMap.put("isOrder", isOrder);
				map.put("sidenav", catalogMap);
			}
		}
		return JsonUtils.writeObjectToJson(map);
	}
	
	/**
	 * 根据课程学习进程获取章节目录树
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getCourseContent")
	@ResponseBody
	public String getCourseContent(WebRequest request) {
		//获取进程ID
		String bamId = request.getParameter("bamId");
		//获取节ID
		String catalogId = request.getParameter("catalogId");
        bamCourseService.updateCourseCatalogStartTime(bamId,catalogId);
		//获取节内容类型
		String sourceType = request.getParameter("fdMtype");
		///点击播放当前素材的id
		String materialId = request.getParameter("materialId");
		Map map = new HashMap();
		if(StringUtil.isNotBlank(bamId)){
			BamCourse bamCourse = bamCourseService.get(BamCourse.class, bamId);
			if(bamCourse!=null && bamCourse.getCatalogs()!=null){
				List<CourseCatalog> catalogs = bamCourse.getCatalogs();
				for(CourseCatalog catalog : catalogs){
					if(catalog.getFdId().equals(catalogId)){
						//设置节信息
						Map prenext=getCurrentCatalog(bamCourse,catalog);
						map.putAll(prenext);
						map.put("type", catalog.getMaterialType());
						if(catalog.getThrough()==null){
							map.put("status", "unfinish");
						}else if(catalog.getThrough()==false){
							map.put("status", "doing");
						}else if(catalog.getThrough()==true){
							map.put("status", "pass");
						}
						map.put("courseName", bamCourse.getCourseInfo().getFdTitle());
						map.put("lectureName", catalog.getFdName());
						map.put("lectureIntro", catalog.getFdDescription());
						map.put("num", catalog.getFdNo());
						map.put("isOptional", catalog.getFdPassCondition()!=null && catalog.getFdPassCondition()==0?true:false);
						//根据素材类型设置节中内容详细信息
						Map returnMap = (Map)bamMaterialService.findMaterialDetailInfo(sourceType, bamCourse, catalog, materialId);
						if(returnMap!=null){
							map.putAll(returnMap);
						}
						break;
					}
				}
			}
		}
		return JsonUtils.writeObjectToJson(map);
	}

	/**
	 * 根据测试id获取测试信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getSubInfoByMaterialId")
	@ResponseBody
	public String getSubInfoByMaterialId(WebRequest request) {
		String materialId = request.getParameter("materialId");
		String catalogId = request.getParameter("catalogId");
		String sourceType = request.getParameter("sourceType");
		String bamId = request.getParameter("bamId");
		BamCourse bamCourse = bamCourseService.get(BamCourse.class, bamId);
		MaterialInfo materialInfo = materialService.get(materialId); 
		//根据素材类型获取素材子表信息
		List listExam = (List)bamMaterialService.findSubInfoByMaterial(sourceType, request);
		List<CourseContent> courseContents = bamCourse.getCourseContents();
        if (courseContents != null){
        	for (CourseContent content : courseContents) {
	            if (content.getMaterial().getFdId().equals(materialId)) {
	            	materialInfo = content.getMaterial();
	            	break;
	            }
	        }
        }
		Map map = new HashMap();
		map.put("id", materialInfo.getFdId());
		map.put("name", materialInfo.getFdName());
		map.put("fullScore", materialService.getTotalSorce(materialId).get("totalscore"));
		map.put("examPaperTime", materialInfo.getFdStudyTime());
		map.put("examPaperIntro", materialInfo.getFdDescription());
		map.put("examPaperStatus", sourceNodeService.getStatus(materialInfo, catalogId, ShiroUtils.getUser().getId()));
		
		map.put("listExam", listExam);
		return JsonUtils.writeObjectToJson(map);
	}
	/**
	 *获取当前选中节的上一节点和下一节点,节点id,是否通过学习
	 * @param catalogs  节列表
	 * @param currentCatalog  当前节
	 * @return 当前节点的上下节点
	 */
		private Map getCurrentCatalog(BamCourse bamCourse,CourseCatalog currentCatalog){
			List<CourseCatalog> catalogs = bamCourse.getCatalogs();
			//分离章节集合中的节
			CourseCatalog prevCatalog=null;
			CourseCatalog nextCatalog=null;
			Map pn=new HashMap();
			if(currentCatalog.getFdNo()==1){//当前节是节1的情况
				pn.put("prevc", "0");
				pn.put("pstatus", false);
				nextCatalog=getpnCatalog(catalogs,currentCatalog.getFdNo()+1);
				if(nextCatalog!=null){//总节数>2节
					pn.put("nextc", nextCatalog.getFdId());
					if(bamCourse.getCourseInfo().getIsOrder()){
						if(null!=currentCatalog.getThrough()&&true==currentCatalog.getThrough()){
							pn.put("nstatus", true);
						}else{
							pn.put("nstatus", false);
						}
					}else{
						pn.put("nstatus", true);
					}
					pn.put("nextBaseType", nextCatalog.getFdMaterialType());
				}else{//不大于2 则当前节是节首也是节尾
					pn.put("nextc", "0");
					pn.put("nstatus", false);
				}
			}
			if(currentCatalog.getFdNo()>1){//节编号大于1说明有上一节点 
				prevCatalog=getpnCatalog(catalogs,currentCatalog.getFdNo()-1);
				pn.put("prevc", prevCatalog.getFdId());
				pn.put("pstatus", true);
				pn.put("prevBaseType", prevCatalog.getFdMaterialType());
				nextCatalog=getpnCatalog(catalogs,currentCatalog.getFdNo()+1);
				if(nextCatalog!=null){//当前节是节尾
					pn.put("nextc", nextCatalog.getFdId());
					if(bamCourse.getCourseInfo().getIsOrder()){
						if(null!=currentCatalog.getThrough()&&true==currentCatalog.getThrough()){
							pn.put("nstatus", true);
						}else{
							pn.put("nstatus", false);
						}
					}else{
						pn.put("nstatus", true);
					}
					pn.put("nextBaseType", nextCatalog.getFdMaterialType());
				}else{
					pn.put("nextc", "0");
					pn.put("nstatus", false);
				}
			}
			int currentCatalogCount=0;
			for(int i=0;i<catalogs.size();i++){
				if(catalogs.get(i).getFdType()==1){
					currentCatalogCount++;
				}
			}
			if(currentCatalog.getFdNo()==currentCatalogCount){
				pn.put("isLast", true);
			}else{
				pn.put("isLast", false);
			}
			return pn;
		}
		/**
		 * 根据节号抽去节
		 * @param catalogs 章节集合
		 * @param no       节号
		 * @return   节信息
		 */
		private CourseCatalog getpnCatalog(List<CourseCatalog> catalogs,Integer no){
			for(CourseCatalog courseCatalog:catalogs){
				if(Constant.CATALOG_TYPE_CHAPTER!=courseCatalog.getFdType()&&courseCatalog.getFdNo()==no){
					return courseCatalog;
				}
			}
			return null;
		}
		
		/**
		 * 根据bamId获取结业证书信息(当前用户)
		 * 
		 * 
		 */
		@RequestMapping(value = "getCourseOverByBamId")
		@ResponseBody
		public String getCourseOverByBamId(HttpServletRequest request){
			String bamId = request.getParameter("bamId");
			BamCourse bamCourse = bamCourseService.get(BamCourse.class, bamId);
			if(bamCourse.getThrough()==false){
				return "notThrough";
			}else{
				Map map = new HashMap();
				Map user = new HashMap();
				SysOrgPerson orgPerson = accountService.load(ShiroUtils.getUser().getId());
				user.put("name",orgPerson.getRealName());
				user.put("enName", orgPerson.getLoginName());
				user.put("imgUrl", orgPerson.getPoto());
				user.put("link", "#");
				CourseInfo courseInfo =((CourseInfo)courseService.get(bamCourse.getCourseId()));
				SysOrgPerson person = courseInfo.getCreator();
				map.put("courseName", courseInfo.getFdTitle() );
				map.put("passTime", DateUtil.convertDateToString(bamCourse.getEndDate()));
				map.put("issuer", (person.getHbmParent()==null?"":person.getHbmParent().getHbmParentOrg().getFdName())+" "+person.getDeptName());
				map.put("user", user);
				List<CourseCatalog> catalogs = bamCourse.getCatalogs();
				List<CourseCatalog> catalog = new ArrayList<CourseCatalog>();
				for (CourseCatalog courseCatalog : catalogs) {
					if(courseCatalog.getFdType()==1){
						catalog.add(courseCatalog);
					}
				}
				ArrayUtils.sortListByProperty(catalog, "fdNo", SortType.HIGHT);
				map.put("firstCId", catalog.get(0).getFdId());
				map.put("firstCType", catalog.get(0).getFdMaterialType());
				map.put("listCId", catalog.get(catalog.size()-1).getFdId());
				map.put("listCType", catalog.get(catalog.size()-1).getFdMaterialType());
				return JsonUtils.writeObjectToJson(map);
			}
		}
		
		/**
		 * 获取用户课程信息
		 * 
		 * 
		 */
		@RequestMapping(value="getUserCourseInfo")
		@ResponseBody
		private String getUserCourseInfo(HttpServletRequest request) {
			Map returnMap = new HashMap();
			String userId = request.getParameter("userId");
			String courseId = request.getParameter("courseId");
			if(userId.equals(ShiroUtils.getUser().getId())){
				returnMap.put("isme", true);
			}else{
				returnMap.put("isme", false);
			}
			SysOrgPerson orgPerson = accountService.load(userId);
			returnMap.put("name", orgPerson.getRealName());
			returnMap.put("img", orgPerson.getPoto());
			returnMap.put("sex", orgPerson.getFdSex());
			returnMap.put("org", orgPerson.getHbmParent()==null?"不详":orgPerson.getHbmParent().getHbmParentOrg().getFdName());
			returnMap.put("dep", orgPerson.getDeptName()==null?"不详":orgPerson.getDeptName());
			returnMap.put("tel", orgPerson.getFdWorkPhone()==null?"不详":orgPerson.getFdWorkPhone());
			returnMap.put("qq", orgPerson.getFdQq()==null?"不详":orgPerson.getFdQq());
			Map userMap = sysOrgPersonService.getUserInfo(userId);
			returnMap.put("bool", userMap.get("fdBloodType"));
			returnMap.put("selfIntroduction", userMap.get("selfIntroduction"));
			CourseInfo courseInfo = courseService.get(courseId);
			List<AttMain> attMains = attMainService.getAttMainsByModelIdAndModelName(courseInfo.getFdId(), CourseInfo.class.getName());
			returnMap.put("courseName",courseInfo.getFdTitle());
			returnMap.put("courseAuther", courseInfo.getFdAuthor());
			returnMap.put("courseImg",attMains.size()==0?"":attMains.get(0).getFdId());
			return JsonUtils.writeObjectToJson(returnMap);
		}
		
		/**
		 * 获取备课心情
		 * 
		 * 
		 */
		@RequestMapping(value="getMessageFeeling")
		@ResponseBody
		private String getMessageFeeling(HttpServletRequest request) {
			String userId = request.getParameter("userId");
			String courseId = request.getParameter("courseId");
			BamCourse bamCourse = bamCourseService.getCourseByUserIdAndCourseId(userId, courseId);
			Map returnMap = new HashMap();
			if(bamCourse==null){
				returnMap.put("list", new ArrayList());
				return JsonUtils.writeObjectToJson(returnMap);
			}else{
				List<Map> list =  new ArrayList<Map>();
				Finder finder = Finder.create("");
				finder.append("from Message m where m.fdType=:fdType and m.fdModelName=:fdModelName and m.fdModelId=:fdModelId order by m.fdCreateTime desc ");
				finder.setParam("fdType",Constant.MESSAGE_TYPE_MOOD);
				finder.setParam("fdModelName",BamCourse.class.getName());
				finder.setParam("fdModelId",bamCourse.getFdId());
				List<Message> messages = messageService.find(finder);
				if(messages.size()==0){
					returnMap.put("list", new ArrayList());
					return JsonUtils.writeObjectToJson(returnMap);
				}
				List<String> dateList = new ArrayList<String>();
				//Set<String> dateSet = new HashSet<String>();
				for ( int i=0;i<messages.size();i++) {
					Message message = messages.get(i);
					SimpleDateFormat sim = new SimpleDateFormat("MM dd yyyy");
					String date = sim.format(message.getFdCreateTime().getTime());
					if(!dateList.contains(date)){
						dateList.add(date);
					}
				}
				for(int j=0;j<dateList.size();j++){
					String string = dateList.get(j);
				//for (String string : dateSet) {
					Map map = new HashMap();
					map.put("date", string);
					List<Map> items = new ArrayList<Map>();
					for(int i=0;i<messages.size();i++){
						SimpleDateFormat sim = new SimpleDateFormat("MM dd yyyy");
						String c2s = sim.format(messages.get(i).getFdCreateTime().getTime()); 
						if(string.equals(c2s)){
							Map item = new HashMap();
							item.put("id", messages.get(i).getFdId());
							item.put("mood",  messages.get(i).getFdContent());
							Map praise = new HashMap();
							praise.put("count", messageService.getSupportCount(messages.get(i).getFdId()));
							praise.put("did", messageReplyService.isSupportMessage(ShiroUtils.getUser().getId(), messages.get(i).getFdId())!=null);
							item.put("praise", praise);
							Map weak = new HashMap();
							weak.put("count", messageService.getOpposeCount(messages.get(i).getFdId()));
							weak.put("did", messageReplyService.isOpposeMessage(ShiroUtils.getUser().getId(), messages.get(i).getFdId())!=null);
							item.put("weak", weak);
							Map comment = new HashMap();
							List<MessageReply> messageReplies = messageReplyService.findByProperty("message.fdId", messages.get(i).getFdId());
							comment.put("count", messageReplies.size());
							List<Map> messageRepliesMap = new ArrayList<Map>();
							for (MessageReply messageReply : messageReplies) {
								Map messageReplyMap = new HashMap();
								SysOrgPerson orgPerson = messageReply.getFdUser();
								Map userMap = new HashMap();
								userMap.put("imgUrl", orgPerson.getPoto());
								userMap.put("link", "/course/courseIndex?userId="+orgPerson.getFdId());
								userMap.put("name", orgPerson.getRealName());
								userMap.put("mail", orgPerson.getFdEmail()==null?"不详":orgPerson.getFdEmail());
								userMap.put("org", orgPerson.getDeptName()==null?"不详":orgPerson.getDeptName());
								messageReplyMap.put("issuer", userMap);
								messageReplyMap.put("comment", messageReply.getFdContent());
								messageReplyMap.put("time", DateUtil.getInterval(DateUtil.convertDateToString(messageReply.getFdCreateTime()), "yyyy/MM/dd hh:mm aa"));
								messageRepliesMap.add(messageReplyMap);
							}
							ArrayUtils.sortListByProperty(messageRepliesMap, "time", SortType.LOW); 
							comment.put("list", messageRepliesMap);
							item.put("comment", comment);
							item.put("time", DateUtil.getInterval(DateUtil.convertDateToString(messages.get(i).getFdCreateTime()), "yyyy/MM/dd hh:mm aa"));
							items.add(item);
						}
					}
					map.put("items", items);
					list.add(map);
				}
				returnMap.put("list",list);
				return JsonUtils.writeObjectToJson(returnMap);
			}
		}
		
		@Autowired
		private LogLoginService logLoginService;
		
		@Autowired
		private LogOnlineService logOnlineService;
		
		@Autowired
		private StudyTrackService studyTrackService;
		
		/**
		 * 备课心情页面，活跃天数
		 * @param request
		 */
		@RequestMapping(value = "getCourseFeelingActive")
		@ResponseBody
		public String getCourseFeelingActive(HttpServletRequest request) {
			String userId = request.getParameter("userId");
			String courseId= request.getParameter("courseId");
			SysOrgPerson orgPerson = accountService.load(userId);
			Map map = new HashMap();
			map.put("lastTime", logLoginService.getNewLoginDate());
			map.put("onlineDay",logOnlineService.getOnlineByUserId(ShiroUtils.getUser().getId()).getLoginDay());
			BamCourse bamCourse = bamCourseService.getCourseByUserIdAndCourseId(userId, courseId);
			Map passMap = studyTrackService.passInfoByBamId(bamCourse.getFdId());
			String currLecture="";
			//String nextCatalog="";
			if(passMap.size()==0){
				currLecture="0%";
			}else{
				if(passMap.get("coursePass")==null){
					List<CourseCatalog> catalogs =bamCourse.getCatalogs();
					int sum=0;
					int finishSum=0;
					for (int i=0;i< catalogs.size();i++) {
						CourseCatalog courseCatalog = catalogs.get(i);
						if(Constant.CATALOG_TYPE_CHAPTER == courseCatalog.getFdType()){
							continue;
						}
						sum++;
						if(courseCatalog.getThrough()!=null&&courseCatalog.getThrough()){
							finishSum++;
						}
					}
					currLecture=(finishSum*100/sum)+"%";
				}else{
					currLecture = "100%";
				}
			}
			int messageCount = messageService.findByCriteria(Message.class,
	                Value.eq("fdModelId", bamCourse.getFdId()),
	                Value.eq("fdModelName", BamCourse.class.getName()),Value.eq("fdType", "02")).size();
			map.put("messageCount", messageCount);
			map.put("currLecture", currLecture);
			return JsonUtils.writeObjectToJson(map);
		}
		
		
		/**
		 * 备课心情页面，进度条
		 * @param request
		 */
		@RequestMapping(value = "getCourseFeelingSchedule")
		@ResponseBody
		public String getCourseFeelingSchedule(HttpServletRequest request) {
			String userId = request.getParameter("userId");
			String courseId= request.getParameter("courseId");
			BamCourse bamCourse = bamCourseService.getCourseByUserIdAndCourseId(userId, courseId);
			Map map = new HashMap();
			Map passMap = studyTrackService.passInfoByBamId(bamCourse.getFdId());
			int sums;//共完成数
			String nextCatalog="";
			String width="";//百分比
			if(passMap.size()==0){
				List<CourseCatalog> catalogs =bamCourse.getCatalogs();
				for (int i=0;i< catalogs.size();i++) {
					CourseCatalog courseCatalog = catalogs.get(i);
					if(Constant.CATALOG_TYPE_LECTURE == courseCatalog.getFdType()){
						nextCatalog = courseCatalog.getFdName();
						break;
					}
				}
				sums=0;
				width="0%";
			}else{
				if(passMap.get("coursePass")==null){
					CourseCatalog catalog = (CourseCatalog)passMap.get("courseCatalogNow");//当前环节
					List<CourseCatalog> catalogs =bamCourse.getCatalogs();
					int sum=0;
					int finishSum=0;
					for (int i=0;i< catalogs.size();i++) {
						CourseCatalog courseCatalog = catalogs.get(i);
						if(Constant.CATALOG_TYPE_CHAPTER == courseCatalog.getFdType()){
							continue;
						}
						sum++;
						if(courseCatalog.getThrough()!=null&&courseCatalog.getThrough()){
							finishSum++;
						}
						//取出当前环节的下一节
						if(courseCatalog.getFdId().equals(catalog.getFdId())){
							try {
								nextCatalog = catalogs.get(i).getFdName();
							} catch (Exception e) {
								nextCatalog = "证书预览";
							}
						}
					}
					sums=finishSum;
					width=(finishSum*100/sum)+"%";
				}else{
					sums=0;
					List<CourseCatalog> catalogs =bamCourse.getCatalogs();
					for (int i=0;i< catalogs.size();i++) {
						CourseCatalog courseCatalog = catalogs.get(i);
						if(Constant.CATALOG_TYPE_CHAPTER == courseCatalog.getFdType()){
							continue;
						}else{
							sums++;
						}
					}
					width="100%";
					nextCatalog = "证书预览";
				}
			}
			map.put("sums", sums);
			map.put("width", width);
			map.put("nextCatalog", nextCatalog);
			return JsonUtils.writeObjectToJson(map);
		}
}
