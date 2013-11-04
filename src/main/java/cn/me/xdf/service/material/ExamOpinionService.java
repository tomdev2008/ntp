package cn.me.xdf.service.material;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.model.material.ExamOpinion;
import cn.me.xdf.service.BaseService;

/**
 * 
 * 试题选项实体service
 * 
 * @author
 * 
 */
@Service
@Transactional(readOnly = false)
public class ExamOpinionService extends BaseService{

	@SuppressWarnings("unchecked")
	@Override
	public  Class<ExamOpinion> getEntityClass() {
		return ExamOpinion.class;
	}

}
