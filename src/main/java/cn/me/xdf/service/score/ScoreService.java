package cn.me.xdf.service.score;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.model.score.Score;
import cn.me.xdf.model.score.ScoreStatistics;
import cn.me.xdf.service.BaseService;
/**
 * 
 * 评分service
 * 
 * @author zhaoq
 * 
 */
@Service
@Transactional(readOnly = false)
public class ScoreService extends BaseService{

	@SuppressWarnings("unchecked")
	@Override
	public  Class<Score> getEntityClass() {
		return Score.class;
	}

	/**
	 * 计算指定 业务Id和分数的记录数量
	 * 
	 */
	public int fingCountModelIdAndScore(String fdModelId,int fdScore){
		Finder finder = Finder
				.create("from Score score ");
		finder.append("where score.fdModelId = :fdModelId and score.fdScore = :fdScore");
		finder.setParam("fdModelId", fdModelId);
		finder.setParam("fdScore", fdScore);
		List<Score> scores = find(finder);
		return scores.size();
	}
}
