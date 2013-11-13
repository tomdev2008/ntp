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
import cn.me.xdf.model.process.SourceNote;
import cn.me.xdf.model.process.TaskRecord;
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
		sourceNode.setFdCourseId(bamCourse.getCourseId());
		sourceNode.setFdCatalogId(catalogId);
		sourceNode.setFdUserId(ShiroUtils.getUser().getId());
		sourceNode.setFdOperationDate(new Date());
		sourceNode.setFdExamTime(info.getFdStudyTime());
		
		// request.getParameterValues("attach_"+task.getFdId())
		List<Task> taskList = taskService.findByProperty("taskPackage.fdId",taskPaperId);
		for (Task task : taskList) {
			String taskAttIds = request.getParameter("attach_" + task.getFdId());
			String[] taskAttId = taskAttIds.split(",");
			for (int i = 0; i < taskAttId.length; i++) {
             
			}
		}
		Set<TaskRecord> answerRecords = new HashSet<TaskRecord>();
		for (Task task : taskList) {
			TaskRecord taskRecord = new TaskRecord();
			taskRecord.setFdTaskId(task.getFdId());
			if(task.getFdType().equals(Constant.TASK_TYPE_ONLINE)){//在线作答
				String answer = request.getParameter("answer_"+task.getFdId());
				taskRecord.setFdAnswer(answer);
				if(StringUtil.isBlank(answer)){
					taskRecord.setFdStatus(Constant.TASK_STATUS_UNFINISH);//00 未答
				} else {
					taskRecord.setFdStatus(Constant.TASK_STATUS_FINISH);//01 答完
				}
			}
			if(task.getFdType().equals(Constant.TASK_TYPE_UPLOAD)){//上传作业
		
				taskRecord.setAttMains(null);
			}
			answerRecords.add(taskRecord);
		}
		sourceNode.setTaskRecords(answerRecords);
		//sourceNodeService.save(sourceNode);
        return null;  
    }
    

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
		    ///作业状态
			taskMap.put("status", null);
			taskMap.put("examType", task.getFdType().equals("01")?"uploadWork":"onlineAnswer");
			taskMap.put("examScore", task.getFdStandardScore().intValue()); 	
			taskMap.put("examName", task.getFdName());
			taskMap.put("examStem", task.getFdSubject());
			
			List<AttMain> taskAtt = attMainService.findByCriteria(AttMain.class,
			                Value.eq("fdModelId", task.getFdId()),
			                Value.eq("fdModelName", Task.class.getName()));
			
			/*List<AttMain> answerAtt = attMainService.findByCriteria(AttMain.class,
	                Value.eq("fdModelId", task.getFdId()),
	                Value.eq("fdModelName", Task.class.getName()));*/
		    
			taskMap.put("listAttachment", taskAtt);//存放作业附件信息
			/*taskMap.put("listTaskAttachment", answerAtt);//存放答题者上传的附件
*/			// ///////////////评分人操作信息
			/*SourceNote sourceNote = sourceNodeService.getSourceNote(materialInfo.getFdId(),
					catalogId, ShiroUtils.getUser().getId());
			if (sourceNote != null) {
				List<Map> recordList = new ArrayList<Map>();
				List<TaskRecord> taskRecordList = sourceNote.getTaskRecords();
				for (TaskRecord taskRecord : taskRecordList) {
					if(taskRecord.getFdTaskId().equals(task.getFdId())){
						Map record = new HashMap();
						record.put("score", taskRecord.getFdScore() == null ? 0: sourceNote.getFdScore());
						record.put("comment", taskRecord.getFdComment());
						SysOrgPerson person = accountService.findById(sourceNote.getFdAppraiserId());
						Map teacher= new HashMap();
						teacher.put("imgUrl", person.getPoto());
						record.put("teacher", teacher);
						recordList.add(record);
					}
				}
				taskMap.put("teacherRating", recordList);
			}*/
			
			list.add(taskMap);
		}
		return list;
	}
}
