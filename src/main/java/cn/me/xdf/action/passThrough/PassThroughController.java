package cn.me.xdf.action.passThrough;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.hibernate4.Value;
import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.page.SimplePage;
import cn.me.xdf.common.utils.ComUtils;
import cn.me.xdf.common.utils.array.ArrayUtils;
import cn.me.xdf.common.utils.array.SortType;
import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseContent;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.course.CourseParticipateAuth;
import cn.me.xdf.model.score.ScoreStatistics;
import cn.me.xdf.service.bam.BamCourseService;
import cn.me.xdf.service.bam.BamMaterialService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.course.CourseParticipateAuthService;
import cn.me.xdf.service.course.CourseService;
import cn.me.xdf.service.score.ScoreStatisticsService;
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
				BamCourse bamCourse = bamCourseService.findUniqueByProperty(BamCourse.class, Value.eq("courseId", course.getFdId()),Value.eq("preTeachId", ShiroUtils.getUser().getId()));
				if(bamCourse!=null){
					//章节信息
					List<CourseCatalog> courseCatalogs = bamCourse.getCatalogs();
					if(courseCatalogs!=null){
						ArrayUtils.sortListByProperty(courseCatalogs, "fdTotalNo", SortType.HIGHT);
					}
					request.setAttribute("catalog", courseCatalogs);
				}
				//当前作者的图片(当作者和创建者是相同时候使用创建者的照片)
				if(course.getFdAuthor().equals(course.getCreator().getRealName())){
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
				request.setAttribute("courseScore", scoreStatistics==null?0:scoreStatistics.getFdAverage());
				
			}else{
				//否则需要跳转到发现课程
				
			}
		}else{
			//否则需要跳转到发现课程
			
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
		CourseInfo course = courseService.get(courseId);
		BamCourse bamCourse = bamCourseService.findUniqueByProperty(BamCourse.class, Value.eq("courseId", course.getFdId()),Value.eq("preTeachId", ShiroUtils.getUser().getId()));
		if(bamCourse==null){
			//如果进程信息为空，则先保存进程信息
			bamCourseService.saveBamCourse(course, ShiroUtils.getUser().getId());
			bamCourse = bamCourseService.findUniqueByProperty(BamCourse.class, Value.eq("courseId", course.getFdId()),Value.eq("preTeachId", ShiroUtils.getUser().getId()));
		}
		if(StringUtil.isBlank(catalogId)&&StringUtil.isBlank(fdMtype)){
			List<CourseCatalog> courseCatalogs = bamCourse.getCatalogs();
			if(courseCatalogs!=null){
				ArrayUtils.sortListByProperty(courseCatalogs, "fdTotalNo", SortType.HIGHT);
			    /////添加开始学习按钮 找出当前人员学习的当前节
				for (CourseCatalog courseCatalog : courseCatalogs) {
					 if(courseCatalog.getFdType().equals(Constant.CATALOG_TYPE_LECTURE)){//表示节
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
			}
		}else{
			//设置正在学习的当前节
			request.setAttribute("catalogId", catalogId);
			request.setAttribute("fdMtype", fdMtype);
			
		}
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
	public String submitExamOrTask(WebRequest request) {
		String fdMtype = request.getParameter("fdMtype");
		String catalogId = request.getParameter("catalogId");
		String bamId = request.getParameter("bamId");
		bamMaterialService.saveSourceNode(fdMtype, request);
		return  "redirect:/passThrough/getStudyContent?bamId="+bamId+"&catalogId="+catalogId+"&fdMtype="+fdMtype;
	}
	
}
