package cn.me.xdf.service.course;

import java.util.List;

import jodd.util.StringUtil;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.page.SimplePage;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.SeriesInfo;
import cn.me.xdf.service.BaseService;
@Service
@Transactional(readOnly = true)
public class SeriesInfoService extends BaseService {

	@SuppressWarnings("unchecked")
	@Override
	public  Class<SeriesInfo> getEntityClass() {
		return SeriesInfo.class;
	}
	/*
	 * 查询系列课程
	 * 
	 * 
	 * author hanhl
	 * */
	@Transactional(readOnly = false)
	public  Pagination findSeriesInfosOrByName(String fdName,String pageNo ,String orderbyStr){
		Finder finder = Finder.create("select *  from ixdf_ntp_series seriesInfo ");
		finder.append(" where seriesInfo.isavailable=1 and seriesInfo.fdparentid is null " );
		//设置页码
		int pageNoI=0;
		if(StringUtil.isNotBlank(pageNo)&&StringUtil.isNotEmpty(pageNo)){
			pageNoI = NumberUtils.createInteger(pageNo);
		} else {
			pageNoI = 1;
		}
		//根据关键字搜索
		if(!("").equals(fdName)&&fdName!=null){
			finder.append("and  seriesInfo.fdname like :ft ");
			finder.setParam("ft", "%"+fdName+"%");
		}
		//排序
		if(StringUtil.isNotBlank(orderbyStr)&&StringUtil.isNotEmpty(orderbyStr)){
	        if(orderbyStr.equalsIgnoreCase("fdname")){
	        	finder.append(" order by seriesInfo.fdname desc ");
	        }else if(orderbyStr.equalsIgnoreCase("fdcreatetime")){
	        	finder.append(" order by seriesInfo.fdcreatetime desc");
	        }
		}else{
			finder.append(" order by seriesInfo.fdcreatetime desc");
		}
		Pagination pagination=getPageBySql(finder, pageNoI, SimplePage.DEF_COUNT);
		return pagination;
	}
	/*
	 * 删除系列:设置系列为无效
	 */
	@Transactional(readOnly = false)
	public void deleteSeries(String seriesId){
		SeriesInfo seriesInfo=get(seriesId);
		seriesInfo.setIsAvailable(false);
		update(seriesInfo);

	}
	/**
	 * 查找课程权限
	 * @param courseId 课程ID
	 * @return List 章节列表
	 */
	@Transactional(readOnly = true)
	public List<SeriesInfo> getSeriesById(String seriesId){
		//根据系列ID查找章节，并按总序号升序
		Finder finder = Finder
				.create("from SeriesInfo series ");
		finder.append("where series.hbmParent.fdId = :seriesId order by series.fdSeiresNo");
		finder.setParam("seriesId", seriesId);		
		return  find(finder);
	}


}
