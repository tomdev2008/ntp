package cn.me.xdf.service.score;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.model.score.Score;
import cn.me.xdf.service.BaseService;

@Service
@Transactional(readOnly = false)
public class ScoreService extends BaseService{

	@SuppressWarnings("unchecked")
	@Override
	public  Class<Score> getEntityClass() {
		return Score.class;
	}


}
