package cn.me.xdf.action.base;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.model.organization.SysOrgPersonTemp;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.RegisterService;
import cn.me.xdf.service.SysOrgPersonService;

/**
 * 用户管理
 * 
 * @author zuoyi
 */
@Controller
@RequestMapping(value = "/admin/user")
public class UserController {
	@Autowired
	private SysOrgPersonService sysOrgPersonService;
	
	@Autowired
	private RegisterService registerService;
	
	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(Model model, String pageNo, HttpServletRequest request) {
		model.addAttribute("active", "user");
		if (StringUtils.isBlank(pageNo)) {
			pageNo = String.valueOf(1);
		}
		Finder finder = Finder.create(" from SysOrgPerson p where p.loginName <> 'admin' ");
		String param = request.getParameter("fdKey");
		if (StringUtils.isNotBlank(param)) {
			finder.append(" and lower(p.loginName) like :param  or lower(p.notifyEntity.realName) like :param or lower(p.hbmParent.fdName) like :param  ").setParam("param",
					"%"+param+"%");
		}
		Pagination page = sysOrgPersonService.getPage(finder,
				Integer.parseInt(pageNo));

		model.addAttribute("page", page);
		model.addAttribute("fdKey", param);
		return "/admin/user/list";
	}
	
	@RequestMapping(value = "updateUserInfo/{id}")
	public String updateUserInfo(Model model,HttpServletRequest request,
			@PathVariable("id") String id) {
		model.addAttribute("active", "user");
		return "forward:/register/updateTeacher?id="+id;
	}
	
	@RequestMapping(value = "resetPassword/{id}")
	public String resetPassword(Model model,HttpServletRequest request,
			@PathVariable("id") String id) {
		model.addAttribute("active", "user");
		request.setAttribute("fdId", id);
		if(StringUtil.isBlank(id)){
			return "redirect:/register/changePwd";
		}
		SysOrgPerson person = sysOrgPersonService.load(id);
		SysOrgPersonTemp personTemp = registerService
                .findUniqueByProperty(SysOrgPersonTemp.class, "fdIdentityCard",
                        person.getFdIdentityCard());
		request.setAttribute("fdTmpId", personTemp!=null?personTemp.getFdId():id);
		request.setAttribute("fdEmail", person.getFdEmail());
		return "/admin/user/rePassword";
	}
	
	@RequestMapping(value = "savePassword")
	public String savePassword(HttpServletRequest request) {
		String fdId = request.getParameter("fdId");
		String fdTmpId = request.getParameter("fdTmpId");
		String fdPassword = request.getParameter("fdPassword");
		registerService.updateTeacherPwd(fdTmpId, fdPassword, fdId);
		return "redirect:/admin/user/list";
	}
}
