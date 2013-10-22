<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="${ctx}/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="${ctx}/resources/css/login.css" rel="stylesheet" type="text/css">
</head>

<body>
<div class="container main">
	<div class="login_wrap">
		<div class="login_tit"></div>
		<div class="login_form">        	
            <p class="forgot-intro">
           	请采用以下方式找回您的登录密码：
            </p>
            <p class="forgot-intro">
            1. 忘记集团邮箱密码，请联系本地邮箱运维工程师；<br />
            2. 忘记临时账号密码，请联系系统管理员为您重置密码。
            </p>
            
       	  <p class="login_btns forgot-btns clearfix">
            <button class="btn btn-register" onClick="location.href='${ctx}/login'" type="button">返回登录</button>
               <button class="btn btn-primary btn-login" onClick="location.href='mailto: yangyifeng@xdf.cn?subject=<%=java.net.URLEncoder.encode("忘记密码","gb2312")%>'" type="button">找回密码</button>
            </p>
        </div>
		
	</div>
</div>
</body>
</html>
