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
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseAuth;
import cn.me.xdf.model.course.CourseCategory;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.course.CourseTag;
import cn.me.xdf.model.course.TagInfo;
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
@RequestMapping(value = "/ajax/course")
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
	public String getBaseCourseInfoById(HttpServletRequest request) {
		//获取课程ID
		String courseId = request.getParameter("courseId");
		Map map = new HashMap();
		
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
			map.put("courseTypeList", cateList);
			//默认将第一个分类选中
			map.put("courseType", categorys.get(0).getFdId());
		}
		if(StringUtil.isNotEmpty(courseId)){
			CourseInfo course = courseService.get(courseId);
			if(course!=null){
				map.put("courseTit", course.getFdTitle());
				map.put("subTit", course.getFdSubTitle());
				if(course.getFdCategory()!=null){
					map.put("courseType", course.getFdCategory().getFdId());
				}				
				//将课程的标签信息返回到页面
				List<TagInfo> tagList = courseTagService.findTagByCourseId(courseId);
				if(tagList!=null && tagList.size()>0){
					List<String> tags = new ArrayList<String>();
					for(TagInfo tag:tagList){
						tags.add(tag.getFdName());
					}
					map.put("keyword", tags);
				}
			}
		}
		return JsonUtils.writeObjectToJson(map);
	}
	
	/**
	 * 保存课程的基本信息
	 * @param request
	 * @return String 课程ID
	 */
	@RequestMapping(value = "saveBaseInfo")
	@ResponseBody
	public String saveBaseInfo(HttpServletRequest request) {
		//获取课程ID
		String courseId = request.getParameter("courseId");
		//获取课程标题
		String courseTitle = request.getParameter("courseTitle");
		//获取课程副标题
		String subTitle = request.getParameter("subTitle");
		//获取课程标签
		String keyword = request.getParameter("keyword");
		//获取课程分类ID
		String courseType = request.getParameter("courseType");
		Map map = new HashMap();
		CourseInfo course = new CourseInfo();
		if(StringUtil.isNotEmpty(courseId)){
			course = courseService.get(courseId);
			if(course==null){
				course = new CourseInfo();
				course.setFdTitle(courseTitle);
				course.setFdSubTitle(subTitle);
				//新建课程时总节数设置为0
				course.setFdTotalPart(0);
				course.setFdStatus(Constant.COURSE_TEMPLATE_STATUS_DRAFT);
				//将分类保存到课程中
				if(StringUtil.isNotEmpty(courseType)){
					CourseCategory category = courseCategoryService.get(courseType);
					course.setFdCategory(category);
				}
				course = courseService.save(course);
			}else{
				course.setFdTitle(courseTitle);
				course.setFdSubTitle(subTitle);
				
				//将分类保存到课程中
				if(StringUtil.isNotEmpty(courseType)){
					CourseCategory category = courseCategoryService.get(courseType);
					course.setFdCategory(category);
				}
				course = courseService.update(course);
			}
		}else{
			course.setFdTitle(courseTitle);
			course.setFdSubTitle(subTitle);
			//新建课程时总节数设置为0
			course.setFdTotalPart(0);
			course.setFdStatus(Constant.COURSE_TEMPLATE_STATUS_DRAFT);
			//将分类保存到课程中
			if(StringUtil.isNotEmpty(courseType)){
				CourseCategory category = courseCategoryService.get(courseType);
				course.setFdCategory(category);
			}
			course = courseService.save(course);
			courseId = course.getFdId();
		}
		
		//保存标签库中没有的标签
		if(StringUtil.isNotEmpty(keyword)){
			String[] tags = keyword.split(",");
			for(String tagName:tags){
				if(StringUtil.isEmpty(tagName)){
					continue;
				}
				TagInfo tagInfo = tagInfoService.getTagByName(tagName);
				if(tagInfo==null){
					tagInfo = new TagInfo();
					tagInfo.setFdName(tagName);
					tagInfo = tagInfoService.save(tagInfo);
				}
				//保存课程与标签的关系
				CourseTag courseTag = new CourseTag();
				courseTag.setCourses(course);
				courseTag.setTag(tagInfo);
				courseTagService.save(courseTag);
			}
		}
		map.put("courseid", courseId);
		return JsonUtils.writeObjectToJson(map);
	}
	
	/**
	 * 获取当前课程的详细信息
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = "getDetailCourseInfoById")
	@ResponseBody
	public String getDetailCourseInfoById(HttpServletRequest request) {
		//获取课程ID
		String courseId = request.getParameter("courseId");
		Map map = new HashMap();
		if(StringUtil.isNotEmpty(courseId)){
			CourseInfo course = courseService.get(courseId);
			if(course!=null){
				//课程摘要
				map.put("courseAbstract", course.getFdSummary());
				//学习目标
				String learnObjectives = course.getFdLearnAim()==null?"":course.getFdLearnAim();
				map.put("learnObjectives", buildString(learnObjectives));
				//建议群体
				String suggestedGroup = course.getFdProposalsGroup()==null?"":course.getFdProposalsGroup();
				map.put("suggestedGroup", buildString(suggestedGroup));
				//课程要求
				String courseRequirements = course.getFdDemand()==null?"":course.getFdDemand();
				map.put("courseRequirements", buildString(courseRequirements));						
			}
		}
		return JsonUtils.writeObjectToJson(map);
	}
	
	/**
	 * 根据#号分隔字符串,返回list
	 * @param String
	 * @return List
	 */
	private static List buildString(String s) {
		List list = new ArrayList();
		String[] ls = s.split("#");
		for(String tmp:ls){
			if(StringUtil.isEmpty(tmp)){
				continue;
			}
			list.add(tmp);
		}
		return list;
	}
	
	/**
	 * 保存课程的详细信息
	 * @param request
	 */
	@RequestMapping(value = "saveDetailInfo")
	@ResponseBody
	public void saveDetailInfo(HttpServletRequest request) {
		//获取课程ID
		String courseId = request.getParameter("courseId");
		//课程摘要
		String courseAbstract = request.getParameter("courseAbstract");
		//学习目标
		String learnObjectives = request.getParameter("learnObjectives");
		//建议群体
		String suggestedGroup = request.getParameter("suggestedGroup");
		//课程要求
		String courseRequirements = request.getParameter("courseRequirements");
		if(StringUtil.isNotEmpty(courseId)){
			CourseInfo course = courseService.get(courseId);
			if(course!=null){
				//课程摘要
					course.setFdSummary(courseAbstract);
				//学习目标
					course.setFdLearnAim(learnObjectives);
				//建议群体
					course.setFdProposalsGroup(suggestedGroup);
				//课程要求
					course.setFdDemand(courseRequirements);
				courseService.update(course);
			}
		}
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
		//key
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
	
	/**
	 * 修改课程权限(是否公开)
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "updateIsPublish")
	@ResponseBody
	public void updateIsPublish(HttpServletRequest request){
		//获取课程ID
		String courseId = request.getParameter("courseId");
		String isPublish = request.getParameter("isPublish");
		String fdPassword = request.getParameter("fdPassword");
		CourseInfo courseInfo = courseService.findUniqueByProperty("fdId", courseId);
		if(isPublish.equals("true")){
			courseInfo.setIsPublish(true);
		}else{
			courseInfo.setIsPublish(false);
			courseInfo.setFdPassword(fdPassword);
		}
		courseService.update(courseInfo);
	}
}
