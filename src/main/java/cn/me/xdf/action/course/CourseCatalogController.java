package cn.me.xdf.action.course;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.service.course.CourseCatalogService;
import cn.me.xdf.service.course.CourseService;




/**
 * 课程目录
 * 
 * @author
 * 
 */
@Controller
@RequestMapping(value = "/catalog")
@Scope("request")
public class CourseCatalogController {
	@Autowired
	private CourseCatalogService courseCatalogService;
	
	@Autowired
	private CourseService courseService;
	
	/**
	 * 获取当前课程的章节信息
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = "ajax/getCatalogJsonByCourseId")
	@ResponseBody
	public String getCatalogJsonByCourseId(HttpServletRequest request) {
		//获取课程ID
		String courseId = request.getParameter("courseId");
		List<Map> chapter = new ArrayList<Map>();
		List<Map> lecture = new ArrayList<Map>();
		if(StringUtil.isNotEmpty(courseId)){
			//根据课程ID取章节列表
			List<CourseCatalog> catalogs = courseCatalogService.getCatalogsByCourseId(courseId);
			if(catalogs!=null && catalogs.size()>0){
				for(CourseCatalog catalog:catalogs){
					if(Constant.CATALOG_TYPE_CHAPTER==catalog.getFdType()){
						//将章加入list中
						Map chapterMap = new HashMap();
						chapterMap.put("id", catalog.getFdId());
						chapterMap.put("index", catalog.getFdTotalNo());
						chapterMap.put("num", catalog.getFdNo());
						chapterMap.put("title", catalog.getFdName());
						chapter.add(chapterMap);
					}else{
						//将节加入list中
						Map lectureMap = new HashMap();
						lectureMap.put("id", catalog.getFdId());
						lectureMap.put("index", catalog.getFdTotalNo());
						lectureMap.put("num", catalog.getFdNo());
						lectureMap.put("title", catalog.getFdName());
						lectureMap.put("type", catalog.getFdMaterialType());
						lecture.add(lectureMap);
					}					
				}
			}
		}		
		//将章节信息转换成json返回到页面
		List<Map> list = new ArrayList<Map>();
		Map map = new HashMap();
		map.put("chapter", JsonUtils.writeObjectToJson(chapter));
		map.put("lecture", JsonUtils.writeObjectToJson(lecture));
		list.add(map);
		return JsonUtils.writeObjectToJson(list);
	}
	
	/**
	 * 根据章节ID删除章节信息
	 * @param request
	 */
	@RequestMapping(value = "ajax/deleteCatalogById")
	@ResponseBody
	public void deleteCatalogById(HttpServletRequest request) {
		//获取章节ID
		String fdId = request.getParameter("fdid");
		courseCatalogService.delete(fdId);
	}
		
	/**
	 * 更新章节的顺序
	 * @param request
	 */
	public void updateCatalogOrder(HttpServletRequest request) {
		//获取章
		String chapter = request.getParameter("chapter");
		//获取节
		String lecture = request.getParameter("lecture");
		//如果章信息不为空，则将章信息转为list循环更新
		if(StringUtil.isNotEmpty(chapter)){
			List<Map> chapters = JsonUtils.readObjectByJson(chapter, List.class);
			if(chapters!=null && chapters.size()>0){
				for(Map chapterMap:chapters){
					String chapterId = (String)chapterMap.get("id");
					if(StringUtil.isNotEmpty(chapterId)){
						CourseCatalog courseCatalog = courseCatalogService.get(chapterId);
						courseCatalog.setFdNo((String)chapterMap.get("num"));
						courseCatalog.setFdTotalNo((String)chapterMap.get("index"));
						courseCatalogService.save(courseCatalog);
					}
				}
			}
		}
		//如果节信息不为空，则将节信息转为list循环更新
		if(StringUtil.isNotEmpty(lecture)){
			List<Map> lectures = JsonUtils.readObjectByJson(lecture, List.class);
			if(lectures!=null && lectures.size()>0){
				for(Map lectureMap:lectures){
					String lectureId = (String)lectureMap.get("id");
					if(StringUtil.isNotEmpty(lectureId)){
						CourseCatalog courseCatalog = courseCatalogService.get(lectureId);
						courseCatalog.setFdNo((String)lectureMap.get("num"));
						courseCatalog.setFdTotalNo((String)lectureMap.get("index"));
						courseCatalogService.save(courseCatalog);
					}
				}
			}
		}
	}
	
	/**
	 * 根据章节ID更新章节名称
	 * @param request
	 */
	@RequestMapping(value = "ajax/updateCatalogNameById")
	@ResponseBody
	public void updateCatalogNameById(HttpServletRequest request) {
		//获取章节ID
		String fdId = request.getParameter("fdid");
		//获取章节名称
		String fdName = request.getParameter("title");
		CourseCatalog courseCatalog = courseCatalogService.get(fdId);
		courseCatalog.setFdName(fdName);
		courseCatalogService.save(courseCatalog);
	}
	
	/**
	 * 新增章节
	 * @param request
	 * @return String 新增的章节id及
	 */
	@RequestMapping(value = "ajax/addCatalog")
	@ResponseBody
	public String addCatalog(HttpServletRequest request) {
		//获取课程ID
		String courseId = request.getParameter("courseid");
		//获取章ID
		String isChapter = request.getParameter("ischapter");
		//获取总序号
		String fdTotalNo = request.getParameter("fdtotalno");	
		//获取章节序号
		String fdNo = request.getParameter("fdno");	
		//获取章节名称
		String fdName = request.getParameter("title");
		CourseCatalog courseCatalog = new CourseCatalog();
		//如果页面没有传课程ID,说明是新增的课程模板，并且新增第一章，则需要先保存课程
		CourseInfo course = new CourseInfo();
		if(StringUtil.isNotEmpty(courseId)){
			course = courseService.get(courseId);
			courseCatalog.setCourseInfo(course);
		}else{			
			course.setFdStatus(Constant.COURSE_TEMPLATE_STATUS_DRAFT);
			courseService.save(course);
			courseCatalog.setCourseInfo(course);
		}
		
		courseCatalog.setFdName(fdName);
		courseCatalog.setFdTotalNo(fdTotalNo);
		courseCatalog.setFdNo(fdNo);
		if("true".equals(isChapter)){
			courseCatalog.setFdType(Constant.CATALOG_TYPE_CHAPTER);
		}else{
			courseCatalog.setFdType(Constant.CATALOG_TYPE_LECTURE);
		}
		 courseCatalogService.save(courseCatalog);
		 List<Map> list = new ArrayList<Map>();
		 Map map = new HashMap();
		 map.put("id", courseCatalog.getFdId());
		 map.put("courseid", course.getFdId());
		 return JsonUtils.writeObjectToJson(list);
	}
}
