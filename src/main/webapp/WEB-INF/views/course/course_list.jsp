<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<%
 String order = request.getParameter("order");
//String fdType = request.getParameter("fdType");

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
				<c:if test="${param.fdType=='12' }">
		        <%@ include file="/WEB-INF/views/course/divcourselist.jsp" %>
		        </c:if>
		        <c:if test="${param.fdType=='11' }">
		        <%@ include file="/WEB-INF/views/course/divserieslist.jsp" %>
		        </c:if>
		       </div>
            <!-- 缓存插叙关键字 -->
			<input type="hidden" id="coursekey" name="coursekey">
			<input type="hidden" id="allFlag" >
			<input type="hidden"  id="cousetype" value="${param.fdType}">
			<input type="hidden" id="cachorder"/>
            
	    </section>
	</section>
</section>
<script type="text/javascript">	
function pressEnter(){//回车事件
	if(event.keyCode==13){
		findeCoursesByKey(1,$('#cachorder').val());
	}
}
function clearserach(){//清理搜索栏并显示数据列表
	//alert('ss');
	$("#serach").attr("value","");
	$("#markshow").html('<a id="containkey"href="#">全部条目</a>');
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
	$('#cachorder').attr('value',order);
	$("#coursekey").attr("value",fdTitle);//关键字赋值
	$("#pageBody").html("");
	if($("#cousetype").val()=='12'){//课程列表
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
	 }else{
		$.ajax({
			type: "post",
			 url: "${ctx}/ajax/series/getSeriesInfosOrByKey",
			data : {
				"fdName" : fdTitle,
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
}
//
function confirmDel(){
	if($("#cousetype").val()=='12'){
		var delekey="";
		$('input[name="ids"]:checked').each(function() {
			delekey+=$(this).val()+",";
		}); 	
		if(delekey==""){
			$.fn.jalert2("当前没有选择要删除的数据!");
			return;
		}
		if($('input[name="selectCheckbox"]:checked').val()==1){//删除所有
			fiterDelete(delekey,1);
			$.fn.jalert("您确认要删除所有课程？",deleteAllCourse);
		}else if($('input[name="selectCheckbox"]:checked').val()==0){
			fiterDelete(delekey,0);
			$.fn.jalert("您确认要删除本页课程？",deleteCourse);
		}else{
			fiterDelete(delekey,0);
			$.fn.jalert("您确认要删除所选课程？",deleteCourse);
			
		}
	}else{
		confirmDelseries();
	}
}
//删除选中列表
function deleteCourse(){
	var delekey="";
	$('input[name="ids"]:checked').each(function() {
		delekey+=$(this).val()+",";
	}); 	
	/* if(delekey==""){
		$.fn.jalert2("当前没有选择要删除的数据!");
		return;
	} */
	// return;
	 $.ajax({
		type: "post",
		async:false,
		 url: "${ctx}/ajax/course/deleteCourse",
		data : {
			"courseId":delekey,
		},
		success:function(data){
			
			window.location.href="${ctx}/course/findcourseInfos?order=fdcreatetime&fdType="+$('#cousetype').val();
		}
	}); 
}
//删除所有
function deleteAllCourse(){
	var delekey = document.getElementById("serach").value;
	 $.ajax({
		type: "post",
		async:false,
		 url: "${ctx}/ajax/course/deleteAllCoursesByKey",
		data : {
			"fdTitle":delekey,
		},
		success:function(data){
			window.location.href="${ctx}/course/findcourseInfos?order=fdcreatetime&fdType="+$('#cousetype').val();
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
function fiterDelete(delekey,deleType){
	$.ajax({
		async:false,
		dataType:'json',
		url: "${ctx}/ajax/course/deleFiter?deleType="+deleType,
		data : {
			"courseId":delekey,
		},
		success:function(data){
			if(data.length==0){
				$('input[name="ids"]').each(function(){
					$(this).attr("checked",false);
					$(this).attr("disabled",true);
				});
				
			}else{
			  for (var i = 0; i < data.length; i++) {
				$('input[name="ids"]').each(function(){
					if($(this).val()==data[i].id){
						$(this).attr("checked",true);
						$(this).attr("disabled",false);
				    }else{
						$(this).attr("checked",false);
						$(this).attr("disabled",true);
					}
						
				});
			  } 
			} 
		}
    });
}
/* --系列信息-- */
function confirmDelseries(){
	var delekey="";
	$('input[name="ids"]:checked').each(function() {
		delekey+=$(this).val()+",";
	}); 	
	if(delekey==""){
		$.fn.jalert2("当前没有选择要删除的数据!");
		return;
	}
	if($('input[name="selectCheckbox"]:checked').val()==1){//删除所有
		$.fn.jalert("您确认要删除系列以及系列下课程信息？",deleteAllSeries);
	}else if($('input[name="selectCheckbox"]:checked').val()==0){
		$.fn.jalert("您确认要删除本页系列信息？",deleteSeries);
	}else{
		$.fn.jalert("您确认要删除所选系列信息？",deleteSeries);
	}
}
function deleteSeries(){
	var delekey="";
	$('input[name="ids"]:checked').each(function() {
		delekey+=$(this).val()+",";
	}); 	
	 $.ajax({
		type: "post",
		async:false,
		 url: "${ctx}/ajax/series/deleteSeries",
		data : {
			"seriesId":delekey,
		},
		success:function(data){
			window.location.href="${ctx}/series/findeSeriesInfos?order=fdcreatetime&fdType="+$('#cousetype').val();
		}
	}); 
}
function deleteAllSeries(){
	var delekey = document.getElementById("serach").value;
	 $.ajax({
		type: "post",
		async:false,
		 url: "${ctx}/ajax/series/deleteAllSeries",
		data : {
			"fdTitle":delekey,
		},
		success:function(data){
			window.location.href="${ctx}/series/findeSeriesInfos?order=fdcreatetime&fdType="+$('#cousetype').val();
		}
	}); 
}

</script>
</body>
</html>
