package cn.me.xdf.action.course;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.course.SeriesInfo;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.course.SeriesInfoService;
/*
 * 系列
 */
@Controller
@RequestMapping(value = "/series")
@Scope("request")
public class SeriesController {
	
	@Autowired
	private SeriesInfoService seriesInfoService;
	@Autowired
	private AttMainService attMainService;
	
	
	/*
	 * 查询系列课程;
	 * author hanhl
	 */
	@RequestMapping(value="findSeriesInfos")
	public String findSeries(Model model,HttpServletRequest request){
		String fdName = request.getParameter("fdName");
		String pageNo = request.getParameter("pageNo");
		String orderbyStr = request.getParameter("order");
		Pagination page=seriesInfoService.findSeriesInfosOrByName(fdName, pageNo, orderbyStr);
		model.addAttribute("page", page);
		return "/course/course_list";
	}
	/**
	 * 创建新系列
	 */
	@RequestMapping(value="add")
	public String editSeries(HttpServletRequest request){
		String seriesId=request.getParameter("seriesId");
		if(StringUtil.isNotEmpty(seriesId)){
			SeriesInfo series = seriesInfoService.get(seriesId);
			if(series!=null){
				request.setAttribute("series", series);
			}
		}
		return "/course/series_add";
	}
	/**
	 * 系列课程
	 */
	@RequestMapping(value="pagefoward")
	public String pagefoward(HttpServletRequest request){
		String seriesId=request.getParameter("seriesId");
		if(StringUtil.isNotEmpty(seriesId)){
			return "redirect:/series/add?"+seriesId;
		}else{
			return "redirect:/series/add";
		}
	}
}
