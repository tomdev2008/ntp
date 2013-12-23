package cn.me.xdf.service.course;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.model.course.CourseContent;
import cn.me.xdf.model.course.CourseGroupAuth;
import cn.me.xdf.service.BaseService;

/**
 * 
 * 课程群组关系service
 * 
 * @author zhaoq
 * 
 */
@Service
@Transactional(readOnly = true)
public class CourseGroupAuthService extends BaseService{

	@SuppressWarnings("unchecked")
	@Override
	public  Class<CourseGroupAuth> getEntityClass() {
		return CourseGroupAuth.class;
	}
}
