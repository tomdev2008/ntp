package cn.me.xdf.action.material;

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
import cn.me.xdf.service.material.MaterialService;

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
			Pagination page = materialService.findMaterialList(fdType, pageNo, pageSize,fdName, order);
			request.setAttribute("page", page);
		}
		return new ModelAndView("forward:/WEB-INF/views/base/material/divMatList.jsp");
	}

}
