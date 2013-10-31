<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="j" uri="/WEB-INF/tags/formtag.tld"%>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
	<section class="section listWrap">
		<ul class="nav list">
			 <j:iter items="${page.list}" var="bean" status="vstatus">
				<li><a href="${ctx}/course/add?courseId=${bean.FDID}"> 
				<input type="checkbox" name="ids" value="${bean.FDID}"/>
				    <span class="title">${bean.FDTITLE}</span> 
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
					</span> <span class="date"><i class="icon-time"></i>${bean.FDCREATETIME}</span>
				</a></li>
			</j:iter> 
		</ul>
	</section>
  
