<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新东方在线教师备课平台</title>
<link href="${ctx}/resources/css/global.css" rel="stylesheet" type="text/css">
<link href="${ctx}/resources/css/iAmMentor.css" rel="stylesheet" type="text/css">
<!--[if lt IE 9]>
<script src="js/html5.js"></script>
<![endif]-->
<script id="personTemplate" type="x-dot-template">
                    <div class="profile">
                        <a href="#">
				<img src="{{?it.url.indexOf('http')>-1}}{{=it.url}}{{??}}${ctx}/{{=it.url}}{{?}}" class="face" alt="头像" /></a>
                        <h5>{{=it.name}} 
						{{?it.sex=='M'}}
							<i class="icon-male"></i></h5>
						{{??}}
							<i class="icon-female"></i></h5>
						{{?}}
                        <p class="muted">
                            {{=it.org}} {{=it.dep}} <br/>
                            最近登录    0 天前<br/>
                            在线统计    0 天
                        </p>
                    </div>
</script>
<script src="${ctx}/resources/js/doT.min.js" type="text/javascript"></script>
</head>
<body>
<section class="container">	
		<div class="clearfix mt20">
	        <div class="pull-left w760">
                <div class="page-header">
                    <span class="muted">我正在看：</span><span id="selectMessage">我指导的备课</span>
                    <div class="pos-r">
                        <a class="btn btn-link dropdown-toggle" href="#" data-toggle="dropdown">修改筛选条件</a>
                        <ul class="dropdown-menu" id="selectUl">
                            <li><a href="javascript:void(0)" data-type="myGuidance">我指导的备课</a></li>
                            <li><a href="javascript:void(0)" data-type="myOrg">我所在机构的备课</a></li>
                            <li><a href="javascript:void(0)" data-type="myDepart">我所在部门的备课</a></li>
                        </ul>
                    </div>
                </div>

			<!-- 列表页面 -->
			<c:import url="/WEB-INF/views/studyTrack/divtracklist.jsp">
				<c:param name="selectType" value="myGuidance"></c:param>
			</c:import>

			</div>
			<div class="pull-right w225">
                <div class="section" id="personFace">
                    <div class="profile">
                        <a href="#"><img src="./images/face-placeholder.png" class="face" alt="头像"/></a>
                        <h5>杨义锋 <i class="icon-male"></i></h5> <!-- 女人用.icon-female -->
                        <p class="muted">
                            集团总公司 知识管理中心 <br/>
                            最近登录    3 天前<br/>
                            在线统计    35 天
                        </p>
                    </div>
                </div>
			<!-- 图片列表页面 -->
			<c:import url="/WEB-INF/views/studyTrack/divimglist.jsp">
			</c:import>
	        </div>
        </div>
</section>
<script type="text/javascript">
$("#selectUl a").bind("click",function(){
	$this = $(this);
	$("#selectTypeHidden").val($this.attr("data-type"));
	$("#selectMessage").html($this.html());
	refreshTrackList($this.attr("data-type"),1,10,'time');
});
var personFn = doT.template(document.getElementById("personTemplate").text);
$.ajax({
	url : "${ctx}/ajax/studyTrack/getPerson",
	async : false,
	dataType : 'json',
	type: "post",
	success : function(result) {
		$("#personFace").html(personFn(result));
	}
});
</script>
</body>
</html>

