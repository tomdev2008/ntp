<%@page import="cn.me.xdf.utils.ShiroUtils"%>
<%@page import="cn.me.xdf.service.ShiroDbRealm.ShiroUser"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
<%
ShiroUser user = ShiroUtils.getUser();
if(user!=null){
	request.setAttribute("userBean", user);
}
String paths = request.getRequestURI();
String[] path = paths.split("/");
request.setAttribute("path", path[path.length-1]);
%>
 <div class="navbar navbar-inverse navbar-fixed-top">
	<div class="navbar-inner">
    	<div class="container pr">
			<a href="${ctx}" class="logo"></a>
	        <ul class="nav" id="topNav">
	          <% if(user!=null){ %>
	          		<li class="itemBg"></li>	
	        	<li class="specific"><a href="${ctx}/course/courseIndexAll">全部课程</a></li>
	          <%} %>
	          <shiro:hasRole name="admin">
	         	<tags:navigation paths="<%=paths%>" checkString="/admin" showString="系统管理" url="${ctx}/admin/user/list"></tags:navigation>
	          </shiro:hasRole>
	          
	          <shiro:hasRole name="guidance">
	          	<tags:navigation paths="<%=paths%>" checkString="/studyTrack/getStudyTrackTutor:/adviser" showString="我是导师" url="${ctx}/studyTrack/getStudyTrackTutor"></tags:navigation>
	          </shiro:hasRole>
	          <shiro:hasRole name="group">
	         	<tags:navigation paths="<%=paths%>" checkString="/studyTrack/getStudyTrackDirector:/material/find:/material/materialFoward:/course/find:/course/add:/series/find:/series/add:/course/get" showString="我是主管" url="${ctx}/studyTrack/getStudyTrackDirector"></tags:navigation>
	          </shiro:hasRole>
	          <% if(user!=null){ %>
	         	 <tags:navigation paths="<%=paths%>" checkString="/course/courseIndex:/passThrough:/successPage" showString="个人首页" url="${ctx}/course/courseIndex"></tags:navigation>
	           <%} %>
</ul>
			<shiro:authenticated>
            <ul class="nav pull-right">
            	<shiro:hasRole name="group">
            	<li>
                    <a class="btn-publish" href="${ctx}/course/add">
                        <i class="icon-book-pencil"></i>
                    </a>
                </li>
                </shiro:hasRole>
              <li class="dropdown">
              	<a href="#" class="dropdown-toggle" data-toggle="dropdown" >
                	<span class="top-face" id="notify" >
                	 <tags:image href="${userBean.poto}" clas="media-object img-face" />
                	 <i class="icon-disc"></i></span>
                    <span class="name"><shiro:principal/></span>
                    <b class="caret"></b>
                </a>
                 <ul class="dropdown-menu">
                   <li><a href="${ctx}/course/courseIndex"><i class="icon-home"></i>个人首页</a></li>
                    <li><a href="${ctx}/notify/list/1/ALL"><i class="icon-envelope"></i>我的私信
                    <span class="icon-disc-bg" id="msgNum"></span></a></li>
                    <li><a href="${ctx}/register/updateTeacher"><i class="icon-user"></i>账号设置</a></li>
                    <li><a href="${ctx}/logout"><i class="icon-off"></i>退出平台</a></li>
                </ul>
              </li>
              <li><a href="${ctx}/logout" class="btn-off"><span class="divider">|</span>退出</a></li>
            </ul>
            </shiro:authenticated>
             <shiro:notAuthenticated>            
            <ul class="nav pull-right">
            <j:ifelse test="${path == 'login' || path=='add'}">
				<j:then>
					<li><a href="${ctx}/">首页</a></li>
				</j:then>
				<j:else>
					<li><a href="${ctx}/login">登录</a></li>
				</j:else>
			</j:ifelse>
             <j:ifelse test="${path=='add'}">
				<j:then>
					<li><a href="${ctx}/login">登录</a></li>
				</j:then>
				<j:else>
					<li><a href="${ctx }/register/add">注册</a></li>
				</j:else>
			 </j:ifelse>
	        </ul>
	        </shiro:notAuthenticated>
	        <div id="notify_box" class="hide"></div>
		</div>
    </div>
</div>
<script type="text/javascript">
function getUnreadNotifyCount(){
 $.ajax({
    type: "post",
    dataType: "json",
    url: "${ctx}/ajax/notify/notifyCount",
    data: {},
    success: function(data){
    	if(data>0){
    		$("#notify_box").html('<a href="#" class="close">×</a>'+data+'条未读消息，<a href="${ctx}/notify/list/1/ALL" >查看信息</a>')
        	.show().find(".close").click(function(){
        		$(this).parent().remove();
        	});
        	$("#notify").append('<i class="icon-disc"></i>');
        	$("#msgNum").text(data);
    	}else{
    		$("#notify .icon-disc").remove();
    		$("#msgNum").remove();
    	}
    }
 }); 
}
///getUnreadNotifyCount();
</script>