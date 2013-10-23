package cn.me.xdf.action.course;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.course.SeriesInfo;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.course.SeriesInfoService;
/*
 * 系列
 */
@Controller
@RequestMapping(value = "/course")
@Scope("request")
public class SeriesController {
	
	@Autowired
	private SeriesInfoService seriesInfoService;
	@Autowired
	private AttMainService attMainService;
	/*
	 * 保存系列
	 */
	@RequestMapping(value="/course/saveSeries")
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
			//不为空 则是阶段
			SeriesInfo supSeries=seriesInfoService.findSeriesById(parentId);
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
	 */
	@RequestMapping(value="/course/updateSeries")
	public String updateSeries(HttpServletRequest request){
		String seriesId=request.getParameter("seriesId");
		SeriesInfo seriesInfo=seriesInfoService.findSeriesById(seriesId);
		String fdName=request.getParameter("fdName");
		String fdDescription=request.getParameter("fdDescription");
		String fileName=request.getParameter("fileName");
		seriesInfo.setFdName(fdName);
		seriesInfo.setFdDescription(fdDescription);
		//修改封面附件信息 先删除原始附件 然后更新为最新上传附件
		AttMain attMain=seriesInfo.getAttMain();
		if(!attMain.getFdFileName().equals(fileName)){
			//判断新上传附件与元附件的,若相同则删除,重新上传
			attMainService.deleteEntity(attMain);
		}
		//
		seriesInfoService.update(seriesInfo);
		return "";
	}
	/*
	 * 删除系列 设置系列状态为无效即可;
	 */
	@RequestMapping(value="/course/deleteSeries")
	public String deleteSeries(HttpServletRequest request){
		String seriesId=request.getParameter("seriesId");
		SeriesInfo seriesInfo=seriesInfoService.findSeriesById(seriesId);
		seriesInfo.setIsAvailable(false);
		seriesInfoService.update(seriesInfo);
		return "";
	}
	
}
