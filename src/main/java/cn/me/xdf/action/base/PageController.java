package cn.me.xdf.action.base;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.me.xdf.model.system.PageConfig;
import cn.me.xdf.service.system.SysPageConfigService;

@Controller
@RequestMapping(value = "/admin/page")
public class PageController {
	
	@Autowired
	private SysPageConfigService pageConfigService;
	
	
	@RequestMapping(value="list")
	public String getPageList(Model model,HttpServletRequest request){
		model.addAttribute("active", "page");
		model.addAttribute("ptype", "school");
		return "/admin/page/list";
	}
	
	@RequestMapping(value="add")
	public String add(Model model,HttpServletRequest request){
		String ptype=request.getParameter("ptype");
		model.addAttribute("ptype", ptype);
	    return "/admin/page/edit";
	}
	
	@RequestMapping(value="savePageConfig")
	public String savePageConfig(HttpServletRequest request){
		String fdType=request.getParameter("fdType");
		String fdElementId=request.getParameter("fdElementId");
		String fdContent=request.getParameter("fdContent");
		int fdOrder=pageConfigService.getMaxOrder();
		if(fdOrder>0){
			fdOrder=fdOrder+1;
		}
		PageConfig page=new PageConfig();
		page.setFdType(fdType);
		page.setFdElementId(fdElementId);
		page.setFdContent(fdContent);
		page.setFdOrder(fdOrder);
		pageConfigService.save(page);
		return "/admin/page/list";
	}
}
