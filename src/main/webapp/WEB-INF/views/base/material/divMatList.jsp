<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="j" uri="/WEB-INF/tags/formtag.tld"%>

	<section class="section box-control">
		<div class="hd">
			<div class="btn-toolbar">
				<a class="btn" href="#rightCont">添加</a>
				<div class="btn-group">
					<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
						操作 <span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<li><a href="#rightCont">导出列表</a></li>
						<li><a href="#rightCont">打包下载</a></li>
						<li><a href="#rightCont">批量删除</a></li>
					</ul>
				</div>
				<form class="toolbar-search">
					<input type="text" id="serach" class="search" placeholder="搜索条目"
					   onblur="pageClick('${param.fdType}');" > 
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
					<button class="btn btn-large active" type="button">名称</button>
					<button class="btn btn-large" type="button">时间</button>
					<button class="btn btn-large" type="button">评分</button>
				</div>
				<label class="radio inline" for="selectCurrPage"><input
					type="radio" id="selectCurrPage" name="selectCheckbox" checked />选中本页</label>
				<label class="radio inline" for="selectAll"><input
					type="radio" id="selectAll" name="selectCheckbox" />选中全部</label>
				<div class="pages pull-right">
					<div class="span2">
						 第<span> 1 - ${page.getTotalPage()}</span> / <span>${page.getTotalCount()}</span>
						 条 
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
				<li><a href="#"> 
				<input type="checkbox" /> 
				    <span class="title">${bean.FDNAME}</span> 
				    <span class="rating-view">
					  <span class="rating-all">
					    <i class="icon-star active"></i>
						<i class="icon-star active"></i> 
						<i class="icon-star"></i> 
						<i class="icon-star"></i> 
						<i class="icon-star"></i>
					  </span> 
					  <b class="text-warning">2.0</b>
					</span> <span class="date"><i class="icon-time"></i>${bean.FDCREATETIME}</span>
						<span class="btns">
							<button type="button" class="btn btn-link">
								<i class="icon-eye"></i>3315
							</button>
							<button type="button" class="btn btn-link">
								<i class="icon-thumbs-up"></i>2940
							</button>
							<button type="button" class="btn btn-link">
								<i class="icon-download"></i>0
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
		 <c:if test="${page.getTotalPage()>2}">
			<button class="btn btn-primary btn-num  dropdown-toggle"
				data-toggle="dropdown" type="button">
				<span class="caret"></span>
			</button>
		 </c:if>
			<ul class="dropdown-menu pull-right">
			  <c:forEach var="i" begin="1" end="${page.getTotalPage()}">
			     <li><a onclick="pageNavClick('${param.fdType}','${i}')">
			         ${i*10+1}-${i*20+1}
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
