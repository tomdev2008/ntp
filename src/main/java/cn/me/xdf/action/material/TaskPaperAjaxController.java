package cn.me.xdf.action.material;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.me.xdf.common.hibernate4.Value;
import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.material.ExamOpinion;
import cn.me.xdf.model.material.ExamQuestion;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.material.Task;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.material.MaterialService;
import cn.me.xdf.service.material.TaskService;

import cn.me.xdf.utils.ShiroUtils;

@Controller
@RequestMapping(value = "/ajax/taskPaper")
@Scope("request")
public class TaskPaperAjaxController {
	
	@Autowired
	private MaterialService materialService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value = "saveOrUpdateTaskPaper")
	@ResponseBody
	public Map<String,String> saveOrUpdateTaskPaper(HttpServletRequest request){
		Map<String,String> resultMap = new HashMap<String,String>();
		String materialId = request.getParameter("materialId");
		MaterialInfo info;
		String taskId = request.getParameter("taskId");
		Task task;
		if(StringUtil.isBlank(materialId)){
			info = new MaterialInfo();
			info.setCreator((SysOrgPerson) accountService.load(ShiroUtils.getUser().getId()));
			info.setFdType(Constant.MATERIAL_TYPE_JOBPACKAGE);
			info.setFdCreateTime(new Date());
			info.setIsPublish(true);
			info.setIsDownload(true);
			info.setIsAvailable(true);
			materialService.save(info);
		}else{
			info = materialService.load(materialId);
		}
		if(StringUtil.isBlank(taskId)||taskId.equals("undefined")){
			task = new Task();
			//题型 01上传作业，02在线作答
			String examType = request.getParameter("examType");
			//作业题目
			String examName = request.getParameter("examName");
			//作业简介
			String fdSubject = request.getParameter("examStem");
			//作业总分
			String examScore = request.getParameter("examScore");
			task.setFdName(examName);
			task.setFdStandardScore(Double.parseDouble(examScore));
			if(examType.equals("uploadWork")){
				task.setFdType(Constant.TASK_TYPE_UPLOAD);//上传作业
			} else {
				task.setFdType(Constant.TASK_TYPE_ONLINE);//在线作答
			}
			task.setFdSubject(fdSubject);
			task.setTaskPackage(info);
			taskService.save(task);
		}
		resultMap.put("materialId", info.getFdId());
		return resultMap;
	}
	
	@RequestMapping(value = "getTaskByMaterialId")
	@ResponseBody
	public String getTaskByMaterialId(HttpServletRequest request) {
		String materialId = request.getParameter("materialId");
		MaterialInfo info = materialService.load(materialId);
		List<Task> taskList = info.getTasks();
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> map = new HashMap<String,String>();
		for(Task task:taskList){
			map.put("id", task.getFdId());
			map.put("subject", task.getFdName());
			map.put("score", task.getFdStandardScore()+"");
		    list.add(map);
		}
		return JsonUtils.writeObjectToJson(list);
	}

	
	@RequestMapping(value = "saveOrUpdate")
	@ResponseBody
	public void saveOrUpdate(HttpServletRequest request){
		String taskId = request.getParameter("taskId");
		Task task;
		if(StringUtil.isBlank(taskId)){
			task = new Task();
			//题型 01上传作业，02在线作答
			String examType = request.getParameter("examType");
			//作业题目
			String examName = request.getParameter("examName");
			//作业简介
			String fdSubject = request.getParameter("examStem");
			//作业总分
			String examScore = request.getParameter("examScore");
			task.setFdName(examName);
			task.setFdStandardScore(Double.parseDouble(examScore));
			if(examType.equals("uploadWork")){
				task.setFdType(Constant.TASK_TYPE_UPLOAD);//上传作业
			} else {
				task.setFdType(Constant.TASK_TYPE_ONLINE);//在线作答
			}
			task.setFdSubject(fdSubject);
			taskService.save(task);
		}
	}
	
	
	
}
