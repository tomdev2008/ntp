package cn.me.xdf.service.score;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.model.score.ScoreStatistics;
import cn.me.xdf.service.BaseService;

@Service
@Transactional(readOnly = false)
public class ScoreStatisticsService extends BaseService{

	@SuppressWarnings("unchecked")
	@Override
	public  Class<ScoreStatistics> getEntityClass() {
		return ScoreStatistics.class;
	}


}
