package cn.me.xdf.action.material;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.page.SimplePage;
import cn.me.xdf.common.utils.excel.AbsExportExcel;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.material.MaterialAuth;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.score.ScoreStatistics;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.material.MaterialAuthService;
import cn.me.xdf.service.material.MaterialService;
import cn.me.xdf.service.score.ScoreStatisticsService;
import cn.me.xdf.utils.ShiroUtils;
import cn.me.xdf.view.model.VCheckTaskData;
import cn.me.xdf.view.model.VMaterialData;

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
	 * 返回编辑，查看素材页面
	 */
	@RequestMapping(value="materialFoward")
	public String materialFoward(Model model ,HttpServletRequest request){
		String fdId = request.getParameter("fdId");
		if(StringUtil.isNotBlank(fdId)){
			MaterialInfo materialInfo = materialService.get(fdId);
			model.addAttribute("materialInfo", materialInfo);
			ScoreStatistics score = scoreStatisticsService.findScoreStatisticsByModelNameAndModelId
					           (MaterialInfo.class.getName(),materialInfo.getFdId());
			if(score!=null){
				model.addAttribute("score", score);
			}
		    AttMain main = attMainService.getByModelId(fdId);
		    if(main!=null){
		    	model.addAttribute("attId", main.getFdId());
		    }
		    String creatorId = materialInfo.getCreator().getFdId();
		    String loginUserId = ShiroUtils.getUser().getId();
		    if(creatorId.equals(loginUserId)||ShiroUtils.isAdmin()){
		    	return fowardEdit(materialInfo.getFdType());//返回的是素材edit页面
		    }
		    if(materialInfo.getIsPublish()==false){
		    	MaterialAuth auth = materialAuthService.findByMaterialIdAndUserId(fdId,ShiroUtils.getUser().getId());
			    if(auth.getIsEditer()==true){
			    	return fowardEdit(materialInfo.getFdType());
				}else{
					return fowardView(materialInfo.getFdType());
				}
		    }else{
				return fowardView(materialInfo.getFdType());
			}
		}
		return null;
	}
	/**
	 * 导出素材列表
	 * @param modelIds
	 * @param fdType
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportMaterialData/{modelIds}/{fdType}")
	public String exportMaterialData(@PathVariable("modelIds") String[] modelIds,@PathVariable("fdType") String fdType,
			 HttpServletRequest request, HttpServletResponse response){
		List<VMaterialData> materialList = new ArrayList<VMaterialData>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd h:m:s a");
		for (String modelId : modelIds) {
			VMaterialData data = new VMaterialData();
			MaterialInfo info = materialService.load(modelId);
			data.setFdDownloads(info.getFdDownloads()==null?0:info.getFdDownloads());
			data.setFdLauds(info.getFdLauds()==null?0:info.getFdLauds());
			data.setFdPlays(info.getFdPlays()==null?0:info.getFdPlays());
			data.setFdName(info.getFdName());
			ScoreStatistics score = scoreStatisticsService.findScoreStatisticsByModelNameAndModelId(MaterialInfo.class.getName(), modelId);
			if(score!=null){
				data.setScore(score.getFdAverage()==null?0:score.getFdAverage());
			}else{
				data.setScore(0.0);
			}
			String time = sdf.format(info.getFdCreateTime());
			data.setFdCreateTime(time);
			if(info.getFdType().equals(Constant.MATERIAL_TYPE_VIDEO)){
				data.setFdType("视频");
			}else if(info.getFdType().equals(Constant.MATERIAL_TYPE_DOC)){
				data.setFdType("文档");
			}else if(info.getFdType().equals(Constant.MATERIAL_TYPE_PPT)){
				data.setFdType("幻灯片");
			}else{
				data.setFdType("素材");
			}
			materialList.add(data);
		}
		AbsExportExcel.exportExcel(materialList, "materialData.xls", response);
		return null;
	}
	
	/**
	 * 返回添加素材的页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="materialAddFoward")
	public String materialAddFoward(HttpServletRequest request){
		String fdType = request.getParameter("fdType");
		return fowardEdit(fdType);
	}
	//返回的是view页面
	private String fowardView(String fdType){
		//测试
    	if(fdType.equals(Constant.MATERIAL_TYPE_TEST)){
    		return "/material/exam_view";
    	}
    	//作业包
    	if(fdType.equals(Constant.MATERIAL_TYPE_JOBPACKAGE)){
    		return "/material/viewExamPaper";
    	}else{
    		//其它素材种类
    		return "/material/viewVideo";
    	}
	}
	private String fowardEdit(String fdType){
		//测试
    	if(fdType.equals(Constant.MATERIAL_TYPE_TEST)){
    		return "/material/exam_add";
    	}
    	//作业包
    	if(fdType.equals(Constant.MATERIAL_TYPE_JOBPACKAGE)){
    		return "/material/addExamPaper";
    	}else{
    		//其它素材种类
    		return "/material/editVideo";
    	}
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
//		String materIalId = request.getParameter("materIalId");
//		model.addAttribute("materIalId", materIalId);
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
//		String materIalId = request.getParameter("materIalId");
//		model.addAttribute("materIalId", materIalId);
		return "/material/exam_view";
	}
	
}
