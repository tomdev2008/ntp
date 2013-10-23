package cn.me.xdf.action.course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.model.course.CourseCategory;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.course.TagInfo;
import cn.me.xdf.model.organization.User;
import cn.me.xdf.service.course.CourseCategoryService;
import cn.me.xdf.service.course.CourseService;
import cn.me.xdf.service.course.CourseTagService;
import cn.me.xdf.service.course.TagInfoService;

/**
 * 课程信息的ajax
 * 
 * @author
 * 
 */
@Controller
@RequestMapping(value = "/course/ajax")
@Scope("request")
public class CourseAjaxController {

	@Autowired
	private CourseService courseService;

	@Autowired
	private CourseCategoryService courseCategoryService;
	
	@Autowired
	private CourseTagService courseTagService;
	
	@Autowired
	private TagInfoService tagInfoService;
	
	/**
	 * 获取当前课程的基本信息
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = "getBaseCourseInfoById")
	@ResponseBody
	public String getCatalogJsonByCourseId(HttpServletRequest request) {
		//获取课程ID
		String courseId = request.getParameter("courseId");
		List<Map> list = new ArrayList<Map>();
		Map map = new HashMap();
		if(StringUtil.isNotEmpty(courseId)){
			CourseInfo course = courseService.get(courseId);
			if(course!=null){
				map.put("courseTit", course.getFdTitle());
				map.put("subTit", course.getFdSubTitle());
				map.put("courseType", course.getFdCategory().getFdId());
				
				//将课程的标签信息返回到页面
				List<TagInfo> tagList = courseTagService.findTagByCourseId(courseId);
				if(tagList!=null && tagList.size()>0){
					List<String> tags = new ArrayList<String>();
					for(TagInfo tag:tagList){
						tags.add(tag.getFdName());
					}
					map.put("keyword", JsonUtils.writeObjectToJson(tags));
				}
			}
		}
		
		//将所有课程分类信息转换成json返回到页面
		List<CourseCategory> categorys = courseCategoryService.findAll();
		if(categorys!=null && categorys.size()>0){
			List<Map> cateList = new ArrayList<Map>();
			for(CourseCategory category:categorys){
				Map catemap = new HashMap();
				catemap.put("id", category.getFdId());
				catemap.put("title", category.getFdName());
				cateList.add(catemap);
			}
			map.put("courseTypeList", JsonUtils.writeObjectToJson(cateList));		
		}
		
		list.add(map);
		return JsonUtils.writeObjectToJson(list);
	}
	
	/**
	 * 根据标签名称模糊查询标签信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "findTagInfosByKey")
	@ResponseBody
	public String findTagInfosByKey(HttpServletRequest request) {
		//获取课程ID
		String key = request.getParameter("key");
		List<TagInfo> tagInfos = tagInfoService.findTagInfosByKey(key);
		//将所有课程标签信息转换成json返回到页面
		List<Map> list = new ArrayList<Map>();
		Map map = new HashMap();
		if(tagInfos!=null && tagInfos.size()>0){
			List<Map> cateList = new ArrayList<Map>();
			for(TagInfo tagInfo:tagInfos){
				Map catemap = new HashMap();
				catemap.put("fdName", tagInfo.getFdName());
				catemap.put("fdDescription", tagInfo.getFdDescription());
				cateList.add(catemap);
			}
			map.put("tagInfoList", JsonUtils.writeObjectToJson(cateList));		
		}
		list.add(map);
		return JsonUtils.writeObjectToJson(list);
	}

}
