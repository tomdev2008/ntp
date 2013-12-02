<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<!--[if lt IE 7]>      <html class="lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class=""> <!--<![endif]-->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新东方在线教师备课平台</title>
<link href="${ctx}/resources/css/global.css" rel="stylesheet" type="text/css">
<link href="${ctx}/resources/css/home-course.css" rel="stylesheet" type="text/css">
<!--[if lt IE 9]>
<script src="js/html5.js"></script>
<![endif]-->
<script id="thumbnailsTemplate" type="text/x-dot-template">
        {{~it.list :item:index}}
            <li>
                <div class="thumbnail">
                    <img src="{{!item.imgUrl}}" alt=""/>
                    {{?it.type == "series"}}
                        <div class="hd2">
                                    <span>
                                        <strong>{{=item.docNum}}个</strong>精品文档
                                    </span>
                                     <span>
                                        <strong>{{=item.learnerNum}}位</strong>老师在学习
                                    </span>
                        </div>
                    {{?}}
                    <div class="bd2">
                        <h3>{{=item.name}}</h3>
                        <p>{{=item.issuer}}</p>
                        <p class="rating">
                                    <span class="rating-view">
                                        <span class="rating-all">
                                            {{ for(var i=0; i< 5; i++){ }}
                                                <i class="icon-star{{?i<item.score}} active{{?}}"></i>
                                            {{ } }}
                                         </span>
                                        <b class="text-warning">{{=item.score}}</b>
                                    </span>
                            ({{=item.raterNum}} 个评分)
                        </p>
                        {{?it.type == "series"}}
                            <dl>
                                <dt>简介：</dt>
                                <dd>{{=item.intro}}</dd>
                                <dt>适合人群：</dt>
                                <dd>{{=item.crowd}}</dd>
                            </dl>
                        {{?}}
                    </div>
                    <div class="ft2 clearfix{{?it.type == 'single'}} bdt1{{?}}">
                        <a class="btn btn-primary pull-right" href="{{=item.link}}"><i class="{{?item.isLearning}}icon-progress-shadow"></i>继续学习{{??}}icon-book-shadow"></i>开始学习{{?}}</a>
                        {{?it.type == 'single'}}
                            <div class="pull-right"><strong>{{=item.learnerNum}}</strong>位老师在学习</div>
                        {{?}}
                    </div>
                </div>
            </li>
        {{~}}
</script>
<script id="courseCategoryTemplate" type="text/x-dot-template">
<ul class="nav" id="navCourse">
    <li class="active"><a href="#">全部课程</a></li>
	{{~it.list :item:index}}
	<li ><a href="#">{{=item.courseCategoryName}}</a></li>
	{{~}}
</ul>
</script>
    <script src="${ctx}/resources/js/doT.min.js"></script>
</head>

