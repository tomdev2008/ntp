<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html>
<head>
<link rel="stylesheet" href="${ctx}/resources/css/global.css" />
<link href="${ctx}/resources/css/settings.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${ctx}/resources/uploadify/uploadify.css"/> 
<style>
.uploadify-button {
    background-color:rgb(67,145,187);
	background-image: -webkit-gradient(
		linear,
		left bottom,
		left top,
		color-stop(0, rgb(67,145,187)),
		color-stop(1, rgb(67,145,187))
	);
	max-width:70px;
	max-height:20px;
	border-radius: 1px;
	border: 0px;
	font: bold 12px Arial, Helvetica, sans-serif;
	display: block;
	text-align: center;
	text-shadow: 0 0px 0 rgba(0,0,0,0.25);
    
}
.uploadify:hover .uploadify-button {
    background-color:rgb(67,145,187);
	background-image: -webkit-gradient(
		linear,
		left bottom,
		left top,
		color-stop(0, rgb(67,145,187)),
		color-stop(1, rgb(67,145,187))
	);
}
</style>
<script src="${ctx}/resources/js/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/resources/uploadify/jquery.uploadify-3.1.min.js?id=12"></script>


</head>

<body>

<div class="fixed-left">
	<div class="container">
		<div class="col-left">
    	<ul class="nav nav-list sidenav">
        	<li class="nav-header">
            	<div class="tit-icon_bg"><i class="icon-gear"></i><i class="icon-sj"></i></div>
                            账号设置
            </li>
    		<li><a href="${ctx}/register/updateTeacher"><i class="icon-user"></i>个人资料</a></li>
            <li class="active"><a href="${ctx}/register/updateIco"><i class="icon-user"></i>修改头像</a></li>
            <li><a href="${ctx}/register/changePwd"><i class="icon-pencil"></i>修改密码</a></li>
    	</ul>
    	</div>
    </div>
</div>
<div class="container">
	<div class="col-right">     	
    	<div class="section">
        <div class="page-header">
        	<div class="tit-icon_bg"><i class="icon-user icon-white"></i><i class="icon-sj"></i></div>
        	<h5>修改头像</h5>
            <a href="${ctx}/dashboard" title="返回主页" class="replyMybk"><i class="icon-home icon-white"></i></a>
        </div> 
       <div class="page-body"> 
          <form method="post" id="inputForm" action="${ctx}/register/updateTeacher" class="reg_form form-horizontal">
          <input type="hidden" name="fdId" value="${fdId}"/>
          <input type="hidden" id="fdIcoUrl" name="fdIcoUrl" value="${fdIcoUrl}" />
          <input type="hidden" id="fdIdentityCard" name="fdIdentityCard" value="${fdIdentityCard}">
           
        	<p class="reg_form-intro">请确认您填写的个人资料，完成临时账号注册。</p>
        	<div class="control-group">
        		<div class="controls" style="height: 110px;">
        		   <label for="face" class="control-label">头像</label>
        		    <div class="controls">
        		    
        		  <table>
        		  <tr>
        		   <td>
                	 <a href="#" class="face pull-left">
                    	<c:if test="${fdIsEmp == '0'}">
                         <img id="imgshow"  align="bottom" src="<%=request.getContextPath()%>/${fdIcoUrl}"/>
                        </c:if>
                        <c:if test="${fdIsEmp != '0'}">
                          <img id="imgshow"  align="bottom" src="${fdIcoUrl}"/>
                        </c:if>
                    	
                        <h6>
                        </h6>
                     
                     </a>
                    <div class="pull-left support-img">支持JPG\JPEG、PNG、BMP格式的图片<br />建议文件小于2M</div>
                      </td>
                </tr>
                <tr>
                <td>
                 <div style="position: relative;top:-20px;">
                 	<tags:simpleupload filename="fdName"
                       filevalue="" id="upPic" exts="*.jpg;*.JPEG;*.png;*.bmp;" imgshow="imgshow" attIdName="attId" attIdID="attIdID">
    			     </tags:simpleupload> 
                </div> 
                </td>
                </tr>
               </table>
               
                   </div>
                </div>
        	</div> 
        	<div class="control-group">
            	<div class="controls">
                	<button type="submit" class="submit btn btn-primary btn-large" >确认修改</button>
                </div>
            </div>
        </form>
        </div>
      </div>
    </div>
</div>

</body>
</html>
