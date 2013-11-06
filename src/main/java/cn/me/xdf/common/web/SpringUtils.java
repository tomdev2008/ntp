package cn.me.xdf.common.web;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-11-6
 * Time: 下午5:56
 * To change this template use File | Settings | File Templates.
 */
public class SpringUtils {


    public static Object getBean(HttpServletRequest request, String name) {
        WebApplicationContext wb = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
        return wb.getBean(name);
    }
}
