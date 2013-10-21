package cn.me.xdf.service.score;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.model.score.ScoreStatistics;
import cn.me.xdf.service.BaseService;
import cn.me.xdf.service.material.MaterialAuthService;
/**
 * 
 * 评分统计service
 * 
 * @author zhaoq
 * 
 */
@Service
@Transactional(readOnly = false)
public class ScoreStatisticsService extends BaseService{

	@Autowired
	private ScoreService scoreService;
	
	@SuppressWarnings("unchecked")
	@Override
	public  Class<ScoreStatistics> getEntityClass() {
		return ScoreStatistics.class;
	}

	/**
	 * 重新计算评分统计
	 * 
	 */
	@Transactional(readOnly = false)
	public ScoreStatistics resetInfo(String ScoreStatisticsId){
		ScoreStatistics scoreStatistics = findUniqueByProperty("fdId", ScoreStatisticsId);
		String fdModelId = scoreStatistics.getFdModelId();
		int score_1 = scoreService.fingCountModelIdAndScore(fdModelId, 1);
		int score_2 = scoreService.fingCountModelIdAndScore(fdModelId, 2);
		int score_3 = scoreService.fingCountModelIdAndScore(fdModelId, 3);
		int score_4 = scoreService.fingCountModelIdAndScore(fdModelId, 4);
		int score_5 = scoreService.fingCountModelIdAndScore(fdModelId, 5);
		int num = score_1+score_2+score_3+score_4+score_5;
		Double average = (score_1+score_2*2+score_3*3+score_4*4+score_5*5)/new Double(num);
		scoreStatistics.setFdAverage(average);
		scoreStatistics.setFdScoreNum(num);
		scoreStatistics.setFdOneScoreNum(score_1);
		scoreStatistics.setFdTwoScoreNum(score_2);
		scoreStatistics.setFdThreeScoreNum(score_3);
		scoreStatistics.setFdFourScoreNum(score_4);
		scoreStatistics.setFdFiveScoreNum(score_5);
		return update(scoreStatistics);
	}
}
