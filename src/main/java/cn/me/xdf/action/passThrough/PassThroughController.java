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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.hibernate4.Value;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.utils.ComUtils;
import cn.me.xdf.common.utils.array.ArrayUtils;
import cn.me.xdf.common.utils.array.SortType;
import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.model.score.ScoreStatistics;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.bam.BamCourseService;
import cn.me.xdf.service.bam.BamMaterialService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.course.CourseCatalogService;
import cn.me.xdf.service.course.CourseParticipateAuthService;
import cn.me.xdf.service.course.CourseService;
import cn.me.xdf.service.log.LogLoginService;
import cn.me.xdf.service.log.LogOnlineService;
import cn.me.xdf.service.message.MessageService;
import cn.me.xdf.service.score.ScoreStatisticsService;
import cn.me.xdf.service.studyTack.StudyTrackService;
import cn.me.xdf.utils.ShiroUtils;

/**
 * 课程闯关
 * 
 * @author zuoyi
 * 
 */
@Controller
@RequestMapping(value = "/passThrough")
@Scope("request")
public class PassThroughController {

	//课程service
	@Autowired
	private CourseService courseService;
	
	//课程进程service
	@Autowired
	private BamCourseService bamCourseService;
	
	//课程参与权限service
	@Autowired
	private CourseParticipateAuthService courseParticipateAuthService;
	
	//评分统计service
	@Autowired
	private ScoreStatisticsService scoreStatisticsService;
	
	@Autowired
	private BamMaterialService bamMaterialService;
	
	@Autowired
	private AttMainService attMainService;
	
	@Autowired
	private CourseCatalogService courseCatalogService;
	
	
	/**
	 * 课程学习首页
	 * @param request
	 */
	@RequestMapping(value = "getCourseHome/{courseId}")
	public String getCourseHome(@PathVariable("courseId") String courseId,HttpServletRequest request) {
		if(StringUtil.isNotEmpty(courseId)){
			CourseInfo course = courseService.get(courseId);
			if(course!=null && course.getIsAvailable()){
				//从进程表中取当前用户所选课程的进程信息
				BamCourse bamCourse = bamCourseService.getCourseByUserIdAndCourseId(ShiroUtils.getUser().getId(),course.getFdId());
				List<CourseCatalog> courseCatalogs = new ArrayList<CourseCatalog>();
				if(bamCourse!=null){
					//章节信息
					courseCatalogs = bamCourse.getCatalogs();
					if(courseCatalogs!=null){
						ArrayUtils.sortListByProperty(courseCatalogs, "fdTotalNo", SortType.HIGHT);
					}
				}else{
					courseCatalogs = courseCatalogService.getCatalogsByCourseId(course.getFdId());
				}
				request.setAttribute("catalog", courseCatalogs);
				//当前作者的图片(当作者和创建者是相同时候使用创建者的照片)
				if(course.getFdAuthor()!=null&&course.getFdAuthor().equals(course.getCreator().getRealName())){
					request.setAttribute("imgUrl",course.getCreator().getPoto());
				}else{
					request.setAttribute("imgUrl",ComUtils.getDefaultPoto());
				}
				//获取当前课程正在学习的新教师
				int studayTotalNo = getLearningTotalNo(course.getFdId());
				request.setAttribute("studayTotalNo", studayTotalNo);
				//获取该课程的评分统计值
				//课程信息
				request.setAttribute("course", course);
				//课程图片
				AttMain attMain = attMainService.getByModelIdAndModelName(courseId, CourseInfo.class.getName());
				request.setAttribute("courseAtt", attMain!=null?attMain.getFdId():"");
				//课程评分统计
				ScoreStatistics scoreStatistics =  scoreStatisticsService.findUniqueByProperty(ScoreStatistics.class,Value.eq("fdModelName", CourseInfo.class.getName()),Value.eq("fdModelId", course.getFdId()));
				request.setAttribute("courseScore", scoreStatistics==null?0.0:scoreStatistics.getFdAverage());
				
			}else{
				//否则需要跳转到发现课程
				return "redirect:/course/courseIndex";
			}
		}else{
			//否则需要跳转到发现课程
			return "redirect:/course/courseIndex";
		}
		return "/passThrough/course_home";
	}
	
