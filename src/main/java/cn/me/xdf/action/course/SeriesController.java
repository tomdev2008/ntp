package cn.me.xdf.action.course;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.me.xdf.common.page.Pagination;
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
	 * 保存系列
	 * author hanhl
	 */
	@RequestMapping(value="saveSeries")
	public String  saveSeries(HttpServletRequest request){
		String parentId=request.getParameter("parentId");
		String fdName=request.getParameter("fdName");
		String fdDescription=request.getParameter("fdDescription");
        
		SeriesInfo seriesInfo=new SeriesInfo();
		if(parentId==null||"".equals(parentId)){
			//父为空则是根  即系列
			seriesInfo.setFdName(fdName);
			seriesInfo.setFdDescription(fdDescription);
			seriesInfo.setIsAvailable(true);//默认有效
			seriesInfo.setVersion(0);
			//同时保存系列的封面(附件)
			
			seriesInfoService.save(seriesInfo);
		}else{
			//不为空 则是阶段 ????这块有问题,如果系列和阶段是在同一操作页面,如何构建级联关系,若直接添加阶段?
			
			SeriesInfo supSeries=seriesInfoService.get(parentId);
			seriesInfo.setFdName(fdName);
			seriesInfo.setFdDescription(fdDescription);
			seriesInfo.setHbmParent(supSeries);
			seriesInfo.setIsAvailable(true);//默认有效
			seriesInfo.setVersion(0);
			//阶段没有封面?
			seriesInfoService.save(seriesInfo);
		}
		
		
		return "";
	}
	/*
	 * 修改系列或阶段信息
	 * author hanhl
	 */
	@RequestMapping(value="updateSeries")
	public String updateSeries(HttpServletRequest request){
		String seriesId=request.getParameter("seriesId");
		SeriesInfo seriesInfo=seriesInfoService.get(seriesId);
		String fdName=request.getParameter("fdName");
		String fdDescription=request.getParameter("fdDescription");
		seriesInfo.setFdName(fdName);
		seriesInfo.setFdDescription(fdDescription);
		//修改封面附件信息 先删除原始附件 然后更新为最新上传附件
		seriesInfoService.update(seriesInfo);
		return "";
	}
	/*
	 * 删除系列 设置系列状态为无效即可;
	 * author hanhl
	 */
	@RequestMapping(value="deleteSeries")
	public String deleteSeries(HttpServletRequest request){
		String seriesId=request.getParameter("seriesId");
		SeriesInfo seriesInfo=seriesInfoService.get(seriesId);
		seriesInfo.setIsAvailable(false);
		seriesInfoService.update(seriesInfo);
		return "";
	}
	/*
	 * 查询系列课程;
	 * author hanhl
	 */
	@RequestMapping(value="findeSeriesInfos")
	public String findSeries(Model model,HttpServletRequest request){
		String fdName = request.getParameter("fdName");
		String pageNo = request.getParameter("pageNo");
		String orderbyStr = request.getParameter("order");
		Pagination page=seriesInfoService.findSeriesInfosOrByName(fdName, pageNo, orderbyStr);
		model.addAttribute("page", page);
		return "/course/course_list";
	}
	
}
