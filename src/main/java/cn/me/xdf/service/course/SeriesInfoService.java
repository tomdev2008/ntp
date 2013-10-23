package cn.me.xdf.service.course;

import org.springframework.stereotype.Service;

import cn.me.xdf.model.course.SeriesInfo;
import cn.me.xdf.service.BaseService;
@Service
public class SeriesInfoService extends BaseService {

	@SuppressWarnings("unchecked")
	@Override
	public  Class<SeriesInfo> getEntityClass() {
		return SeriesInfo.class;
	}
    /*
     * 增加系列
     * author hanhl
     */
	public void save(SeriesInfo seriesInfo){
		super.save(seriesInfo);
	} 
	
    /*
     * 修改系列
     * author hanhl
     */
	public void update(SeriesInfo seriesInfo){
		super.update(seriesInfo);
		
	} 
	
    /*
     * 删除系列   (先删系列下的课程 -阶段-系列)
     * 课程下的章 节  课程内容是否一并删除?
     * author hanhl
     */
	public void delete(SeriesInfo seriesInfo){
		super.delete(seriesInfo.getFdId());
	} 
}
