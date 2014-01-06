package cn.me.xdf.action.letter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.page.SimplePage;
import cn.me.xdf.model.letter.ConnectLetter;
import cn.me.xdf.model.letter.PrivateLetter;
import cn.me.xdf.model.letter.RelationLetter;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.letter.ConnectLetterService;
import cn.me.xdf.service.letter.PrivateLetterService;
import cn.me.xdf.service.letter.RelationLetterService;
import cn.me.xdf.utils.ShiroUtils;

/**
 * 私信的controller
 * @author yuhuizhe
 */
@Controller
@RequestMapping(value = "/letter")
@Scope("request")
public class PrivateLetterController {
	
	@Autowired
	private PrivateLetterService privateLetterService;
	
	@Autowired
	private RelationLetterService relationLetterService;
	
	@Autowired
	private ConnectLetterService connectLetterService;
	
	@Autowired
	private AccountService accountService;
	
	/**
	 * 返回发送私信页面
	 * @return
	 */
	@RequestMapping(value="sendLetter")
	public String sendLetter(Model model){
		model.addAttribute("active", "sendLetter");
		model.addAttribute("sendUserId", ShiroUtils.getUser().getId());
		return "letter/letter_add";
	}
	/**
	 * 返回发送邮件页面
	 * @return
	 */
	@RequestMapping(value="sendEmail")
	public String sendEmail(Model model){
		model.addAttribute("active", "sendEmail");
		return "letter/sendEmail";
	}
	/**
	 * 返回与某个人的对话
	 * @return
	 */
	@RequestMapping(value="letterDetail")
	public String letterDetail(Model model){
		model.addAttribute("active", "letterList");
		return "letter/letter_detail";
	}
	/**
	 * 找出我的私信列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="findLetterList")
	public String findLetterList(Model model){
		model.addAttribute("active", "letterList");
		return "/letter/letter_list";
	}
	
	
	
}





