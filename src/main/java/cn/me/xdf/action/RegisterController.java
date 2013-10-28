package cn.me.xdf.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.NotifyEntity;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.organization.SysOrgElement;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.model.organization.SysOrgPersonTemp;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.RegisterService;
import cn.me.xdf.service.SysOrgElementService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.utils.ShiroUtils;

@Controller
@RequestMapping(value = "/register")
@Scope("request")
public class RegisterController {
	

	@Autowired
	private RegisterService registerService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private SysOrgElementService sysOrgElementService;
	
	@Autowired
    private AttMainService attMainService;

	@RequestMapping(value = "add")
	public String registerForm(Model model) {
		List<SysOrgElement> elements = sysOrgElementService.findTypeis1();
		model.addAttribute("elements", elements);
		return "/base/register/add";
	}
	
	/**
	 * 更改用户基本信息)
	 * @param sysOrgPersonTemp
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "updateOtherData", method = RequestMethod.POST)
	public String updateOtherData(SysOrgPersonTemp sysOrgPersonTemp,
			HttpServletRequest request) {
		if (ShiroUtils.getUser() == null) {
			return "redirect:/login";
		}
		SysOrgPersonTemp personTemp = registerService
				.findUniqueByProperty(SysOrgPersonTemp.class, "fdIdentityCard",
						sysOrgPersonTemp.getFdIdentityCard());
		if(personTemp != null){
			registerService.updateOtherData(sysOrgPersonTemp,sysOrgPersonTemp.getPersonId());
		}else {
			registerService.updatePersonData(sysOrgPersonTemp,sysOrgPersonTemp.getPersonId());
		}
		
		return "redirect:/register/updateTeacher";
	}
	/**
	 * 返回修改图像
	 * @return
	 */
	@RequestMapping(value = "updateIco")
	public String updateIco(Model model){
		if (ShiroUtils.getUser() == null) {
			return "redirect:/login";
		}
		String uid = ShiroUtils.getUser().getId();
		SysOrgPerson person = accountService.load(uid);
		SysOrgPersonTemp personTemp = registerService
				.findUniqueByProperty(SysOrgPersonTemp.class, "fdIdentityCard",
						person.getFdIdentityCard());
		if(personTemp !=null){
			model.addAttribute("fdId",personTemp.getFdId());
		}
		model.addAttribute("fdIsEmp",person.getFdIsEmp());
		model.addAttribute("fdIdentityCard",person.getFdIdentityCard());
		model.addAttribute("fdIcoUrl",person.getPoto());
		return "/base/newTeacher/editIco";
	}
	/**
	 * 更新新教师图像
	 * @param sysOrgPersonTemp
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "updateTeacher", method = RequestMethod.POST)
	public String updateTeacher(SysOrgPersonTemp sysOrgPersonTemp,
			HttpServletRequest request) {
		if (ShiroUtils.getUser() == null) {
			return "redirect:/login";
		}
		String uid = ShiroUtils.getUser().getId();
		SysOrgPersonTemp personTemp = registerService
				.findUniqueByProperty(SysOrgPersonTemp.class, "fdIdentityCard",
						sysOrgPersonTemp.getFdIdentityCard());
		if(personTemp != null){
	    	String attMainId=request.getParameter("attId");
	    	if(StringUtil.isNotBlank(attMainId)){
		    	AttMain attMain = attMainService.get(attMainId);
		    	attMain.setFdModelId(personTemp.getFdId());
		    	attMain.setFdModelName("cn.me.xdf.model.organization.SysOrgPersonTemp");
		    	attMain.setFdKey("Person");
		    	attMainService.update(attMain);
			}
	    	
		  registerService.updateTeacherPic(sysOrgPersonTemp,uid);
		} else {
		  registerService.updatePerToDBIXDF(sysOrgPersonTemp.getFdIcoUrl(),uid);
		}
		return "redirect:/register/updateTeacher";
	}
	/**
	 * 点击修改密码时返回修改密码页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "changePwd")
	public String changePwd(Model model){
		if (ShiroUtils.getUser() == null) {
			return "redirect:/login";
		}
		String uid = ShiroUtils.getUser().getId();
		SysOrgPerson person = accountService.load(uid);
		SysOrgPersonTemp personTemp = registerService
				.findUniqueByProperty(SysOrgPersonTemp.class, "fdIdentityCard",
						person.getFdIdentityCard());
		if(personTemp != null){
		  model.addAttribute("fdId", personTemp.getFdId());
		  model.addAttribute("fdEmail", personTemp.getFdEmail());
		}
		model.addAttribute("fdIcoUrl", person.getPoto());
		model.addAttribute("fdIsEmp", personTemp.getFdIsEmp());
		return "/base/newTeacher/changePwd";
	}
	/**
	 * 修改密码的方法
	 * @param sysOrgPersonTemp
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "updateTeacherPwd", method = RequestMethod.POST)
	public String updateTeacherPwd(SysOrgPersonTemp sysOrgPersonTemp,HttpServletRequest request){
		if (ShiroUtils.getUser() == null) {
			return "redirect:/login";
		}
		String uid = ShiroUtils.getUser().getId();
		String newPwd = sysOrgPersonTemp.getFdPassword();
		String fdId = sysOrgPersonTemp.getFdId();
		registerService.updateTeacherPwd(fdId, newPwd, uid);
		return "redirect:/register/changePwd";
	}
	
	/**
	 * 新老师更改信息
	 * @param model
	 */
	@RequestMapping(value = "updateTeacher")
	public String updateTeacher(Model model) {
		if (ShiroUtils.getUser() == null) {
			return "redirect:/login";
		}
		String uid = ShiroUtils.getUser().getId();
		SysOrgPerson person = accountService.load(uid);
		if ("0".equals(person.getFdIsEmp())){
			SysOrgPersonTemp sysOrgPersonTemp = registerService
					.findUniqueByProperty(SysOrgPersonTemp.class, "fdIdentityCard",
							person.getFdIdentityCard());
			if (sysOrgPersonTemp == null) {
		        sysOrgPersonTemp = new SysOrgPersonTemp();
			    sysOrgPersonTemp.setDeptName(person.getDeptName());
			    sysOrgPersonTemp.setDepatId(person.getDepatId());;
			    sysOrgPersonTemp.setFdIcoUrl(person.getPoto());
			    sysOrgPersonTemp.setFdSex(person.getFdSex());
			    sysOrgPersonTemp.setFdIdentityCard(person.getFdIdentityCard());
			    NotifyEntity notifyEntity = new NotifyEntity();
			    notifyEntity.setFdMobileNo(person.getFdMobileNo()==null?"":person.getFdMobileNo());
			    sysOrgPersonTemp.setNotifyEntity(notifyEntity);
			    sysOrgPersonTemp.setPersonId(person.getFdId());
				model.addAttribute("bean", sysOrgPersonTemp);
			}else {
			   sysOrgPersonTemp.setPersonId(person.getFdId());
			   model.addAttribute("bean", sysOrgPersonTemp);
			}
		}else{
			SysOrgPersonTemp sysOrgPersonTemp = new SysOrgPersonTemp();
		    sysOrgPersonTemp.setDeptName(person.getDeptName());
		    sysOrgPersonTemp.setDepatId(person.getDepatId());;
		    sysOrgPersonTemp.setFdIcoUrl(person.getPoto());
		    sysOrgPersonTemp.setFdSex(person.getFdSex());
		    sysOrgPersonTemp.setFdIdentityCard(person.getFdIdentityCard());
		    NotifyEntity notifyEntity = new NotifyEntity();
		    notifyEntity.setFdMobileNo(person.getFdMobileNo()==null?"":person.getFdMobileNo());
		    sysOrgPersonTemp.setNotifyEntity(notifyEntity);
		    sysOrgPersonTemp.setPersonId(person.getFdId());
		    SysOrgPersonTemp personTemp = registerService
					.findUniqueByProperty(SysOrgPersonTemp.class, "fdIdentityCard",
							person.getFdIdentityCard());
			if (personTemp != null) {		        
			    sysOrgPersonTemp.setFdBirthDay(personTemp.getFdBirthDay());
			    sysOrgPersonTemp.setFdBloodType(personTemp.getFdBloodType());				
			}		    
			model.addAttribute("bean", sysOrgPersonTemp);
		}
		model.addAttribute("fdIcoUrl",person.getPoto());
		model.addAttribute("fdIsEmp", person.getFdIsEmp());
		if(person.getHbmParent() != null && person.getHbmParent().getHbmParentOrg()!= null){
			   model.addAttribute("sysParOrg",person.getHbmParent().getHbmParentOrg().getFdName());
			   model.addAttribute("sysParOrgId",person.getHbmParent().getHbmParentOrg().getFdId());
		}
		List<SysOrgElement> elements = sysOrgElementService.findTypeis1();
		model.addAttribute("elements", elements);
		if("0".equals(person.getFdIsEmp())){
			return "/base/newTeacher/edit";
		}
		return "/base/newTeacher/view";
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
