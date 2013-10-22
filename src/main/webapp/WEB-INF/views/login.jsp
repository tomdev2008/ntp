<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新东方在线教师备课平台</title>
<link href="${ctx}/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="${ctx}/resources/css/login.css" rel="stylesheet" type="text/css">
</head>

<body>
<div class="header">
	<div class="container">
		<a href="#" class="logo"></a>
		<div class="top_menu"><a href="#">首页</a><a href="#">注册</a></div>
	</div>
</div>
<div class="container main">
	<div class="login_wrap">
		<div class="login_tit"></div>
		<div class="login_form">
        	<input type="text" class="input-block-level inp inp-mail" placeholder="yourname@xdf.cn">
        	<input type="text" class="input-block-level inp inp-password" placeholder="请输入您的登录密码"> 
            <span class="help-block text-warning"><b class="icon-disc-bg warning">!</b>电子邮箱账号/密码输入错误，请重新登录</span>
            <p class="bar_link">
           	<a href="./forgot.html" class="btn btn-link"><i class="icon-qm"></i>忘记密码</a>
            </p>
       	  <p class="login_btns clearfix">
            <button class="btn btn-register" type="button" onClick="location.href='${ctx }/register/add'" >注册</button>
            <button type="submit" id="loginSubmit"  class="btn btn-primary btn-login" type="button">登录</button>
            </p>
        </div>
		<div class="login_intro">请使用您的集团邮箱账号及密码直接登录本平台，或者注册临时账号以保证快速访问。<br />
        您在登录或者注册过程中遇到任何问题，请直接联系我们。</div>
	</div>
</div>
</body>
</html>
