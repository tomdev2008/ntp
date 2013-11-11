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
import cn.me.xdf.common.hibernate4.Value;
import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.page.SimplePage;
import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseContent;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.material.ExamOpinion;
import cn.me.xdf.model.material.ExamQuestion;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.material.Task;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.model.process.SourceNote;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.bam.BamCourseService;
import cn.me.xdf.service.bam.BamMaterialService;
import cn.me.xdf.service.bam.process.SourceNodeService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.course.CourseService;
import cn.me.xdf.service.material.ExamQuestionService;
import cn.me.xdf.service.material.MaterialService;
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
	 * 学习页面更具作业包id寻找信息
	 * @param fdId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "findTaskByPaperId")
	@ResponseBody
	public String findTaskByPaperId(HttpServletRequest request){
		String materialId = request.getParameter("fdId");
		String catalogId = request.getParameter("catalogId");//节id
		MaterialInfo info = materialService.load(materialId);
		Map map = new HashMap();
		List<Task> taskList = info.getTasks();
		//存放作业列表
		List<Map> list = new ArrayList<Map>();
		double fullScore =0;
		
		for(Task task:taskList){
			fullScore += task.getFdStandardScore();
			Map taskMap = new HashMap();
			taskMap.put("id", task.getFdId());
			taskMap.put("index", task.getFdOrder());
		    ///作业状态
			taskMap.put("status", null);
			taskMap.put("examType", task.getFdType());
			taskMap.put("examScore", task.getFdStandardScore().intValue()); 	
			taskMap.put("examName", task.getFdName());
			taskMap.put("examStem", task.getFdSubject());
		    
			List<AttMain> attList = attMainService.findByCriteria(AttMain.class,
					Value.eq("fdModelId", task.getFdId()),
				Value.eq("fdModelName", Task.class.getName()));
			
			 List<Map> taskAtt = new ArrayList<Map>();
			 List<Map> answerAtt = new ArrayList<Map>();
			
			for (AttMain attMain : attList) {
			  if(attMain.getFdKey().equals("taskAtt")){
				 //存放作业附件信息
				
				 Map attMap = new HashMap();
				 
				 attMap.put("index", attMain.getFdOrder());
				 attMap.put("name", attMain.getFdFileName());
				 attMap.put("url", "#");
				 taskAtt.add(attMap);
			   }else if(attMain.getFdKey().equals("answerAtt")){
				 //存放答题者上传的附件
				
				 Map answerMap = new HashMap();
				 answerMap.put("id", attMain.getFdId());
				 answerMap.put("name", attMain.getFdFileName());
				 answerMap.put("url", "#");
				 answerAtt.add(answerMap);
			   }
			  
			}
			taskMap.put("listAttachment", taskAtt);//存放作业附件信息
			taskMap.put("listTaskAttachment", answerAtt);//存放答题者上传的附件
			list.add(taskMap);
		}
		
		
		map.put("listExam", list);
		//作业列表信息结束
		//存放作业包信息
		map.put("id", info.getFdId());
		map.put("name", info.getFdName()); 
		map.put("fullScore", fullScore);
		map.put("examPaperTime", info.getFdStudyTime());
		map.put("examPaperIntro", info.getFdDescription());
		//作业包状态
		map.put("examPaperStatus", getStatus(info,catalogId,ShiroUtils.getUser().getId()));
		//存放作业包信结束///////////
		
		/////////////////评分人操作信息
        SourceNote sourceNote = sourceNodeService.getSourceNote(materialId, catalogId, ShiroUtils.getUser().getId());
        if(sourceNote!=null){
        	Map teacherRating = new HashMap();
            teacherRating.put("score", sourceNote.getFdScore()==null?0:sourceNote.getFdScore());
            teacherRating.put("comment", sourceNote.getFdComment());
            Map teacherMap = new HashMap();
            SysOrgPerson person = accountService.findById(sourceNote.getFdAppraiserId());
            teacherMap.put("imgUrl", person.getPoto());
            teacherRating.put("teacher", teacherMap);
            map.put("teacherRating", teacherRating);
        }
		return JsonUtils.writeObjectToJson(map);
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
		Pagination page = courseService.discoverCourses(ShiroUtils.getUser().getId(),1,5);
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
				List<CourseCatalog> catalogs = bamCourse.getCatalogs();
				Map catalogMap = new HashMap();
				List<Map> chapter = new ArrayList();
				List<Map> lecture = new ArrayList();
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
						tmp.put("name", catalog.getFdName());
						tmp.put("intro", catalog.getFdDescription());
						if(catalog.getThrough()==null){
							tmp.put("status", "untreated");
						}else if(catalog.getThrough()==false){
							tmp.put("status", "doing");
						}else if(catalog.getThrough()==true){
							tmp.put("status", "pass");
						}
						
						lecture.add(tmp);
					}
				}
				catalogMap.put("chapter", chapter);
				catalogMap.put("lecture", lecture);
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
		Map map = new HashMap();
		if(StringUtil.isNotBlank(bamId)){
			BamCourse bamCourse = bamCourseService.get(BamCourse.class, bamId);
			if(bamCourse!=null && bamCourse.getCatalogs()!=null){
				List<CourseCatalog> catalogs = bamCourse.getCatalogs();
				for(CourseCatalog catalog : catalogs){
					if(catalog.getFdId().equals(catalogId)){
						//设置节信息
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
						//设置节中内容
						List<MaterialInfo> material = bamCourse.getMaterialByCatalog(catalog);
						List list = new ArrayList();
						if(material!=null){
							for(MaterialInfo minfo:material){
								Map materialTemp = new HashMap();
								materialTemp.put("id", minfo.getFdId());
								materialTemp.put("name", minfo.getFdName());
								Map m = materialService.getTotalSorce(minfo.getFdId());
								materialTemp.put("fullScore", m.get("totalscore"));
								materialTemp.put("examCount", m.get("num"));
								materialTemp.put("examPaperTime", minfo.getFdStudyTime());
								materialTemp.put("examPaperIntro", minfo.getFdDescription());
								materialTemp.put("examPaperStatus", getStatus(minfo, catalogId, ShiroUtils.getUser().getId()));
								list.add(materialTemp);
							}
						}
						map.put("listExamPaper", list);
						break;
					}
				}
			}
		}
		return JsonUtils.writeObjectToJson(map);
	}
	
	private String getStatus(MaterialInfo minfo,String catalogId,String userId){
		if(minfo.getThrough()){
			return "pass";
		}else{
			SourceNote node = sourceNodeService.getSourceNote(minfo.getFdId(), catalogId, userId);
			if(node==null){
				return "unfinish";
			}
			Boolean iStudy=node.getIsStudy();
			if(iStudy==null){
				return "finish";
			}
		}
		return "fail";
	}
	/**
	 * 根据测试id获取测试信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getExamInfoByquestionId")
	@ResponseBody
	public String getExamInfoByquestionId(WebRequest request) {
		String questionId = request.getParameter("questionId");
		String catalogId = request.getParameter("catalogId");
		String bamId = request.getParameter("bamId");
		BamCourse bamCourse = bamCourseService.get(BamCourse.class, bamId);
		MaterialInfo examQuestion = materialService.get(questionId);
		List<Map> listExam = new ArrayList<Map>();
		List<ExamQuestion> examQuestions = examQuestion.getQuestions();
		List<CourseContent> courseContents = bamCourse.getCourseContents();
        if (courseContents != null){
        	for (CourseContent content : courseContents) {
	            if (content.getMaterial().getFdId().equals(questionId)) {
	            	examQuestion = content.getMaterial();
	            	break;
	            }
	        }
        }
		Map map = new HashMap();
		map.put("id", examQuestion.getFdId());
		map.put("name", examQuestion.getFdName());
		map.put("fullScore", materialService.getTotalSorce(questionId).get("totalscore"));
		map.put("examPaperTime", examQuestion.getFdStudyTime());
		map.put("examPaperIntro", examQuestion.getFdDescription());
		map.put("examPaperStatus", getStatus(examQuestion, catalogId, ShiroUtils.getUser().getId()));
		for (ExamQuestion examQuestion2 : examQuestions) {
			Map map2 = new HashMap();
			map2.put("id", examQuestion2.getFdId());
			map2.put("index", examQuestion2.getFdOrder());
			map2.put("status", null);
			map2.put("examScore", examQuestion2.getFdStandardScore());
			map2.put("examType", examQuestion2.getFdType().equals(Constant.EXAM_QUESTION_SINGLE_SELECTION)?"single":(examQuestion2.getFdType().equals(Constant.EXAM_QUESTION_MULTIPLE_SELECTION)?"multiple":"completion"));
			map2.put("examStem", examQuestion2.getFdSubject());
			List<ExamOpinion> examOpinions = examQuestion2.getOpinions();
			List<Map> opinionlist = new ArrayList<Map>();
			for (ExamOpinion examOpinion : examOpinions) {
				Map opinionMap = new HashMap();
				opinionMap.put("index", examOpinion.getFdOrder());
				opinionMap.put("name", examOpinion.getOpinion());
				opinionMap.put("isAnswer", examOpinion.getIsAnswer());
				opinionMap.put("isChecked", false);
				opinionlist.add(opinionMap);
			}
			map2.put("listExamAnswer", opinionlist);
			List<AttMain> attMains = attMainService.findByCriteria(AttMain.class,
	                Value.eq("fdModelId", examQuestion2.getFdId()),
	                Value.eq("fdModelName", ExamQuestion.class.getName()));	
			List<Map> attlist = new ArrayList<Map>();
			for (AttMain attMain : attMains) {
				Map attMap = new HashMap();
				attMap.put("index", attMain.getFdOrder());
				attMap.put("name", attMain.getFdFileName());
				attMap.put("url", attMain.getFdFilePath());
				attlist.add(attMap);
			}
			map2.put("listAttachment", attlist);
			listExam.add(map2);
		}
		map.put("listExam", listExam);
		return JsonUtils.writeObjectToJson(map);
	}
	
	
}
