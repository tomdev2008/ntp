package cn.me.xdf.action.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin/page")
public class PageController {

	@RequestMapping(value="getPageList")
	public String getPageList(HttpServletRequest request){
		return "/admin/page/list";
	}
	
}
