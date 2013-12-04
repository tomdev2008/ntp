package cn.me.xdf.action.course;




import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sun.java2d.pipe.SpanShapeRenderer.Simple;

import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.page.SimplePage;
import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseAuth;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.course.CourseParticipateAuth;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.model.score.ScoreStatistics;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.course.CourseAuthService;
import cn.me.xdf.service.course.CourseCatalogService;
import cn.me.xdf.service.course.CourseParticipateAuthService;
import cn.me.xdf.service.course.CourseService;
import cn.me.xdf.service.score.ScoreStatisticsService;
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
    
    @Autowired
	private CourseAuthService courseAuthService;
    
    @Autowired
    private ScoreStatisticsService statisticsService;
    
    @Autowired
    private CourseParticipateAuthService courseParticipateAuthService;
    
    @Autowired 
    private AccountService accountService;
	/*
	 * 
	 * 获取课程列表
	 * author hanhl
	 * */
	@RequestMapping(value="findcourseInfos")
	public String findcourseInfos(Model model,HttpServletRequest request){
		String fdTitle = request.getParameter("fdTitle");
		String pageNoStr = request.getParameter("pageNo");
		String orderbyStr = request.getParameter("order");
		Pagination page=courseService.findCourseInfosByName(fdTitle, pageNoStr, orderbyStr,Constant.COUSER_TEMPLATE_MANAGE);
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
				courseService.save(course);
			}
		}
		return "redirect:/course/findcourseInfos?fdType=12&order=fdcreatetime";
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
	/*
	 * 根据权限判定跳转
	 */
	@RequestMapping(value="pagefoward")
	public String pagefoward(HttpServletRequest request){
		String courseId = request.getParameter("courseId");
		if(getUserAuth(courseId)){
			return "redirect:/course/add?courseId="+courseId;//有编辑权限(当然用户是课程创建者)到编辑页面
		}else{
			return "redirect:/course/previewCourse?courseId="+courseId;//到视图页面
		}
	}
	/*
	 * 查询当前用户课程权限
	 */
	private Boolean getUserAuth(String courseId){
		if(ShiroUtils.isAdmin()){//超管直接到edit
			return true;
		}
		CourseInfo courseInfo =courseService .load(courseId);
		if(courseInfo.getCreator().getFdId().equals(ShiroUtils.getUser().getId())){//当前创建者
			return true;
		}
		CourseAuth auth = courseAuthService.findByCourseIdAndUserId(courseId,ShiroUtils.getUser().getId());//有编辑权限
	    if(auth!=null&&auth.getIsEditer()==true){
	    	return true;
	    }
		return false;//公开的
	}
	/*
	 * 课程授权列表
	 * author hanhl
	 */
    @RequestMapping(value="getCourseAuthInfos")
    public String getCourseAuthInfos(Model model,HttpServletRequest request){
    	String fdTitle = request.getParameter("fdTitle");
		String pageNoStr = request.getParameter("pageNo");
		String orderbyStr = request.getParameter("order");
		Pagination page = courseService.findCourseInfosByName( fdTitle,
				pageNoStr, orderbyStr,Constant.COUSER_AUTH_MANAGE);
		model.addAttribute("page", page);
    	return "/course/courseauth_list";
    	
    }
    /**
	 * 某课程授权
	 */
	@RequestMapping(value="getSingleCourseAuthInfo")
	public String getCourseAuthInfo( HttpServletRequest request){
		return "/course/courseauth_manage";
	}
	
	@RequestMapping(value = "courseIndex")
	public String courseIndex(HttpServletRequest request) {
		if(StringUtil.isEmpty(request.getParameter("userId"))){
			request.setAttribute("userId", ShiroUtils.getUser().getId());
		}else{
			request.setAttribute("userId",request.getParameter("userId"));
		}
		return "/course/course_index";
	}
	
	@RequestMapping(value = "courseIndexAll")
	public String courseIndexAll(HttpServletRequest request) {
		request.setAttribute("userId", ShiroUtils.getUser().getId());
		return "/course/course_index_all";
	}
	
}
