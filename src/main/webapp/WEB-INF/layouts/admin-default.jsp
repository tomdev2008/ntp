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
<!-- 
<link rel="stylesheet" href="${ctx}/resources/css/bootstrap.min.css" />
 -->
<link rel="stylesheet" href="${ctx}/resources/css/global.css" />
<link rel="stylesheet" href="${ctx}/resources/css/DTotal.css" />
<script src="${ctx}/resources/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/resources/js/bootstrap.min.js" type="text/javascript"></script>
<sitemesh:head />
</head>
<body>
  <%@ include file="/WEB-INF/layouts/header.jsp"%>
  <section class="container">
	<section class="clearfix mt20">
     <section class="col-left pull-left">
    	<ul class="nav nav-list sidenav">
    		<li class="nav-header first"><span>系统管理</span></li>
            <tags:shirourl url="${ctx}/admin/user/list" active="user" text="用户管理" iconName="icon-user"  para="${active}"></tags:shirourl>
            <tags:shirourl url="${ctx}/admin/role/list" active="role" text="角色管理" iconName="icon-user"  para="${active}"></tags:shirourl>
    	</ul>
    </section>
    	 
		<section class="w790 pull-right" id="rightCont">
	        <div class="page-header bder2">
                <span class="muted">我正在看：</span>
                 <j:if test="${active=='user'}">用户管理</j:if>
                 <j:if test="${active=='role'}">角色管理</j:if>
                <div class="backHome">
                    <a href="${ctx}/admin/user/list"><span class="muted">返回</span>系统管理<span class="muted">首页</span> <i class="icon-home icon-white"></i> </a>
                </div>
	        </div>
	        <div class="page-body" id="pageBody">
  <sitemesh:body />
  			</div>
         </section>
	</section>

</section>
  <%@ include file="/WEB-INF/layouts/footer.jsp"%>
</body>
</html>