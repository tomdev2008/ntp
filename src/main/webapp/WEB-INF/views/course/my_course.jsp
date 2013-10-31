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
			        
			        <section class="section box-control">
				     <div class="hd">
					<div class="btn-toolbar">
						<a class="btn" href="${ctx}/course/add">发布新课程</a>
						<div class="btn-group">
							<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
								操作 <span class="caret"></span>
							</a>
							<ul class="dropdown-menu">
								<li><a href="#rightCont">导出列表</a></li>
								<li><a href="#rightCont">打包下载</a></li>
								<li><a href="#" onclick="confirmDel();">批量删除</a></li>
							</ul>
						</div>
						<form class="toolbar-search">
							<input type="text" id="serach" class="search" placeholder="搜索课程"
							   onblur="findeCoursesByKey('${param.fdTitle}');" onkeydown="showSearch();" onkeyup="showSearch();" > 
							<i class="icon-search"></i>
						</form>
						<span class="showState"> <span class="muted">当前显示：</span>含“<a id="containkey"
							href="#"></a>”的条目
						</span> <a class="btn btn-link" href="#" onclick="clearserach();" >清空搜索结果</a>
					</div>
				</div>
				<div class="bd">
					<div class="btn-toolbar">
						<label class="muted">排序</label>
						<div class="btn-group btns-radio" data-toggle="buttons-radio">
						 <a onclick="findeCoursesByKey('1','fdtitle')">
						   <c:if test="${param.order=='fdtitle'}">
							<button class="btn btn-large active" type="button">名称</button>
						   </c:if>
						   <c:if test="${param.order!='fdtitle'}">
							<button class="btn btn-large" type="button">名称</button>
						   </c:if>
						 </a>
						 <a onclick="findeCoursesByKey('1','fdcreatetime')">
						   <c:if test="${param.order=='fdcreatetime'}">
							<button class="btn btn-large active" type="button">时间</button>
						   </c:if>
						   <c:if test="${param.order!='fdcreatetime'}">
							<button class="btn btn-large" type="button">时间</button>
						   </c:if>
						</a>
					    <a onclick="findeCoursesByKey('1','fdscorce')">
					      <c:if test="${param.order=='fdscorce'}">
							<button class="btn btn-large active" type="button">评分</button>
						   </c:if>
						   <c:if test="${param.order!='fdscorce'}">
							<button class="btn btn-large" type="button">评分</button>
						   </c:if>
						</a> 
						</div>
						<label class="radio inline" for="selectCurrPage"><input
							type="radio" id="selectCurrPage" name="selectCheckbox"  onclick="checkcurrpage()"/>选中本页</label>
						<label class="radio inline" for="selectAll"><input
							type="radio" id="selectAll" name="selectCheckbox" />选中全部</label>
						<div class="pages pull-right">
							<div class="span2">
								第<span> 
								 <c:if test="${page.getTotalPage()==1}">
								   1 - ${page.getTotalCount()}
								 </c:if>  
								 <c:if test="${page.getTotalPage()>1}">
								   <c:if test="${page.getPageNo()==1}">
								    1-5
								   </c:if>
								   <c:if test="${page.getPageNo()!=1}">
								    <c:if test="${page.getPageNo()<page.getTotalPage()}">
								     ${page.getPageNo()*5+1} - ${page.getPageNo()*10}
								    </c:if>
									<c:if test="${page.getPageNo()==page.getTotalPage()}">
								     ${page.getPageNo()*5-5+1} - ${page.getTotalCount()}
								   </c:if>
								  </c:if>
								</c:if>
							   </span> 
								 / <span>${page.getTotalCount()}</span> 条 
							</div>
							<div class="btn-group">
		
							<c:if test="${page.isFirstPage()==true}">
								<button class="btn btn-primary btn-ctrl" type="button" disabled>
									<i class="icon-chevron-left icon-white"></i>
								</button>
							</c:if>
							<c:if test="${page.isFirstPage()==false}">
								<a onclick="findeCoursesByKey('${page.getPrePage()}')">
									<button class="btn btn-primary btn-ctrl" type="button">
										<i class="icon-chevron-left icon-white"></i>
									</button>
								</a>
							</c:if>
							<c:if test="${page.isLastPage()==true}">
								<button class="btn btn-primary btn-ctrl" type="button" disabled>
									<i class="icon-chevron-right icon-white"></i>
								</button>
							</c:if>
							<c:if test="${page.isLastPage()!=true}">
								<a onclick="findeCoursesByKey('${page.getNextPage()}')">
									<button class="btn btn-primary btn-ctrl" type="button">
										<i class="icon-chevron-right icon-white"></i>
									</button>
								</a>
							</c:if>
						</div>
						</div>
					</div>
				</div>
			</section>
			<div class="page-body" id="pageBody">
	        <%@ include file="/WEB-INF/views/course/divcourselist.jsp" %>
	        </div>
				    <div class="pages">
					<div class="btn-group dropup">
					<c:if test="${page.isFirstPage()==true}">
						<button class="btn btn-primary btn-ctrl" type="button" disabled>
							<i class="icon-chevron-left icon-white"></i>
						</button>
					</c:if>
					<c:if test="${page.isFirstPage()==false}">
						<a onclick="findeCoursesByKey('${page.getPrePage()}')">
							<button class="btn btn-primary btn-ctrl" type="button">
								<i class="icon-chevron-left icon-white"></i>
							</button>
						</a>
					</c:if>

				  <c:forEach var="i" begin="1" end="${page.getTotalPage()}">
			            <c:choose>
			                <c:when test="${page.getPageNo() == i}">
			                    <button class="btn btn-primary btn-num active" type="button" >${i}</button>
			                </c:when>
			                <c:otherwise>
			                    <button class="btn btn-primary btn-num" type="button">${i}</button>
			                </c:otherwise>
			            </c:choose>
		          </c:forEach>
					<button class="btn btn-primary btn-num  dropdown-toggle"
						data-toggle="dropdown" type="button">
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu pull-right">
					  <c:forEach var="i" begin="1" end="${page.getTotalPage()}">
						<li><a onclick="findeCoursesByKey('${i}')"> 
						<c:if test="${page.getTotalPage()==1}">
						   1 - ${page.getTotalCount()}
						</c:if> 
						<c:if test="${page.getTotalPage()>1}">
							<c:if test="${page.getPageNo()<page.getTotalPage()}">
								  ${page.getPageNo()*i*5-5+1} - ${page.getPageNo()*5*i}
							</c:if>
						</c:if> <%-- ${i*10+1}-${i*20+1} --%>
						</a></li>
					</c:forEach>
					</ul>

					<c:if test="${page.isLastPage()==true}">
						<button class="btn btn-primary btn-ctrl" type="button" disabled>
							<i class="icon-chevron-right icon-white"></i>
						</button>
					</c:if>
					<c:if test="${page.isLastPage()!=true}">
						<a onclick="findeCoursesByKey('${page.getNextPage()}')">
							<button class="btn btn-primary btn-ctrl" type="button">
								<i class="icon-chevron-right icon-white"></i>
							</button>
						</a>
					</c:if>

					</div>
				</div>    
                       
	    </section>
	</section>