	/**
	 * 获取课程学习内容
	 * @param request
	 */
	@RequestMapping(value = "getStudyContent")
	public String getStudyContent(HttpServletRequest request) {
		String courseId = request.getParameter("courseId");
		String catalogId = request.getParameter("catalogId");
		String fdMtype = request.getParameter("fdMtype");
		String fdPassword = request.getParameter("fdPassword");
		Map<String,?> map = RequestContextUtils.getInputFlashMap(request);   
        if (map!=null)  {
        	courseId = (String)map.get("courseId");
    		catalogId = (String)map.get("catalogId");
    		fdMtype = (String)map.get("fdMtype");
    		fdPassword = (String)map.get("fdPassword");
        }
		if(StringUtil.isBlank(courseId)){
			return "redirect:/course/courseIndex";
		}
		CourseInfo course = courseService.get(courseId);
		if(!course.getIsPublish()){
			if(course.getFdPassword()!=null){//密码课
				if(StringUtil.isBlank(fdPassword)||
						(StringUtil.isNotBlank(fdPassword)&&!fdPassword.equals(course.getFdPassword()))){
					return "redirect:/passThrough/getCourseHome/"+courseId;
				}
			}else{//授权课
				boolean canStudy= courseParticipateAuthService
						.findCouseParticipateAuthById(courseId,ShiroUtils.getUser().getId());
				if(canStudy){//无权
					return "redirect:/passThrough/getCourseHome/"+courseId;
				}
			}
		}
		BamCourse bamCourse = bamCourseService.
				getCourseByUserIdAndCourseId(ShiroUtils.getUser().getId(),courseId);
		if(bamCourse==null){
			//如果进程信息为空，则先保存进程信息
			bamCourseService.saveBamCourse(course, ShiroUtils.getUser().getId());
			bamCourse = bamCourseService.getCourseByUserIdAndCourseId(ShiroUtils.getUser().getId(),courseId);
		}
		if(StringUtil.isBlank(catalogId)&&StringUtil.isBlank(fdMtype)){
			List<CourseCatalog> courseCatalogs = bamCourse.getCatalogs();
			if(courseCatalogs!=null){
				ArrayUtils.sortListByProperty(courseCatalogs, "fdTotalNo", SortType.HIGHT);
			    /////添加开始学习按钮 找出当前人员学习的当前节
				for (CourseCatalog courseCatalog : courseCatalogs) {
				  if(courseCatalog.getFdType().equals(Constant.CATALOG_TYPE_CHAPTER)){
					  continue;
				  }
				  if(bamCourse.getThrough()){
					 //设置正在学习的当前节
					 request.setAttribute("catalogId", courseCatalog.getFdId());
					 //设置正在学习的当前节的素材类型
					 request.setAttribute("fdMtype", courseCatalog.getFdMaterialType());
					 break;   
				  }
				 
					if(courseCatalog.getThrough()!=null&&courseCatalog.getThrough()){
						continue;
					}else{
						//设置正在学习的当前节
						request.setAttribute("catalogId", courseCatalog.getFdId());
						//设置正在学习的当前节的素材类型
						request.setAttribute("fdMtype", courseCatalog.getFdMaterialType());
						break; 
					}
				 
			   }
			}
		}else{
			//设置正在学习的当前节
			request.setAttribute("catalogId", catalogId);
			request.setAttribute("fdMtype", fdMtype);
		}
		request.setAttribute("courseId", course.getFdId());
		request.setAttribute("bamId", bamCourse.getFdId());
		bamCourseService.updateCourseStartTime(bamCourse.getFdId());
		//页面跳转，跳转到课程学习页面
		return "/passThrough/course_content_study";
	}

	/**
	 * 从课程学习进程中获取当前学习的教师总数
	 * @param courseId 课程ID
	 * @return int 学习的教师总数
	 */
	private int getLearningTotalNo(String courseId) {
		Finder finder = Finder.create(" from BamCourse where courseId = :courseId");
		finder.setParam("courseId", courseId);
		Pagination page = bamCourseService.getPage(finder, 1, 15);
		return page.getTotalCount();
	}
	
	
	/**
	 * 提交试题后作业
	 * @param request
	 */
	@RequestMapping(value = "submitExamOrTask")
	public String submitExamOrTask(WebRequest request,RedirectAttributes redirectAttributes) {
		String fdMtype = request.getParameter("fdMtype");
		String catalogId = request.getParameter("catalogId");
		String courseId =request.getParameter("courseId");
		bamMaterialService.saveSourceNode(fdMtype, request);
		redirectAttributes.addFlashAttribute("courseId", courseId);
		redirectAttributes.addFlashAttribute("catalogId", catalogId);
		redirectAttributes.addFlashAttribute("fdMtype", fdMtype);
		CourseInfo course = courseService.get(courseId);
		if(!course.getIsPublish()&&StringUtil.isNotBlank(course.getFdPassword())){
			redirectAttributes.addFlashAttribute("fdPassword", course.getFdPassword());
		}
		return  "redirect:/passThrough/getStudyContent";
	}
	
	/**
	 * 备课心情页面
	 * @param request
	 */
	@RequestMapping(value = "getCourseFeeling")
	public String getCourseFeeling(HttpServletRequest request) {
		String userId="";
		if (StringUtil.isEmpty(request.getParameter("userId"))) {
			userId = ShiroUtils.getUser().getId();
		} else {
			userId = request.getParameter("userId");
		}
		request.setAttribute("userId", userId);
		request.setAttribute("courseId", request.getParameter("courseId"));
		request.setAttribute("isMe", userId.equals(ShiroUtils.getUser().getId()));
		BamCourse bamCourse = bamCourseService.getCourseByUserIdAndCourseId(userId, request.getParameter("courseId"));
		if(bamCourse==null){
			return "/course/course_index";
		}else{
			return "/passThrough/course_feeling";
		}
		
	}
	/**
	 * 备课心情页面
	 * @param request
	 */
	@RequestMapping(value = "getCourseFeelingByBamId")
	public String getCourseFeelingByBamId(HttpServletRequest request){
		String bamId=request.getParameter("bamId");
		BamCourse bamCourse = bamCourseService.get(BamCourse.class, bamId);
		request.setAttribute("userId", bamCourse.getPreTeachId());
		request.setAttribute("courseId", bamCourse.getCourseId());
		request.setAttribute("isMe", bamCourse.getPreTeachId().equals(ShiroUtils.getUser().getId()));
		return "/passThrough/course_feeling";
	}
	
	
}
