package cn.me.xdf.action.base;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 在线用户设置
 * 
 * @author zhaoqi
 */
@Controller
@RequestMapping(value = "/admin/online")
public class OnlineContorller {
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(Model model, String pageNo, HttpServletRequest request) {
		model.addAttribute("active", "online");
		if (StringUtils.isBlank(pageNo)) {
			pageNo = String.valueOf(1);
		}
		
		
		return "/admin/online/list";
	}
}
