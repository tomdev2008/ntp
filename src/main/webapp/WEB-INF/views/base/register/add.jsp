<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新东方在线教师备课平台</title>
<link href="${ctx}/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="${ctx}/resources/css/datepicker.css" rel="stylesheet" type="text/css">
<link href="${ctx}/resources/css/register.css" rel="stylesheet" type="text/css">

</head>

<body>
<div class="header">
	<div class="container">
		<a href="#" class="logo"></a>
		<div class="top_menu"><a href="#">首页</a><a href="#">注册</a></div>
	</div>
</div>
<div class="container">
  	<div class="main">
    	<p class="reg_intro">如果您尚未开通新东方集团邮箱账号，请按照如下步骤注册临时账号。</p>
        <ul class="reg_steps clearfix" id="reg_steps">
        	<li><a href="#"><i class="icon-reg-step01 pass"></i><h4>创建账号</h4></a></li>
        	<li><div class="line"></div></li>
        	<li><a href="#"><i class="icon-reg-step02"></i><h4>填写资料</h4></a></li>
        	<li><div class="line"></div></li>
        	<li><a href="#"><i class="icon-reg-step03"></i><h4>完成注册</h4></a></li>
        </ul>
        <form action="#" class="reg_form form-horizontal" id="form-step01">
        	<p class="reg_form-intro">请填写您的个人邮箱及密码，然后点击进入下一步。</p>
        	
            <div class="control-group">
        		<label for="user" class="control-label">个人邮箱<span class="text-error">*</span></label>
        		<div class="controls">
                	<input id="user" class="span4" type="text" value="" placeholder="请填写您的个人邮箱作为临时账号" />
                </div>
        	</div>
             <div class="control-group">
        		<label for="password" class="control-label">登录密码<span class="text-error">*</span></label>
        		<div class="controls">
                	<input id="password" class="span4" type="text" value="" placeholder="请填写你的密码" />
                </div>
        	</div>
            <div class="control-group">
        		<label for="confirmPwd" class="control-label">确认密码<span class="text-error">*</span></label>
        		<div class="controls">
                	<input id="confirmPwd" class="span4" type="text" value="" placeholder=" 请确认您的密码" />
                </div>
        	</div>
             
            <div class="control-group">
            	<div class="controls">
                	<button type="button" class="submit btn btn-primary btn-large" id="submit_step01" >创建完毕，进入下一步</button>
                </div>
            </div>
        </form>
        <form action="#" class="reg_form form-horizontal hide" id="form-step02">
        	<p class="reg_form-intro">请确认您填写的个人资料，以便大家更加了解您。</p>
        	<div class="control-group">
        		<label for="face" class="control-label">头像</label>
        		<div class="controls">
                	<a href="#" class="face pull-left">
                    	<img src="images/face-placeholder.png" alt="" >
                        <h6>修改</h6>
                    </a>
                    <div class="pull-left support-img">支持JPG\JPEG、PNG、BMP格式的图片<br />
