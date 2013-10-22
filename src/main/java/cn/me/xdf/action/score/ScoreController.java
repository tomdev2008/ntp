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
	 * 根据业务Id和业务Name得到评分统计信息
	 * 
	 * @return 评分统计信息json
	 */
	@RequestMapping(value = "ajax/getScoreStatisticsByfdModelId")
	@ResponseBody
	public String getScoreStatisticsByfdModelId(String fdModelId,String fdModelName){
		ScoreStatistics scoreStatistics =  scoreStatisticsService.findScoreStatisticsByModelNameAndModelId(fdModelName, fdModelId);
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
		scoreService.pushScore(fdModelName, fdModelId, fdScore, userId);
		ScoreStatistics scoreStatistics = scoreStatisticsService.resetInfoByFdModelId(fdModelName,fdModelId);
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
	 * 是否可以评分
	 * 
	 * @param fdModelId   业务Id
	 * @param fdModelName   业务Name
	 * @param userId      评分人Id
	 * 
	 * @return  String(true:可以评分；false：不可以评分)
	 */
	@RequestMapping(value = "ajax/canPushScore")
	@ResponseBody
	public String canPushScore(String fdModelName, String fdModelId,String userId) {
		Score score = scoreService.findByModelIdAndUserId(fdModelName,fdModelId, userId);
		return (score == null) ? "false" : "true";
	}
}
