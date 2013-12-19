package cn.me.xdf.action.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.model.log.LogLogin;
import cn.me.xdf.model.log.LogOnline;
import cn.me.xdf.service.log.LogOnlineService;
import cn.me.xdf.utils.DateUtil;

/**
 * 在线用户设置
 * 
 * @author zhaoqi
 */
@Controller
@RequestMapping(value = "/admin/online")
public class OnlineContorller {
	
	
	@Autowired
	private LogOnlineService logOnlineService;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(Model model, String pageNo, HttpServletRequest request) {
		model.addAttribute("active", "online");
		if (StringUtils.isBlank(pageNo)) {
			pageNo = String.valueOf(1);
		}
		
		Pagination page=null;
		List<Map> returnList = new ArrayList<Map>();
		Finder finder = Finder.create("");
		finder.append("from LogOnline l where l.isOnline=:isOnline");
		finder.append("order by l.loginTime desc");
		finder.setParam("isOnline", true);
		page= logOnlineService.getPage(finder,Integer.parseInt(pageNo));
		List<LogOnline> list = (List<LogOnline>) page.getList();
		for (int i = 0; i < list.size(); i++) {
			Map map = new HashMap();
			map.put("fdLogId", list.get(i).getFdId());
			map.put("fdUserName", list.get(i).getPerson().getFdName());
			map.put("fdUserDep", list.get(i).getPerson().getHbmParent()==null?"":list.get(i).getPerson().getHbmParent().getFdName());
			map.put("time", DateUtil.convertDateToString(list.get(i).getLoginTime(), "yyyy-MM-dd HH:mm:ss"));
			map.put("loginNum", list.get(i).getLoginNum());
			map.put("loginDay", list.get(i).getLoginDay());
			returnList.add(map);
		}
		model.addAttribute("page", page);
		model.addAttribute("list", returnList);
		return "/admin/online/list";
	}
}
