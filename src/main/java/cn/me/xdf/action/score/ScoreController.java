package cn.me.xdf.action.score;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.model.score.Score;
import cn.me.xdf.model.score.ScoreStatistics;
import cn.me.xdf.service.score.ScoreService;
import cn.me.xdf.service.score.ScoreStatisticsService;

/**
 * 评分
 * 
 * @author
 * 
 */
@Controller
@RequestMapping(value = "/score")
@Scope("request")
public class ScoreController {
	
	@Autowired
	private ScoreService scoreService ;
	
	@Autowired
	private ScoreStatisticsService scoreStatisticsService;
	
	/**
	 * 根据业务Id得到评分统计信息
	 * 
	 * @return 评分统计信息json
	 */
	@RequestMapping(value = "ajax/getScoreStatisticsByfdModelId")
	@ResponseBody
	public String getScoreStatisticsByfdModelId(String fdModelId){
		ScoreStatistics scoreStatistics =  scoreStatisticsService.findUniqueByProperty("fdModelId", fdModelId);
		List<Map> list = new ArrayList<Map>();
		Map map = new HashMap();
		map.put("fdId", scoreStatistics.getFdId());
		map.put("fdModelId", scoreStatistics.getFdModelId());
		map.put("fdAverage", scoreStatistics.getFdAverage());
		map.put("fdScoreNum", scoreStatistics.getFdScoreNum());
		map.put("fdFiveScoreNum", scoreStatistics.getFdFiveScoreNum());
		map.put("fdFourScoreNum", scoreStatistics.getFdFourScoreNum());
		map.put("fdThreeScoreNum", scoreStatistics.getFdThreeScoreNum());
		map.put("fdTwoScoreNum", scoreStatistics.getFdTwoScoreNum());
		map.put("fdOneScoreNum", scoreStatistics.getFdOneScoreNum());
		list.add(map);
		return JsonUtils.writeObjectToJson(list);
	}
	
	/**
	 * 为相应业务打分
	 * 
	 * @param fdModelName 业务名称
	 * @param fdModelId   业务Id
	 * @param fdScore     分数
	 * @param userId      评分人Id
	 * 
	 * @return 该业务的评分统计信息json
	 */
	@RequestMapping(value = "ajax/pushScore")
	@ResponseBody
	public String pushScore(String fdModelName,String fdModelId,String fdScore,String userId){
		SysOrgPerson orgPerson = new SysOrgPerson();
		orgPerson.setFdId(userId);
		Score score = new Score();
		score.setFdModelName(fdModelName);
		score.setFdModelId(fdModelId);
		score.setFdCreateTime(new Date());
		score.setFdScore(new Integer(fdScore));
		score.setFdUser(orgPerson);
		scoreService.save(score);
		ScoreStatistics scoreStatistics = scoreStatisticsService.resetInfoByFdModelId(fdModelId);
		List<Map> list = new ArrayList<Map>();
		Map map = new HashMap();
		map.put("fdId", scoreStatistics.getFdId());
		map.put("fdModelId", scoreStatistics.getFdModelId());
		map.put("fdAverage", scoreStatistics.getFdAverage());
		map.put("fdScoreNum", scoreStatistics.getFdScoreNum());
		map.put("fdFiveScoreNum", scoreStatistics.getFdFiveScoreNum());
		map.put("fdFourScoreNum", scoreStatistics.getFdFourScoreNum());
		map.put("fdThreeScoreNum", scoreStatistics.getFdThreeScoreNum());
		map.put("fdTwoScoreNum", scoreStatistics.getFdTwoScoreNum());
		map.put("fdOneScoreNum", scoreStatistics.getFdOneScoreNum());
		list.add(map);
		return JsonUtils.writeObjectToJson(list);
	}
}
