<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
	<section class="section box-control">
		<div class="hd">
			<div class="btn-toolbar">
				<a class="btn" href="${ctx}/material/materialAddFoward?fdType=${param.fdType}">添加</a>
				<div class="btn-group">
					<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
						操作 <span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<!-- <li><a href="#rightCont">导出列表</a></li>
						<li><a href="#rightCont">打包下载</a></li> -->
						<li><a href="#rightCont" onclick="batchDelete();">批量删除</a></li>
					</ul>
				</div>
				<form class="toolbar-search" onkeydown="pressEnter();">
					<input type="text" id="serach" class="search" placeholder="搜索素材"
					   onblur="pageNavClick('${param.fdType}');"  onkeydown="showSearch();" onkeyup="showSearch();"> 
					<i class="icon-search"  onclick="pageNavClick('${param.fdType}','1','FDCREATETIME');"></i>
				</form>
				<span class="showState"> <span class="muted">当前显示：</span>
				 	<span id="markshow">
						<a id="containkey"href="#">全部条目</a>
					</span>
				</span>
				 <a class="btn btn-link" style="position:relative;right: 5px;padding-right:1px;padding-left:1px" href="#rightCont">清空搜索结果</a>
			</div>
		</div>
		<div class="bd">
			<div class="btn-toolbar">
				<label class="muted">排序</label>
				<div class="btn-group btns-radio" data-toggle="buttons-radio">
				 <a onclick="pageNavClick('${param.fdType}','1','FDCREATETIME')">
				   <c:if test="${param.order=='FDCREATETIME'}">
					<button class="btn btn-large active" type="button">时间</button>
				   </c:if>
				   <c:if test="${param.order!='FDCREATETIME'}">
					<button class="btn btn-large" type="button">时间</button>
				   </c:if>
				</a>
				 <a onclick="pageNavClick('${param.fdType}','1','FDNAME')">
				   <c:if test="${param.order=='FDNAME'}">
					<button class="btn btn-large active" type="button">名称</button>
				   </c:if>
				   <c:if test="${param.order!='FDNAME'}">
					<button class="btn btn-large" type="button">名称</button>
				   </c:if>
				 </a>
				</div>
				<label class="checkbox inline" for="selectCurrPage">
				   <input type="checkbox" id="selectCurrPage" name="selectCheckbox" onclick="checkcurrpage()"/>选中本页</label>
				<label class="checkbox inline" for="selectAll">
				   <input type="checkbox" id="selectAll" name="selectCheckbox"  onclick="selectAll()"/>选中全部</label>
				<div class="pages pull-right">
					<div class="span2">
						 第<span> 
						  ${page.startNum} - ${page.endNum}
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
			
				<li><a href="${ctx}/material/materialFoward?fdId=${bean.FDID}&fdType=${param.fdType}"> 
				<input type="checkbox" name="ids" value="${bean.FDID}"/> 
				    <span class="title">${bean.FDNAME}</span> 
				     <span class="date"><i class="icon-time"></i>
					<fmt:formatDate value="${bean.FDCREATETIME}" pattern="yyyy/MM/dd hh:mm aa"/></span>
					<span class="btns">
                           <button type="button" class="btn btn-link"><i class="icon-exam-num"></i><strong>${bean.TASKNUM}</strong> 题 </button>
                           <b>|</b>
                           <button type="button" class="btn btn-link"><i class="icon-exam-score"></i>满分 ：<strong> ${bean.FULLMARKS}</strong> </button>
                           <b>|</b>
                           <button type="button" class="btn btn-link"><i class="icon-hourglass"></i><strong>${bean.FDSTUDYTIME}</strong> 天</button>
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
                <a onclick="pageNavClick('${param.fdType}','${i}','${param.order}')">
                    <button class="btn btn-primary btn-num" type="button">${i}</button>
                    </a>
                </c:otherwise>
            </c:choose>
          </c:forEach>
			<button class="btn btn-primary btn-num  dropdown-toggle"
				data-toggle="dropdown" type="button">
				<span class="caret"></span>
			</button>
			<ul class="dropdown-menu pull-right">
			  <c:forEach var="i" begin="${page.startOperate}" end="${page.endOperate}"> 
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

