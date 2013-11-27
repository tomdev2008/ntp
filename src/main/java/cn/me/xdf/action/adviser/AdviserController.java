package cn.me.xdf.action.adviser;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.page.SimplePage;
import cn.me.xdf.common.utils.excel.AbsExportExcel;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.material.Task;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.model.process.SourceNote;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.adviser.AdviserService;
import cn.me.xdf.service.bam.process.SourceNodeService;
import cn.me.xdf.service.course.CourseCatalogService;
import cn.me.xdf.service.course.CourseService;
import cn.me.xdf.service.material.MaterialService;
import cn.me.xdf.utils.ShiroUtils;
import cn.me.xdf.view.model.VCheckTaskData;

@Controller
@RequestMapping(value = "/adviser")
@Scope("request")
public class AdviserController {
	
	@Autowired
	private MaterialService materialService;
	
	@Autowired
	private AdviserService adviserService;
	
	@Autowired
	private SourceNodeService sourceNodeService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private CourseCatalogService courseCatalogService;
	
	/**
	 * 导出xls文件
	 * @param modelIds
	 * @param fdType 批改/未批改
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportDataList/{modelIds}/{fdType}")
	public String exportDataList(@PathVariable("modelIds") String[] modelIds,@PathVariable("fdType") String fdType,
			 HttpServletRequest request, HttpServletResponse response){
		List<VCheckTaskData> adviserList = new ArrayList<VCheckTaskData>();
		for (String modelId : modelIds) {
			SourceNote note = sourceNodeService.get(SourceNote.class, modelId);
			SysOrgPerson person = (SysOrgPerson)accountService.load(note.getFdUserId());//人员信息
			VCheckTaskData vdata = new VCheckTaskData();
			vdata.setUserName(person.getRealName());//名字
			vdata.setUserDept(person.getDeptName());//部门名字
			vdata.setUserEmail(person.getFdEmail());//邮箱
			vdata.setUserTel(person.getFdMobileNo());//电话
			CourseInfo courseInfo = courseService.get(note.getFdCourseId());
			vdata.setCourseName(courseInfo.getFdTitle());//课程
			CourseCatalog courseCatalog = courseCatalogService.get(note.getFdCatalogId());
			vdata.setCurrentCatalog(courseCatalog.getFdName());//节
			vdata.setGuideName(ShiroUtils.getUser().getName());//导师名字
			if(fdType.equalsIgnoreCase("check")){
				MaterialInfo info = materialService.load(note.getFdMaterialId());
				List<Task> tasks = info.getTasks();
				Double totalScore = 0.0;
				for (Task task : tasks) {
					totalScore += task.getFdStandardScore();
				}
				vdata.setTotalScore(totalScore);//总分
				vdata.setPassScore(info.getFdScore());//标准分
				vdata.setGuideScore(note.getFdScore());//导师打分
				if(note.getFdStatus().equals(Constant.TASK_STATUS_FAIL)){
					vdata.setPass("否");
				}
				if(note.getFdStatus().equals(Constant.TASK_STATUS_PASS)){
					vdata.setPass("是");
				}
			}
			adviserList.add(vdata);
		}
		AbsExportExcel.exportExcel(adviserList, fdType+"Data.xls", response);
		return null;
	}
	/**
	 * 导出全部导师批改作业列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportAllDataList/{fdType}")
	public String exportAllDataList(@PathVariable("fdType") String fdType,
			    HttpServletRequest request, HttpServletResponse response){
		String order = request.getParameter("order");
		String keyword = request.getParameter("keyword");
		List<VCheckTaskData> adviserList = new ArrayList<VCheckTaskData>();
		Pagination page = adviserService.findAdivserCouserList(fdType, 1, SimplePage.DEF_COUNT, keyword, order);
		
		AbsExportExcel.exportExcel(adviserList, fdType+"Data.xls", response);
		return null;
	}
	
	/**
	 * 返回到时批改作业页面
	 * @return
	 */
	@RequestMapping(value = "checkTask")
	public String checkTask(){
		return "/adviser/checkTask";
	}

}
