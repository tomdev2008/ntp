package cn.me.xdf.action.passThrough;


import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.hibernate4.Value;
import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.page.SimplePage;
import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.course.CourseParticipateAuth;
import cn.me.xdf.model.score.ScoreStatistics;
import cn.me.xdf.service.bam.BamCourseService;
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
	
	/**
	 * 课程学习首页
	 * @param request
	 */
	@RequestMapping(value = "getCourseHome")
	public String getCourseHome(HttpServletRequest request) {
		//获取课程ID
		String courseId = request.getParameter("courseId");
		if(StringUtil.isNotEmpty(courseId)){
			CourseInfo course = courseService.get(courseId);
			if(course!=null){
				//从进程表中取当前用户所选课程的进程信息
				BamCourse bamCourse = bamCourseService.findUniqueByProperty(BamCourse.class, Value.eq("courseId", course.getFdId()),Value.eq("preTeachId", ShiroUtils.getUser().getId()));
				if(bamCourse==null){
					//如果进程信息为空，则先保存进程信息
					bamCourseService.saveBamCourse(course, ShiroUtils.getUser().getId());
					bamCourse = bamCourseService.findUniqueByProperty(BamCourse.class, Value.eq("courseId", course.getFdId()),Value.eq("preTeachId", ShiroUtils.getUser().getId()));
				}
				//获取当前课程正在学习的新教师
				int studayTotalNo = getLearningTotalNo(course.getFdId());
				request.setAttribute("studayTotalNo", studayTotalNo);
				//获取该课程的评分统计值
				ScoreStatistics scoreStatistics =  scoreStatisticsService.findUniqueByProperty(ScoreStatistics.class,Value.eq("fdModelName", CourseInfo.class.getName()),Value.eq("fdModelId", course.getFdId()));
				//课程进程ID
				request.setAttribute("bamId", bamCourse.getFdId());
				//课程信息
				request.setAttribute("course", course);
				//课程评分统计
				request.setAttribute("courseScore", scoreStatistics==null?0:scoreStatistics.getFdAverage());
				//章节信息
				request.setAttribute("catalog", JsonUtils.readObjectByJson(bamCourse.getCatalogJson(), CourseCatalog.class));
			}
		}
		return "/passThrough/course_home";
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
}
