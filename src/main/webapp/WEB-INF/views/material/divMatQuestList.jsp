<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="j" uri="/WEB-INF/tags/formtag.tld"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<link href="${ctx}/resources/css/DTotal.css" rel="stylesheet" type="text/css">
	<section class="section box-control">
		<div class="hd">
			<div class="btn-toolbar">
				<a class="btn" href="${ctx}/material/addVideo?fdType=${param.fdType}">添加</a>
				<div class="btn-group">
					<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
						操作 <span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<li><a href="#rightCont">导出列表</a></li>
						<li><a href="#rightCont">打包下载</a></li>
						<li><a href="#rightCont" onclick="batchDelete();">批量删除</a></li>
					</ul>
				</div>
				<form class="toolbar-search">
					<input type="text" id="serach" class="search" placeholder="搜索条目"
					   onblur="questionClick('${param.fdType}');"  onkeydown="showSearch();" onkeyup="showSearch();"> 
					<i class="icon-search"></i>
				</form>
				<span class="showState"> <span class="muted">当前显示：</span>含“<a
					href="#"><span id="show">雅思</span></a>”的条目
				</span> <a class="btn btn-link" href="#rightCont">清空搜索结果</a>
			</div>
		</div>
		<div class="bd">
			<div class="btn-toolbar">
				<label class="muted">排序</label>
				<div class="btn-group btns-radio" data-toggle="buttons-radio">
				 <a onclick="questionClick('${param.fdType}','1','FDNAME')">
				   <c:if test="${param.order=='FDNAME'}">
					<button class="btn btn-large active" type="button">名称</button>
				   </c:if>
				   <c:if test="${param.order!='FDNAME'}">
					<button class="btn btn-large" type="button">名称</button>
				   </c:if>
				 </a>
				 <a onclick="questionClick('${param.fdType}','1','FDCREATETIME')">
				   <c:if test="${param.order=='FDCREATETIME'}">
					<button class="btn btn-large active" type="button">时间</button>
				   </c:if>
				   <c:if test="${param.order!='FDCREATETIME'}">
					<button class="btn btn-large" type="button">时间</button>
				   </c:if>
				</a>
			    <a onclick="questionClick('${param.fdType}','1','FDSCORE')">
			      <c:if test="${param.order=='FDSCORE'}">
					<button class="btn btn-large active" type="button">评分</button>
				   </c:if>
				   <c:if test="${param.order!='FDSCORE'}">
					<button class="btn btn-large" type="button">评分</button>
				   </c:if>
				</a> 
				</div>
				<label class="radio inline" for="selectCurrPage">
				   <input type="radio" id="selectCurrPage" name="selectCheckbox" checked />选中本页</label>
				<label class="radio inline" for="selectAll">
				   <input type="radio" id="selectAll" name="selectCheckbox" onclick="selectAll()"/>选中全部</label>
				<div class="pages pull-right">
					<div class="span2">
						 第<span> 
						 <c:if test="${page.totalPage==1}">
						   1 - ${page.totalCount}
						 </c:if>  
						 <c:if test="${page.totalPage>1}">
						   <c:if test="${page.pageNo==1}">
						    1-10
						   </c:if>
						   <c:if test="${page.pageNo!=1}">
						    <c:if test="${page.pageNo<page.totalPage}">
						     ${page.pageNo*10+1} - ${page.pageNo*20}
						    </c:if>
							<c:if test="${page.pageNo==page.totalPage}">
						     ${page.pageNo*10-10+1} - ${page.totalCount}
						   </c:if>
						  </c:if>
						</c:if>
					   </span> 
						 / <span>${page.totalCount}</span> 条 
					</div>
					<div class="btn-group">

					<c:if test="${page.pageNo <= 1}">
						<button class="btn btn-primary btn-ctrl" type="button" disabled>
							<i class="icon-chevron-left icon-white"></i>
						</button>
					</c:if>
					<c:if test="${page.pageNo > 1}">
						<a onclick="questionClick('${param.fdType}','${page.prePage}','${param.order}')">
							<button class="btn btn-primary btn-ctrl" type="button">
								<i class="icon-chevron-left icon-white"></i>
							</button>
						</a>
					</c:if>
					<c:if test="${page.pageNo >= page.totalPage}">
						<button class="btn btn-primary btn-ctrl" type="button" disabled>
							<i class="icon-chevron-right icon-white"></i>
						</button>
					</c:if>
					<c:if test="${page.pageNo < page.totalPage}">
						<a onclick="questionClick('${param.fdType}','${page.nextPage}','${param.order}')">
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
	<section class="section listWrap">
		<ul class="nav list">
			 <j:iter items="${page.list}" var="bean" status="vstatus">
			
				<li><a href="#"> 
				<input type="checkbox" name="ids" value="${bean.FDID}"/> 
				    <span class="title">${bean.FDNAME}</span> 
				     <span class="date"><i class="icon-time"></i>
					<fmt:formatDate value="${bean.FDCREATETIME}" pattern="yyyy/MM/dd hh:mm aa"/></span>
					<span class="btns">
                               <button type="button" class="btn btn-link"><i class="icon-exam-num"></i><strong>${bean.QUESTIONNUM}</strong> 题 </button>
                               <b>|</b>
                               <button type="button" class="btn btn-link"><i class="icon-exam-score"></i>满分 / 及格：<strong> ${bean.FDTOTALNUM}</strong>  / <strong>${bean.FDSCORE}</strong> </button>
                               <b>|</b>
                               <button type="button" class="btn btn-link"><i class="icon-hourglass"></i><strong>${bean.FDSTUDYTIME}</strong> 分钟</button>
                           </span>

				</a></li>
			</j:iter> 
		</ul>
	</section>
	<div class="pages">
		<div class="btn-group dropup">
		<c:if test="${page.firstPage==true}">
			<button class="btn btn-primary btn-ctrl" type="button" disabled>
				<i class="icon-chevron-left icon-white"></i>
			</button>
		</c:if>
		<c:if test="${page.firstPage==false}">
			<a onclick="questionClick('${param.fdType}','${page.prePage}','${param.order}')">
				<button class="btn btn-primary btn-ctrl" type="button">
					<i class="icon-chevron-left icon-white"></i>
				</button>
			</a>
		</c:if>

		<c:forEach var="i" begin="1" end="${page.totalPage}">
            <c:choose>
                <c:when test="${page.pageNo == i}">
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
			  <c:forEach var="i" begin="1" end="${page.totalPage}">
				<li><a onclick="questionClick('${param.fdType}','${i}','${param.order}')">
				${i*10-10+1} - ${i*10} 
				</a></li>
			</c:forEach>
			</ul>

		<c:if test="${page.lastPage==true}">
			<button class="btn btn-primary btn-ctrl" type="button" disabled>
				<i class="icon-chevron-right icon-white"></i>
			</button>
		</c:if>
		<c:if test="${page.lastPage!=true}">
			<a onclick="questionClick('${param.fdType}','${page.nextPage}','${param.order}')">
				<button class="btn btn-primary btn-ctrl" type="button">
					<i class="icon-chevron-right icon-white"></i>
				</button>
			</a>
		</c:if>
	</div>
</div>
<script src="${ctx}/resources/js/jquery.jalert.js" type="text/javascript"></script>
<script type="text/javascript">
function questionClick(fdType,pageNo,order){
	var fdName = document.getElementById("serach").value;
	$("#pageBody").html("");
	$.ajax({
		type: "post",
		 url: "${ctx}/ajax/material/getMatQuestionsOrByKey",
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