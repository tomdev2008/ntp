package cn.me.xdf.action.material;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.page.SimplePage;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.score.ScoreStatistics;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.material.MaterialAuthService;
import cn.me.xdf.service.material.MaterialService;
import cn.me.xdf.service.score.ScoreStatisticsService;

/**
 * 材料信息
 * @author yuhuizhe
 */
@Controller
@RequestMapping(value = "/material")
@Scope("request")
public class MaterialController {
	
	@Autowired
	private MaterialService materialService;
	
	@Autowired
	private MaterialAuthService materialAuthService;
	
	@Autowired
	private ScoreStatisticsService scoreStatisticsService;
	
	@Autowired
	private AttMainService attMainService;
	
	
	/**
	 * 返回增加视频页面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="addVideo")
	public String addVideo(Model model ,HttpServletRequest request){
		return "/material/addVideo";
	}
	/**
	 * 返回编辑视频页面
	 */
	@RequestMapping(value="updateVideo")
	public String updateVideo(Model model ,HttpServletRequest request){
		String fdId = request.getParameter("fdId");
		if(StringUtil.isNotBlank(fdId)&&StringUtil.isNotEmpty(fdId)){
			MaterialInfo materialInfo = materialService.get(fdId);
			model.addAttribute("materialInfo", materialInfo);
			ScoreStatistics score = scoreStatisticsService.findScoreStatisticsByModelNameAndModelId
					           ("cn.me.xdf.model.material.MaterialInfo",materialInfo.getFdId());
		    model.addAttribute("score", score);
		    AttMain main = attMainService.getByModelId(fdId);
		    if(main!=null){
		    	model.addAttribute("attId", main.getFdId());
		    }
		}
		return "/material/editVideo";
	}

	/**
	 * 查找素材列表
	 * fdType 类型 01视频 02音频 03图片 04文档等
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "findList", method = RequestMethod.GET)
	public String findList(Model model , HttpServletRequest request) {
		String fdType = request.getParameter("fdType");
		String pageNoStr = request.getParameter("pageNo");
		String fdName = request.getParameter("fdName");
		String order = request.getParameter("order");
		int pageNo;
		if(StringUtil.isNotBlank(pageNoStr)&&StringUtil.isNotEmpty(pageNoStr)){
			pageNo = Integer.parseInt(pageNoStr);
		} else {
			pageNo = 1;
		}
		if(StringUtil.isNotBlank(fdType)&&StringUtil.isNotEmpty(fdType)){
			Pagination page = materialService.findMaterialList(fdType, pageNo, SimplePage.DEF_COUNT,fdName,order);
			model.addAttribute("page", page);
		}
		return "/material/materialList";
	}

	/**
	 * 跳转至测试添加页面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="addExam")
	public String addExam(HttpServletRequest request){
		return "/material/exam_add";
	}
	
}
