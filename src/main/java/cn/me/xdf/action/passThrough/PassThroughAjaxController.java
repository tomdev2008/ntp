package cn.me.xdf.action.passThrough;

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

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.page.SimplePage;
import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.bam.BamCourseService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.course.CourseService;
import cn.me.xdf.utils.ShiroUtils;


@Controller
@RequestMapping(value = "/ajax/passThrough")
@Scope("request")
public class PassThroughAjaxController {
	
	@Autowired
	private AttMainService attMainService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private BamCourseService bamCourseService;
	
	@Autowired
	private AccountService accountService;

	/**
	 * 最新课程列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getNewCourseList")
	@ResponseBody
	public String getNewCourseList(HttpServletRequest request) {
		List result = new ArrayList();
		Pagination page = courseService.discoverCourses(ShiroUtils.getUser().getId(),1,5);
		if(page.getTotalCount()>0){
			List list = page.getList();
			for(int i=0;i<list.size();i++){
				Map course = new HashMap();
				course = (Map)list.get(i);
				AttMain attMain = attMainService.getByModelIdAndModelName((String)course.get("FDID"), CourseInfo.class.getName());
				course.put("ATTID", attMain==null?"":attMain.getFdId());
				result.add(course);
			}
		}
		return JsonUtils.writeObjectToJson(result);
	}
	
	/**
	 * 正在学习课程的教师列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getPageByLeaningTeacher")
	@ResponseBody
	public String getPageByLeaningTeacher(HttpServletRequest request) {
		String pageNoStr = request.getParameter("pageNo");
		String courseId = request.getParameter("courseId");
		int pageNo;
		if (StringUtil.isNotBlank(pageNoStr)) {
			pageNo = Integer.parseInt(pageNoStr);
		} else {
			pageNo = 1;
		}
		List result = new ArrayList();
		Finder finder = Finder.create(" from BamCourse b where b.courseId = :courseId ");
		finder.setParam("courseId", courseId);
		Pagination page = bamCourseService.getPage(finder, pageNo, SimplePage.DEF_COUNT);
		if(page.getTotalCount()>0){
			List list = page.getList();
			for(int i=0;i<list.size();i++){
				BamCourse bam = (BamCourse)list.get(i);
				SysOrgPerson person = accountService.load(bam.getPreTeachId());
				Map people = new HashMap();
				people.put("id", person.getFdId());
				people.put("name", person.getRealName());
				people.put("photoUrl", person.getPoto());
				result.add(people);
			}
		}
		Map map = new HashMap();
		map.put("totalPage", page.getTotalPage());
		map.put("currentPage", pageNo);
		map.put("list", result);
		map.put("totalCount", page.getTotalCount());
		return JsonUtils.writeObjectToJson(map);
	}
}
