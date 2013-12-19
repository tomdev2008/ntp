package cn.me.xdf.action.base;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.model.organization.RoleEnum;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.model.organization.UserRole;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.UserRoleService;
import cn.me.xdf.service.base.DictionService;

/**
 * 日志设置
 * 
 * @author zhaoqi
 */
@Controller
@RequestMapping(value = "/admin/log")
public class LogContorller {

	
	@Autowired
	private UserRoleService userRoleService;
	@Autowired
	private AccountService accountService;

	@Autowired
	private DictionService dictionService;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(Model model, String pageNo, HttpServletRequest request) {
		model.addAttribute("active", "log");
		if (StringUtils.isBlank(pageNo)) {
			pageNo = String.valueOf(1);
		}
		Finder finder = Finder.create("from UserRole");
		String fdType = request.getParameter("fdType");
		if (StringUtils.isNotBlank(fdType)) {
			finder.append("where roleEnum=:role").setParam("role",
					RoleEnum.valueOf(fdType));
		}
		finder.append("order by roleEnum ASC,fdId ASC");
		Pagination page = userRoleService.getPage(finder,
				Integer.parseInt(pageNo));

		// 处理角色有，组织架构无此人(admin除外)
		List<UserRole> delList = new ArrayList<UserRole>();
		for (Object obj : page.getList()) {
			UserRole ur = (UserRole) obj;
			String personId = ur.getSysOrgPerson().getFdId();
			if (!"1183b0b84ee4f581bba001c47a78b2d9".equals(personId)) {
				SysOrgPerson sop = accountService.findById(personId);
				if (sop == null) {
					delList.add(ur);
					userRoleService.deleteEntity(ur);
				}
			}
		}
		page.getList().removeAll(delList);

		model.addAttribute("page", page);
		model.addAttribute("fdType", fdType);
		return "/admin/role/list";
	}
}
