package cn.me.xdf.service.bam.source.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.material.Task;
import cn.me.xdf.service.SimpleService;
import cn.me.xdf.service.bam.source.ISourceService;
import cn.me.xdf.service.base.AttMainService;

@Service("materialTaskService")
public class MaterialTaskService extends SimpleService implements ISourceService  {
	
	@Autowired
	private AttMainService attMainService;
	
	@Override
    public Object findSourceByMaterials(BamCourse bamCourse, CourseCatalog catalog) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object saveSourceNode(WebRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

	@Override
	public Object findSubInfoByMaterial(MaterialInfo materialInfo) {
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
			
			List<AttMain> taskAtt = attMainService.
					getByModeslIdAndModelNameAndKey(task.getFdId(),Task.class.getName(),"taskAtt");
			
			List<AttMain> answerAtt = attMainService.
					getByModeslIdAndModelNameAndKey(task.getFdId(),Task.class.getName(),"answerAtt");
		    
			taskMap.put("listAttachment", taskAtt);//存放作业附件信息
			taskMap.put("listTaskAttachment", answerAtt);//存放答题者上传的附件
			// ///////////////评分人操作信息
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
