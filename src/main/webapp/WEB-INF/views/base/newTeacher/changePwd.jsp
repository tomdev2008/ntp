<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新东方在线教师备课平台</title>
<link rel="stylesheet" href="${ctx}/resources/css/global.css" />
<link href="${ctx}/resources/css/settings.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${ctx}/resources/js/jquery.validate.min.js"></script>
</head>
<body>
<script type="text/javascript">
$.Placeholder.init();
function checkOldPwd(){
	var oldPwd=$("#oldPwd").val();
	$.ajax({
		type : "post",
		dataType : "json",
		url : "${ctx}/ajax/register/checkOldPwd",
		data : {
			"str" : oldPwd
		},
		success : function(data) {
			if (data) {
				checkFlag.oldPwd=true;
			} else {
				checkFlag.oldPwd=false;
				var node = document.getElementById("oldPwd");
				var child1 = node.parentNode;
				var child2 = child1.parentNode;
			    child2.className = "control-group warning";
			}
		}
	});
}
//检查新密码格式
function checkNewPwd(){
  var node = document.getElementById("newPwd");
  var newPwd = $('#newPwd').val();
  var pattern = /^[a-zA-Z]{1}([a-zA-Z0-9]|[._]){7,19}$/;
  if(!pattern.test(newPwd)){
	  checkFlag.newPwd=false;
	  var child1 = node.parentNode;
	  var child2 = child1.parentNode;
	  child2.className = "control-group warning";
  } else {
	  checkFlag.newPwd=true;
  }
}
//检查确认密码格式
function confirmNewPwd(){
	var node = document.getElementById("confirmPwd");
	var newPwd = $('#newPwd').val();
	var confirmPwd = $('#confirmPwd').val();
	if(newPwd != confirmPwd){
		checkFlag.confirmPwd=false;
		var child1 = node.parentNode;
		var child2 = child1.parentNode;
		child2.className = "control-group warning";
	} else {
		checkFlag.confirmPwd=true;
	}
}
//清楚警告显示
function clearCss(node){
  var node1 = node.parentNode.parentNode;	
  node1.className = "control-group";
}
//表单项目是否通过检查的标识
var checkFlag = {
	"newPwd" :false,
	"oldPwd" :false,
	"confirmPwd" :false,
};
function checkSubmit(){
	if (checkFlag.newPwd && checkFlag.oldPwd && checkFlag.confirmPwd) {
		  return true;// 允许提交
		} else {
		  if(!checkFlag.newPwd){
			  checkOldPwd();
		  }
		  if(!checkFlag.oldPwd){
			  checkNewPwd();
		  }
		  if(!checkFlag.confirmPwd){
			  confirmNewPwd(); 
		  }
		  return false;// 阻止提交
		}
}
</script>
<div class="fixed-left">
	<div class="container">
		<div class="col-left">
    	<ul class="nav nav-list sidenav">
        	<li class="nav-header">
            	<div class="tit-icon_bg"><i class="icon-gear"></i><i class="icon-sj"></i></div>
                            账号设置
            </li>
    		<li><a href="${ctx}/register/updateTeacher"><i class="icon-user"></i>个人资料</a></li>
            <li><a href="${ctx}/register/updateIco"><i class="icon-user"></i>修改头像</a></li>
            <li class="active"><a href="${ctx}/register/changePwd"><i class="icon-pencil"></i>修改密码</a></li>
    	</ul>
    	</div>
    </div>
</div>
<div class="container">
	<div class="col-right">    	
    	<div class="section">
        <div class="page-header">
        	<div class="tit-icon_bg"><i class="icon-pencil icon-white"></i><i class="icon-sj"></i></div>
        	<h5>修改密码</h5>
            <a href="${ctx}/dashboard" class="replyMybk" title="返回主页" ><i class="icon-home icon-white"></i></a>
        </div>        
       <div class="page-body"> 
      	 
          <form id="subForm" onsubmit="return checkSubmit();" action="${ctx}/register/updateTeacherPwd" method="post" class="reg_form form-horizontal">
           <input type="hidden" name="fdId" value="${fdId}"/>
           <input type="hidden" name="fdIsEmp" value="${fdIsEmp}"/>
        	<p class="reg_form-intro">请确认您要修改的新密码。</p>
        	<div class="control-group" style="height: 40px;">
        		<label for="user" class="control-label">临时账号</label>
        		<div class="controls">
                	<span class="inp-placeholder">${fdEmail}</span>
                </div>
        	</div>    
             <div class="control-group" style="height: 40px;">
        		<label for="oldPwd" class="control-label">原密码 <span class="text-error">*</span></label>
        		<div class="controls">
        		   <c:if test="${fdIsEmp != '0'}">
        		     <input id="oldPwd" type="password" class="span4" value="" disabled="disabled" readonly/>
        		   </c:if>
        		   <c:if test="${fdIsEmp == '0'}">
                	<input id="oldPwd" type="password" onclick="clearCss(this);" onblur="checkOldPwd();"class="span4" value="" placeholder="请填写您的原密码" />
                   </c:if>
                    <span class="help-inline"><b class="icon-disc-bg warning">!</b>请正确填写原密码</span>
                </div>
        	</div>
            <div class="control-group" style="height: 40px;">
        		<label for="newPwd" class="control-label">新密码<span class="text-error">*</span></label>
        		<div class="controls">
        		   <c:if test="${fdIsEmp != '0'}">
        		     <input id="newPwd" type="password" name="fdPassword"   class="span4"  disabled="disabled" readonly>
        		   </c:if>
        		   <c:if test="${fdIsEmp == '0'}">
                	 <input id="newPwd" type="password" name="fdPassword"  onclick="clearCss(this);" onblur="checkNewPwd();"  class="span4" placeholder="请填写您的新密码">
                    </c:if>
                    <span class="help-inline"><b class="icon-disc-bg warning">!</b>请正确填写新密码</span>
                </div>
        	</div>     
            <div class="control-group" style="height: 40px;">
        		<label for="confirmPwd" class="control-label">确认密码<span class="text-error">*</span></label>
        		<div class="controls">
        		<c:if test="${fdIsEmp != '0'}">
        		<input id="confirmPwd" type="password" class="span4"  value=""  disabled="disabled" readonly>         
        		</c:if>
        		<c:if test="${fdIsEmp == '0'}">
                  <input id="confirmPwd" type="password" class="span4" onblur="confirmNewPwd();" onclick="clearCss(this);" value="" placeholder="请再次确认您的新密码 ">         
                </c:if>
                  <span class="help-inline"><b class="icon-disc-bg warning">!</b>密码输入不一致，请重新确认</span>
                </div>
        	</div>           
            <div class="control-group" style="height: 40px;">
            	<div class="controls">
            	<c:if test="${fdIsEmp != '0'}">
            	<button type="submit" disabled="disabled" id="submitForm" class="submit btn btn-primary btn-large" >确认修改</button>
            	</c:if>
            	<c:if test="${fdIsEmp == '0'}">
                	<button type="submit" id="submitForm"  class="submit btn btn-primary btn-large" >确认修改</button>
                </c:if>
                </div>
            </div>
        </form>
        </div>
      </div>
    
    </div>
	
</div>

</body>
</html>
