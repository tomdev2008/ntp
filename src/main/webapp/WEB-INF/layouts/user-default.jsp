<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<!--[if lt IE 7]>      <html lang="zh_CN" class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html lang="zh_CN" class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html lang="zh_CN" class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html lang="zh_CN" class="no-js"> <!--<![endif]-->
<head>
<meta charset="UTF-8" />
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta http-equiv="Cache-Control" content="no-cache,no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="author" content="" />
<meta name="description" content="" />
<meta name="keywords" content="" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="black" name="apple-mobile-web-app-status-bar-style" />
<meta content="telephone=no" name="format-detection" />
<title>新东方教师在线备课平台</title>
<link rel="shortcut icon" href="${ctx}/resources/img/favicon.ico" />
<!--[if lt IE 9]>
	<script src="js/html5.js"></script>
<![endif]-->
<link rel="stylesheet" href="${ctx}/resources/css/global.css" />
<link href="${ctx}/resources/css/settings.css" rel="stylesheet" type="text/css">
<script src="${ctx}/resources/js/jquery.js" type="text/javascript"></script>

<sitemesh:head />
</head>
<body>
  <%@ include file="/WEB-INF/layouts/header.jsp"%>
  <div class="fixed-left">
	<div class="container">
		<div class="col-left">
    	<ul class="nav nav-list sidenav">
        	<li class="nav-header">
            	<div class="tit-icon_bg"><i class="icon-gear"></i><i class="icon-sj"></i></div>
                            账号设置
            </li>
            <tags:shirourl url="${ctx}/register/updateTeacher" active="user" text="个人资料" iconName="icon-user"  para="${active}"></tags:shirourl>
    		<tags:shirourl url="${ctx}/register/updateIco" active="photo" text="修改头像" iconName="icon-user" para="${active}"></tags:shirourl>
    		<tags:shirourl url="${ctx}/register/changePwd" active="pwd" text="修改密码" iconName="icon-pencil"  para="${active}"></tags:shirourl>
    	</ul>
    	</div>
    </div>
</div>
<div class="container">
	<div class="col-right">    	
    	<div class="section">
        <div class="page-header">
        	<div class="tit-icon_bg"><i class="icon-user icon-white"></i><i class="icon-sj"></i></div>
        	<h5><j:if test="${active=='user'}">个人资料</j:if><j:if test="${active=='photo'}">修改头像</j:if><j:if test="${active=='pwd'}">修改密码</j:if></h5>
            <a href="${ctx}/course/courseIndex" title="个人首页" class="replyMybk"><i class="icon-home icon-white"></i></a>
        </div>
  <sitemesh:body />
         </div>
    </div>
  </div>
  <%@ include file="/WEB-INF/layouts/footer.jsp"%>
  <script src="${ctx}/resources/js/bootstrap.min.js" type="text/javascript"></script>
</body>
</html>