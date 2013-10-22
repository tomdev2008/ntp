package cn.me.xdf.action.score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.me.xdf.common.json.JsonUtils;
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
	 * 根据业务Id评分统计信息
	 * 
	 * @return json
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
		return JsonUtils.writeObjectToJson(list);
	}
}
