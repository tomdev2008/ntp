package cn.me.xdf.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.me.xdf.common.spring.ApplicationContextHelper;
import cn.me.xdf.model.log.LogApp;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.ShiroDbRealm.ShiroUser;
import cn.me.xdf.service.log.LogAppService;
import cn.me.xdf.utils.ShiroUtils;

public class LogAppFilter implements Filter{
	
	private LogAppService logAppService;
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest)arg0;
		String url = servletRequest.getRequestURI();
		ShiroUser user = ShiroUtils.getUser();
		boolean canSave =(url.endsWith(".css")|| url.endsWith(".js")||url.endsWith(".swf")||url.endsWith(".jpg")||url.endsWith(".png")||url.endsWith(".psd"));
		if(user==null||canSave){
			arg2.doFilter(arg0, arg1); 
			return;
		}else{
			LogApp app = new LogApp();
			app.setIp(arg0.getLocalAddr());
			app.setMethod(servletRequest.getMethod());
			SysOrgPerson person = new SysOrgPerson();
			person.setFdId( user.getId());
			app.setPerson(person);
			app.setSessionId(servletRequest.getSession().getId());
			app.setUrl(url);
			app.setTime(new Date());
			logAppService.save(app);
			arg2.doFilter(arg0, arg1);
			return;
		}
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(arg0.getServletContext());
		logAppService = (LogAppService) wac.getBean("logAppService");
	}

}
