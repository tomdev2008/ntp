<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
<div class="section navTeacher" data-spy="affix" data-offset-top="384">
	<div class="hd">
	<c:if test="${param.type=='tutor'}">
		<h5>我是导师</h5>
	</c:if>
	<c:if test="${param.type=='director'}">
		<h5>我是主管</h5>
	</c:if>
	</div>
    <div class="bd">
    	<div class="listImg">
            <c:if test="${param.type=='tutor'}">
            <a href="${ctx}/studyTrack/getStudyTrackTutor">
                 <img src="${ctx}/resources/images/iAmTeacher/track.jpg" alt="">
                    <span class="mask"></span>
                    <span class="caption">
                         <h6>学习跟踪</h6>
                    </span>
             </a>
            <a href="${ctx}/adviser/checkTask?order=fdcreatetime">
    			<img src="${ctx}/resources/images/iAmTeacher/schedule.jpg" alt="">
    			<span class="mask"></span>
    			<span class="caption">
                	<h6>批改作业</h6>
                </span>
            </a>            
            </c:if>
             <c:if test="${param.type=='director'}">
             <a href="#">
                 <img src="${ctx}/resources/images/iAmTeacher/track.jpg" alt="">
                    <span class="mask"></span>
                    <span class="caption">
                         <h6>学习跟踪</h6>
                    </span>
             </a>
              <a href="${ctx}/course/getCourseAuthInfos?fdType=13&order=fdcreatetime">
    			<img src="${ctx}/resources/images/iAmTeacher/schedule.jpg" alt="">
    			<span class="mask"></span>
    			<span class="caption">
                	<h6>授权学习</h6>
                </span>
            </a>
             <a href="${ctx}/course/findcourseInfos?fdType=12&order=fdcreatetime">
    			<img src="${ctx}/resources/images/iAmTeacher/schedule.jpg" alt="">
    			<span class="mask"></span>
    			<span class="caption">
                	<h6>课程管理</h6>
                </span>
            </a>
            <a href="${ctx}/material/findList?fdType=01&order=FDCREATETIME">
    			<img src="${ctx}/resources/images/iAmTeacher/schedule.jpg" alt="">
    			<span class="mask"></span>
    			<span class="caption">
                	<h6>课程素材库</h6>
                </span>
            </a>
            </c:if>
        </div>
    </div>
</div>