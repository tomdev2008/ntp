package cn.me.xdf.service.course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.model.course.CourseAuth;
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
	
	/**
	 * 修改课程权限
	 */
	public void updateCourseAuth(String courseId,List<CourseAuth> courseAuths){
		courseAuthService.deleCourseAuthByCourseId(courseId);
		for (CourseAuth courseAuth : courseAuths) {
			courseAuthService.save(courseAuth);
		}
	}
	
}
