<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="j" uri="/WEB-INF/tags/formtag.tld"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
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
					   onblur="pageNavClick('${param.fdType}');"  onkeydown="showSearch();" onkeyup="showSearch();"> 
					<i class="icon-search"></i>
				</form>
				<span class="showState"> <span class="muted">当前显示：</span>含“<a
					href="#"><span id="show"></span></a>”的条目
				</span> <a class="btn btn-link" href="#rightCont" onclick="clearserach();">清空搜索结果</a>
			</div>
		</div>
		<div class="bd">
			<div class="btn-toolbar">
				<label class="muted">排序</label>
				<div class="btn-group btns-radio" data-toggle="buttons-radio">
				 <a onclick="pageNavClick('${param.fdType}','1','FDNAME')">
				   <c:if test="${param.order=='FDNAME'}">
					<button class="btn btn-large active" type="button">名称</button>
				   </c:if>
				   <c:if test="${param.order!='FDNAME'}">
					<button class="btn btn-large" type="button">名称</button>
				   </c:if>
				 </a>
				 <a onclick="pageNavClick('${param.fdType}','1','FDCREATETIME')">
				   <c:if test="${param.order=='FDCREATETIME'}">
					<button class="btn btn-large active" type="button">时间</button>
				   </c:if>
				   <c:if test="${param.order!='FDCREATETIME'}">
					<button class="btn btn-large" type="button">时间</button>
				   </c:if>
				</a>
			    <a onclick="pageNavClick('${param.fdType}','1','FDSCORE')">
			      <c:if test="${param.order=='FDSCORE'}">
					<button class="btn btn-large active" type="button">评分</button>
				   </c:if>
				   <c:if test="${param.order!='FDSCORE'}">
					<button class="btn btn-large" type="button">评分</button>
				   </c:if>
				</a> 
				</div>
				<label class="radio inline" for="selectCurrPage">
				   <input type="checkbox" id="selectCurrPage" name="selectCheckbox" onclick="checkcurrpage()"/>选中本页</label>
				<label class="radio inline" for="selectAll">
				   <input type="checkbox" id="selectAll" name="selectCheckbox"  onclick="selectAll()"/>选中全部</label>
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
						<a onclick="pageNavClick('${param.fdType}','${page.prePage}','${param.order}')">
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
						<a onclick="pageNavClick('${param.fdType}','${page.nextPage}','${param.order}')">
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
				<li><a href="${ctx}/material/updateVideo?fdId=${bean.FDID}&fdType=${bean.FDTYPE}"> 
				<input type="checkbox" name="ids" value="${bean.FDID}"/> 
				    <span class="title">${bean.FDNAME}</span> 
				    <span class="rating-view">
				      <c:if test="${bean.FDAVERAGE!=null}">
					  <span class="rating-all">
					  <c:forEach var="i" begin="1" end="${bean.FDAVERAGE}">
					   <i class="icon-star active"></i>
					  </c:forEach>
					  <c:forEach var="i" begin="1" end="${5-bean.FDAVERAGE}">
					   <i class="icon-star"></i>
					  </c:forEach>
					  </span> 
					  <b class="text-warning">${bean.FDAVERAGE}</b>
					  </c:if>
					  
					  <c:if test="${bean.FDAVERAGE==null}">
					  <span class="rating-all">
					  <c:forEach var="i" begin="1" end="5">
					   <i class="icon-star"></i>
					  </c:forEach>
					  </span> 
					  <b class="text-warning">0</b>
					  </c:if>
					  
					</span> <span class="date"><i class="icon-time"></i>
					<fmt:formatDate value="${bean.FDCREATETIME}" pattern="yyyy/MM/dd hh:mm aa"/></span>
						<span class="btns">
						
						 <button type="button" class="btn btn-link">
						 <i class="icon-eye"></i><strong>3315</strong></button><b>|</b>
                         <button type="button" class="btn btn-link">
                         <i class="icon-thumbs-up"></i><strong>1530</strong></button><b>|</b>
							   <button type="button" class="btn btn-link">
								<i class="icon-download"></i>
								<c:if test="${bean.FDDOWNLOADS==null}">
								 <strong>0</strong>
								</c:if>
								<c:if test="${bean.FDDOWNLOADS!=null}">
								  <strong>${bean.FDDOWNLOADS}</strong>
								</c:if>
							  </button>
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
			<a onclick="pageNavClick('${param.fdType}','${page.prePage}','${param.order}')">
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
				<li><a onclick="pageNavClick('${param.fdType}','${i}','${param.order}')">
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
			<a onclick="pageNavClick('${param.fdType}','${page.nextPage}','${param.order}')">
				<button class="btn btn-primary btn-ctrl" type="button">
					<i class="icon-chevron-right icon-white"></i>
				</button>
			</a>
		</c:if>
	</div>
</div>
<input type="hidden" id="fdType" value="${param.fdType}">

<script src="${ctx}/resources/js/jquery.jalert.js" type="text/javascript"></script>
<script type="text/javascript">
function showSearch(){
	$("#show").html($("#serach").val());
}
function clearserach(){
	$("#serach").attr("value","");
	$("#show").html("");
	pageNavClick('${param.fdType}',1,'fdcreatetime');
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
<script type="text/javascript">	
function pageNavClick(fdType,pageNo,order){
	var fdName = document.getElementById("serach").value;
	$("#showkey").attr("value",fdName);//关键字赋值
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
			$("#show").html($("#showkey").val());
			$("#serach").attr("value",$("#showkey").val());
			if($("#allFlag").val()=='true'){
				document.getElementById("selectAll").checked=true;
				selectAll();
			}
		}
	}); 
}
</script>
<script type="text/javascript">	
//jquery获取复选框值  
function batchDelete() {
	var delekey="";
	$('input[name="ids"]:checked').each(function() {
		delekey+=$(this).val()+",";
	});
	
	if(delekey==""){
		$.fn.jalert("当前没有选择要删除的数据？",function(){return;});
		return;
	}
	//是否全部选中
	if($("#allFlag").val()=='true'){
		$.fn.jalert("是否删除所有素材？",deleteAllMaterial);
	}else {
		$.ajax({
			type: "post",
			url: "${ctx}/ajax/material/batchDelete",
			data : {
				"materialIds":delekey,
			},
			success:function(data){
				window.location.href="${ctx}/material/findList?fdType="+$("#fdType").val();
			}
	  }); 
	}
	
}
//删除所有
function deleteAllMaterial(){
	var fdName = document.getElementById("serach").value;
	 $.ajax({
		type: "post",
		url: "${ctx}/ajax/material/deleteAllMaterial",
		data : {
			"fdName":fdName,
			"fdType":$("#fdType").val(),
		},
		success:function(data){
			window.location.href="${ctx}/material/findList?fdType="+$("#fdType").val();
		}
	}); 
}

</script>