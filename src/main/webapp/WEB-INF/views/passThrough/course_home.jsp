<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="${ctx}/resources/theme/default/css/global.css" rel="stylesheet" type="text/css">
<link href="${ctx}/resources/theme/default/css/home_zht.css" rel="stylesheet" type="text/css">
<!--[if lt IE 9]>
<script src="js/html5.js"></script>
<![endif]-->
</head>

<body>
<!--头部 S-->
<!--  
<header class="navbar navbar-inverse navbar-fixed-top">
	<div class="navbar-inner">
    	<div class="container">
			<a href="#" class="logo"></a>
	        <ul class="nav">
	          <li><a href="#">系统管理</a></li>
	          <li><a href="#">我是导师</a></li>
	          <li><a href="#">我是主管</a></li>
	        </ul>
			
            <ul class="nav pull-right">
              <li class="dropdown">
              	<a href="#" class="dropdown-toggle" data-toggle="dropdown" >
                	<span class="top-face"><img src="./images/temp-face.jpg" alt=""><i class="icon-disc"></i></span>
                    <span class="name">杨义锋</span>
                    <b class="caret"></b>
                </a>
                <ul class="dropdown-menu">
                	<li><a href="#"><i class="icon-home"></i>备课首页</a></li>
                    <li><a href="#"><i class="icon-envelope"></i>我的私信<span class="icon-disc-bg">2</span></a></li>
                    <li><a href="profile.html"><i class="icon-user"></i>个人资料</a></li>
                    <li><a href="changePwd.html"><i class="icon-pencil"></i>修改密码</a></li>
                    <li><a href="#"><i class="icon-off"></i>退出平台</a></li>
                </ul>
              </li>
              <li><a href="#" class="btn-off"></a></li>
            </ul>
		</div>
    </div>
</header>
-->
<!--头部 E-->

