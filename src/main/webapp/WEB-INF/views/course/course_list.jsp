<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="j" uri="/WEB-INF/tags/formtag.tld"%>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<%
 String fdType = request.getParameter("fdType");
 String order = request.getParameter("order");
 String mark=request.getParameter("mark");
%>
 <html class=""> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新东方在线教师备课平台</title>

<link href="${ctx}/resources/css/DTotal.css" rel="stylesheet" type="text/css">
<script src="${ctx}/resources/js/jquery.jalert.js" type="text/javascript"></script>
</head>
<body>

		<section class="container">
			<section class="clearfix mt20">
			  <section class="col-left pull-left">
		    	 <%@ include file="/WEB-INF/views/group/menu.jsp" %>
			  </section>
				<section class="w790 pull-right" id="rightCont">
			        <div class="page-header">
		                <span class="muted">我的课程</span> 
		                <div class="backHome">
		                    <a href="#"><span class="muted">返回</span>主管<span class="muted">首页</span> <i class="icon-home icon-white"></i> </a>
		                </div>
			        </div>
			     
				<div class="page-body" id="pageBody">
		        <%@ include file="/WEB-INF/views/course/divcourselist.jsp" %>
		       </div>
            <!-- 缓存插叙关键字 -->
			<input type="hidden" id="coursekey" name="coursekey">
			<input type="hidden" id="allFlag" >

	    </section>
	</section>
</section>
<script type="text/javascript">	
function clearserach(){
	//alert('ss');
	$("#serach").attr("value","");
	$("#containkey").html('<a id="containkey"href="#">全部条目</a>');
	findeCoursesByKey(1,'fdcreatetime');
}
function showSearch(){
	var fdTitle = document.getElementById("serach").value;
	$("#markshow").html('含“<a id="containkey"href="#"></a>”的条目');
	$("#containkey").html(fdTitle);
}
function findeCoursesByKey(pageNo,order){
	var fdTitle = document.getElementById("serach").value;
	if($('input[name="selectCheckbox"]:checked').val()==1){
		$("#allkey").attr("value",1);
	}
	$("#coursekey").attr("value",fdTitle);//关键字赋值
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
			//alert(data);
			var serachkey=$("#coursekey").val();
			$("#pageBody").html(data);
			if(fdTitle!=""&&fdTitle!=null){
				$("#markshow").html('含“<a id="containkey"href="#"></a>”的条目');
				$("#containkey").html(fdTitle);
			}
			else{
				$("#containkey").html('<a id="containkey"href="#">全部条目</a>');
				
			}
			
			$("#serach").attr("value",serachkey);
			if($("#allFlag").val()=='true'){
				document.getElementById("selectAll").checked=true;
				selectAll();
			}
		}
	}); 
}
//
function confirmDel(){
	if($('input[name="selectCheckbox"]:checked').val()==1){//删除所有
		$.fn.jalert("您确认要删除所有课程？",deleteAllCourse);
	}else if($('input[name="selectCheckbox"]:checked').val()==0){
		$.fn.jalert("您确认要删除本页课程？",deleteCourse);
	}else{
		$.fn.jalert("您确认要删除所选课程？",deleteCourse);
	}
		
}
//删除选中列表
function deleteCourse(){
	var delekey="";
	$('input[name="ids"]:checked').each(function() {
		delekey+=$(this).val()+",";
	}); 	
	if(delekey==""){
		$.fn.jalert("当前没有选择要删除的数据!",function(){return;});
		return;
	}
	// return;
	 $.ajax({
		type: "post",
		 url: "${ctx}/ajax/course/deleteCourse",
		data : {
			"courseId":delekey,
		},
		success:function(data){
			window.location.href="${ctx}/course/findcourseInfos";
		}
	}); 
}
//删除所有
function deleteAllCourse(){
	var delekey = document.getElementById("serach").value;
	 $.ajax({
		type: "post",
		 url: "${ctx}/ajax/course/deleteAllCoursesByKey",
		data : {
			"fdTitle":delekey,
		},
		success:function(data){
			window.location.href="${ctx}/course/findcourseInfos";
		}
	}); 
}
//选中当前页
function checkcurrpage(){
	if(document.getElementById("selectCurrPage").checked){
		document.getElementById("selectAll").checked=false;
		$('input[name="ids"]').each(function(){
			$(this).attr("checked",true);// 
			$(this).attr("disabled",false);
		});
		$("#allFlag").attr("value",false);
	} else {
		$('input[name="ids"]').each(function(){
			$(this).attr("checked",false);// 
		});
		$("#allFlag").attr("value",false);
	}
}
//全部选中
function selectAll(){
	if(document.getElementById("selectAll").checked){
		document.getElementById("selectCurrPage").checked=false;
		$('input[name="ids"]').each(function(){
			$(this).attr("checked",true);// disabled="disabled"
			$(this).attr("disabled",true);
		});
		$("#allFlag").attr("value",true);
	} else {
		$('input[name="ids"]').each(function(){
			$(this).attr("checked",false);// 
		});
		$("#allFlag").attr("value",false);
	}
}
</script>
</body>
</html>
