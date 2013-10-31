package cn.me.xdf.action.course;




import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.course.CourseCatalogService;
import cn.me.xdf.service.course.CourseService;
import cn.me.xdf.utils.ShiroUtils;

/**
 * 课程
 * 
 * @author
 * 
 */
@Controller
@RequestMapping(value = "/course")
@Scope("request")
public class CourseController {
	@Autowired
	private CourseService courseService;
	
    @Autowired
    private AttMainService attMainService;
    
    @Autowired
	private CourseCatalogService courseCatalogService;
	/*
	 * 
	 * 获取课程列表
	 * author hanhl
	 * */
	@RequestMapping(value="findcourseInfos")
	public String findcourseInfos(Model model,HttpServletRequest request){
		String userId = ShiroUtils.getUser().getId();
		String fdTitle = request.getParameter("fdTitle");
		String pageNoStr = request.getParameter("pageNo");
		String orderbyStr = request.getParameter("order");
		Pagination page=courseService.findCourseInfosByName(userId, fdTitle, pageNoStr, orderbyStr);
		model.addAttribute("page", page);
		return "/course/course_list";
	}
	
	@RequestMapping(value = "add")
	public String editCourse(HttpServletRequest request) {
		//获取课程ID
		String courseId = request.getParameter("courseId");
		if(StringUtil.isNotEmpty(courseId)){
			CourseInfo course = courseService.get(courseId);
			if(course!=null){
				request.setAttribute("course", course);
			}
		}
		return "/course/course_add";
	}
	
    /**
	 * 发布课程
	 * @param request
	 */
	@RequestMapping(value = "releaseCourse")
	public String releaseCourse(HttpServletRequest request) {
		//获取课程ID
		String courseId = request.getParameter("courseId");
		if(StringUtil.isNotEmpty(courseId)){
			CourseInfo course = courseService.get(courseId);
			if(course!=null && Constant.COURSE_TEMPLATE_STATUS_DRAFT.equals(course.getFdStatus())){
				course.setFdStatus(Constant.COURSE_TEMPLATE_STATUS_RELEASE);
				courseService.update(course);
			}
		}
		return "redirect:/course/findcourseInfos";
	}
	
	/**
	 * 预览课程
	 * @param request
	 */
	@RequestMapping(value = "previewCourse")
	public String previewCourse(HttpServletRequest request) {
		//获取课程ID
		String courseId = request.getParameter("courseId");
		if(StringUtil.isNotEmpty(courseId)){
			CourseInfo course = courseService.get(courseId);
			if(course!=null){
				List<CourseCatalog> catalog = courseCatalogService.getCatalogsByCourseId(courseId);
				request.setAttribute("course", course);
				request.setAttribute("catalog", catalog);
			}
		}
		return "/course/course_preview";
	}
	
}
