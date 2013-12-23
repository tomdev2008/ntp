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
<link href="${ctx}/resources/css/datepicker.css" rel="stylesheet" type="text/css">
<link href="${ctx}/resources/css/settings.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="${ctx}/resources/js/jquery.validate.min.js"></script>
<script type="text/javascript">
//初始化部门列表
$(document).ready(function(){
	$.Placeholder.init();
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
    var val = document.getElementById("deptId").value;
    if(val == "0" || val==''){
    	checkFlag.depart = false;
    	var child1 = node.parentNode;
		var child2 = child1.parentNode;
		child2.className = "control-group warning";
		node.focus();
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
	//var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
	//var reg = /^(\d{3}-\d{8}|\d{4}-\d{7})$/;
	var myreg = /^(\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/;
	if (tel == null || tel == "") {
		checkFlag.tel = false;
		var child1 = node.parentNode;
		var child2 = child1.parentNode;
		child2.className = "control-group warning";
		node.focus();
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
		node.focus();
	} else if (!myreg.test(val)) {
		checkFlag.inputrealname = false;
		var child1 = node.parentNode;
		var child2 = child1.parentNode;
		child2.className = "control-group warning";
		node.focus();
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
function selectDept(){
 	var depart = document.getElementById("department");
	var index = depart.selectedIndex;
	var departid = depart.options[index].value;
	document.getElementById("deptId").value = departid;
}
//提交时总验证
function checkSubmit(){
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
       <div class="page-body"> 
          <form method="post" id="inputForm" onsubmit="return checkSubmit();" action="${ctx}/register/updateOtherData" class="reg_form form-horizontal">
          <input type="hidden" name="fdId" value="${person.fdId}"/>
          <input type="hidden"  name="adminFlag" value="${admin}" />
          <input type="hidden" id="deptId" name="deptId" value="${person.deptId}" /> 
          <input type="hidden" id="sysParOrgId" value="${sysParOrgId}" />
        	<p class="reg_form-intro">以下信息将显示在您的
        	<a href="${ctx}/course/courseIndex">个人主页</a>
        	上，方便大家了解您。</p>
        	<div class="control-group" style="height: 70px;">
        		<label for="face" class="control-label">头像</label>
        		<div class="controls">
                	<a class="face pull-left">                	
                     <tags:image href="${fdIcoUrl}" clas="media-object img-face" />
                        <h6 align="left" >
                        </h6>
                     </a>
                </div>
        	</div> 
        	
             <div class="control-group">
        		<label for="name" class="control-label">姓名<span class="text-error">*</span></label>
        		<div class="controls">
        		<input type="hidden"  name="notifyEntity.fdEmail" value="${person.fdEmail}" >
        		<j:ifelse test="${person.fdIsEmp=='1'}">
        		   <j:then>
        		    <input id="inputrealname" type="text" class="span4" name="fdName"  value="${person.realName}" readOnly>
        		   </j:then>
        		   <j:else>
        		   <input id="inputrealname" type="text" class="span4" name="fdName"  value="${person.realName}"
                	  onblur="checkName();" onclick="clearCss(this);" placeholder="请填写您的真实姓名">
                   <span class="help-inline"><b class="icon-disc-bg warning">!</b>请正确填写真实姓名</span>
                   </j:else>
        		</j:ifelse>
               </div>
        	</div>
            <div class="control-group">
        		<label for="ID" class="control-label">证件号码<span class="text-error">*</span></label>
        		<div class="controls">
                	<input type="text" id="fdIdentityCard" name="fdIdentityCard"class="span4" 
                	value="${person.fdIdentityCard}" readOnly>
                </div>
        	</div>
             <div class="control-group">
        		<label for="org" class="control-label">机构/部门<span class="text-error">*</span></label>
        		<div class="controls">
        		 <j:ifelse test="${person.fdIsEmp=='1'}">
        		   <j:then>
        		    <select name="org" disabled="disabled">
                      <option value="">${sysParOrg}</option>
                	</select>
                    <select id="department" disabled="disabled">
                	    <option value='${person.deptName}'>${person.deptName}</option>
                	</select> 
                	</j:then>
                	<j:else>
                	 <select name="org" id="org" onchange="changedepart(this)">
                        <c:forEach items="${elements }" var="e">
							<option value="${e.fdId }" <j:if test="${e.fdId eq sysParOrgId}"> selected="selected" </j:if>>${e.fdName}</option>
					   </c:forEach> 
                	</select>
                    <select  id="department" onchange="selectDept();">
                       <option value='${person.deptId}'>${person.deptName}</option>
                	</select>  
                     <span class="help-inline"><b class="icon-disc-bg warning">!</b>请认真选择机构/部门</span>
                	</j:else>
                 </j:ifelse>
                </div>
        	</div>
            <div class="control-group">
        		<label for="tel" class="control-label">电话<span class="text-error">*</span></label>
        		<div class="controls">
                	<input id="tel" type="text" class="span4" 
                	  name="fdWorkPhone" value="${person.fdWorkPhone}"
                	  onblur="checkTel();" onclick="clearCss(this);" placeholder="请填写您的常用联系方式，如手机/座机等 ">
                    <span class="help-inline"><b class="icon-disc-bg warning">!</b>请正确填写通讯方式</span>
                </div>
        	</div>
            <div class="control-group">
        		<label for="sex" class="control-label">性别<span class="text-error">*</span></label>
        		<div class="controls">
                 	<label for="male" class="radio inline"><input name="fdSex" id="male" type="radio" value="M"  <j:if test="${person.fdSex=='M' || person.fdSex==null || person.fdSex==''}">checked</j:if>> 男</label>
                    <label for="female" class="radio inline"><input name="fdSex" id="female" type="radio" value="F" <j:if test="${person.fdSex=='F'}">checked</j:if>> 女</label>
              </div>
        	</div>
             <div class="control-group">
        		<label for="birthday" class="control-label">出生日期</label>
        		<div class="controls">
        		 <div class="input-append date" id="dpYear" data-date="1990/01/10" data-date-format="yyyy/mm/dd" >
					  <input id="birthday" type="text" name="fdBirthDay" value="${person.fdBirthDay}" class="span4" placeholder="请输入您的出生日期 ">
					  <span class="add-on"><i class="icon-th"></i></span>
				 </div>
                </div>
        	</div>
             <div class="control-group">
        		<label for="blood" class="control-label">血型</label>
        		<div class="controls">
                    <label for="A" class="radio inline">
                    <input name="fdBloodType" id="A" type="radio" value="A" <j:if test="${person.fdBloodType =='A'}">checked</j:if> > A</label>
                    <label for="B" class="radio inline">
                    <input name="fdBloodType" id="B" type="radio" value="B" <j:if test="${person.fdBloodType =='B'}">checked</j:if> > B </label>
                    <label for="AB" class="radio inline">
                    <input name="fdBloodType" id="AB" type="radio" value="AB"  <j:if test="${person.fdBloodType =='AB'}">checked</j:if> > AB</label>
                    <label for="O" class="radio inline">
                    <input name="fdBloodType" id="O" type="radio" value="O" <j:if test="${person.fdBloodType =='O'}">checked</j:if> >O </label>
                    <label for="RH" class="radio inline">
                    <input name="fdBloodType" id="RH" type="radio" value="RH" <j:if test="${person.fdBloodType =='RH'}">checked</j:if> >RH </label>
                    <label for="OTHER" class="radio inline">
                    <input name="fdBloodType" id="OTHER" type="radio" value="OTHER" <j:if test="${person.fdBloodType == 'OTHER'}">checked</j:if> >不详 </label>
                </div>
        	</div>
            <div class="control-group">
        		<label for="selfIntro" class="control-label">自我介绍</label>
        		<div class="controls">
                	<textarea id="selfIntro" onblur="CountStrByte();" onclick="clearCss(this);" name="selfIntroduction" class="span4" rows="5" placeholder="请填写200字以内的自我介绍">${person.selfIntroduction}</textarea>
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

<script type="text/javascript" src="${ctx}/resources/js/bootstrap-datepicker.js"></script>
<script type="text/javascript">
$("#dpYear").datepicker();
</script>
</body>
</html>
