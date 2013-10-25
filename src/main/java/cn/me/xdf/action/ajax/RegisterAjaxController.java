package cn.me.xdf.action.ajax;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.me.xdf.model.base.NotifyEntity;
import cn.me.xdf.model.organization.SysOrgElement;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.model.organization.SysOrgPersonTemp;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.RegisterService;
import cn.me.xdf.utils.ShiroUtils;

/**
 * 注册验证
 * 
 * @author
 * 
 */
@Controller
@RequestMapping(value = "/ajax/register")
@Scope("request")
public class RegisterAjaxController {
	
	@Autowired
	private AccountService accountService;

	@Autowired
	private RegisterService registerService;

	@RequestMapping(value = "checkIdentityCard")
	@ResponseBody
	public int checkIdentityCard(HttpServletRequest request) {
		String str = request.getParameter("str");

		int count = registerService.checkIdentityCard(str);

		return count;
	}
	
	@RequestMapping(value = "checkIdentitymail")
	@ResponseBody
	public int checkIdentitymail(HttpServletRequest request) {
		String str = request.getParameter("str");
		int count = registerService.checkIdentitymail(str);
		return count;
	}

	@RequestMapping(value = "getDeparts")
	@ResponseBody
	public String getDeparts(HttpServletRequest request) {
		String str = request.getParameter("id");
		String info = "";
		List<SysOrgElement> list1 = registerService.getDepartsByParent(str);
		for (SysOrgElement sysOrgElement : list1) {
			String id = sysOrgElement.getFdId();
			String name = sysOrgElement.getFdName();
			info = info + id + ":" + name + "=";

		}
		return info;

	}

	@RequestMapping(value = "register")
	@ResponseBody
	public String register(HttpServletRequest request) {
		try {
			String password = request.getParameter("password");
			String email = request.getParameter("email");
			String name = request.getParameter("name");
			String cradid = request.getParameter("cradid");
			String departid = request.getParameter("departid");
			String deptName = request.getParameter("deptName");
			String tel = request.getParameter("tel");
			String img = request.getParameter("img");
			String sex = request.getParameter("sex");
			String birthday = request.getParameter("birthday");
			String bloodend = request.getParameter("bloodend");
			SysOrgPersonTemp sysOrgPersonTemp = new SysOrgPersonTemp();
			sysOrgPersonTemp.setFdPassword(password);
		    NotifyEntity notifyEntity = new NotifyEntity();
		    notifyEntity.setFdMobileNo(tel);
		    notifyEntity.setFdEmail(email);
		    notifyEntity.setRealName(name);
		    sysOrgPersonTemp.setNotifyEntity(notifyEntity);
			sysOrgPersonTemp.setDeptName(deptName);
			sysOrgPersonTemp.setFdSex(sex);
			sysOrgPersonTemp.setFdIdentityCard(cradid);
			sysOrgPersonTemp.setDepatId(departid);
			sysOrgPersonTemp.setFdBirthDay(birthday);
			sysOrgPersonTemp.setFdBloodType(bloodend);
			sysOrgPersonTemp.setFdIcoUrl(img);
			registerService.registerTemp(sysOrgPersonTemp);
			if (ShiroUtils.getUser() == null) {
				return "redirect:/login";
			}
			return "redirect:/register/list";
		} catch (Exception e) {
			e.printStackTrace();
			if (ShiroUtils.getUser() == null) {
				return "redirect:/loginerr";
			}
			return "redirect:/register/listerr";
			
		}
		

	}
	/**
	 * 修改密码时验证原密码是否正确
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "checkOldPwd")
	@ResponseBody
	public boolean checkOldPwd(HttpServletRequest request) {
		
		String oldPwd = request.getParameter("str");
		String uid = ShiroUtils.getUser().getId();
		SysOrgPersonTemp fdPerson = accountService.load(uid);
        String userPwd = fdPerson.getFdPassword();
        if(oldPwd.equals(userPwd)){
        	return true;
        } else {
        	return false;
        }
	}
	

}
