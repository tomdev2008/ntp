package cn.me.xdf.action.course;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseCategory;
import cn.me.xdf.model.course.SeriesInfo;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.course.SeriesCoursesService;
import cn.me.xdf.service.course.SeriesInfoService;
import cn.me.xdf.utils.ShiroUtils;
@Controller
@RequestMapping(value = "/ajax/series")
@Scope("request")
public class SeriesAjaxContrller {
	@Autowired
	private SeriesInfoService seriesInfoService;
	@Autowired
	private SeriesCoursesService seriesCoursesService;
	@Autowired
	private AccountService accountService;
	/*
	 * 保存系列
	 * author hanhl
	 */
	@RequestMapping(value="saveSeries")
	@ResponseBody
	public String  saveSeries(HttpServletRequest request){
		String seriesTitle=request.getParameter("seriesTitle");
		String seriesDesc=request.getParameter("seriesDesc");
		SeriesInfo series=new SeriesInfo();
		series.setFdName(seriesTitle);
		series.setFdDescription(seriesDesc);
		series.setFdCreateTime(new Date());
		series.setVersion(0);
		series.setCreator(accountService.findById(ShiroUtils.getUser().getId()));
	    seriesInfoService.save(series);
	    Map map=new HashMap();
		map.put("seriesId", series.getFdId());
		return JsonUtils.writeObjectToJson(map);
	}
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
	/*
	 * 删除系列阶段课程 author hanhl
	 * 删除时,把系列或阶段设置为无效即可
	 */
	@RequestMapping(value="deleteSeries")
	@ResponseBody
	public void deleteSeries(HttpServletRequest request){
		String seriesId=request.getParameter("seriesId");
		if(StringUtils.isNotEmpty(seriesId)){
			String[] courses = seriesId.split(",");
			for(int i=0;i<courses.length;i++){
				seriesInfoService.deleteSeries(courses[i]);//先删阶段与课程关系再删除阶段 系列
			}
		}
		
	}
	
	/*
	 * 选择课程删除时:删除课程与系列的关系  author hanhl
	 */
	@RequestMapping(value="deleteSeriesOfCourse")
	public void deleteSeriesOfCourse(HttpServletRequest request){
		String courseId=request.getParameter("courseId");
		seriesCoursesService.deleteByCourseId(courseId);
	}
	
	/*
	 * 删除系列阶段课程 author hanhl
	 * 删除时,把系列或阶段设置为无效即可
	 */
	@RequestMapping(value="deleteAllSeries")
	@ResponseBody
	public void deleteAllSeries(HttpServletRequest request){
		String fdTitle = request.getParameter("fdTitle");
		String pageNoStr = request.getParameter("pageNo");
		String orderbyStr = request.getParameter("order");
		Pagination page = seriesInfoService.findSeriesInfosOrByName( fdTitle,
				pageNoStr, orderbyStr);
		int i = page.getTotalPage();
		if(i>0){ 
			for(int j=0;j<i;j++){
				page = seriesInfoService.findSeriesInfosOrByName( fdTitle,
						"1", orderbyStr);
				List list = page.getList();
				if(list!=null && list.size()>0){
					for(Object obj:list){
						Map map = (Map)obj;
						String courseId = (String)map.get("FDID");
						seriesInfoService.deleteSeries(courseId);
					}
				}
			}
		}
	}
}
