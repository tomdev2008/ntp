package cn.me.xdf.service.material;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.model.material.ExamOpinion;
import cn.me.xdf.model.material.ExamQuestion;
import cn.me.xdf.service.BaseService;

/**
 * 
 * 试题实体service
 * 
 * @author
 * 
 */
@Service
@Transactional(readOnly = false)
public class ExamQuestionService extends BaseService{

	@Autowired
	private ExamOpinionService examOpinionService;
	
	@SuppressWarnings("unchecked")
	@Override
	public  Class<ExamQuestion> getEntityClass() {
		return ExamQuestion.class;
	}
	
	public void deleQuestion(String questionId){
		//删除选项
		List<ExamOpinion> examOpinions = examOpinionService.findByProperty("question.fdId", questionId);
		for (ExamOpinion examOpinion : examOpinions) {
			examOpinionService.delete(examOpinion.getFdId());
		}
		//删除试题
		delete(questionId);
	}

}
