package cn.me.xdf.service.course;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.model.course.CourseContent;
import cn.me.xdf.model.course.SeriesCourses;
import cn.me.xdf.service.BaseService;

/**
 * 
 * 系列课程关系表service
 * 
 * @author zuoyi
 * 
 */
@Service
@Transactional(readOnly = true)
public class SeriesCoursesService  extends BaseService{
	@SuppressWarnings("unchecked")
	@Override
	public  Class<SeriesCourses> getEntityClass() {
		return SeriesCourses.class;
	}
	
	/**
	 * 根据课程ID删除课程与系列的关系
	 * @param courseId 课程ID
	 */
	@Transactional(readOnly = false)
	public void deleteByCourseId(String courseId) {
		Finder finder = Finder
				.create(" from SeriesCourses seriesCourse where  seriesCourse.courses.fdId=:courseId");	
		finder.setParam("courseId", courseId);
		List<SeriesCourses> list = super.find(finder);
		if(list!=null && list.size()>0){
			for(SeriesCourses series:list){
				super.deleteEntity(series);
			}
		}
	}
	/*根据系列ID删除课程与系列的关系
	 * author hanhl
	 */
	@Transactional(readOnly = false)
	public void deleteBySeriesId(String seriesId) {
		Finder finder = Finder
				.create(" from SeriesCourses seriesCourse where  seriesCourse.series.fdId=:seriesId");	
		finder.setParam("seriesId", seriesId);
		List<SeriesCourses> list = super.find(finder);
		if(list!=null && list.size()>0){
			for(SeriesCourses series:list){
				super.deleteEntity(series);
			}
		}
	}
}
