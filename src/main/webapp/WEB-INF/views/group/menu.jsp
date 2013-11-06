<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
    	<ul class="nav nav-list sidenav" id="sideNav">
                <li class="nav-header first"><a href="#">学习跟踪</a></li>
                <li class="nav-header"><a href="#">授权学习</a></li>
	            <li class="nav-header">
                    <span>课程管理</span>
	            </li>
	             <c:if test="${param.fdType=='11'}">
	            <li class="active">
	             </c:if>
	             <c:if test="${param.fdType!='11'}">
	            <li>
	             </c:if>
	             <a href="${ctx}/series/findeSeriesInfos?fdType=11&order=fdcreatetime"">
	             <i class="icon-course-series"></i>我的系列课程</a></li>
	            
	            <c:if test="${param.fdType=='12'}">
	            <li class="active">
	             </c:if>
	             <c:if test="${param.fdType!='12'}">
	            <li>
	             </c:if>
	             
	              <a href="${ctx}/course/findcourseInfos?fdType=12&order=fdcreatetime">
	              <i class="icon-course"></i>我的课程</a>
	             </li>
	             <li class="nav-header">
                     <span>课程素材库</span>
	            </li>
                <c:if test="${param.fdType=='01'}">
                <li class="active">
                </c:if>
                <c:if test="${param.fdType!='01'}">
                <li>
                </c:if>
                  <a href="${ctx}/material/findList?fdType=01&order=FDCREATETIME"><i class="icon-video">
                </i>视频</a></li>
                </li>
	            <c:if test="${param.fdType=='04'}">
                <li class="active">
                </c:if>
                <c:if test="${param.fdType!='04'}">
                <li>
                </c:if>
                  <a href="${ctx}/material/findList?fdType=04&order=FDCREATETIME"><i class="icon-doc">
                </i>文档</a></li>
                <c:if test="${param.fdType=='05'}">
                <li class="active">
                </c:if>
                <c:if test="${param.fdType!='05'}">
                <li>
                </c:if>
                  <a href="${ctx}/material/findList?fdType=05&order=FDCREATETIME"><i class="icon-ppt">
                </i>幻灯片</a></li>
                <c:if test="${param.fdType=='08'}">
                <li class="active">
                </c:if>
                <c:if test="${param.fdType!='08'}">
                <li>
                </c:if>
                    <a href="${ctx}/material/findList?fdType=08&order=FDCREATETIME"><i class="icon-exam">
                </i>测试</a></li>
                
                <c:if test="${param.fdType=='10'}">
                 <li class="active">
                </c:if>
                <c:if test="${param.fdType!='10'}">
                 <li>
                </c:if>
                  <a href="${ctx}/material/findList?fdType=10&order=FDCREATETIME"><i class="icon-task"></i>作业</a></li>
	    </ul>

