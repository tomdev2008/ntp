package cn.me.xdf.action.passThrough;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import cn.me.xdf.common.hibernate4.Finder;
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
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.bam.BamCourseService;
import cn.me.xdf.service.bam.BamMaterialService;
import cn.me.xdf.service.bam.process.SourceNodeService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.course.CourseService;
import cn.me.xdf.service.material.ExamQuestionService;
import cn.me.xdf.service.material.MaterialService;
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
		Finder finder = Finder.create(" from BamCourse b where b.courseId = :courseId ");
		finder.setParam("courseId", courseId);
		Pagination page = bamCourseService.getPage(finder, pageNo, SimplePage.DEF_COUNT);
		if(page.getTotalCount()>0){
			List list = page.getList();
			for(int i=0;i<list.size();i++){
				BamCourse bam = (BamCourse)list.get(i);
				SysOrgPerson person = accountService.load(bam.getPreTeachId());
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
		
}
