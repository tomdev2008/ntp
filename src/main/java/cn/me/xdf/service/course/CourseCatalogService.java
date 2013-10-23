package cn.me.xdf.service.course;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.service.BaseService;

/**
 * 
 * 课程章节service
 * 
 * @author zuoyi
 * 
 */
@Service
@Transactional(readOnly = true)
public class CourseCatalogService extends BaseService{

	@SuppressWarnings("unchecked")
	@Override
	public  Class<CourseCatalog> getEntityClass() {
		return CourseCatalog.class;
	}
	
	/**
	 * 查找课程权限
	 * @param courseId 课程ID
	 * @return List 章节列表
	 */
	@Transactional(readOnly = true)
	public List<CourseCatalog> getCatalogsByCourseId(String courseId){
		//根据课程ID查找章节，并按总序号升序
		Finder finder = Finder
				.create("from CourseCatalog catalog ");
		finder.append("where catalog.courseInfo.fdId = :userId order by catalog.fdTotalNo");
		finder.setParam("courseId", courseId);		
		return  super.find(finder);
	}
	
}
