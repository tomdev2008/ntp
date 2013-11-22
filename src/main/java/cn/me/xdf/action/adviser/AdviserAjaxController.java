package cn.me.xdf.action.adviser;

import java.util.ArrayList;
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
import cn.me.xdf.common.page.SimplePage;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseCategory;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.material.Task;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.adviser.AdviserService;
import cn.me.xdf.service.course.CourseCatalogService;
import cn.me.xdf.service.course.CourseCategoryService;
import cn.me.xdf.service.course.CourseService;
import cn.me.xdf.service.material.MaterialService;
import cn.me.xdf.utils.ShiroUtils;

@Controller
@RequestMapping(value = "/ajax/adviser")
@Scope("request")
public class AdviserAjaxController {
	
	@Autowired
	private AdviserService adviserService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private CourseCatalogService courseCatalogService;
	
	@Autowired
	private MaterialService materialService;
	
	/**
	 * 找出我所批改的作业
	 * @param model
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "findCheckTaskList")
	@ResponseBody
	public List findCheckTaskList(Model model, HttpServletRequest request){
		String pageNoStr = request.getParameter("pageNo");
		String fdName = request.getParameter("fdName");
		String fdType = request.getParameter("fdType");//已批改、未批改
		String order = request.getParameter("order");
		if(StringUtil.isBlank(order)){
			order = "fdcreatetime";
		}
		if(StringUtil.isBlank(fdType)){
			fdType = "unchecked";
		}
		int pageNo;
		if (StringUtil.isNotBlank(pageNoStr)) {
			pageNo = Integer.parseInt(pageNoStr);
		} else {
			pageNo = 1;
		}
		Pagination page  = adviserService.findAdivserCouserList("unchecked", pageNo, SimplePage.DEF_COUNT, fdName, order);
		model.addAttribute("page", page);
		List checkedData = new ArrayList();
		if(page.getTotalCount()>0){
			List list = page.getList();
			for(int i=0;i<list.size();i++){
				Map map = new HashMap();
				Map pageMap = (Map) list.get(i);
				map.put("id", (String)pageMap.get("FDID"));
				SysOrgPerson person = accountService.findById((String)pageMap.get("FDUSERID"));//学习课程人员
				Map user = new HashMap();//封装人员信息
				user.put("name", person.getRealName());
				user.put("imgUrl", person.getPoto());
				user.put("org", person.getHbmParent());
				user.put("department", person.getDeptName());
				user.put("phone", person.getFdNo());
				user.put("mail", person.getFdEmail());
				map.put("user", user);////封装人员信息
				CourseInfo courseInfo = courseService.get((String)pageMap.get("FDCOURSEID"));
				map.put("courseName", courseInfo.getFdTitle());//课程名字
				map.put("mentor", ShiroUtils.getUser().getName());
				CourseCatalog courseCatalog = courseCatalogService.get((String)pageMap.get("FDCATALOGID"));
				map.put("currLecture", courseCatalog.getFdName());//节名称
				if(fdType.equalsIgnoreCase("unchecked")){//未检查
					map.put("downloadBoxUrl", null);//下载作业包
				}else{
					MaterialInfo info = materialService.load((String)pageMap.get("FDMATERIALID"));
					List<Task> tasks = info.getTasks();
					Double totalScore = 0.0;
					for (Task task : tasks) {
						totalScore += task.getFdStandardScore();
					}
					map.put("scoreTotal", totalScore);//总分
					map.put("scorePass", info.getFdScore());//及格分
					map.put("score", pageMap.get("FDSCORE"));//老师打分
					if(pageMap.get("FDSTATUS").equals(Constant.TASK_STATUS_FAIL)){
						map.put("isPass", false);//没有通过
					}else if(pageMap.get("FDSTATUS").equals(Constant.TASK_STATUS_PASS)){
						map.put("isPass", true);//通过
					}
				}
				checkedData.add(map);
			} 
		}
		return checkedData;
	}
	
	

}
