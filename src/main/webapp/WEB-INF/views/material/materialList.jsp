<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="j" uri="/WEB-INF/tags/formtag.tld"%>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<%
 String fdType = request.getParameter("fdType");
 String order = request.getParameter("order");
%>
 <html class=""> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新东方在线教师备课平台</title>

<link href="${ctx}/resources/css/DTotal.css" rel="stylesheet" type="text/css">
</head>
<body>

<section class="container">
	<section class="clearfix mt20">
	
     <section class="col-left pull-left">
	  <%@ include file="/WEB-INF/views/group/menu.jsp" %>
	  </section>
    	 
		<section class="w790 pull-right" id="rightCont">
	        <div class="page-header">
	           <c:if test="${param.fdType!=1000}">
                <span class="muted">我正在看：</span> 
                 <c:if test="${param.fdType=='01'}">视频</c:if>
                 <c:if test="${param.fdType=='02'}">音频</c:if>
                 <c:if test="${param.fdType=='04'}">文档</c:if> 
                 <c:if test="${param.fdType=='05'}">幻灯片</c:if> 
                 <c:if test="${param.fdType=='08'}">测试</c:if> 
                 <c:if test="${param.fdType=='10'}">作业包</c:if> 
                </c:if> 
              
                <div class="backHome">
                    <a href="#"><span class="muted">返回</span>主管<span class="muted">首页</span> <i class="icon-home icon-white"></i> </a>
                </div>
	        </div>
	        <div class="page-body" id="pageBody">
	        <%@ include file="/WEB-INF/views/material/divMatList.jsp" %>
            </div>           
	    </section>
	</section>

</section>
<input type="hidden" id="showkey" name="showkey">
<input type="hidden" id="allFlag" >


</body>
</html>
