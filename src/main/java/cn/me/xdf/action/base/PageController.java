package cn.me.xdf.action.base;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.RequestWrapper;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.organization.SysOrgElement;
import cn.me.xdf.model.system.PageConfig;
import cn.me.xdf.service.SysOrgDepartService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.system.SysPageConfigService;

@Controller
@RequestMapping(value = "/admin/page")
public class PageController {
	
	@Autowired
	private SysPageConfigService pageConfigService;

	@Autowired
	private AttMainService attMainService;
	
	@Autowired
	private SysOrgDepartService sysOrgDepartService;
	
	@RequestMapping(value="list")
	public String getPageList(Model model,HttpServletRequest request){
		String ptype=request.getParameter("ptype");
		if(StringUtil.isNotEmpty(ptype)){
			model.addAttribute("ptype", ptype);
		}else{
			model.addAttribute("ptype", "02");
		}
		model.addAttribute("active", "page");
		
		return "/admin/page/list";
	}
	
	@RequestMapping(value="add")
	public String add(Model model,HttpServletRequest request){
		String ptype=request.getParameter("ptype");
		model.addAttribute("ptype", ptype);
		model.addAttribute("active", "page");
	    return "/admin/page/add";
	}
	@RequestMapping("edit")
	public String edit(Model model,HttpServletRequest request){
		String ptype=request.getParameter("ptype");
		String pId=request.getParameter("pid");//配置页信息id;
		if(StringUtil.isNotEmpty(ptype)){
				if(StringUtil.isNotEmpty(pId)){
					PageConfig page=pageConfigService.get(pId);
					model.addAttribute("pid",page.getFdId());
					model.addAttribute("ptype",ptype);
				//老师信息
				if("01".equals(ptype)){
					SysOrgElement org=sysOrgDepartService.getSysOrgElementById(page.getFdElementId());
					model.addAttribute("elementName",org.getFdName());
					model.addAttribute("elementId", page.getFdElementId());
					model.addAttribute("content", page.getFdContent());
				}		
				//学校信息
				if("02".equals(ptype)){
					SysOrgElement org=sysOrgDepartService.getSysOrgElementById(page.getFdElementId());
					model.addAttribute("elementName",org.getFdName());
					model.addAttribute("elementId", page.getFdElementId());
					model.addAttribute("content", page.getFdContent());
					AttMain attMain=attMainService.getByModelIdAndModelName(page.getFdId(),PageConfig.class.getName());
					if(attMain!=null){
						model.addAttribute("attId", attMain.getFdId());
					}else{
						model.addAttribute("attId", "");
					}
				}
			}
		}
		return "/admin/page/edit";
	}
	@RequestMapping(value="savePageConfig")
	public String savePageConfig(Model model,HttpServletRequest request){
		String fdType=request.getParameter("ptype");
		String fdElementId=request.getParameter("fdElementId");
		String fdContent=request.getParameter("fdContent");
		int fdOrder=pageConfigService.getMaxOrder(fdType);
		fdOrder=fdOrder+1;
		PageConfig page=new PageConfig();
		page.setFdType(fdType);
		page.setFdElementId(fdElementId);
		page.setFdContent(fdContent);
		page.setFdOrder(fdOrder);
		pageConfigService.save(page);
		//如果是学校联盟就要保存学校的图片
		if("02".equals(fdType)){
			String attId=request.getParameter("attIdID");
			AttMain att = attMainService.get(attId);
			att.setFdModelId(page.getFdId());
			att.setFdModelName(PageConfig.class.getName());
			attMainService.save(att);
		}
		if(StringUtil.isNotEmpty(fdType)){
			model.addAttribute("ptype", fdType);
		}else{
			model.addAttribute("ptype", "02");
		}
		model.addAttribute("active", "page");
		return "/admin/page/list";
	}
	
	@RequestMapping("updatePageConfig")
	public String updatePageConfig(HttpServletRequest request){
		String pId=request.getParameter("pid");
		String ptype=request.getParameter("ptype");
		String fdElementId=request.getParameter("fdElementId");
		String fdContent=request.getParameter("fdContent");
		if(StringUtil.isNotEmpty(pId)){
			PageConfig page=pageConfigService.get(pId);
			page.setFdElementId(fdElementId);
			page.setFdContent(fdContent);
			if("02".equals(ptype)){
				String attId=request.getParameter("attIdID");
				//清理原始附件
			    attMainService.deleteAttMainByModelId(pId);
			    //添加新附件
			    AttMain att = attMainService.get(attId);
				att.setFdModelId(pId);
				att.setFdModelName(PageConfig.class.getName());
				attMainService.save(att);
				pageConfigService.save(page);
			}else{
				pageConfigService.save(page);
			}
		}
		return "/admin/page/list";
	}
}
