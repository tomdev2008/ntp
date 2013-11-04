package cn.me.xdf.service.material;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@SuppressWarnings("unchecked")
	@Override
	public  Class<ExamQuestion> getEntityClass() {
		return ExamQuestion.class;
	}

}
