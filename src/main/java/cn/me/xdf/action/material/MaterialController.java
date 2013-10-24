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
import cn.me.xdf.service.material.MaterialService;

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
		String pageSizeStr = request.getParameter("pageSize");
		String fdName = request.getParameter("fdName");
		String order = request.getParameter("order");
		int pageNo;
		if(StringUtil.isNotBlank(pageNoStr)&&StringUtil.isNotEmpty(pageNoStr)){
			pageNo = Integer.parseInt(pageNoStr);
		} else {
			pageNo = 1;
		}
		int pageSize;
		if(StringUtil.isNotBlank(pageSizeStr)&&StringUtil.isNotEmpty(pageSizeStr)){
			pageSize = Integer.parseInt(pageSizeStr);
		} else {
			pageSize =10;
		}
		if(StringUtil.isNotBlank(fdType)&&StringUtil.isNotEmpty(fdType)){
			Pagination page = materialService.findMaterialList(fdType, pageNo, pageSize,fdName,order);
			request.setAttribute("page", page);
		}
		return "/base/material/materialList";
	}

}
