package cn.me.xdf.service.course;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.model.course.CourseTag;
import cn.me.xdf.model.course.TagInfo;
import cn.me.xdf.service.BaseService;

@Service
@Transactional(readOnly = false)
public class TagInfoService extends BaseService {

	@SuppressWarnings("unchecked")
	@Override
	public  Class<TagInfo> getEntityClass() {
		return TagInfo.class;
	}
	
	/**
	 * 根据key模糊查找TagInfo
	 */
	public List<TagInfo> findTagInfosByKey(String key){
		Finder finder = Finder
				.create("from TagInfo tagInfo ");
		finder.append("where tagInfo.fdName like :key");
		finder.setParam("key", '%' + key + '%');
		return find(finder);
	}
}