</section>
<script type="text/javascript">	
function clearserach(){
	//alert('ss');
	$("#serach").attr("value","");
	$("#containkey").html("");
	findeCoursesByKey(1,'fdcreatetime');
}
function showSearch(){
	var fdTitle = document.getElementById("serach").value;
	$("#containkey").html(fdTitle);
}
function findeCoursesByKey(pageNo,order){
	var fdTitle = document.getElementById("serach").value;
	
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
			$("#pageBody").html(data);
		}
	}); 
}
//
function confirmDel(){
	$.fn.jalert("您确认要删除所选课程？",deleteCourse);
}
//删除列表
function deleteCourse(){
	var delekey="";
	//取已选中的值
	$('input[name="ids"]:checked').each(function() {
		delekey+=$(this).val()+",";
	}); 
/* 	alert($('input[name="ids"]').length);
	for(var i=0;i<.length;i++){
		delekey+=$("#ids")[i].val()+",";
	} */
	//不用检查直接取值
/* 	for(var i=0 ;i<$('input[name="ids"]').length;i++){
		delekey+=$('input[name="ids"]')[i].value+",";
	} */
	if(delekey==""){
		$.fn.jalert("当前没有选择要删除的数据!",function(){return;});
		return;
	}
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
//选中当前页
function checkcurrpage(){
	
	$('input[name="ids"]').each(function(){
		$(this).attr("checked",true);// 
	});
}
//选中所有数据,如果关键字有值则根据关键字查出需要操作的list,如果没有关键字则表示全部内容.
function checkall(){
	
}
</script>
</body>
</html>
