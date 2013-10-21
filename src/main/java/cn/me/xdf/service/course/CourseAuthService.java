package cn.me.xdf.service.course;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.model.course.CourseAuth;
import cn.me.xdf.service.BaseService;

@Service
@Transactional(readOnly = false)
public class CourseAuthService extends BaseService{

	@SuppressWarnings("unchecked")
	@Override
	public  Class<CourseAuth> getEntityClass() {
		return CourseAuth.class;
	}
	
	
	/**
	 * 添加或修改课程权限
	 * 
	 */
	@Transactional(readOnly = false)
	public CourseAuth saveOrUpdateCourseAuth(CourseAuth courseAuth){
		CourseAuth auth =findByCourseIdAndUserId(courseAuth.getCourse().getFdId(), courseAuth.getFdUser().getFdId());
		if(auth==null){
			return save(courseAuth);
		}else{
			courseAuth.setFdId(auth.getFdId());
			return update(courseAuth);
		}
	}
	
	
	/**
	 * 删除课程权限
	 * 
	 */
	@Transactional(readOnly = false)
	public void deleCourseAuth(CourseAuth courseAuth){
		delete(courseAuth);
	}
	
	
	/**
	 * 查找课程权限
	 * 
	 */
	@Transactional(readOnly = true)
	public CourseAuth findByCourseIdAndUserId(String courseId,String userId){
		Finder finder = Finder
				.create("from CourseAuth anth ");
		finder.append("where anth.courseInfo.fdId like :courseId and anth.fdUser.fdId like :userId");
		finder.setParam("courseId", courseId);
		finder.setParam("userId", userId);
		return (CourseAuth) super.find(finder).get(0);
	}

	/**
	 * 根据课程Id删除课程权限
	 * 
	 */
	@Transactional(readOnly = false)
	public void deleCourseAuthByCourseId(String courseId){
		Finder finder = Finder
				.create("from CourseAuth anth ");
		finder.append("where anth.courseInfo.fdId like :courseId");
		finder.setParam("courseId", courseId);
		List<CourseAuth> list = super.find(finder);
		for (CourseAuth courseAuth : list) {
			delete(courseAuth);
		}
	}
}
