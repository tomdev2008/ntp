<%@ page import="cn.me.xdf.common.web.SpringUtils" %>
<%@ page import="cn.me.xdf.service.base.AttMainService" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%--
  Created by IntelliJ IDEA.
  User: xiaobin268
  Date: 13-12-17
  Time: 上午11:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<%
    Logger logger = LoggerFactory.getLogger(this.getClass());
    String status = request.getParameter("status");
    System.out.println("status=="+status);
    if (!"OK".equals(status)) {
        return;
    }
    try {
        String videoid = request.getParameter("videoid");
        AttMainService attMainService = (AttMainService) SpringUtils.getBean(request, "attMainService");
        System.out.println("status1=="+status);
        attMainService.updateConvertThrough(videoid);
    } catch (Exception e) {
        logger.error("更新视频转换标志位错误", e);
    }
    //request.getRequestDispatcher("/CCCallbackServlet").forward(request, response);
%>
</body>
</html>