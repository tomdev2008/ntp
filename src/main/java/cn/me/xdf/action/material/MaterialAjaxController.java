package cn.me.xdf.action.material;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.page.SimplePage;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.material.MaterialAuthService;
import cn.me.xdf.service.material.MaterialService;
import cn.me.xdf.utils.ShiroUtils;

@Controller
@RequestMapping(value = "/ajax/material")
@Scope("request")
public class MaterialAjaxController {
	
	@Autowired
	private MaterialService materialService;
	
	@Autowired
	private MaterialAuthService materialAuthService;
	
	@Autowired
	private AttMainService attMainService;
	
	/**
	 * ajax找出素材列表
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "findList")
	@ResponseBody
	public ModelAndView findList(Model model , HttpServletRequest request) {
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
			Pagination page = materialService.findMaterialList(fdType, pageNo,SimplePage.DEF_COUNT,fdName, order);
			model.addAttribute("page", page);
		}
		return new ModelAndView("forward:/WEB-INF/views/material/divMatList.jsp");
	}
	
	@RequestMapping(value = "getMaterialBykey")
	@ResponseBody
	public List<Map> getMaterialBykey(HttpServletRequest request){
		String key = request.getParameter("q");
		String type = request.getParameter("type");
		return materialService.getMaterialsTop10Bykey(key, type);
	}
	
	@RequestMapping(value = "saveMaterial")
	@ResponseBody
	public Map saveMaterial(HttpServletRequest request){
		String type = request.getParameter("type");
		String fileName = request.getParameter("fileName");
		String attId = request.getParameter("attId");
		MaterialInfo materialInfo = new MaterialInfo();
		materialInfo.setFdName(fileName);
		materialInfo.setFdType(type);
		materialInfo.setIsAvailable(true);
		materialInfo.setIsPublish(true);
		materialInfo.setIsDownload(true);
		SysOrgPerson creator = new SysOrgPerson();
		creator.setFdId(ShiroUtils.getUser().getId());
		materialInfo.setCreator(creator);
		List<AttMain> attMains = new ArrayList<AttMain>();
		AttMain attMain = new AttMain();
		attMain.setFdId(attId);
		attMains.add(attMain);
		materialInfo.setAttMains(attMains);
		materialService.save(materialInfo);
		Map map = new HashMap();
		map.put("id", materialInfo.getFdId());
		map.put("name", materialInfo.getFdName());
		return map;
	}
	
	/**
	 * 更新或保存素材
	 * @param materialInfo
	 * @return
	 */
	@RequestMapping(value="saveOrUpdateVideo", method = RequestMethod.POST)
	public void saveOrUpdateVideo(HttpServletRequest request){
		MaterialInfo info = new MaterialInfo();
		info.setFdAuthor(request.getParameter("author"));
		info.setFdAuthorDescription(request.getParameter("authorIntro"));
		info.setFdLink(request.getParameter("videoUrl"));
		info.setFdName(request.getParameter("videoName"));
		info.setFdDescription(request.getParameter("videoIntro"));
		String permission = request.getParameter("permission");
		String kingUser = request.getParameter("kingUser");
		String attId = request.getParameter("attId");
		if(permission.equals("open")){
			info.setIsPublish(true);
		} else {
			info.setIsPublish(false);
		}
		String fdId = request.getParameter("fdId");
		if(StringUtil.isBlank(fdId)){
			SysOrgPerson creator = new SysOrgPerson();
			creator.setFdId(ShiroUtils.getUser().getId());
			info.setFdType(request.getParameter("fdType"));
			info.setCreator(creator);
			info.setFdCreateTime(new Date());
			info.setIsAvailable(true);
			//保存素材信息
			materialService.save(info);
			//保存权限信息
			materialService.saveMaterAuth(kingUser,info.getFdId());
			//保存附件信息
			saveAtt(attId,info.getFdId());
		} else {
			materialService.updateMaterial(info, fdId);
			materialService.saveMaterAuth(kingUser,fdId);
		}
	}
	/**
	 * @param attId
	 * @param modelId
	 */
	public void saveAtt(String attId,String modelId){
		AttMain att = attMainService.get(attId);
		att.setFdModelId(modelId);
		att.setFdModelName("cn.me.xdf.model.material.MaterialInfo");
		attMainService.update(att);
	}
	/**
	 * 更新附件信息
	 * @param attId
	 * @param modelId
	 */
	public void updateAtt(String attId,String modelId){
		//先清除以前的附件
		attMainService.deleteAttMainByModelId(modelId);
		saveAtt(attId,modelId);
	}
	
	/**
	 * 得到指定课程的权限信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getAuthInfoByMaterId")
	@ResponseBody
	public String getAuthInfoByMaterId(HttpServletRequest request) {
		// 获取课程ID
		String MaterialId = request.getParameter("MaterialId");
		List<Map> list = materialService.findAuthInfoByMaterialId(MaterialId);
		Map map = new HashMap();
		map.put("user", list);
		return JsonUtils.writeObjectToJson(map);
	}

}
