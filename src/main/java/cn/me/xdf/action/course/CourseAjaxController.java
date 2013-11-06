package cn.me.xdf.action.course;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

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
import cn.me.xdf.model.course.CourseAuth;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseCategory;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.course.CourseTag;
import cn.me.xdf.model.course.TagInfo;
import cn.me.xdf.model.material.MaterialAuth;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.SysOrgPersonService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.course.CourseAuthService;
import cn.me.xdf.service.course.CourseCatalogService;
import cn.me.xdf.service.course.CourseCategoryService;
import cn.me.xdf.service.course.CourseContentService;
import cn.me.xdf.service.course.CourseService;
import cn.me.xdf.service.course.CourseTagService;
import cn.me.xdf.service.course.SeriesCoursesService;
import cn.me.xdf.service.course.TagInfoService;
import cn.me.xdf.utils.ShiroUtils;

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

	@Autowired
	private SeriesCoursesService seriesCoursesService;

	@Autowired
	private CourseContentService courseContentService;

	@Autowired
	private CourseCatalogService courseCatalogService;

	@Autowired
	private CourseAuthService courseAuthService;
	
	@Autowired
	private AttMainService attMainService;
  
	@Autowired
	private AccountService accountService;
	/**
	 * 获取当前课程的基本信息
	 * 
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = "getBaseCourseInfoById")
	@ResponseBody
	public String getBaseCourseInfoById(HttpServletRequest request) {
		// 获取课程ID
		String courseId = request.getParameter("courseId");
		Map map = new HashMap();

		// 将所有课程分类信息转换成json返回到页面
		List<CourseCategory> categorys = courseCategoryService.findAll();
		if (categorys != null && categorys.size() > 0) {
			List<Map> cateList = new ArrayList<Map>();
			for (CourseCategory category : categorys) {
				Map catemap = new HashMap();
				catemap.put("id", category.getFdId());
				catemap.put("title", category.getFdName());
				cateList.add(catemap);
			}
			map.put("courseTypeList", cateList);
			// 默认将第一个分类选中
			map.put("courseType", categorys.get(0).getFdId());
		}
		if (StringUtil.isNotEmpty(courseId)) {
			CourseInfo course = courseService.get(courseId);
			if (course != null) {
				map.put("courseTit", course.getFdTitle());
				map.put("subTit", course.getFdSubTitle());
				if (course.getFdCategory() != null) {
					map.put("courseType", course.getFdCategory().getFdId());
				}
				// 将课程的标签信息返回到页面
				List<TagInfo> tagList = courseTagService
						.findTagByCourseId(courseId);
				if (tagList != null && tagList.size() > 0) {
					List<String> tags = new ArrayList<String>();
					for (TagInfo tag : tagList) {
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
	 * 
	 * @param request
	 * @return String 课程ID
	 */
	@RequestMapping(value = "saveBaseInfo")
	@ResponseBody
	public String saveBaseInfo(HttpServletRequest request) {
		// 获取课程ID
		String courseId = request.getParameter("courseId");
		// 获取课程标题
		String courseTitle = request.getParameter("courseTitle");
		// 获取课程副标题
		String subTitle = request.getParameter("subTitle");
		// 获取课程标签
		String keyword = request.getParameter("keyword");
		// 获取课程分类ID
		String courseType = request.getParameter("courseType");
		//获取当前用户信息
		SysOrgPerson sysOrgPerson=accountService.load(ShiroUtils.getUser().getId());
		//创建时间
		Date createdate=new Date();
		
		Map map = new HashMap();
		CourseInfo course = new CourseInfo();
		if (StringUtil.isNotEmpty(courseId)) {
			course = courseService.get(courseId);
			if (course == null) {
				course = new CourseInfo();
				course.setFdTitle(courseTitle);
				course.setFdSubTitle(subTitle);
				// 新建课程时总节数设置为0
				course.setFdTotalPart(0);
				course.setFdStatus(Constant.COURSE_TEMPLATE_STATUS_DRAFT);
				course.setIsAvailable(true);
				course.setIsPublish(true);
				// 将分类保存到课程中
				if (StringUtil.isNotEmpty(courseType)) {
					CourseCategory category = courseCategoryService
							.get(courseType);
					course.setFdCategory(category);
				}
				course.setCreator(sysOrgPerson);
				course.setFdCreateTime(createdate);
				course = courseService.save(course);
			} else {
				course.setFdTitle(courseTitle);
				course.setFdSubTitle(subTitle);

				// 将分类保存到课程中
				if (StringUtil.isNotEmpty(courseType)) {
					CourseCategory category = courseCategoryService
							.get(courseType);
					course.setFdCategory(category);
				}
				course = courseService.update(course);
			}
		} else {
			course.setFdTitle(courseTitle);
			course.setFdSubTitle(subTitle);
			// 新建课程时总节数设置为0
			course.setFdTotalPart(0);
			course.setFdStatus(Constant.COURSE_TEMPLATE_STATUS_DRAFT);
			course.setIsAvailable(true);
			course.setIsPublish(true);
			// 将分类保存到课程中
			if (StringUtil.isNotEmpty(courseType)) {
				CourseCategory category = courseCategoryService.get(courseType);
				course.setFdCategory(category);
			}
			course.setCreator(sysOrgPerson);
			course.setFdCreateTime(createdate);
			course = courseService.save(course);
			courseId = course.getFdId();
		}

		// 保存标签库中没有的标签
		if (StringUtil.isNotEmpty(keyword)) {
			String[] tags = keyword.split(",");
			for (String tagName : tags) {
				if (StringUtil.isEmpty(tagName)) {
					continue;
				}
				TagInfo tagInfo = tagInfoService.getTagByName(tagName);
				if (tagInfo == null) {
					tagInfo = new TagInfo();
					tagInfo.setFdName(tagName);
					tagInfo = tagInfoService.save(tagInfo);
				}
				// 保存课程与标签的关系
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
	 * 
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = "getDetailCourseInfoById")
	@ResponseBody
	public String getDetailCourseInfoById(HttpServletRequest request) {
		// 获取课程ID
		String courseId = request.getParameter("courseId");
		Map map = new HashMap();
		if (StringUtil.isNotEmpty(courseId)) {
			CourseInfo course = courseService.get(courseId);
			if (course != null) {
				// 课程摘要
				map.put("courseAbstract", course.getFdSummary());
				// 课程作者
				map.put("courseAuthor", course.getFdAuthor());
				// 作者描述
				map.put("authorDescrip", course.getFdAuthorDescription());
				// 学习目标
				String learnObjectives = course.getFdLearnAim() == null ? ""
						: course.getFdLearnAim();
				map.put("learnObjectives", buildString(learnObjectives));
				// 建议群体
				String suggestedGroup = course.getFdProposalsGroup() == null ? ""
						: course.getFdProposalsGroup();
				map.put("suggestedGroup", buildString(suggestedGroup));
				// 课程要求
				String courseRequirements = course.getFdDemand() == null ? ""
						: course.getFdDemand();
				map.put("courseRequirements", buildString(courseRequirements));
			}
		}
		return JsonUtils.writeObjectToJson(map);
	}

	/**
	 * 根据#号分隔字符串,返回list
	 * 
	 * @param String
	 * @return List
	 */
	private static List buildString(String s) {
		List list = new ArrayList();
		String[] ls = s.split("#");
		for (String tmp : ls) {
			if (StringUtil.isEmpty(tmp)) {
				continue;
			}
			list.add(tmp);
		}
		return list;
	}

	/**
	 * 保存课程的详细信息
	 * 
	 * @param request
	 */
	@RequestMapping(value = "saveDetailInfo")
	@ResponseBody
	public void saveDetailInfo(HttpServletRequest request) {
		// 获取课程ID
		String courseId = request.getParameter("courseId");
		// 课程摘要
		String courseAbstract = request.getParameter("courseAbstract");
		// 课程作者
		String courseAuthor = request.getParameter("courseAuthor");
		// 作者描述
		String authorDescrip = request.getParameter("authorDescrip");
		// 学习目标
		String learnObjectives = request.getParameter("learnObjectives");
		// 建议群体
		String suggestedGroup = request.getParameter("suggestedGroup");
		// 课程要求
		String courseRequirements = request.getParameter("courseRequirements");
		if (StringUtil.isNotEmpty(courseId)) {
			CourseInfo course = courseService.get(courseId);
			if (course != null) {
				// 课程摘要
				course.setFdSummary(courseAbstract);
				// 课程作者
				course.setFdAuthor(courseAuthor);
				// 作者描述
				course.setFdAuthorDescription(authorDescrip);
				// 学习目标
				course.setFdLearnAim(learnObjectives);
				// 建议群体
				course.setFdProposalsGroup(suggestedGroup);
				// 课程要求
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
	public List<TagInfo> findTagInfosByKey(HttpServletRequest request) {
		// key
		String key = request.getParameter("q");
		List<TagInfo> tagInfos = tagInfoService.findTagInfosByKey(key);
		return tagInfos;
	}

	/**
	 * 修改课程权限(是否公开)
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "updateIsPublish")
	@ResponseBody
	public void updateIsPublish(HttpServletRequest request) {
		// 获取课程ID
		String courseId = request.getParameter("courseId");
		String isPublish = request.getParameter("isPublish");
		String fdPassword = request.getParameter("fdPassword");
		CourseInfo courseInfo = courseService.findUniqueByProperty("fdId",
				courseId);
		if (isPublish.equals("open")) {
			courseInfo.setIsPublish(true);
			courseInfo.setFdPassword("");
		} else {
			courseInfo.setIsPublish(false);
			courseInfo.setFdPassword(fdPassword);
		}
		courseService.update(courseInfo);
	}

	/**
	 * 得到指定课程的权限信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getAuthInfoByCourseId")
	@ResponseBody
	public String getAuthInfoByCourseId(HttpServletRequest request) {
		// 获取课程ID
		String courseId = request.getParameter("courseId");
		List<Map> list = courseService.findAuthInfoByCourseId(courseId);
		Map map = new HashMap();
		map.put("user", list);
		return JsonUtils.writeObjectToJson(map);
	}

	/**
	 * 得到指定课程的是否公开信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getIsPublishInfo")
	@ResponseBody
	public String getIsPublishInfo(HttpServletRequest request) {
		String courseId = request.getParameter("courseId");
		CourseInfo courseInfo = courseService.get(courseId);
		Map map = new HashMap();
		map.put("action", "");
		map.put("permission", (courseInfo.getIsPublish() == null || courseInfo
				.getIsPublish() == true) ? "open" : "encrypt");
		map.put("encryptType",
				StringUtil.isBlank(courseInfo.getFdPassword()) ? "authorized"
						: "passwordProtect");
		map.put("coursePwd", courseInfo.getFdPassword());
		return JsonUtils.writeObjectToJson(map);
	}

	/*
	 * 查询课程列表 或者根据关键字搜索 author hanhl
	 */
	@RequestMapping(value = "getCoureInfosOrByKey")
	public String getCoureInfosOrByKey(Model model, HttpServletRequest request) {
		String fdTitle = request.getParameter("fdTitle");
		String pageNoStr = request.getParameter("pageNo");
		String orderbyStr = request.getParameter("order");
		Pagination page = courseService.findCourseInfosByName( fdTitle,
				pageNoStr, orderbyStr);
		model.addAttribute("page", page);
		return "/course/divcourselist";
	}
	
	/*
	 * 查询课程列表 或者根据关键字搜索 author hanhl
	 */
	@RequestMapping(value = "deleteAllCoursesByKey")
	public String deleteAllCoursesByKey(Model model, HttpServletRequest request) {
		String fdTitle = request.getParameter("fdTitle");
		String pageNoStr = request.getParameter("pageNo");
		String orderbyStr = request.getParameter("order");
		Pagination page = courseService.findCourseInfosByName( fdTitle,
				pageNoStr, orderbyStr);
		int i = page.getTotalPage();
		if(i>0){
			for(int j=0;j<i;j++){
				page = courseService.findCourseInfosByName( fdTitle,
						"1", orderbyStr);
				List list = page.getList();
				if(list!=null && list.size()>0){
					for(Object obj:list){
						Map map = (Map)obj;
						String courseId = (String)map.get("FDID");
						if(ShiroUtils.isAdmin()){//管理员删除不判断权限
							delCourseById(courseId);
						}
						if(StringUtil.isNotBlank(findEditAuth(courseId))){//非管理员删除
							delCourseById(courseId);
						}
					}
				}
			}
		}
		
		return "redirect:/ajax/course/getCoureInfosOrByKey";
	}

	/**
	 * 修改课程授权信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "updateCourseAuth")
	@ResponseBody
	public void updateCourseAuth(HttpServletRequest request) {
		String courseId = request.getParameter("courseId");

		String data = request.getParameter("data");
		List<Map> list = JsonUtils.readObjectByJson(data, List.class);
		List<CourseAuth> auths = new ArrayList<CourseAuth>();
		CourseInfo course = new CourseInfo();
		course.setFdId(courseId);
		for (Map map : list) {
			CourseAuth auth = new CourseAuth();
			auth.setCourse(course);
			SysOrgPerson fdUser = accountService.load((String) map.get("id"));
			auth.setFdUser(fdUser);
			auth.setIsAuthStudy((Boolean) map.get("tissuePreparation"));
			auth.setIsEditer((Boolean) map.get("editingCourse"));
			auths.add(auth);
		}
		courseService.updateCourseAuth(courseId, auths);
	}

	/**
	 * 删除课程
	 * 
	 * @param request
	 */
	@RequestMapping(value = "deleteCourse")
	@ResponseBody
	public void deleteCourse(HttpServletRequest request) {
		// 获取课程ID
		String fdIds = request.getParameter("courseId");
		if (StringUtil.isNotEmpty(fdIds)) {
			String[] courses = fdIds.split(",");
			String courseId = "";
			for(int i=0;i<courses.length;i++){
				courseId = courses[i];
				//delCourseById(courseId);
				System.out.println(courseId);
			}
		}
	}
	
	//删除课程
	private void delCourseById(String courseId){
		CourseInfo course = courseService.get(courseId);
		if (course != null && course.getIsAvailable()) {
			// 需要判断课程状态是发布还是草稿，如果是发布，则只改是否有效的状态，如果是草稿，则删除课程及课程相关数据。
			if (Constant.COURSE_TEMPLATE_STATUS_DRAFT.equals(course
					.getFdStatus())) {
				// 删除课程与关键字的关系
				courseTagService.deleteByCourseId(courseId);
				// 删除课程权限
				courseAuthService.deleCourseAuthByCourseId(courseId);
				// 获取课程下的所有章节
				List<CourseCatalog> list = courseCatalogService
						.getCatalogsByCourseId(courseId);
				if (list != null && list.size() > 0) {
					for (CourseCatalog catalog : list) {
						if (Constant.CATALOG_TYPE_LECTURE == catalog
								.getFdType()) {
							// 删除节与内容的关系
							courseContentService.deleteByCatalogId(catalog
									.getFdId());
						}
					}
				}
				// 删除章节
				courseCatalogService.deleteByCourseId(courseId);
				// 删除课程
				courseService.delete(courseId);
			} else {
				// 删除已发布课程模板时，需要删除课程与系列的关系，则否会在系列中显示该课程，其他关系保持不变。
				seriesCoursesService.deleteByCourseId(courseId);
				// 修改课程模板有效状态
				course.setIsAvailable(false);
				courseService.update(course);
			}
		}
	}

	/*
	 * 课程封页图片 author hanhl
	 */
	@RequestMapping(value = "cover")
	@ResponseBody
	public void courseCover(HttpServletRequest request) {
		String courseId = request.getParameter("courseId");
		String attMainId = request.getParameter("attId");
		if(StringUtil.isNotBlank(attMainId)){
			// 先清理附件库(清理该课程下的原始附件)
			attMainService.deleteAttMainByModelId(courseId);
			AttMain attMain = attMainService.get(attMainId);
			attMain.setFdModelId(courseId);
			attMain.setFdModelName(CourseInfo.class.getName());
			attMain.setFdKey("Course");
			// 保存最新的附件
			attMainService.update(attMain);
		}
	}

	/*
	 * 加载课程页面
	 */
	@RequestMapping(value = "showcover")
	@ResponseBody
	public String getCourseCoverById(HttpServletRequest

	request) {
		String courseId = request.getParameter("courseId");
		AttMain attMain = attMainService.getByModelId(courseId);
		Map map = new HashMap();
		if(attMain!=null){
		map.put("coverUrl", attMain.getFdId());
		}else{
			map.put("coverUrl", "");	
		}
		return JsonUtils.writeObjectToJson(map);
	}
	/*
	 * 删除数据过滤:
	 */
	
	@RequestMapping(value = "deleFiter")
	@ResponseBody
	public String deleteFiter(HttpServletRequest request){
		String fdIds = request.getParameter("courseId");
		String fdName= request.getParameter("fdName");
		String deleType=request.getParameter("deleType");
		if("0".equals(deleType)){//选择删除
			if(ShiroUtils.isAdmin()){
				return "redirect:/ajaxt/course/deleteCourse?courseId"+fdIds;//超管直接跳到删除方法删除
			}else{
				return getDeleteKeys(fdIds);//获取可删除id
			}
		}else{//删除全部
			if(ShiroUtils.isAdmin()){//管理员删除
				return "redirect:/ajaxt/course/deleteAllCoursesByKey?fdTitle="+fdName;//超管直接跳到删除方法删除
			}else{
				return getDeleteKeys(fdIds);
			}
		}
	}
	/*
	 * 根据课程id查找课程权限
	 * author hanhl
	 * 1.当前课程创建者
	 * 2.拥有编辑权限
	 */
	private String findEditAuth(String couserId){
		CourseInfo courseInfo =courseService .load(couserId);
		if(courseInfo.getCreator().getFdId().equals(ShiroUtils.getUser().getId())){
			return courseInfo.getFdId();
		}
		CourseAuth auth = courseAuthService.findByCourseIdAndUserId(couserId,ShiroUtils.getUser().getId());
	    if(auth!=null&&auth.getIsEditer()==true){
	    	return courseInfo.getFdId();
	    }
		
		return null;
		   
	}
	/*
	 * 获取可执行删除的课程id返回页面
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private String getDeleteKeys(String fdIds){
		List deltes=new ArrayList();
		Map delekeys = new HashMap();
		String[] courseIds = fdIds.split(",");
		String fdId = "";
		String auth = "";
		for (int i = 0; i < courseIds.length; i++) {
			fdId = courseIds[i];
			auth = findEditAuth(fdId);
			if(StringUtil.isBlank(auth)){
				continue;
			}else{
				delekeys.put("id",auth);
				deltes.add(delekeys);
			}
		}
		return JsonUtils.writeObjectToJson(deltes);
		 
	}

}
