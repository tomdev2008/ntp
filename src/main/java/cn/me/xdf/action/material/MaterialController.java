package cn.me.xdf.action.material;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.page.SimplePage;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.material.MaterialAuth;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.score.ScoreStatistics;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.material.MaterialAuthService;
import cn.me.xdf.service.material.MaterialService;
import cn.me.xdf.service.score.ScoreStatisticsService;
import cn.me.xdf.utils.ShiroUtils;

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
	 * 返回添加作业包的页面
	 * @return
	 */
	@RequestMapping(value="addExamPaper")
	public String addWorkPacket(Model model ,HttpServletRequest request){
		String materialId  = request.getParameter("materialId");
		if(StringUtil.isNotBlank(materialId)){
			MaterialInfo materialInfo = materialService.get(materialId);
			model.addAttribute("materialInfo", materialInfo);
		}
		return "/material/addExamPaper";
	}
	/**
	 * 返回编辑视频页面
	 */
	@RequestMapping(value="updateVideo")
	public String updateVideo(Model model ,HttpServletRequest request){
		String fdId = request.getParameter("fdId");
		String fdType = request.getParameter("fdType");
		if(StringUtil.isNotBlank(fdId)&&StringUtil.isNotEmpty(fdId)){
			MaterialInfo materialInfo = materialService.get(fdId);
			model.addAttribute("materialInfo", materialInfo);
			ScoreStatistics score = scoreStatisticsService.findScoreStatisticsByModelNameAndModelId
					           (MaterialInfo.class.getName(),materialInfo.getFdId());
		    model.addAttribute("score", score);
		    AttMain main = attMainService.getByModelId(fdId);
		    if(main!=null){
		    	model.addAttribute("attId", main.getFdId());
		    }
		    if(materialInfo.getCreator().getFdId().equals(ShiroUtils.getUser().getId())){
		    	return "/material/editVideo";
		    }
		    if(materialInfo.getIsPublish()==false){
		    	MaterialAuth auth = materialAuthService.findByMaterialIdAndUserId(fdId,ShiroUtils.getUser().getId());
			    if(auth.getIsEditer()==true){
				   return "/material/editVideo";
				}else{
				   return "/material/viewVideo";
				}
		    }
		}
		return "/material/viewVideo";
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
	public String addExam(Model model,HttpServletRequest request){
		String materIalId = request.getParameter("materIalId");
		model.addAttribute("materIalId", materIalId);
		return "/material/exam_add";
	}
	
	/**
	 * 跳转至测试添加页面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="viewExam")
	public String viewExam(Model model,HttpServletRequest request){
		String materIalId = request.getParameter("materIalId");
		model.addAttribute("materIalId", materIalId);
		return "/material/exam_view";
	}
	
}
