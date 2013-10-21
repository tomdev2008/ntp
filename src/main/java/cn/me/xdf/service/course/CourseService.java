package cn.me.xdf.service.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.service.BaseService;

@Service
@Transactional(readOnly = false)
public class CourseService  extends BaseService{
	
	@Autowired
	private CourseParticipateAuthService courseParticipateAuthService;
	
	@Autowired
	private CourseAuthService courseAuthService;
	
	@SuppressWarnings("unchecked")
	@Override
	public  Class<CourseInfo> getEntityClass() {
		return CourseInfo.class;
	}
	
	
}
