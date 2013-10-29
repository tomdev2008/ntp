<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="j" uri="/WEB-INF/tags/formtag.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新东方在线教师备课平台</title>

<link href="${ctx}/resources/css/datepicker.css" rel="stylesheet" type="text/css">
<link href="${ctx}/resources/css/settings.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="${ctx}/resources/js/jquery.validate.min.js"></script>
<script type="text/javascript">
//初始化部门列表
$(document).ready(function(){
	var id = $("#sysParOrgId").val();
	var depId = $("#depId").val();
	$.ajax({
		type : "post",
		url : "${ctx}/ajax/register/getDeparts",
		data : {
			"id" : id,
		},
		success : function(msg) {
			msg = msg.substr(1, msg.length - 2);
			document.getElementById("department").innerHTML = "";
			var ss = msg.split("=");
			var html = "<option value=0>请输入您的部门</option>";
			for ( var i = 0; i < (ss.length - 1); i++) {
				var s1 = ss[i].split(":");
				if(s1[0]==depId){
					html += "<option value="+s1[0]+" selected "+" >" + s1[1] + "</option>";
				}else{
					html += "<option value="+s1[0]+">" + s1[1] + "</option>";
				}
				
			}
			$("#department").append(html);
		}
	});
});

//清楚警告显示
function clearCss(node){
  var node1 = node.parentNode.parentNode;	
  node1.className = "control-group";
}
//效验部门为空
function checkdepart() {
	var node = document.getElementById("department");
    var val = document.getElementById("department").value;
    if(val == "0"){
    	checkFlag.depart = false;
    	var child1 = node.parentNode;
		var child2 = child1.parentNode;
		child2.className = "control-group warning";
    }else{
    	checkFlag.depart = true;
    	var child1 = node.parentNode;
		var child2 = child1.parentNode;
		child2.className = "control-group";
    }
}
//选择机构后ajax调出相应部门
function changedepart(n) {
	var index = n.selectedIndex;
	var id = n.options[index].value;
	$.ajax({
		type : "post",
		url : "${ctx}/ajax/register/getDeparts",
		data : {
			"id" : id,
		},
		success : function(msg) {
			msg = msg.substr(1, msg.length - 2);
			document.getElementById("department").innerHTML = "";
			var ss = msg.split("=");
			var html = "<option value=0>请输入您的部门</option>";
			for ( var i = 0; i < (ss.length - 1); i++) {
				var s1 = ss[i].split(":");
				html += "<option value="+s1[0]+">" + s1[1] + "</option>";
			}
			$("#department").append(html);
		}
	});

}
//验证电话号码
function checkTel() {
	var tel = $('#tel').val();
	var node = document.getElementById("tel");
	var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
	if (tel == null || tel == "") {
		checkFlag.tel = false;
		var child1 = node.parentNode;
		var child2 = child1.parentNode;
		child2.className = "control-group warning";
	} else if (!myreg.test(tel)) {
		checkFlag.tel = false;
		var child1 = node.parentNode;
		var child2 = child1.parentNode;
		child2.className = "control-group warning";
	} else {
		checkFlag.tel = true;
		var child1 = node.parentNode;
		var child2 = child1.parentNode;
		child2.className = "control-group";
	}
}
//验证用户名
function checkName() {
	var node = document.getElementById("inputrealname");
	var val = $('#inputrealname').val();
	var myreg = /^[\u0391-\u9fa5]+$/;
	if (val == null || val == "") {
		checkFlag.inputrealname = false;
		var child1 = node.parentNode;
		var child2 = child1.parentNode;
		child2.className = "control-group warning";
	} else if (!myreg.test(val)) {
		checkFlag.inputrealname = false;
		var child1 = node.parentNode;
		var child2 = child1.parentNode;
		child2.className = "control-group warning";
	} else {
		checkFlag.inputrealname = true;
		var child1 = node.parentNode;
		var child2 = child1.parentNode;
		child2.className = "control-group";
	}
}
//表单项目是否通过检查的标识
var checkFlag = {
	"tel" :false,
	"inputrealname" :false,
	"depart" :false,
	"selfIntr" : false
};
//提交时总验证
function checkSubmit(){
	var depart = document.getElementById("department");
	var index = depart.selectedIndex;
	var departid = depart.options[index].value;
	var deptName = depart.options[index].text;
	document.getElementById("depName").value = deptName;
	document.getElementById("depId").value = departid;
	 if(!checkFlag.tel ){
		  checkTel();
	  }
	  if(!checkFlag.inputrealname){
		  checkName();
	  }
	  if(!checkFlag.depart){
		  checkdepart();
	  }
	  if(!checkFlag.selfIntr){
		  CountStrByte();
	  }
	if (checkFlag.tel && checkFlag.inputrealname && checkFlag.depart && checkFlag.selfIntr) {
		  return true;// 允许提交
		} else {
		  return false;// 阻止提交
		}
}
//验证自我介绍不得超过200字符
function CountStrByte(){
	var StrValue = document.getElementById("selfIntro").value;
	var conent = StrValue.trim();
	var node = document.getElementById("selfIntro");
    if(conent.length > 200){
    	checkFlag.selfIntr=false;
    	var child1 = node.parentNode;
		var child2 = child1.parentNode;
		child2.className = "control-group warning";
    }else{
    	checkFlag.selfIntr=true;
    	var child1 = node.parentNode;
		var child2 = child1.parentNode;
		child2.className = "control-group";
    }

}

