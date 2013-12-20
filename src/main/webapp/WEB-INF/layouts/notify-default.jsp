<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="zh_CN">
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
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="black" name="apple-mobile-web-app-status-bar-style" />
<meta content="telephone=no" name="format-detection" />
<title>新东方教师在线备课平台</title>
<link rel="shortcut icon" href="${ctx}/resources/img/favicon.ico" />
<link rel="stylesheet" href="${ctx}/resources/css/bootstrap.min.css">
<link rel="stylesheet" href="${ctx}/resources/css/global.css?id=12" />
<link rel="stylesheet" href="${ctx}/resources/css/admin-default.css?id=12" />
<script src="${ctx}/resources/js/lib/jquery/jquery.min.js" type="text/javascript"></script>

<sitemesh:head />
</head>
<body>
  <%@ include file="/WEB-INF/layouts/header.jsp"%>
  <div class="fixed-left">
		<div class="container">
			<div class="col-left">
	    	<ul class="nav nav-list sidenav">
	    		<!-- 消息中心 -->
	        	<li class="nav-header">
	            	<div class="tit-icon_bg"><i class="icon-gear"></i><i class="icon-sj"></i></div>
	                           消息中心
	            </li>
		          <tags:shirourl url="${ctx}/notify/list/1/ALL" active="notify" text="我的消息" iconName="dashboard-06_16.png" para="${active}"></tags:shirourl>
	    	</ul>
	    	</div>
	    </div>
  </div>
  <div class="container">
	<div class="col-right">    	
    	<div class="section">
        <div class="page-header">
        	<div class="tit-icon_bg"><i class="icon-pencil icon-white"></i><i class="icon-sj"></i></div>
        	<h5>消息中心</h5>
            <a href="${ctx}/dashboard" class="replyMybk" title="返回主页" ><i class="icon-home icon-white"></i></a>
        </div>        
       <div class="page-body"> 
         <sitemesh:body />
        </div>
      </div>
    
    </div>
	
  </div>

  <%@ include file="/WEB-INF/layouts/footer.jsp"%>
  <script src="${ctx}/resources/js/lib/bootstrap/bootstrap.min.js" type="text/javascript"></script>
</body>
</html>