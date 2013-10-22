package cn.me.xdf.service.course;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.model.course.CourseTag;
import cn.me.xdf.model.course.TagInfo;
import cn.me.xdf.service.BaseService;

/**
 * 
 * 课程标签中间表service
 * 
 * @author zuoyi
 * 
 */
@Service
@Transactional(readOnly = false)
public class CourseTagService extends BaseService{
	@SuppressWarnings("unchecked")
	@Override
	public  Class<CourseTag> getEntityClass() {
		return CourseTag.class;
	}

	/**
	 * 查找所有课程分类
	 * @return List 分类列表
	 */
	@Transactional(readOnly = true)
	public List<TagInfo> findTagByCourseId(String courseId){
		//根据课程ID查找标签
		Finder finder = Finder
				.create("select tag.tag from CourseTag tag where tag.courses.fdid=:courseId");	
		finder.setParam("courseId", courseId);		
		return  super.find(finder);
	}
}
