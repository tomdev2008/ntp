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
    	<ul class="nav nav-list sidenav" id="sideNav">
                <li class="nav-header first"><a href="#">学习跟踪</a></li>
                <li class="nav-header"><a href="#">授权学习</a></li>
	            <li class="nav-header">
                    <span>课程管理</span>
	            </li>
	            <li><a href="#"><i class="icon-course-series"></i>我的系列课程</a></li>
	            <c:if test="${param.fdType==1000}">
	            <li class="active">
	             </c:if>
	             <c:if test="${param.fdType!=1000}">
	            <li>
	             </c:if>
	             
	              <a href="${ctx}/course/findcourseInfos?fdType=1000" id="courseInfos">
	              <i class="icon-course"></i>我的课程</a>
	             </li>
	             <li class="nav-header">
                     <span>课程素材库</span>
	            </li>
                <c:if test="${param.fdType==01}">
                <li class="active">
                </c:if>
                <c:if test="${param.fdType!=01}">
                <li>
                </c:if>
                  <a href="${ctx}/material/findList?fdType=01"><i class="icon-video">
                </i>视频</a></li>
                </li>
	            <c:if test="${param.fdType==04}">
                <li class="active">
                </c:if>
                <c:if test="${param.fdType!=04}">
                <li>
                </c:if>
                  <a href="${ctx}/material/findList?fdType=04"><i class="icon-doc">
                </i>文档</a></li>
                <c:if test="${param.fdType==05}">
                <li class="active">
                </c:if>
                <c:if test="${param.fdType!=05}">
                <li>
                </c:if>
                  <a href="${ctx}/material/findList?fdType=05"><i class="icon-ppt">
                </i>幻灯片</a></li>
                <c:if test="${param.fdType==08}">
                <li class="active">
                </c:if>
                <c:if test="${param.fdType!=08}">
                <li>
                </c:if>
                  <a href="${ctx}/material/findList?fdType=08"><i class="icon-exam">
                </i>测试</a></li>
                
                <c:if test="${param.fdType==10}">
                 <li class="active">
                </c:if>
                <c:if test="${param.fdType!=10}">
                 <li>
                </c:if>
                  <a href="${ctx}/material/findList?fdType=10"><i class="icon-task"></i>作业</a></li>
	    </ul>
	  </section>
		<section class="w790 pull-right" id="rightCont">
	        <div class="page-header">
	           <c:if test="${param.fdType!=1000}">
                <span class="muted">我正在看：</span> 
                 <c:if test="${param.fdType==01}">视频</c:if>
                 <c:if test="${param.fdType==02}">音频</c:if>
                 <c:if test="${param.fdType==04}">文档</c:if> 
                 <c:if test="${param.fdType==05}">幻灯片</c:if> 
                 <c:if test="${param.fdType==08}">测试</c:if> 
                 <c:if test="${param.fdType==10}">作业包</c:if> 
                </c:if> 
                <c:if test="${param.fdType==1000}">
                   <div class="page-header">
                   <span class="muted">我的课程</span> 
	        </div>
                </c:if>
                <div class="backHome">
                    <a href="#"><span class="muted">返回</span>主管<span class="muted">首页</span> <i class="icon-home icon-white"></i> </a>
                </div>
	        </div>
	        <div class="page-body" id="pageBody">
	         <c:if test="${param.fdType!=1000}">
             <%@ include file="/WEB-INF/views/material/divMatList.jsp" %>
             </c:if>
             <c:if test="${param.fdType==1000}">
               <%@ include file="/WEB-INF/views/course/course_list.jsp" %>
               </c:if>  
            </div>           
	    </section>
	</section>

<!--底部 S-->
	<%-- <footer>
		<div class="navbar clearfix">
			<div class="nav">
				<li><a href="http://www.xdf.cn/" target="_blank">新东方网</a></li>
				<li><a href="http://me.xdf.cn/" target="_blank">知识管理平台</a></li>
				<li><a href="${ctx }/login">登录</a></li>
				<li><a href="${ctx }/self_register">注册</a></li>
				<li class="last"><a href="mailto:yangyifeng@xdf.cn">帮助</a></li>
			</div>
            <p style="font-size:13px">&copy; 2013 新东方教育科技集团&nbsp;知识管理中心</p>
		</div>
	</footer> --%>
<!--底部 E-->
</section>

<script type="text/javascript">	
function pageNavClick(fdType,pageNo,order){
	var fdName = document.getElementById("serach").value;
	$("#pageBody").html("");
	$.ajax({
		type: "post",
		 url: "${ctx}/ajax/material/findList",
		data : {
			"fdName" : fdName,
			"fdType" : fdType,
			"pageNo" : pageNo,
			"order" : order,
		},
		cache: false, 
		dataType: "html",
		success:function(data){		
			$("#pageBody").html(data);
		}
	}); 
}
</script>
<script type="text/javascript">	

function findeCoursesByKey(pageNo,order){
	var fdTitle = document.getElementById("serach").value;
	$("#pageBody").html("");
	$.ajax({
		type: "post",
		 url: "${ctx}/ajax/course/getCoureInfosOrByKey",
		data : {
			"fdTitle" : fdTitle,
			"pageNo" : pageNo,
			"order" : order,
		},
		cache: false, 
		dataType: "html",
		success:function(data){		
			$("#pageBody").html(data);
		}
	}); 
}
</script>
</body>
</html>
