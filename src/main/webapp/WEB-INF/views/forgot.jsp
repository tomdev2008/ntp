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
            1. 忘记集团邮箱密码，请<a href="https://adpassport.xdf.cn/resetpasswd.aspx">点击这里</a>重置密码；<br />
            2. 忘记临时账号密码，请<a href="mailto:yangyifeng@xdf.cn?subject=NTP临时账号忘记密码&body=账号名称：">联系我们</a>为您重置密码。
            </p>
            
       	  <p class="login_btns forgot-btns clearfix">
            <button class="btn btn-register" onClick="location.href='${ctx}/login'" type="button">返回登录</button>
               <button class="btn btn-primary btn-login" onClick="location.href='mailto:yangyifeng@xdf.cn?subject=NTP临时账号忘记密码&body=账号名称：'" type="button">找回密码</button>
            </p>
        </div>
		
	</div>
</div>
</body>
</html>
