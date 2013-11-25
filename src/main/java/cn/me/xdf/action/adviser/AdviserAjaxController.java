package cn.me.xdf.action.adviser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseCategory;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.material.Task;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.model.process.SourceNote;
import cn.me.xdf.model.process.TaskRecord;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.adviser.AdviserService;
import cn.me.xdf.service.bam.process.SourceNodeService;
import cn.me.xdf.service.base.AttMainService;
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
	
	@Autowired
	private SourceNodeService sourceNodeService;
	
	@Autowired
	private AttMainService attMainService;
	
	/**
	 * 找出我所批改的作业
	 * @param model
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "findCheckTaskList")
	@ResponseBody
	public String findCheckTaskList(HttpServletRequest request){
		String pageNoStr = request.getParameter("pageNo");
		String keyword = request.getParameter("keyword");//搜索项
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
		Pagination page  = adviserService.findAdivserCouserList(fdType, pageNo, SimplePage.DEF_COUNT, keyword, order);
		Map data =  new HashMap();
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
				user.put("org", person.getHbmParent()==null?"":person.getHbmParent());
				user.put("department", person.getDeptName());
				user.put("phone", person.getFdMobileNo());
				user.put("mail", person.getFdEmail());
				map.put("user", user);////封装人员信息
				CourseInfo courseInfo = courseService.get((String)pageMap.get("FDCOURSEID"));
				map.put("courseName", courseInfo.getFdTitle());//课程名字
				map.put("mentor", ShiroUtils.getUser().getName());
				CourseCatalog courseCatalog = courseCatalogService.get((String)pageMap.get("FDCATALOGID"));
				map.put("currLecture", courseCatalog.getFdName());//节名称
				MaterialInfo info = materialService.load((String)pageMap.get("FDMATERIALID"));
				if(fdType.equalsIgnoreCase("unchecked")){//未检查
					SourceNote note = sourceNodeService.get(SourceNote.class, (String)pageMap.get("FDID"));
					Set<TaskRecord> taskRexords = note.getTaskRecords();
					List<String> attAll = new ArrayList<String>();
					for (TaskRecord taskRecord : taskRexords) {
						List<AttMain> attMains = attMainService.getAttMainsByModelIdAndModelName(taskRecord.getFdId(), TaskRecord.class.getName());
						if(attMains!=null&&!attMains.isEmpty()){
							for (AttMain attMain : attMains) {
								if(attMain!=null){
									attAll.add(attMain.getFdId());
								}
							}
						}
					}
					String zipname = courseInfo.getFdTitle()+"_"+info.getFdName()+"_"+person.getRealName();
					map.put("downloadBoxUrl", attAll.toArray());//下载作业包
					map.put("zipname", zipname);
				}else{
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
		Map paging = new HashMap();
		paging.put("totalPage", page.getTotalPage());
		paging.put("currentPage", page.getPageNo());
		paging.put("totalCount", page.getTotalCount());
		paging.put("StartPage", page.getStartPage());
		paging.put("EndPage",page.getEndPage());
		paging.put("StartOperate", page.getStartOperate());
		paging.put("EndOperate", page.getEndOperate());
		paging.put("startNum", page.getStartNum());
		paging.put("endNum", page.getEndNum());
		data.put("checkedData", checkedData);
		data.put("paging",paging);
		return JsonUtils.writeObjectToJson(data);
	}
	
	

}