<!--主体 S-->
<section class="container">	
		<div class="section mt20">
        	<div class="media box-pd20">
            <a href="#" class="pull-left"><img src="${ctx}/resources/images/zht-main-img.jpg" alt="" class="media-object"></a>
       	    <div class="media-body">
        		<div class="media-heading">
        		  <h2>${course.fdTitle}</h2>
                  <a href="#" class="btn-next icon-disc-lg-bg"><i class="icon-chevronBig-right"></i></a>
                  <a href="#" class="btn-home icon-disc-lg-bg"><i class="icon-home icon-white"></i></a>
        		</div>
              <p>${course.fdAuthor}
              </p>
              	<div class="rating-view" id="courseScore">
                		<span class="rating-all">
                		  <c:forEach var="i" begin="1" end="${courseScore}">
						   <i class="icon-star active"></i>
						  </c:forEach>
						  <c:forEach var="i" begin="1" end="${5-courseScore}">
						   <i class="icon-star"></i>
						  </c:forEach>
                         </span>
                         <b class="text-warning">${courseScore}</b>
                 </div>
                <div class="media-foot">
               		<a href="#" class="btn btn-warning"><i class="icon-gift icon-white"></i>免费</a>
                    <span class="text-warning">${studayTotalNo}</span>位老师在学习
                </div>
        	</div>
            </div>
        </div>
		<div class="clearfix mt20">
	        <div class="pull-left w760">
	        	<div class="section">
	       	  		<div class="hd">
		        		<h2>作者  ${course.fdAuthor}</h2>
                        <div class="ab_r">
                        	<span class="pub_time"><i class="icon-time"></i>15分钟前</span>
                        </div>
		        	</div>
                    <div class="bd">
                    	<div class="box-pd20 box-txt">
                        	<p>Welcome to NTP! If you're a new instructor (or a seasoned one looking for new tips), you're in the right place! This course is taught by NTP.                        	</p>
                        	<p> In the first part of this course, you will learn essential information in creating a high quality online course on NTP. You will learn:</p>
                          <ul>
                              <li> what NTP is and what a NTP course looks like, including standards for the marketplace                                </li>
                              <li>4 steps in designing and creating a NTP course</li>
                              <li>what resources NTP provides to help you be a successful instructor on NTP</li>
                              <li> technical know-how's on the platform                                </li>
                              <li>tips in producing high quality video and other materials for teaching and learning </li>
                          </ul>
                        </div>
                    </div>
		        </div>
                <div class="section mt20">
                	<div class="box-pd20">
                    	<div class="larning-sections">
                    		<j:iter items="${catalog}" var="bean" status="vstatus">
                    	    <c:set var="i" value="1"/>
                    		<c:if test="${bean.fdType==0}">
                    		<dl>
                    			<dt>
                                	<span class="icon-disc-lg-bg disc-bd">${bean.fdNo}</span>
                                    <b class="caret"></b>
                                    <div class="span5"><span class="tit">业务学习</span></div>
                                    <div class="span2">建议时长</div>
                                    <div class="span1">学习进度</div>
                                </dt>
                                <j:iter items="${catalog}" var="lecture" status="stauts">
                                	<c:if test="${lecture.fdType==1 && lecture.hbmParent!=null && lecture.hbmParent.fdId==bean.fdId}">
		                    			<dd>
		                                	<div class="span5">
		                                    	<span class="tit">${bean.fdNo}.${i}&nbsp;${lecture.fdName}</span>
		                                        <button class="btn">再次学习</button>
		                                    </div>
		                                    <div class="span2">${lecture.fdStudyTime}</div>
		                                    <div class="span1"><i class="icon-circle-progress"><i class="icon-progress"></i></i></div>
		                                </dd>
		                                <c:set var="i" value="${i+1}"/>
		                            </c:if>
                                </j:iter>
                                </dl>
                            </c:if>
                            <c:if test="${bean.fdType==1 && bean.hbmParent==null}">
                             <dl>
                    			<dd>
                                	<div class="span5">
                                    	<span class="tit">${bean.fdNo}&nbsp;${bean.fdName}</span>
                                        <button class="btn">再次学习</button>
                                    </div>
                                    <div class="span2">${bean.fdStudyTime}</div>
                                    <div class="span1"><i class="icon-circle-progress"><i class="icon-progress"></i></i></div>
                                </dd>
                             </dl>
                            </c:if>
                    		</j:iter>
                    	</div>
                    </div>
                   
                </div>
                 <c:import url="/WEB-INF/views/comment.jsp">
                </c:import>
			</div>
			<div class="pull-right w225">
				 <!-- 评分评论页面 -->
	        	 <c:import url="/WEB-INF/views/passThrough/score_course.jsp">
                	<c:param name="courseId" value="${course.fdId}" />
                </c:import>
		        <!-- 学习当前课程的新教师列表 -->
                <c:import url="/WEB-INF/views/passThrough/learning_teacher_list.jsp">
                	<c:param name="courseId" value="${course.fdId}" />
                </c:import>
                
                <!-- 发现课程之最新课程列表 -->
                <c:import url="/WEB-INF/views/passThrough/new_course_list.jsp"></c:import>
	        </div>
        </div>

<!--底部 S-->
<!--  
	<footer>
		<div class="navbar clearfix">
			<div class="nav">
				<li><a href="http://www.xdf.cn/" target="_blank">新东方网</a></li>
				<li><a href="http://me.xdf.cn/" target="_blank">知识管理平台</a></li>
				<li><a href="${ctx }/login">登录</a></li>
				<li><a href="${ctx }/self_register">注册</a></li>
				<li class="last"><a href="mailto:yangyifeng@xdf.cn">帮助</a></li>
			</div>
            <p style="font-size:13px">&copy; 2013 新东方教育科技集团&nbsp;知识管理中心</p>
		</div>
	</footer>
-->
<!--底部 E-->
</section>
<!--主体 E-->
</body>
</html>
