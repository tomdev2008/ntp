package cn.me.xdf.action.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.model.organization.SysOrgElement;
import cn.me.xdf.model.system.PageConfig;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.SysOrgDepartService;
import cn.me.xdf.service.system.SysPageConfigService;
@Controller
@RequestMapping(value = "/ajax/page")
@Scope("request")
public class PageAjaxController {
	
	@Autowired
	private SysPageConfigService pageConfigService;
	
	@Autowired
	private SysOrgDepartService sysOrgDepartService;
	/**
	 * 配置列表
	 */
	@RequestMapping(value="getPages")
	@ResponseBody
	public String getPages(HttpServletRequest request){
		String ptype=request.getParameter("ptype");
		List<PageConfig> list=pageConfigService.getPageConfigs(ptype);
		List<Map> maps=new ArrayList<Map>();
		for(PageConfig page:list){
			Map map=new HashMap();
			map.put("id", page.getFdId());
			SysOrgElement element=sysOrgDepartService.getSysOrgElementById(page.getFdElementId());
			map.put("name", element.getFdName());
			map.put("content", page.getFdContent());
			map.put("order", page.getFdOrder());
			maps.add(map);
		}
		Map mapAll=new HashMap();
		mapAll.put("list",maps);
		return JsonUtils.writeObjectToJson(mapAll);
	}
	/**
	 * 更新顺序
	 */
	@RequestMapping(value="updatePageOrder")
	public void updatePageOrder(HttpServletRequest request){
		
	}
}
