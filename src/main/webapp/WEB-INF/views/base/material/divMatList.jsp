<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="j" uri="/WEB-INF/tags/formtag.tld"%>

	<section class="section box-control">
		<div class="hd">
			<div class="btn-toolbar">
				<a class="btn" href="${ctx}/material/addOrUpdateVideo">添加</a>
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
					   onblur="pageNavClick('${param.fdType}');" > 
					<i class="icon-search"></i>
				</form>
				<span class="showState"> <span class="muted">当前显示：</span>含“<a
					href="#">雅思</a>”的条目
				</span> <a class="btn btn-link" href="#rightCont">清空搜索结果</a>
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
				   <input type="radio" id="selectCurrPage" name="selectCheckbox" checked />选中本页</label>
				<label class="radio inline" for="selectAll">
				   <input type="radio" id="selectAll" name="selectCheckbox" onclick="selectAll()"/>选中全部</label>
				<div class="pages pull-right">
					<div class="span2">
						 第<span> 
						 <c:if test="${page.getTotalPage()==1}">
						   1 - ${page.getTotalCount()}
						 </c:if>  
						 <c:if test="${page.getTotalPage()>1}">
							<c:if test="${page.getPageNo()<page.getTotalPage()}">
						   ${page.getPageNo()*10+1} - ${page.getPageNo()*20}
						 </c:if>
							<c:if test="${page.getPageNo()==page.getTotalPage()}">
						   ${page.getPageNo()*10+1} - ${page.getTotalCount()}
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
						<a onclick="pageNavClick('${param.fdType}','${page.getPrePage()}')">
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
						<a onclick="pageNavClick('${param.fdType}','${page.getNextPage()}')">
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
				<li><a href="${ctx}/material/addOrUpdateVideo?fdId=${bean.FDID}&fdType=01"> 
				<input type="checkbox" name="ids" value="${bean.FDID}"/> 
				    <span class="title">${bean.FDNAME}</span> 
				    <span class="rating-view">
				      <c:if test="${materialScore!=null}">
					  <span class="rating-all">
					  <c:forEach var="i" begin="1" end="${materialScore}">
					   <i class="icon-star active"></i>
					  </c:forEach>
					  <c:forEach var="i" begin="1" end="${5-materialScore}">
					   <i class="icon-star"></i>
					  </c:forEach>
					  </span> 
					  <b class="text-warning">${materialScore}</b>
					  </c:if>
					  
					  <c:if test="${materialScore==null}">
					  <span class="rating-all">
					  <c:forEach var="i" begin="1" end="5">
					   <i class="icon-star"></i>
					  </c:forEach>
					  </span> 
					  <b class="text-warning">0</b>
					  </c:if>
					  
					</span> <span class="date"><i class="icon-time"></i>${bean.FDCREATETIME}</span>
						<span class="btns">
							<button type="button" class="btn btn-link">
								<i class="icon-eye"></i>3315
							</button>
							<button type="button" class="btn btn-link">
								<i class="icon-thumbs-up"></i>2940
							</button>
							<button type="button" class="btn btn-link">
								<i class="icon-download"></i>
								<c:if test="${bean.FDDOWNLOADS==null}">
								  0
								</c:if>
								<c:if test="${bean.FDDOWNLOADS!=null}">
								  ${bean.FDDOWNLOADS}
								</c:if>
							</button>
					</span>
				</a></li>
			</j:iter> 
		</ul>
	</section>
	<div class="pages">
		<div class="btn-group dropup">
		<c:if test="${page.isFirstPage()==true}">
			<button class="btn btn-primary btn-ctrl" type="button" disabled>
				<i class="icon-chevron-left icon-white"></i>
			</button>
		</c:if>
		<c:if test="${page.isFirstPage()==false}">
			<a onclick="pageNavClick('${param.fdType}','${page.getPrePage()}')">
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
				<li><a onclick="pageNavClick('${param.fdType}','${i}')"> 
				<c:if test="${page.getTotalPage()==1}">
						   1 - ${page.getTotalCount()}
				</c:if> 
				<c:if test="${page.getTotalPage()>1}">
					<c:if test="${page.getPageNo()<page.getTotalPage()}">
						  ${page.getPageNo()*10+1} - ${page.getPageNo()*20}
					</c:if>
				    <c:if test="${page.getPageNo()==page.getTotalPage()}">
						   ${page.getPageNo()*10+1} - ${page.getTotalCount()}
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
			<a onclick="pageNavClick('${param.fdType}','${page.getNextPage()}')">
				<button class="btn btn-primary btn-ctrl" type="button">
					<i class="icon-chevron-right icon-white"></i>
				</button>
			</a>
		</c:if>
	</div>
	</div>
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