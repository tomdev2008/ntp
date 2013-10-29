<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="j" uri="/WEB-INF/tags/formtag.tld"%>
	        <div class="page-body" id="pageBody">
	<section class="section box-control">
		<div class="hd">
			<div class="btn-toolbar">
				<a class="btn" href="#rightCont">发布新课程</a>
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
					<input type="text" id="serach" class="search" placeholder="搜索课程"
					   onblur="findeCoursesByKey('${param.fdTitle}');" > 
					<i class="icon-search"></i>
				</form>
				<span class="showState"> <span class="muted">当前显示：</span>含“<a
					href="#">雅思</a>”的条目
				</span> <a class="btn btn-link" href="#rightCont" >清空搜索结果</a>
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
			    <a onclick="findeCoursesByKey('1','fdsorce')">
			      <c:if test="${param.order=='fdsorce'}">
					<button class="btn btn-large active" type="button">评分</button>
				   </c:if>
				   <c:if test="${param.order!='fdsorce'}">
					<button class="btn btn-large" type="button">评分</button>
				   </c:if>
				</a> 
				</div>
				<label class="radio inline" for="selectCurrPage"><input
					type="radio" id="selectCurrPage" name="selectCheckbox" checked />选中本页</label>
				<label class="radio inline" for="selectAll"><input
					type="radio" id="selectAll" name="selectCheckbox" />选中全部</label>
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
	<section class="section listWrap">
		<ul class="nav list">
		    
			 <j:iter items="${page.list}" var="bean" status="vstatus">
				<li><a href="#"> 
				
				<input type="checkbox" /> 
				    <span class="title">${bean.FDTITLE}</span> 
				    <span class="rating-view">
				    <c:if test="${FDSCORE!=null}">
					  <span class="rating-all">
					  <c:forEach var="i" begin="1" end="${FDSCORE}">
					   <i class="icon-star active"></i>
					  </c:forEach>
					  <c:forEach var="i" begin="1" end="${5-FDSCORE}">
					   <i class="icon-star"></i>
					  </c:forEach>
					  </span> 
					  <b class="text-warning">${FDSCORE}</b>
					  </c:if>
					  
					  <c:if test="${FDSCORE==null}">
					  <span class="rating-all">
					  <c:forEach var="i" begin="1" end="5">
					   <i class="icon-star"></i>
					  </c:forEach>
					  </span> 
					  <b class="text-warning">0</b>
					  </c:if>
					  
					</span> <span class="date"><i class="icon-time"></i>${bean.FDCREATETIME}</span>
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
				<li><a onclick="findeCoursesByKey('','${i}')"> 
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
			<a onclick="findeCoursesByKey('${page.getNextPage()}')">
				<button class="btn btn-primary btn-ctrl" type="button">
					<i class="icon-chevron-right icon-white"></i>
				</button>
			</a>
		</c:if>

	</div>
	</div>
    </div>         