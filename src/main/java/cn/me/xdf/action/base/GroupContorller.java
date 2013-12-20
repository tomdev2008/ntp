package cn.me.xdf.action.base;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.model.organization.SysOrgGroup;
import cn.me.xdf.model.organization.UserRole;
import cn.me.xdf.service.SysOrgPersonService;

/**
 * 群组管理
 * 
 * @author zuoyi
 */
@Controller
@RequestMapping(value = "/admin/group")
public class GroupContorller {
	
	@Autowired
	private SysOrgPersonService sysOrgPersonService;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	// @RequiresRoles({"admin"})
	public String list(Model model, String pageNo, HttpServletRequest request) {
		model.addAttribute("active", "group");
		if (StringUtils.isBlank(pageNo)) {
			pageNo = String.valueOf(1);
		}
		Finder finder = Finder.create("from SysOrgGroup g   ");

		String param = request.getParameter("fdKey");
		if (StringUtils.isNotBlank(param)) {
			finder.append(" where g.fdName like :param ").setParam("param",
					"%"+param+"%");
		}
		Pagination page = sysOrgPersonService.getPage(finder,
				Integer.parseInt(pageNo));

		model.addAttribute("page", page);
		model.addAttribute("fdKey", param);
		return "/admin/group/list";
	}
	
	@RequestMapping(value = "view/{id}")
	public String view(Model model, @PathVariable("id") String id) {
		model.addAttribute("active", "group");
		SysOrgGroup group = sysOrgPersonService.load(SysOrgGroup.class, id);
		model.addAttribute("bean", group);
		return "/admin/group/view";
	}
}
