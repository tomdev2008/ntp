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
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.log.LogApp;
import cn.me.xdf.model.log.LogLogin;
import cn.me.xdf.model.log.LogLogout;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.log.LogAppService;
import cn.me.xdf.service.log.LogLoginService;
import cn.me.xdf.service.log.LogLogoutService;
import cn.me.xdf.utils.DateUtil;

/**
 * 日志设置
 * 
 * @author zhaoqi
 */
@Controller
@RequestMapping(value = "/admin/log")
public class LogContorller {

	
	@Autowired
	private AccountService accountService;
	@Autowired
	private LogLoginService logLoginService;
	@Autowired
	private LogLogoutService logLogoutService;
	@Autowired
	private LogAppService logAppService;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(Model model, String pageNo, HttpServletRequest request) {
		model.addAttribute("active", "log");
		if (StringUtils.isBlank(pageNo)) {
			pageNo = String.valueOf(1);
		}
		
		String fdType = request.getParameter("fdType");
		if (StringUtils.isEmpty(fdType)) {
			fdType="LogLogin";
		}
		Finder finder = Finder.create("");
		Pagination page=null;
		List<Map> returnList = new ArrayList<Map>();
		if(fdType.equals("LogLogin")){
			finder.append("from LogLogin l");
			finder.append("order by l.time desc");
			page= logLoginService.getPage(finder,Integer.parseInt(pageNo));
			List<LogLogin> list = (List<LogLogin>) page.getList();
			for (int i = 0; i < list.size(); i++) {
				Map map = new HashMap();
				map.put("fdLogId", list.get(i).getFdId());
				map.put("fdUserName", list.get(i).getPerson().getFdName());
				map.put("fdUserDep", list.get(i).getPerson().getHbmParent()==null?"":list.get(i).getPerson().getHbmParent().getFdName());
				map.put("time", DateUtil.convertDateToString(list.get(i).getTime(), "yyyy-MM-dd HH:mm:ss"));
				map.put("logType", "登录");
				returnList.add(map);
			}
		}else if(fdType.equals("LogLogout")){
			finder.append("from LogLogout l");
			finder.append("order by l.time desc");
			page= logLogoutService.getPage(finder,Integer.parseInt(pageNo));
			List<LogLogout> list = (List<LogLogout>) page.getList();
			for (int i = 0; i < list.size(); i++) {
				Map map = new HashMap();
				map.put("fdLogId", list.get(i).getFdId());
				map.put("fdUserName", list.get(i).getPerson().getFdName());
				map.put("fdUserDep", list.get(i).getPerson().getHbmParent()==null?"":list.get(i).getPerson().getHbmParent().getFdName());
				map.put("time", DateUtil.convertDateToString(list.get(i).getTime(), "yyyy-MM-dd HH:mm:ss"));
				map.put("logType", "登出");
				returnList.add(map);
			}
		}else if(fdType.equals("LogApp")){
			finder.append("from LogApp l");
			finder.append("order by l.time desc");
			page= logAppService.getPage(finder,Integer.parseInt(pageNo));
			List<LogApp> list = (List<LogApp>) page.getList();
			for (int i = 0; i < list.size(); i++) {
				Map map = new HashMap();
				map.put("fdLogId", list.get(i).getFdId());
				SysOrgPerson orgPerson = accountService.load(list.get(i).getPersonId());
				map.put("fdUserName", orgPerson.getFdName());
				map.put("fdUserDep", orgPerson.getHbmParent()==null?"":orgPerson.getHbmParent().getFdName());
				map.put("time", DateUtil.convertDateToString(list.get(i).getTime(), "yyyy-MM-dd HH:mm:ss"));
				if(list.get(i).getMethod().equals(Constant.DB_UPDATE)){
					map.put("logType", "修改");
				}else if(list.get(i).getMethod().equals(Constant.DB_DELETE)){
					map.put("logType", "删除");
				}else{
					map.put("logType", "插入");
				}
				returnList.add(map);
			}
		}
		
		model.addAttribute("page", page);
		model.addAttribute("list", returnList);
		model.addAttribute("fdType", fdType);
		return "/admin/log/list";
	}
}
