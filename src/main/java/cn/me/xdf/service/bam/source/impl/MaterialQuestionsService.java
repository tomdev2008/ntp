package cn.me.xdf.service.bam.source.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import cn.me.xdf.common.hibernate4.Value;
import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.material.ExamOpinion;
import cn.me.xdf.model.material.ExamQuestion;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.material.Task;
import cn.me.xdf.model.process.AnswerRecord;
import cn.me.xdf.model.process.SourceNote;
import cn.me.xdf.service.SimpleService;
import cn.me.xdf.service.bam.BamCourseService;
import cn.me.xdf.service.bam.process.SourceNodeService;
import cn.me.xdf.service.bam.source.ISourceService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.material.ExamQuestionService;
import cn.me.xdf.service.material.MaterialService;
import cn.me.xdf.utils.ShiroUtils;



/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-31
 * Time: 下午5:05
 * To change this template use File | Settings | File Templates.
 */
@Service("materialQuestionsService")
public class MaterialQuestionsService extends SimpleService implements ISourceService {
	
	@Autowired
	private SourceNodeService sourceNodeService;
	
	@Autowired
	private AttMainService attMainService;
	
	@Autowired
	private ExamQuestionService examQuestionService;
	
	@Autowired
	private MaterialService materialService;
	
	@Autowired
	private BamCourseService bamCourseService;
	
    @Override
    public Object findSourceByMaterials(BamCourse bamCourse, CourseCatalog catalog) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object saveSourceNode(WebRequest request) {
    	String fdMtype = request.getParameter("fdMtype");
		String catalogId = request.getParameter("catalogId");
		String bamId = request.getParameter("bamId");
		String materialFdid = request.getParameter("fdid");
		String[] anwers = request.getParameterValues("examAnswer");
		int time=0;
		long starTime =new Long( request.getParameter("startTime"));
		Date endTime= new Date();
		time = (int) ((endTime.getTime()-starTime)/(60*1000));
		BamCourse bamCourse = bamCourseService.get(BamCourse.class, bamId);
		//试题对应选项信息
		Map<String,String> anwer = new HashMap<String,String>();
		if(anwers!=null){
			for (String an : anwers) {
				String[] anwerSS= an.split(":");
				if(!anwer.containsKey(anwerSS[0])){
					anwer.put(anwerSS[0], anwerSS[1]);
				}else{
					anwer.put(anwerSS[0], anwer.get(anwerSS[0])+"#"+anwerSS[1]);
				};
			}
		}
		List<ExamQuestion> examQuestions = examQuestionService.findByProperty("exam.fdId", materialFdid);
		double sorce=0;
		for (ExamQuestion examQuestion : examQuestions) {
			String answer=anwer.get(examQuestion.getFdId());
			String rightAnswer = examQuestion.getFdQuestion();
			if((answer+"#").equals(rightAnswer)){
				sorce =  sorce+examQuestion.getFdStandardScore();
			}
		}
		MaterialInfo info = materialService.get(materialFdid);
		double passSorce  = info.getFdScore();
		SourceNote sourceNode =new SourceNote();
		sourceNode.setFdCourseId(bamCourse.getCourseId());
		sourceNode.setFdCatalogId(catalogId);
		sourceNode.setFdUserId(ShiroUtils.getUser().getId());
		sourceNode.setIsStudy(sorce>=passSorce);
		sourceNode.setFdOperationDate(new Date());
		sourceNode.setFdScore(sorce);
		sourceNode.setFdExamTime(time);
		sourceNode.setFdMaterialId(info.getFdId());
		Set<AnswerRecord> answerRecords = new HashSet<AnswerRecord>();
		for (ExamQuestion question : examQuestions) {
			AnswerRecord answerRecord = new AnswerRecord();
			answerRecord.setFdQuestionId(question.getFdId());
			answerRecord.setFdAnswer(anwer.get(question.getFdId()));
			answerRecords.add(answerRecord);
		}
		sourceNode.setAnswerRecords(answerRecords);
		return sourceNodeService.saveSourceNode(sourceNode);

    }

	@Override
	public Object findSubInfoByMaterial(WebRequest request) {
		List<Map> listExam = new ArrayList<Map>();
		String catalogId = request.getParameter("catalogId");
		String materialId =request.getParameter("materialId");
		MaterialInfo materialInfo = materialService.get(materialId); 
		if(!materialInfo.getIsAvailable()){
			return listExam;
		}
		SourceNote sourceNote = sourceNodeService.getSourceNote(materialId, catalogId, ShiroUtils.getUser().getId());
		List<ExamQuestion> examQuestions = materialInfo.getQuestions();
		for (ExamQuestion examQuestion2 : examQuestions) {
			Map map2 = new HashMap();
			map2.put("id", examQuestion2.getFdId());
			map2.put("index", examQuestion2.getFdOrder());
			if(sourceNote==null){
				map2.put("status", "null");
			}else{
				Set<AnswerRecord> answerRecords = sourceNote.getAnswerRecords();
				for (AnswerRecord answerRecord : answerRecords) {
					if(answerRecord.getFdQuestionId().equals(examQuestion2.getFdId())){
						map2.put("status", (answerRecord.getFdAnswer()+"#").equals(examQuestion2.getFdQuestion())?"success":"error");
						break;
					}
				}
			}
			
			map2.put("examScore", examQuestion2.getFdStandardScore());
			map2.put("examType", examQuestion2.getFdType().equals(Constant.EXAM_QUESTION_SINGLE_SELECTION)?"single":(examQuestion2.getFdType().equals(Constant.EXAM_QUESTION_MULTIPLE_SELECTION)?"multiple":"completion"));
			map2.put("examStem", examQuestion2.getFdSubject());
			List<ExamOpinion> examOpinions = examQuestion2.getOpinions();
			List<Map> opinionlist = new ArrayList<Map>();
			for (ExamOpinion examOpinion : examOpinions) {
				Map opinionMap = new HashMap();
				opinionMap.put("id", examOpinion.getFdId());
				opinionMap.put("index", examOpinion.getFdOrder());
				opinionMap.put("name", examOpinion.getOpinion());
				opinionMap.put("isAnswer", examOpinion.getIsAnswer());
				if(sourceNote==null){
					opinionMap.put("isChecked", false);
				}else{
					Set<AnswerRecord> answerRecords = sourceNote.getAnswerRecords();
					for (AnswerRecord answerRecord : answerRecords) {
						if(answerRecord.getFdQuestionId().equals(examQuestion2.getFdId())){
							if(answerRecord.getFdAnswer()==null){
								opinionMap.put("isChecked", false);
							}else{
								opinionMap.put("isChecked", answerRecord.getFdAnswer().contains(examOpinion.getFdId()));
							}
							break;
						}
					}
				}
				
				opinionlist.add(opinionMap);
			}
			map2.put("listExamAnswer", opinionlist);
			
			List<AttMain> taskAtt = attMainService.
					getByModeslIdAndModelNameAndKey(examQuestion2.getFdId(),ExamQuestion.class.getName(),"ExamQuestion");
			
			map2.put("listAttachment", taskAtt);
			listExam.add(map2);
		}
		return listExam;
	}
}
