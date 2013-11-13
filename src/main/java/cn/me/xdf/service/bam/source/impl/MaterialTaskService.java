package cn.me.xdf.service.bam.source.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import cn.me.xdf.common.hibernate4.Value;
import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.material.Task;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.model.process.SourceNote;
import cn.me.xdf.model.process.TaskRecord;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.SimpleService;
import cn.me.xdf.service.bam.BamCourseService;
import cn.me.xdf.service.bam.process.SourceNodeService;
import cn.me.xdf.service.bam.source.ISourceService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.material.MaterialService;
import cn.me.xdf.service.material.TaskService;
import cn.me.xdf.utils.ShiroUtils;

@Service("materialTaskService")
public class MaterialTaskService extends SimpleService implements ISourceService  {
	
	@Autowired
	private AttMainService attMainService;
	
	@Autowired
	private BamCourseService bamCourseService;
	
	@Autowired
	private MaterialService materialService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private SourceNodeService sourceNodeService;
	
	@Autowired
	private AccountService accountService;
	
	@Override
    public Object findSourceByMaterials(BamCourse bamCourse, CourseCatalog catalog) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object saveSourceNode(WebRequest request) {
		String catalogId = request.getParameter("catalogId");
		String bamId = request.getParameter("bamId");
		String taskPaperId = request.getParameter("fdid");
		BamCourse bamCourse = bamCourseService.get(BamCourse.class, bamId);
		MaterialInfo info = materialService.get(taskPaperId);
		SourceNote sourceNode =new SourceNote();
		List<Task> taskList = taskService.findByProperty("taskPackage.fdId",taskPaperId);
		Set<TaskRecord> answerRecords = new HashSet<TaskRecord>();
		List<AttMain> attMains = new ArrayList<AttMain>();
		for (Task task : taskList) {
			TaskRecord taskRecord = new TaskRecord();
			taskRecord.setFdTaskId(task.getFdId());//对应作业id
			if(task.getFdType().equals(Constant.TASK_TYPE_ONLINE)){//在线作答
				String answer = request.getParameter("answer_"+task.getFdId());
				taskRecord.setFdAnswer(answer);//保存答案
				if(StringUtil.isBlank(answer)){
					taskRecord.setFdStatus(Constant.TASK_STATUS_UNFINISH);//00 未答
				} else {
					taskRecord.setFdStatus(Constant.TASK_STATUS_FINISH);//01 答完
				}
			}
			if(task.getFdType().equals(Constant.TASK_TYPE_UPLOAD)){//上传作业
				////先清空以前的附件
				attMainService.deleteAttMainByModelId(taskRecord.getFdId());
				///保存附件
				String[] taskAttArrId = request.getParameterValues("attach_"+task.getFdId());
				for (String taskAttId : taskAttArrId) {
					AttMain attMain = attMainService.get(taskAttId);
					attMain.setFdModelId(taskRecord.getFdId());
					attMain.setFdModelName(TaskRecord.class.getName());
					attMain.setFdKey(task.getFdId());
					attMainService.save(attMain);
					attMains.add(attMain);
				}
				if(attMains.isEmpty()){
					taskRecord.setFdStatus(Constant.TASK_STATUS_UNFINISH);//00 未答
				} else {
					taskRecord.setFdStatus(Constant.TASK_STATUS_FINISH);//01 答完
				}
				taskRecord.setAttMains(attMains);
			}
			answerRecords.add(taskRecord);
		}
		sourceNode.setTaskRecords(answerRecords);
		sourceNode.setFdCourseId(bamCourse.getCourseId());
		sourceNode.setFdCatalogId(catalogId);
		sourceNode.setFdUserId(ShiroUtils.getUser().getId());
		sourceNode.setFdOperationDate(new Date());
		sourceNode.setFdMaterialId(info.getFdId());
		if(answerRecords.isEmpty()){
			sourceNode.setFdStatus(Constant.TASK_STATUS_UNFINISH);
		}else{
			sourceNode.setFdStatus(Constant.TASK_STATUS_FINISH);
		}
		sourceNodeService.save(sourceNode);
        return null;  
    }
    

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object findSubInfoByMaterial(WebRequest request) {
		String materialId = request.getParameter("materialId");
		String catalogId = request.getParameter("catalogId");//节id
		MaterialInfo materialInfo = materialService.load(materialId);
		List<Task> taskList = materialInfo.getTasks();
		//存放作业列表
		List<Map> list = new ArrayList<Map>();
		for(Task task:taskList){
			Map taskMap = new HashMap();
			taskMap.put("id", task.getFdId());
			taskMap.put("index", task.getFdOrder());
		    
			taskMap.put("examType", task.getFdType().equals(Constant.TASK_TYPE_UPLOAD)?"uploadWork":"onlineAnswer");
			taskMap.put("examScore", task.getFdStandardScore().intValue()); 	
			taskMap.put("examName", task.getFdName());
			taskMap.put("examStem", task.getFdSubject());
			
			List<AttMain> taskAtt = attMainService.getByModeslIdAndModelNameAndKey(task.getFdId(), Task.class.getName(),"taskAtt");
		    
			taskMap.put("listAttachment", taskAtt);//存放作业附件信息
			
			// ///////////////评分人操作信息
			SourceNote sourceNote = sourceNodeService.getSourceNote(materialInfo.getFdId(),
					catalogId, ShiroUtils.getUser().getId());
			if (sourceNote != null) {
				List<Map> recordList = new ArrayList<Map>();
				Set<TaskRecord> taskRecordList = sourceNote.getTaskRecords();
				for (TaskRecord taskRecord : taskRecordList) {
					if(taskRecord.getFdTaskId().equals(task.getFdId())){
						Map record = new HashMap();
						record.put("score", taskRecord.getFdScore() == null ? 0: sourceNote.getFdScore());
						record.put("comment", taskRecord.getFdComment());
						if(StringUtil.isNotBlank(sourceNote.getFdAppraiserId())){
							SysOrgPerson person = accountService.load(sourceNote.getFdAppraiserId());
							Map teacher= new HashMap();
							teacher.put("imgUrl", person.getPoto());
							record.put("teacher", teacher);
							
						}
						recordList.add(record);
						///作业状态
						if(taskRecord.getFdStatus().equals(Constant.TASK_STATUS_UNFINISH)){
							taskMap.put("status", "null");
						}else if(taskRecord.getFdStatus().equals(Constant.TASK_STATUS_FINISH)){
							taskMap.put("status", "finish");
						}
						
						List<AttMain> answerAtt = attMainService.getByModeslIdAndModelNameAndKey(taskRecord.getFdId(), TaskRecord.class.getName(),task.getFdId());
						taskMap.put("listTaskAttachment", answerAtt);//存放答题者上传的附件
						taskMap.put("answer", taskRecord.getFdAnswer());
						break;
					}
				}
				taskMap.put("teacherRating", recordList);
			}
			
			list.add(taskMap);
		}
		return list;
	}
}
