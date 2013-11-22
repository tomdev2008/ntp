<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
<div class="section navTeacher" data-spy="affix" data-offset-top="384">
	<div class="hd">
		<h5>我是导师</h5>
	</div>
    <div class="bd">
    	<div class="listImg">
        	<a href="#">
    			<img src="images/iAmTeacher/track.jpg" alt="">
    			<span class="mask"></span>
    			<span class="caption">
                	<h6>学习跟踪</h6>
                </span>
            </a>
            <a href="#">
    			<img src="images/iAmTeacher/checkwork.jpg" alt="">
    			<span class="mask"></span>
    			<span class="caption">
                	<h6>批改作业</h6>
                </span>
            </a>
            <a href="#">
    			<img src="images/iAmTeacher/schedule.jpg" alt="">
    			<span class="mask"></span>
    			<span class="caption">
                	<h6>安排日程</h6>
                </span>
            </a>
        </div>
    </div>
</div>