</script>

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
    		<li class="active"><a href="${ctx}/register/updateTeacher"><i class="icon-user"></i>个人资料</a></li>
            <li><a href="${ctx}/register/updateIco"><i class="icon-user"></i>修改头像</a></li>
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
        	<h5>个人资料</h5>
            <a href="${ctx}/dashboard" title="返回主页" class="replyMybk"><i class="icon-home icon-white"></i></a>
        </div> 
       <div class="page-body"> 
          <form method="post" id="inputForm" onsubmit="return checkSubmit();" action="${ctx}/register/updateOtherData" class="reg_form form-horizontal">
          <input type="hidden" name="fdId" value="${bean.fdId}"/>
          <input type="hidden" name="personId" value="${bean.personId}"/>
          <input type="hidden" id="depName" name="deptName" value="${bean.deptName}" />
          <input type="hidden" id="depId" name="depatId" value="${bean.depatId}" />
          <input type="hidden" id="sysParOrgId" value="${sysParOrgId}" />
        	<p class="reg_form-intro">以下信息将显示在您的
        	<a href="${ctx}/trainee/welcome">备课主页</a>
        	上，方便大家了解您。</p>
        	<div class="control-group" style="height: 70px;">
        		<label for="face" class="control-label">头像</label>
        		<div class="controls">
                	<a class="face pull-left">                	
                     <tags:image href="${bean.fdIcoUrl}" clas="media-object img-face" />
                        <h6 align="left" >
                        </h6>
                     </a>
                </div>
        	</div> 
        	
             <div class="control-group">
        		<label for="name" class="control-label">姓名<span class="text-error">*</span></label>
        		<div class="controls">
                	<input id="inputrealname" type="text" class="span4"
                	  name="notifyEntity.realName"  value="${bean.notifyEntity.realName}" 
                	  onblur="checkName();" onclick="clearCss(this);" placeholder="请填写您的真实姓名">
                   <span class="help-inline"><b class="icon-disc-bg warning">!</b>请正确填写真实姓名</span>
               </div>
        	</div>
            <div class="control-group">
        		<label for="ID" class="control-label">证件号码<span class="text-error">*</span></label>
        		<div class="controls">
                	<input type="text" id="fdIdentityCard" name="fdIdentityCard"class="span4" 
                	value="${bean.fdIdentityCard}" readOnly>
                </div>
        	</div>
             <div class="control-group">
        		<label for="org" class="control-label">机构/部门<span class="text-error">*</span></label>
        		<div class="controls">
                <%-- 	<input id="org" type="text" class="span2" value="${sysParOrg}" >
                    <input id="department" type="text" class="span2" value="${bean.deptName}" > --%>
                    <select name="org" id="org" onchange="changedepart(this)">
                      <option value="0">请输入您的机构</option>
                       <c:forEach items="${elements }" var="e">
								<option value="${e.fdId }" <j:if test="${e.fdId== sysParOrgId}"> selected="selected" </j:if>>${e.fdName }</option>
					   </c:forEach>
                	</select>
                    <select name="department" id="department" onchange="checkdepart()">
                	
                	</select>  
                     <span class="help-inline"><b class="icon-disc-bg warning">!</b>请认真选择机构/部门</span>
                </div>
        	</div>
            <div class="control-group">
        		<label for="tel" class="control-label">电话<span class="text-error">*</span></label>
        		<div class="controls">
                	<input id="tel" type="text" class="span4" 
                	  name="notifyEntity.fdMobileNo" value="${bean.notifyEntity.fdMobileNo}" 
                	  onblur="checkTel();" onclick="clearCss(this);" placeholder="请填写您的常用联系方式，如手机/座机等 ">
                    <span class="help-inline"><b class="icon-disc-bg warning">!</b>请正确填写通讯方式</span>
                </div>
        	</div>
            <div class="control-group">
        		<label for="sex" class="control-label">性别<span class="text-error">*</span></label>
        		<div class="controls">
                 	<label for="male" class="radio inline"><input name="fdSex" id="male" type="radio" value="M"  <j:if test="${bean.fdSex=='M' || bean.fdSex==null || bean.fdSex==''}">checked</j:if>> 男</label>
                    <label for="female" class="radio inline"><input name="fdSex" id="female" type="radio" value="F" <j:if test="${bean.fdSex=='F'}">checked</j:if>> 女</label>
              </div>
        	</div>
             <div class="control-group">
        		<label for="birthday" class="control-label">出生日期</label>
        		<div class="controls">
        		 <div class="input-append date" id="dpYear" data-date="1990/01/10" data-date-format="yyyy/mm/dd" >
					  <input id="birthday" type="text" name="fdBirthDay" value="${bean.fdBirthDay}" class="span4" placeholder="请输入您的出生日期 " readonly>
					  <span class="add-on"><i class="icon-th"></i></span>
				 </div>
                </div>
        	</div>
             <div class="control-group">
        		<label for="blood" class="control-label">血型</label>
        		<div class="controls">
                    <label for="A" class="radio inline">
                    <input name="fdBloodType" id="A" type="radio" value="A" <j:if test="${bean.fdBloodType =='A'}">checked</j:if> > A</label>
                    <label for="B" class="radio inline">
                    <input name="fdBloodType" id="B" type="radio" value="B" <j:if test="${bean.fdBloodType =='B'}">checked</j:if> > B </label>
                    <label for="AB" class="radio inline">
                    <input name="fdBloodType" id="AB" type="radio" value="AB"  <j:if test="${bean.fdBloodType =='AB'}">checked</j:if> > AB</label>
                    <label for="O" class="radio inline">
                    <input name="fdBloodType" id="O" type="radio" value="O" <j:if test="${bean.fdBloodType =='O'}">checked</j:if> >O </label>
                    <label for="RH" class="radio inline">
                    <input name="fdBloodType" id="RH" type="radio" value="RH" <j:if test="${bean.fdBloodType =='RH'}">checked</j:if> >RH </label>
                    <label for="OTHER" class="radio inline">
                    <input name="fdBloodType" id="OTHER" type="radio" value="OTHER" <j:if test="${bean.fdBloodType == 'OTHER'}">checked</j:if> >不详 </label>
                </div>
        	</div>
            <div class="control-group">
        		<label for="selfIntro" class="control-label">自我介绍</label>
        		<div class="controls">
                	<textarea id="selfIntro" onblur="CountStrByte();" onclick="clearCss(this);" name="selfIntroduction" class="span4" rows="5" placeholder="请填写200字以内的自我介绍">${bean.selfIntroduction}</textarea>
                    <span class="help-inline"><b class="icon-disc-bg warning">!</b>请填写200字以内的自我介绍</span>
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

<script type="text/javascript" src="${ctx}/resources/js/bootstrap-datepicker.js"></script>
<script type="text/javascript">
$("#dpYear").datepicker();
</script>
</body>
</html>