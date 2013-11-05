package cn.me.xdf.action.course;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.service.course.SeriesInfoService;
import cn.me.xdf.utils.ShiroUtils;
@Controller
@RequestMapping(value = "/ajax/series")
@Scope("request")
public class SeriesAjaxContrller {
	@Autowired
	private SeriesInfoService seriesInfoService;
	/*
	 * 查询课程列表 或者根据关键字搜索 author hanhl
	 */
	@RequestMapping(value = "getSeriesInfosOrByKey")
	public String getSeiesOrByKey(Model model, HttpServletRequest request) {
		String fdName = request.getParameter("fdName");
		String pageNo = request.getParameter("pageNo");
		String orderbyStr = request.getParameter("order");
		Pagination page=seriesInfoService.findSeriesInfosOrByName(fdName, pageNo, orderbyStr);
		model.addAttribute("page", page);
		return "/course/divserieslist";
		
	}
}
