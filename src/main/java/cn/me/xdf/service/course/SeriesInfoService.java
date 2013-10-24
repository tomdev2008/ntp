package cn.me.xdf.service.course;

import org.springframework.stereotype.Service;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.model.course.SeriesInfo;
import cn.me.xdf.service.BaseService;
@Service
public class SeriesInfoService extends BaseService {

	@SuppressWarnings("unchecked")
	@Override
	public  Class<SeriesInfo> getEntityClass() {
		return SeriesInfo.class;
	}


}
