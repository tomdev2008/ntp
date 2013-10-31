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
//jquery获取复选框值  
function batchDelete() {
	var chk_value = [];
	$('input[name="ids"]:checked').each(function() {
		chk_value.push($(this).val());
	});
	if (chk_value.length == 0) {
		alert('请选择需要批量删除的内容。');
		return false;
	}
	if (!confirm('您确定要批量删除吗？')) {
		return false;
	}
	document.form.method = "post";
	document.form.action = '${ctx}/material/batchDelete';
	document.form.submit();
	return;
}

function selectAll(){ 
	//设置变量form的值为name等于select的表单 
	 var form=document.select;
	 alert(form);
	//取得触发事件的按钮的name属性值 
	var action=event.srcElement.name;
	alert(form.elements.length);
	for (var i=0;i<form.elements.length;i++){//遍历表单项 
		alert("11113333");
	    //将当前表单项form.elements[i]对象简写为e 
	   var e = form.elements[i];
	   //如果当前表单项的name属性值为iTo， 
	   //执行下一行代码。限定脚本处理的表单项范围。 
	   if (e.name == "iTo") 
	    /*如果单击事件发生在name为selectall的按钮上，就将当前表单项的checked属性设为true(即选中)，否则设置为当前设置的相反值(反选)*/ 
	         e.checked =(action=="selectCheckbox")?(form.selectall.checked):(!e.checked); 
	   } 
} 
</script>
</body>
</html>
