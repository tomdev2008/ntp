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
import cn.me.xdf.model.organization.SysOrgDepart;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.model.organization.SysOrgPersonTemp;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.RegisterService;
import cn.me.xdf.service.SysOrgDepartService;
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
    private SysOrgDepartService SysOrgDepartService;

    @Autowired
    private AttMainService attMainService;

    @RequestMapping(value = "add")
    public String registerForm(Model model) {
        List<SysOrgDepart> elements = SysOrgDepartService.findTypeis1();
        model.addAttribute("elements", elements);
        return "/base/register/add";
    }

    /**
     * 更改用户基本信息)
     *
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
        if (personTemp != null) {
            registerService.updateOtherData(sysOrgPersonTemp, sysOrgPersonTemp.getPersonId());
        } else {
            SysOrgPerson person = accountService.load(sysOrgPersonTemp.getPersonId());
            sysOrgPersonTemp.setFdIsEmp(person.getFdIsEmp());
            sysOrgPersonTemp.setDepatId(person.getDepatId());
            registerService.save(sysOrgPersonTemp);
            registerService.updatePersonData(sysOrgPersonTemp, sysOrgPersonTemp.getPersonId());
        }

        return "redirect:/register/updateTeacher";
    }

    /**
     * 返回修改图像
     *
     * @return
     */
    @RequestMapping(value = "updateIco")
    public String updateIco(Model model) {
        if (ShiroUtils.getUser() == null) {
            return "redirect:/login";
        }
        model.addAttribute("active", "photo");
        String uid = ShiroUtils.getUser().getId();
        SysOrgPerson person = accountService.load(uid);
        SysOrgPersonTemp personTemp = registerService
                .findUniqueByProperty(SysOrgPersonTemp.class, "fdIdentityCard",
                        person.getFdIdentityCard());
        if (personTemp != null) {
            model.addAttribute("fdId", personTemp.getFdId());
        }
        model.addAttribute("fdIsEmp", person.getFdIsEmp());
        model.addAttribute("fdIdentityCard", person.getFdIdentityCard());
        model.addAttribute("fdIcoUrl", person.getPoto());
        model.addAttribute("fdUserName", person.getLoginName());
        return "/base/newTeacher/editIco";
    }

    /**
     * 更新新教师图像
     *
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
        String fdUserName = request.getParameter("fdUserName");
        String attMainId = request.getParameter("attId");
        if (StringUtil.isNotBlank(attMainId)) {
            AttMain attMain = attMainService.get(attMainId);
            if (personTemp != null) {
                attMain.setFdModelId(personTemp.getFdId());
                attMain.setFdModelName(SysOrgPersonTemp.class.getName());
                attMain.setFdKey("Person");
                attMainService.update(attMain);
                registerService.updateTeacherPic(sysOrgPersonTemp, uid);
            } else {
                registerService.updatePerToDBIXDF(fdUserName, attMain.getFdFilePath(), uid);
            }
        }
        return "redirect:/register/updateTeacher";
    }

    /**
     * 点击修改密码时返回修改密码页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "changePwd")
    public String changePwd(Model model) {
        if (ShiroUtils.getUser() == null) {
            return "redirect:/login";
        }
        model.addAttribute("active", "pwd");
        String uid = ShiroUtils.getUser().getId();
        SysOrgPerson person = accountService.load(uid);
        SysOrgPersonTemp personTemp = registerService
                .findUniqueByProperty(SysOrgPersonTemp.class, "fdIdentityCard",
                        person.getFdIdentityCard());
        if (personTemp != null) {
            model.addAttribute("fdId", personTemp.getFdId());
        }
        model.addAttribute("fdEmail", person.getFdEmail());
        model.addAttribute("fdIcoUrl", person.getPoto());
        model.addAttribute("fdIsEmp", person.getFdIsEmp());
        return "/base/newTeacher/changePwd";
    }

    /**
     * 修改密码的方法
     *
     * @param sysOrgPersonTemp
     * @param request
     * @return
     */
    @RequestMapping(value = "updateTeacherPwd", method = RequestMethod.POST)
    public String updateTeacherPwd(SysOrgPersonTemp sysOrgPersonTemp, HttpServletRequest request) {
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
     *
     * @param model
     */
    @RequestMapping(value = "updateTeacher")
    public String updateTeacher(Model model,HttpServletRequest request) {
        String uid = request.getParameter("id");
        if(StringUtil.isBlank(uid)){
        	uid = ShiroUtils.getUser().getId();
        }
        model.addAttribute("active", "user");
        SysOrgPerson person = accountService.load(uid);
        SysOrgPersonTemp sysOrgPersonTemp = registerService
                .findUniqueByProperty(SysOrgPersonTemp.class, "fdIdentityCard",
                        person.getFdIdentityCard());
        model.addAttribute("person", person);
        if (sysOrgPersonTemp != null) {
            model.addAttribute("bean", sysOrgPersonTemp);
        }
        model.addAttribute("fdIcoUrl", person.getPoto());
        if (person.getHbmParent() != null && person.getHbmParent().getHbmParentOrg() != null) {
            model.addAttribute("sysParOrg", person.getHbmParent().getHbmParentOrg().getFdName());
            model.addAttribute("sysParOrgId", person.getHbmParent().getHbmParentOrg().getFdId());
        }
        List<SysOrgDepart> elements = SysOrgDepartService.findTypeis1();
        model.addAttribute("elements", elements);
        return "/base/newTeacher/edit";
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