<body>
<section class="container">
	<section class="mt20 section">
        <div class="clearfix row1">
            <div class="pull-left media profile">
                <div class="pull-left"><img src="./images/temp-face70.jpg" class="media-object" alt=""/></div>
                <div class="media-body">
                    <div class="media-heading">
                        杨义锋 <i class="icon-female"></i>
                        <a class="icon-circle-bg" href="#"><i class="icon-pencil-mini"></i></a>
                        <a class="icon-circle-bg" href="#"><i class="icon-cloth"></i></a>
                    </div>
                    <p class="muted">
                        机构  集团总公司 <br/>
                        部门  知识管理中心<br/>
                        电话  135-8165-1017<br/>
                        年龄  27<br/>
                        星座  射手座<br/>
                        血型  A
                    </p>
                </div>
            </div>
            <div class="pull-right box1">
                <div class="clearfix">
                    <div class="pull-left">
                        完成学习 <strong class="num">80</strong>个课程
                    </div>
                    <div class="pull-right">
                        正在学习 <strong class="num">52</strong>个课程
                    </div>
                </div>
                <div class="well">
                    <i class="icon-shyhl"></i>
                    <span class="txt">这家伙很懒，也不好好介绍一下自己~ :-(</span>
                    <i class="icon-shyhr"></i>
                </div>
            </div>
        </div>
	</section>

    <section class="md">
        <div class="hd"><h2>系列课程</h2></div>
        <div class="bd">
            <ul class="thumbnails" id="list-series">
            </ul>
        </div>
        <div class="ft">
            <a href="#"id="loadMoreSeries">点击加载更多 <i class="icon-circle"></i><i class="icon-circle"></i><i class="icon-circle"></i></a>
        </div>
    </section>

    <section class="md">
        <div class="hd navbar">
            <div class="navbar-inner" id="courseCategoryDiv">
               
            </div>
        </div>
        <div class="bd">
            <ul class="thumbnails"  id="list-course">
            </ul>
        </div>
        <div class="ft">
            <a href="#" id="loadMoreCourse">点击加载更多 <i class="icon-circle"></i><i class="icon-circle"></i><i class="icon-circle"></i></a>
        </div>
    </section>

</section>
<script type="text/javascript" src="${ctx}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.placeholder.1.3.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/bootstrap.min.js"></script>

<script type="text/javascript">	
	var thumbnailsFn = doT.template(document.getElementById("thumbnailsTemplate").text);
	var courseCategoryFn = doT.template(document.getElementById("courseCategoryTemplate").text);

	//初始化课程分类
	initCourseCategory();
    var seriesData = {
        type: "series",
        list:[
            {
                imgUrl: "./images/zht-main-img.jpg",
                docNum: 254,
                learnerNum: 17002,
                name: "广东民俗与课堂教学",
                issuer: "集团国外考试推广管理中心",
                score: 4,
                raterNum: 76,
                intro: "300个实用、时鲜的英文词汇与100个生活必备句子，让学习的过程轻松愉快，效率更高。",
                crowd: "办公人员、白领、职场新人、毕业生。",
                isLearning: false
            },
            {
                imgUrl: "./images/zht-main-img.jpg",
                docNum: 254,
                learnerNum: 17002,
                name: "广东民俗与课堂教学",
                issuer: "集团国外考试推广管理中心",
                score: 4,
                raterNum: 76,
                intro: "300个实用、时鲜的英文词汇与100个生活必备句子，让学习的过程轻松愉快，效率更高。",
                crowd: "办公人员、白领、职场新人、毕业生。",
                isLearning: true
            },
            {
                imgUrl: "./images/zht-main-img.jpg",
                docNum: 254,
                learnerNum: 17002,
                name: "广东民俗与课堂教学",
                issuer: "集团国外考试推广管理中心",
                score: 4,
                raterNum: 76,
                intro: "300个实用、时鲜的英文词汇与100个生活必备句子，让学习的过程轻松愉快，效率更高。",
                crowd: "办公人员、白领、职场新人、毕业生。",
                isLearning: true
            }
        ]
    };
    var courseData = {
        type: "single",
        list:[
            {
                imgUrl: "./images/zht-main-img.jpg",
                learnerNum: 17002,
                name: "广东民俗与课堂教学",
                issuer: "集团国外考试推广管理中心",
                score: 4,
                raterNum: 76,
                isLearning: false
            },
            {
                imgUrl: "./images/zht-main-img.jpg",
                learnerNum: 17002,
                name: "广东民俗与课堂教学",
                issuer: "集团国外考试推广管理中心",
                score: 4,
                raterNum: 76,
                isLearning: true
            },
            {
                imgUrl: "./images/zht-main-img.jpg",
                learnerNum: 17002,
                name: "广东民俗与课堂教学",
                issuer: "集团国外考试推广管理中心",
                score: 4,
                raterNum: 76,
                isLearning: true
            }
        ]
    };
    
    $("#list-series").append(thumbnailsFn(seriesData));
    $("#list-course").append(thumbnailsFn(courseData));

    $("#loadMoreSeries").click(function(e){//点击加载更多
        e.preventDefault();
        $("#list-series").append(thumbnailsFn(seriesData));
    });
    $("#loadMoreCourse").click(function(e){//点击加载更多
        e.preventDefault();
        $("#list-course").append(thumbnailsFn(courseData));
    });

    $("#navCourse>li>a").click(function(e){
        e.preventDefault();
        $(this).parent().addClass("active").siblings().removeClass("active");
    })
    
    function initCourseCategory(){
    	$.ajax({
    		url : "${ctx}/ajax/category/getCourseCategory",
    		async : false,
    		dataType : 'json',
    		type: "post",
    		success : function(result) {
    			$("#courseCategoryDiv").append(courseCategoryFn(result));
    		}
    	});
    }
</script>
</body>
</html>
