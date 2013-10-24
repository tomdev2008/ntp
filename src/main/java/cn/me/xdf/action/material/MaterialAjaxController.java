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

@Controller
@RequestMapping(value = "/ajax/material")
@Scope("request")
public class MaterialAjaxController {
	
	@Autowired
	private MaterialService materialService;
	
	@RequestMapping(value = "findList", method = RequestMethod.GET)
	public String findList(Model model , HttpServletRequest request) {
		String fdType = request.getParameter("fdType");
		String pageNoStr = request.getParameter("pageNo");
		String pageSizeStr = request.getParameter("pageSize");
		String fdName = request.getParameter("fdName");
		Integer pageNo = 1;
		if(StringUtil.isNotBlank(pageNoStr)&&StringUtil.isNotEmpty(pageNoStr)){
			pageNo = Integer.parseInt(pageNoStr);
		}
		Integer pageSize =10;
		if(StringUtil.isNotBlank(pageNoStr)&&StringUtil.isNotEmpty(pageNoStr)){
			pageSize = Integer.parseInt(pageSizeStr);
		}
		if(StringUtil.isNotBlank(fdType)&&StringUtil.isNotEmpty(fdType)){
			Pagination page = materialService.findMaterialList(fdType, pageNo, pageSize,fdName);
			request.setAttribute("page", page);
		}
		return "/base/material/divMatList";
	}

}
