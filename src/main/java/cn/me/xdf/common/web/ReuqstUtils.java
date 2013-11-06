package cn.me.xdf.common.web;



import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-11-6
 * Time: 下午5:30
 * To change this template use File | Settings | File Templates.
 */
public class ReuqstUtils {


    public static void getBeanByAttr(Object entity,WebRequest servletRequest){
        WebRequestDataBinder dataBinder = new WebRequestDataBinder(entity);
        dataBinder.bind(servletRequest);
    }


}
