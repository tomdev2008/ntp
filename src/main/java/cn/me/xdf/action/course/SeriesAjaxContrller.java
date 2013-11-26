package cn.me.xdf.action.course;

import java.util.ArrayList;
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
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseCategory;
import cn.me.xdf.model.course.CourseContent;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.course.SeriesCourses;
import cn.me.xdf.model.course.SeriesInfo;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.course.CourseService;
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
	
	@Autowired
	private AttMainService attMainService;
	
	@Autowired
	private CourseService courseService;

	

	/*
	 * 保存阶段信息
	 * author hanhl
	 */
	@RequestMapping(value="saveSeries")
	@ResponseBody
	public String  saveSeries(HttpServletRequest request){
		String seriesId=request.getParameter("seriesId");
		String fdName = request.getParameter("title");
		int fdNo = Integer.parseInt(request.getParameter("fdno"));
		SysOrgPerson creator=accountService.findById(ShiroUtils.getUser().getId());
		SeriesInfo series=new SeriesInfo();
		series.setFdName(fdName);
		series.setVersion(0);
		series.setFdCreateTime(new Date());
		series.setCreator(creator);
		series.setFdSeiresNo(fdNo);
		//没有系列id说明是新增系列  否则就是新增阶段
		 Map map=new HashMap();
		if(StringUtil.isNotEmpty(seriesId)){
			SeriesInfo seriessup=seriesInfoService.get(seriesId);
			series.setHbmParent(seriessup);
			seriesInfoService.save(series);
			map.put("seriesId", seriesId);
			map.put("id", series.getFdId());
		}else{
			SeriesInfo seriessup=new SeriesInfo();//先创建系列
			seriessup.setIsPublish(false);//初始化为非发布状态
			seriessup.setVersion(0);
			seriessup.setFdCreateTime(new Date());
			seriessup.setCreator(creator);
			seriessup.setIsAvailable(true);//有效的
			seriesInfoService.save(seriessup);
			series.setHbmParent(seriessup);
			seriesInfoService.save(series);//再保存阶段
			map.put("id", series.getFdId());
			map.put("seriesId", seriessup.getFdId());
		}
		
		return JsonUtils.writeObjectToJson(map);
	}
	/**
	 * 根据阶段id更新阶段名称
	 * @param request
	 */
	@RequestMapping(value = "updateSeriesFdNameById")
	@ResponseBody
	public void updateSeriesFdNameById(HttpServletRequest request) {
		//获取阶段id
		String fdId = request.getParameter("fdid");
		//获取章节名称
		String fdName = request.getParameter("title");
		SeriesInfo seriesInfo = seriesInfoService.get(fdId);
		seriesInfo.setFdName(fdName);
		seriesInfoService.save(seriesInfo);
	}
	/**
	 * 保存系列基本信息
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="saveSeriesBaseInfo")
	@ResponseBody
	public void saveSeriesBaseInfo(HttpServletRequest request){
		String seriesId=request.getParameter("seriesId");
		String seriesTitle=request.getParameter("seriesTitle");
		String seriesDesc=request.getParameter("seriesDesc");
		String isavailable=request.getParameter("isavailable");
		SeriesInfo series=seriesInfoService.get(seriesId);
		series.setFdName(seriesTitle);
		series.setFdDescription(seriesDesc);
		series.setIsAvailable(Boolean.parseBoolean(isavailable));
		seriesInfoService.save(series);
	}
	/**
	 * 加载系列封页
	 * 	 */
	@RequestMapping(value = "showcover")
	@ResponseBody
	public String getCourseCoverById(HttpServletRequest

	request) {
		String seriesId = request.getParameter("seriesId");
		AttMain attMain = attMainService.getByModelId(seriesId);
		Map map = new HashMap();
		if(attMain!=null){
		map.put("coverUrl", attMain.getFdId());
		}else{
			map.put("coverUrl", "");	
		}
		return JsonUtils.writeObjectToJson(map);
	}
	/**
	 * 保存系列封页信息
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "saveSeriesPic")
	@ResponseBody
	public void saveSeriesPic(HttpServletRequest request) {
		String seriesId=request.getParameter("seriesId");
		String attMainId = request.getParameter("attId");
		if(StringUtil.isNotBlank(attMainId)){
			// 先清理附件库(清理该课程下的原始附件)
			attMainService.deleteAttMainByModelId(seriesId);
			AttMain attMain = attMainService.get(attMainId);
			attMain.setFdModelId(seriesId);
			attMain.setFdModelName(SeriesInfo.class.getName());
			attMain.setFdKey("SeriesInfo");
			// 保存最新的附件
			attMainService.save(attMain);
		}
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
						String seriesId = (String)map.get("FDID");
						seriesInfoService.deleteSeries(seriesId);
					}
				}
			}
		}
	}
	/**
	 * 删除阶段信息
	 */
	@RequestMapping(value="deletePhasesById")
	@ResponseBody
	public void deletePhasesById(HttpServletRequest request){
		String phasesId = request.getParameter("phasesId");
	    if(StringUtil.isNotEmpty(phasesId)){
	    	List<SeriesCourses> seriescourseslist=seriesCoursesService.getSeriesCourseByseriesId(phasesId);
	    	if(seriescourseslist!=null){
	    		for(SeriesCourses seriescourse:seriescourseslist){
	    			seriesCoursesService.deleteBySeriesId(seriescourse.getFdId());
	    		}
	    	}
	    }
	}
	/**
	 * 更新阶段的顺序
	 * @param request
	 */
	@RequestMapping(value = "updateSeriesOrder")
	@ResponseBody
	public void updateCatalogOrder(HttpServletRequest request) {
		//获取系列id
		String seriesId = request.getParameter("seriesId");
		//获取阶段
		String chapter = request.getParameter("chapter");
		//如果章信息不为空，则将章信息转为list循环更新
		if(StringUtil.isNotEmpty(chapter)){
			List<Map> chapters = JsonUtils.readObjectByJson(chapter, List.class);
			if(chapters!=null && chapters.size()>0){
				for(Map chapterMap:chapters){
					String chapterId = (String)chapterMap.get("id");
					if(StringUtil.isNotEmpty(chapterId)){
						SeriesInfo seriesInfo = seriesInfoService.get(chapterId);
						seriesInfo.setFdSeiresNo((Integer)chapterMap.get("num"));
						seriesInfoService.save(seriesInfo);
					}
				}
			}
		}
	}
	/**
	 * 根据课程标题或者副标题进行课程查询
	 */
	@RequestMapping(value = "getCourseBykey")
	@ResponseBody
	public List<Map> getCourseBykey(HttpServletRequest request) {
		String key = request.getParameter("q");
		List<Map> courseInfos=new ArrayList<Map>();
		List<CourseInfo> courses=courseService.findCourseInfoByCouseNameTop10(key);
		if(courses!=null&&courses.size()>0){
			for(CourseInfo courseInfo:courses){
				Map map=new HashMap();
				map.put("id", courseInfo.getFdId());//课程id
				map.put("name", courseInfo.getFdTitle());//课程名称
				courseInfos.add(map);
			}
		}
		return courseInfos;
	}
	/**
	 * 添加阶段课程
	 */
	@RequestMapping(value="saveSeriesCourse")
	@ResponseBody
	public void saveSeriesCourse(HttpServletRequest request){
		//获取阶段Id
		String phasesId = request.getParameter("phasesId");
		//获取阶段描述
		String seriesDesc = request.getParameter("sectionsIntro");
		//获取节内容列表
		String courseList = request.getParameter("mediaList");
		if(StringUtil.isNotEmpty(phasesId)){
			SeriesInfo seriesSub=seriesInfoService.get(phasesId);
			if(seriesSub!=null){
				if(StringUtil.isNotEmpty(seriesDesc)){
					seriesSub.setFdDescription(seriesDesc);
					seriesInfoService.save(seriesSub);//保存阶段信息
				}
				if(StringUtil.isNotEmpty(courseList)){
					//解析页面传递的素材列表
					List<Map> cousers = JsonUtils.readObjectByJson(courseList, List.class);
					if(cousers!=null && cousers.size()>0){
						for(Map map:cousers){
							String courseId = (String)map.get("id");
							String index = (String)map.get("index");
							String title = (String)map.get("title");
							if(StringUtil.isNotEmpty(courseId)){
								CourseInfo courseInfo = courseService.get(courseId);
								if(courseInfo!=null && courseInfo.getIsAvailable()){
									SeriesCourses seriesCourses = new SeriesCourses();
									seriesCourses.setSeries(seriesSub);
									seriesCourses.setCourses(courseInfo);
									seriesCourses.setVersion(0);
									seriesCourses.setFdCourseNo(Integer.parseInt(index));
									seriesCoursesService.save(seriesCourses);//保存阶段课程信息
								}
							}
						}
					}
				}
				
			}
		}
	}
	/**
	 * 通过阶段id提取课程信息
	 */
	@RequestMapping(value="getSeriesCourseById")
	@ResponseBody
	public String getSeriesCourseById(HttpServletRequest request){
		String phasesId=request.getParameter("phasesId");
		List<SeriesCourses> sclist=seriesCoursesService.getSeriesCourseByseriesId(phasesId);
		Map map=new HashMap();
		List courselist=null;
		String phasesName="";
		String phasesDes="";
		if(sclist!=null){
			courselist=new ArrayList();
			SeriesInfo phases=sclist.get(0).getSeries();
			phasesName=phases.getFdName();
			phasesDes=phases.getFdDescription();
			for(SeriesCourses seriescourse:sclist){
				Map courseM=new HashMap();
				CourseInfo course=seriescourse.getCourses();
				courseM.put("id", course.getFdId());
				courseM.put("title", course.getFdTitle());
				courseM.put("index", seriescourse.getFdCourseNo());
				courselist.add(courseM);
			}
		}
		map.put("lectureName", phasesName);
		map.put("sectionsIntro", phasesDes);
		map.put("mediaList" ,courselist);
		return JsonUtils.writeObjectToJson(map);
	}
	/**
	 * 获取当前系列的阶段信息
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = "getSeriesBySeriesId")
	@ResponseBody
	public String getSeriesBySeriesId(HttpServletRequest request) {
		//获取系列id
		String seriesId = request.getParameter("seriesId");
		List<Map> phaseses=null;
		if(StringUtil.isNotEmpty(seriesId)){
			//根据课程ID取章节列表
			List<SeriesInfo> serieses = seriesInfoService.getSeriesById(seriesId);
			if(serieses!=null && serieses.size()>0){
				phaseses= new ArrayList<Map>();
				for(SeriesInfo series:serieses){
						Map phasesM = new HashMap();
						phasesM.put("id", series.getFdId());
						phasesM.put("index", series.getFdSeiresNo());
						phasesM.put("num", serieses.size());
						phasesM.put("title", series.getFdName());
						phaseses.add(phasesM);
					}
				}
			}	
		//将阶段信息转换成json返回到页面
		Map map = new HashMap();
		map.put("chapter", phaseses);
		return JsonUtils.writeObjectToJson(map);
	}
	/**
	 * 获取系列的基本信息
	 */
	@RequestMapping(value="getBaseSeriesInfoById")
	@ResponseBody
	public String getBaseSeriesInfoById(HttpServletRequest request){
		String seriesId = request.getParameter("seriesId");
		Map map=new HashMap();
		if(StringUtil.isNotEmpty(seriesId)){
			SeriesInfo seriesInfo=seriesInfoService.get(seriesId);
			map.put("fdName", seriesInfo.getFdName());
			map.put("fdDescription", seriesInfo.getFdDescription());
			map.put("isavailable", seriesInfo.getIsAvailable());
			
		}
		return JsonUtils.writeObjectToJson(map);
	}
}
