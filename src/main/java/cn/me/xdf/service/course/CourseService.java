package cn.me.xdf.service.course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.model.course.CourseAuth;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.service.BaseService;
/**
 * 
 * 课程service
 * 
 * @author zhaoq
 * 
 */
@Service
@Transactional(readOnly = true)
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
		//删除所有相关的权限信息
		courseAuthService.deleCourseAuthByCourseId(courseId);
		//插入权限信息
		for (CourseAuth courseAuth : courseAuths) {
			courseAuthService.save(courseAuth);
		}
	}
	/*
	 * 课程列表
	 * author hanhl
	 */
	public Pagination findAllCourseInfos(String userId,Integer pageNo){
		/*根据当前用户  
		 * 查询用户的课程
		 * 
		 * 此处需要添加客车评分?//遗留
		 * author hanhl
		 * */
		Finder finder = Finder
				.create("from CouserInfo ci ");
		finder.append("where ci.creator.fdId = :userId and ci.isAvailable='01' ");
		finder.setParam("userId", userId);
	    Pagination pagination=getPage(finder, pageNo);
		return pagination;
	}

	/*
	 * 根据课程名称模糊搜索
	 * 
	 * 此处需要添加课程评分?//遗留
	 * author hanhl
	 * */
	public  Pagination findCourseInfosByName(String userId,String fdName,Integer pageNo ,String orderbyStr){
		Finder finder = Finder
				.create("from CourseInfo ci ");
		finder.append("where ci.creator.fdId=:userId  and  ci.isAvailable='01'");/*发布*/
		finder.append("and  ( ci.fdTitle = :ft  or ci.fdSubTitle like :fs )");
		finder.setParam("userId", userId);
		finder.setParam("ft", "%"+fdName+"%");
		finder.setParam("fs", "%"+fdName+"%");

		Pagination pagination=getPage(finder,pageNo);
		return pagination;
	}
}