建议文件小于2M</div>
                </div>
        	</div>           
             <div class="control-group">
        		<label for="name" class="control-label">姓名<span class="text-error">*</span></label>
        		<div class="controls">
                	<input id="name" type="text" class="span4" value="" placeholder="请输入您的姓名">
                </div>
        	</div>
            <div class="control-group">
        		<label for="ID" class="control-label">证件号码<span class="text-error">*</span></label>
        		<div class="controls">
                	<input id="ID" type="text" class="span4" value="" placeholder="请输入您的证件号码">
                </div>
        	</div>
             <div class="control-group">
        		<label for="org" class="control-label">机构/部门 <span class="text-error">*</span></label>
        		<div class="controls">
                	<select name="org" id="org">
                		<option value="">请输入您的机构</option>
                        <option value="">集团总公司</option>
                	</select>
                    <select name="department" id="department">
                		<option value="">请输入您的部门</option>
                        <option value="">知识管理中心</option>
                	</select>                	
                </div>
        	</div>
            <div class="control-group">
        		<label for="tel" class="control-label">电话<span class="text-error">*</span></label>
        		<div class="controls">
                	<input id="tel" type="text" class="span4" value="" placeholder="请输入您的电话 ">
                </div>
        	</div>
            <div class="control-group">
        		<label for="sex" class="control-label">性别<span class="text-error">*</span></label>
        		<div class="controls">
                	<label for="male" class="radio inline"><input name="sex" id="male" type="radio" checked >男</label>
                    <label for="female" class="radio inline"><input name="sex" id="female" type="radio" >女</label>
                </div>
        	</div>
             <div class="control-group">
        		<label for="birthday" class="control-label">出生日期</label>
        		<div class="controls">
	                <div class="input-append date" id="dpYear" data-date="1986/01/10" data-date-format="yyyy/mm/dd" >
					  <input id="birthday" type="text" class="span4" value="1986/01/10" placeholder="请输入您的出生日期 " readonly>
					  <span class="add-on"><i class="icon-th"></i></span>
					</div>
                	
                </div>
        	</div>
             <div class="control-group">
        		<label for="blood" class="control-label">血型</label>
        		<div class="controls">
                	<label for="a" class="radio inline"><input name="blood" id="a" type="radio" checked > A</label>
                    <label for="b" class="radio inline"><input name="blood" id="b" type="radio" > B</label>
                    <label for="ab" class="radio inline"><input name="blood" id="ab" type="radio" > AB</label>
                    <label for="o" class="radio inline"><input name="blood" id="o" type="radio" > O</label>
                    <label for="rh" class="radio inline"><input name="blood" id="rh" type="radio" > RH</label>
                    <label for="bld-other" class="radio inline"><input name="blood" id="bld-other" type="radio" >不详 </label>
                </div>
        	</div>
            <div class="control-group">
            	<div class="controls">
                	<button type="button" class="submit btn btn-primary btn-large" id="submit_step02" >填写完毕，进入下一步</button>
                </div>
            </div>
        </form>
        <form action="#" class="reg_form form-horizontal hide" id="form-step03">
        	<p class="reg_form-intro">请确认您填写的个人资料，完成临时账号注册。</p>
        	<div class="control-group">
        		<label class="control-label">头像</label>
        		<div class="controls">
                	<img src="images/temp-face.jpg" alt="" class="face">
                </div>
        	</div>
            <div class="control-group">
        		<label class="control-label">个人邮箱<span class="text-error">*</span></label>
        		<div class="controls">
                	<span class="inp-placeholder">cnyangyifeng@gmail.com</span>
                </div>
        	</div>
             <div class="control-group">
        		<label class="control-label">姓名<span class="text-error">*</span></label>
        		<div class="controls">
                	<span class="inp-placeholder">杨义峰</span>                	
                </div>
        	</div>
            <div class="control-group">
        		<label class="control-label">证件号码<span class="text-error">*</span></label>
        		<div class="controls">
                	<span class="inp-placeholder">371121198601100217</span>                	
                </div>
        	</div>
             <div class="control-group">
        		<label class="control-label">机构/部门 <span class="text-error">*</span></label>
        		<div class="controls">
                	<span class="inp-placeholder">集团总公司 知识管理中心</span>                 	
                </div>
        	</div>
            <div class="control-group">
        		<label class="control-label">电话<span class="text-error">*</span></label>
        		<div class="controls">                	
                    <span class="inp-placeholder">135 8165 1017</span>
                </div>
        	</div>
            <div class="control-group">
        		<label class="control-label">性别<span class="text-error">*</span></label>
        		<div class="controls">
                	<span class="inp-placeholder">男</span>                	
                </div>
        	</div>
             <div class="control-group">
        		<label class="control-label">出生日期</label>
        		<div class="controls">
                	<span class="inp-placeholder">1986/01/10</span>	               
                </div>
        	</div>
             <div class="control-group">
        		<label class="control-label">血型</label>
        		<div class="controls">
                	<span class="inp-placeholder">A</span>	                  	
                </div>
        	</div>
            <div class="control-group">
            	<div class="controls">
                	<button type="button" class="submit btn btn-primary btn-large" onClick="location.href='./login.html'" >确认完毕，完成注册</button>
                </div>
            </div>
        </form>
    </div>	
</div>

<script type="text/javascript" src="${ctx}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/bootstrap-datepicker.js"></script>
<script type="text/javascript">
$(function(){
	var datepicker = $("#dpYear").datepicker()
		.on('changeDate', function(ev){
			datepicker.datepicker("hide");
		});
	var $regSteps = $("#reg_steps>li>a");
	var $lines = $("#reg_steps>li>.line");
	$("#submit_step01").bind("click",function(){
		$regSteps.eq(1).children("i").add($lines.eq(0)).addClass("pass");
		$("#form-step02").removeClass("hide").siblings("form").addClass("hide");
	});
	$("#submit_step02").bind("click",function(){
		$(window).scrollTop(80);
		$regSteps.eq(2).children("i").add($lines.eq(1)).addClass("pass");
		$("#form-step03").removeClass("hide").siblings("form").addClass("hide");		
	});
	$regSteps.each(function(i){
		$(this).bind("click",function(e){
			e.preventDefault();
			if($(this).find("i").hasClass("pass")){console.log(i);
				$regSteps.slice(i+1).children("i").add($lines.slice(i)).removeClass("pass");
				$("#form-step0" + (i+1)).removeClass("hide").siblings("form").addClass("hide");
			}
		});
	});
});

</script>
</body>
</html>
