package cn.me.xdf.action.course;



import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.organization.SysOrgElement;
import cn.me.xdf.service.base.AttMainService;
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
	/*
	 * 
	 * 获取课程列表
	 * author hanhl
	 * */
	@RequestMapping(value="getCoureseInfos")
	public String getCourseInfos(Model model, String pageNo, HttpServletRequest request){
		String userId = ShiroUtils.getUser().getId();
		Pagination page=courseService.findAllCourseInfos(userId, NumberUtils.createInteger(pageNo));
		model.addAttribute("page", page);
		return "/course/coures_list";
	}
	
	/*
	 * 根据关键字搜索课程信息
	 * author hanhl
	 * */
	@RequestMapping(value="getCoureInfosByKey")
	public String getCourseInfosByKey(Model model, String fdName,String pageNo,String orderbyStr, HttpServletRequest request){
		String userId = ShiroUtils.getUser().getId();
		Pagination page=courseService.findCourseInfosByName(userId, fdName, NumberUtils.createInteger(pageNo), orderbyStr);
		model.addAttribute("page", page);
		return "/course/coures_list";
	}
	
	@RequestMapping(value = "add")
	public String addCourse() {
		return "/course/course_add";
	}
	/*
     * 课程封页图片
     * author hanhl
     */

	 @RequestMapping(value = "coverpage", method = RequestMethod.GET)
	    public String init() {
	        return "/course/course_cover";
	    }

    @RequestMapping(value = "cover", method = RequestMethod.POST)
    public String courseCover(HttpServletRequest request) {
    	String courseId=request.getParameter("courseId");
    	String attMainId=request.getParameter("attId");
    	AttMain attMain = attMainService.get(attMainId);
    	 attMain.setFdModelId(courseId);
    	 attMain.setFdModelName(""+CourseInfo.class);
    	 attMain.setFdKey("Course");
    	 attMainService.save(attMain);
        
      return "redirect:/course/coverpage";
    }
}
