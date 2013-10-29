package cn.me.xdf.action.material;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.page.SimplePage;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.material.MaterialService;
import cn.me.xdf.utils.ShiroUtils;

@Controller
@RequestMapping(value = "/ajax/material")
@Scope("request")
public class MaterialAjaxController {
	
	@Autowired
	private MaterialService materialService;
	
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
		return new ModelAndView("forward:/WEB-INF/views/base/material/divMatList.jsp");
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

}
