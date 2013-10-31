<%@page import="cn.me.xdf.utils.ShiroUtils"%>
<%@page import="cn.me.xdf.service.ShiroDbRealm.ShiroUser"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="j" uri="/WEB-INF/tags/formtag.tld"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
<%
ShiroUser user = ShiroUtils.getUser();
if(user!=null){
	request.setAttribute("userBean", user);
}
String[] path = request.getRequestURI().split("/");
request.setAttribute("path", path[path.length-1]);
%>
 <div class="navbar navbar-inverse navbar-fixed-top">
	<div class="navbar-inner">
    	<div class="container pr">
			<a href="#" class="logo"></a>
	        <ul class="nav">
	          <shiro:hasAnyRoles name="admin,group">
	          <li><a href="${ctx}/admin/role/list">系统管理</a></li>
	          </shiro:hasAnyRoles>
	          <li><a href="#">我是导师</a></li>
	          <li><a href="${ctx}/material/findList?fdType=01&order=FDNAME">我是主管</a></li>
	        </ul>
			<shiro:authenticated>
            <ul class="nav pull-right">
              <li class="dropdown">
              	<a href="#" class="dropdown-toggle" data-toggle="dropdown" >
                	<span class="top-face" id="notify" >
                	 <tags:image href="${userBean.poto}" clas="media-object img-face" />
                	 <i class="icon-disc"></i></span>
                    <span class="name"><shiro:principal/></span>
                    <b class="caret"></b>
                </a>
                 <ul class="dropdown-menu">
                   <shiro:hasRole name="trainee">
                	<li><a href="${ctx}/trainee/welcome"><i class="icon-home"></i>备课首页</a></li>
                   </shiro:hasRole>
                   
                   
                   <shiro:hasAnyRoles name="admin,group">
                	<li><a href="${ctx}/group/report/statList"><i class="icon-director"></i>集团主管</a></li>
                  </shiro:hasAnyRoles>
                  
                   <shiro:hasRole name="campus">
                	<li><a href="${ctx}/campus/flow/list"><i class="icon-director2"></i>学校主管</a></li>
                   </shiro:hasRole>
                   
                   <shiro:hasRole name="coach">
                	<li><a href="${ctx}/coach/progress/list"><i class="icon-teacher"></i>指导教师</a></li>
                   </shiro:hasRole>

                    <li><a href="${ctx}/notify/list/1/ALL"><i class="icon-envelope"></i>我的私信
                    <span class="icon-disc-bg" id="msgNum"></span></a></li>
                    <li><a href="${ctx}/register/updateTeacher"><i class="icon-user"></i>账号设置</a></li>
                    <li><a href="${ctx}/logout"><i class="icon-off"></i>退出平台</a></li>
                </ul>
              </li>
              <li><a href="${ctx}/logout" class="btn-off"></a></li>
            </ul>
            </shiro:authenticated>
             <shiro:notAuthenticated>            
            <ul class="nav pull-right">
            <j:ifelse test="${path == 'login' || path=='self_register'}">
				<j:then>
					<li><a href="${ctx}/">首页</a></li>
				</j:then>
				<j:else>
					<li><a href="${ctx}/login">登录</a></li>
				</j:else>
			</j:ifelse>
             <j:ifelse test="${path=='self_register'}">
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
getUnreadNotifyCount();
</script>