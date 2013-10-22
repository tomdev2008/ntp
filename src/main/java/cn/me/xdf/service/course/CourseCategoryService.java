package cn.me.xdf.service.course;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.model.course.CourseCategory;
import cn.me.xdf.service.BaseService;


/**
 * 
 * 课程分类service
 * 
 * @author zuoyi
 * 
 */
@Service
@Transactional(readOnly = false)
public class CourseCategoryService extends BaseService{
	@SuppressWarnings("unchecked")
	@Override
	public  Class<CourseCategory> getEntityClass() {
		return CourseCategory.class;
	}

	/**
	 * 查找所有课程分类
	 * @return List 分类列表
	 */
	@Transactional(readOnly = true)
	public List<CourseCategory> findAll(){
		//根据课程ID查找章节，并按总序号升序
		Finder finder = Finder
				.create("from CourseCategory category ");		
		return  super.find(finder);
	}
}
