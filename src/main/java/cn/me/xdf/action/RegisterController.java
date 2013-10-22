package cn.me.xdf.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.model.organization.SysOrgElement;
import cn.me.xdf.model.organization.SysOrgPersonTemp;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.RegisterService;
import cn.me.xdf.service.SysOrgElementService;
import cn.me.xdf.utils.ShiroUtils;

@Controller
@RequestMapping(value = "/register")
public class RegisterController {
	

	@Autowired
	private RegisterService registerService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private SysOrgElementService sysOrgElementService;

	@RequestMapping(value = "add")
	public String registerForm(Model model) {
		List<SysOrgElement> elements = sysOrgElementService.findTypeis1();
		model.addAttribute("elements", elements);
		return "/base/register/add";
	}

	/*
	 * 新教师保存
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(SysOrgPersonTemp sysOrgPersonTemp,
			HttpServletRequest request) {
		registerService.registerTemp(sysOrgPersonTemp);
		if (ShiroUtils.getUser() == null) {
			return "redirect:/login";
		}
		return "redirect:/register/list";
	}
	/*
	 * 管理员编辑新教师
	 */
	@RequestMapping(value = "edit/{fdId}")
	public String edit(Model model, @PathVariable("fdId") String fdId) {
		SysOrgPersonTemp sysOrgPersonTemp = registerService.get(
				SysOrgPersonTemp.class, fdId);

		model.addAttribute("bean", sysOrgPersonTemp);
		List<SysOrgElement> elements = sysOrgElementService.findTypeis1();
		model.addAttribute("elements", elements);
		return "/base/register2/edit";
	}

	/*
	 * 邮件注册入口
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "register/{randomCode}")
	public String register(Model model,
			@PathVariable("randomCode") String randomCode) {
		Finder finder = Finder
				.create("from SysOrgPersonTemp sopt where sopt.isRegistered = '0' and sopt.randomCode = :randomCode");
		finder.setParam("randomCode", randomCode);
		List<SysOrgPersonTemp> sysOrgPersonTempList = registerService
				.find(finder);
		SysOrgPersonTemp sysOrgPersonTemp = new SysOrgPersonTemp();
		if (sysOrgPersonTempList != null && sysOrgPersonTempList.size() > 0) {
			sysOrgPersonTemp = sysOrgPersonTempList.get(0);
		} else {
			return "/login";
		}

		model.addAttribute("sopt", sysOrgPersonTemp);

		return "/base/register/register";
	}
	
}
