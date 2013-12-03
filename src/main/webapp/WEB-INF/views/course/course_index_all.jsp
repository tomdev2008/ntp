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
					<img src="{{?item.imgUrl!=""}}${ctx}/common/file/image/{{=item.imgUrl}}{{??}}${ctx }/resources/images/temp-newClass.jpg{{?}}" alt="">
                    {{?it.type == "series"}}
                        <div class="hd2">
                                    <span>
                                        <strong>{{=item.docNum}}个</strong>课程
                                    </span>
                                     <span>
                                        <strong>{{=item.learnerNum}}位</strong>老师在学习
                                    </span>
                        </div>
                    {{?}}
                    <div class="bd2">
                        <h3>{{=item.name}}</h3>
                        <p>{{=item.issuer}}</p>
						{{?it.type == "single"}}
                        <p class="rating">
                                    <span class="rating-view">
                                        <span class="rating-all">
                                            {{ for(var i=1; i<= 5; i++){ }}
                                                <i class="icon-star{{?i<item.score}} active{{?}}"></i>
                                            {{ } }}
                                         </span>
                                        <b class="text-warning">{{=item.score}}</b>
                                    </span>
                            ({{=item.raterNum}} 个评分)
                        </p>
						{{?}}
                        {{?it.type == "series"}}
                            <dl>
                                <dt>简介：</dt>
                                <dd>{{=item.intro}}</dd>
                            </dl>
                        {{?}}
                    </div> 
                    <div class="ft2 clearfix{{?it.type == 'single'}} bdt1{{?}}">
						{{?it.type == "series"}}
                        	<a class="btn btn-warning pull-right" href="${ctx}/series/previewSeries?seriesId={{=item.dataId}}">
 						{{?}}
						{{?it.type == "single"}}
                        	<a class="btn btn-warning pull-right" href="${ctx}/passThrough/getCourseHome/{{=item.dataId}}">
 						{{?}}
						<i class="{{?item.isLearning}}icon-progress-shadow"></i>继续学习{{??}}icon-book-shadow"></i>开始学习{{?}}</a>
                        {{?it.type == 'single'}}
                            <div class="pull-right"><strong>{{=item.learnerNum}}</strong>位老师在学习</div>
                        {{?}}
                    </div>
                </div>
            </li>
        {{~}}
    </script>
<script id="courseCategoryTemplate" type="text/x-dot-template">
    <li class="active"><a href="javascript:void(0)" data-id="all">全部课程</a></li>
	{{~it.list :item:index}}
	<li ><a href="javascript:void(0)" data-id="{{=item.courseCategoryId}}">{{=item.courseCategoryName}}</a></li>
	{{~}}
</script>
    <script src="${ctx}/resources/js/doT.min.js"></script>
</head>

<body>

<section class="container">
	<section class="mt20">
        <div id="myCarousel" class="carousel slide" data-toggle="carousel" data-interval="4000">
            <div class="carousel-l"><img src="${ctx}/resources/images/courseHomeSlider/slide-05.png" alt=""/></div>
            <div class="carousel-r"><img src="${ctx}/resources/images/courseHomeSlider/slide-02.png" alt=""/></div>
            <div class="carousel-mask"></div>
            <div class="carousel-inner">
                <div class="item active">
                    <img src="${ctx}/resources/images/courseHomeSlider/slide-01.jpg" alt=""/>
                </div>
                <div class="item">
                    <img src="${ctx}/resources/images/courseHomeSlider/slide-02.png" alt=""/>
                </div>
                <div class="item">
                    <img src="${ctx}/resources/images/courseHomeSlider/slide-03.png" alt=""/>
                </div>
                <div class="item">
                    <img src="${ctx}/resources/images/courseHomeSlider/slide-04.png" alt=""/>
                </div>
                <div class="item">
                    <img src="${ctx}/resources/images/courseHomeSlider/slide-05.png" alt=""/>
                </div>
            </div>
            <div class="left carousel-btn" data-target="#myCarousel" data-slide="prev"></div>
            <div class="right carousel-btn" data-target="#myCarousel" data-slide="next"></div>
            <ol class="carousel-indicators">
                <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
                <li data-target="#myCarousel" data-slide-to="1"></li>
                <li data-target="#myCarousel" data-slide-to="2"></li>
                <li data-target="#myCarousel" data-slide-to="3"></li>
                <li data-target="#myCarousel" data-slide-to="4"></li>
            </ol>
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
            <div class="navbar-inner">
                <ul class="nav" id="navCourse">
                </ul>
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

<!--底部 E-->
</section>
<script type="text/javascript" src="${ctx}/resources/js/jquery.placeholder.1.3.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/bootstrap.min.js"></script>

<script type="text/javascript">	
	var thumbnailsFn = doT.template(document.getElementById("thumbnailsTemplate").text);
	var courseCategoryFn = doT.template(document.getElementById("courseCategoryTemplate").text);
	//初始化课程分类
	initCourseCategory();
	//初始化系列
	initSeries(1);

	//初始化课程
	initCouese("all",1);
  /*   var courseData = {
        type: "single",
        list:[
            {
                imgUrl: "./images/zht-main-img.jpg",
                learnerNum: 17002,
                name: "广东民俗与课堂教学",
                issuer: "集团国外考试推广管理中心",
                score: 4,
                raterNum: 76,
                link: "##",
                isLearning: false
            },
            {
                imgUrl: "./images/zht-main-img.jpg",
                learnerNum: 17002,
                name: "广东民俗与课堂教学",
                issuer: "集团国外考试推广管理中心",
                score: 4,
                raterNum: 76,
                link: "##",
                isLearning: true
            },
            {
                imgUrl: "./images/zht-main-img.jpg",
                learnerNum: 17002,
                name: "广东民俗与课堂教学",
                issuer: "集团国外考试推广管理中心",
                score: 4,
                raterNum: 76,
                link: "##",
                isLearning: true
            }
        ]
    };
    $("#list-course").append(thumbnailsFn(courseData)); */

    $("#loadMoreSeries").click(function(e){//点击加载更多
    	 e.preventDefault();
         var pageNo = $("#list-series li").length/3+1;
         $.ajax({
        	 url : "${ctx}/ajax/series/getSeries",
     		async : false,
     		dataType : 'json',
     		data:{
     			pageNo:pageNo
     		},
     		success : function(result) {
     			$("#list-series").append(thumbnailsFn(result));
     			if(result.hasMore){
   				 	$("#loadMoreSeries").removeClass("hide");
   				}else{
   				 	$("#loadMoreSeries").addClass("hide");
   				}
     		}
     	});
    });
    $("#loadMoreCourse").click(function(e){//点击加载更多
    	e.preventDefault();
        var pageNo = $("#list-course li").length/3+1;
        var type = $("#navCourse .active a").attr("data-id");
        $.ajax({
    		url : "${ctx}/ajax/course/getAllCoursesIndexInfo",
    		async : false,
    		dataType : 'json',
    		data:{
    			userId:"${userId}",
    			type:type,
    			pageNo:pageNo,
    		},
    		success : function(result) {
    			$("#list-course").append(thumbnailsFn(result));
    			if(result.hasMore){
    				 $("#loadMoreCourse").removeClass("hide");
    			}else{
    				 $("#loadMoreCourse").addClass("hide");
    			}
    		}
    	});
    });

    $("#navCourse>li>a").click(function(e){
    	 e.preventDefault();
         var type = $(this).attr("data-id");
         initCouese(type,1);
         $(this).parent().addClass("active").siblings().removeClass("active");
    });
    $("#myCarousel").on("slid",function(e){
        var $this = $(this);
        $this.find(".carousel-inner>.item").each(function(i){
            if($(this).hasClass("active")){
                $this.find(".carousel-l>img").attr("src",$(this).prev().length ? $(this).prev().children("img").attr("src") : $this.find(".carousel-inner>.item:last>img").attr("src"));
                $this.find(".carousel-r>img").attr("src",$(this).next().length ? $(this).next().children("img").attr("src") : $this.find(".carousel-inner>.item:first>img").attr("src"));
            }
        })
    });
    $("#myCarousel>.carousel-inner>.item").click(function(e){
        switch ($(this).index()){
            case 0:
                alert("第一张图链接");
                break;
            case 1:
                alert("第二张图链接");
                break;
            case 2:
                alert("第三张图链接");
                break;
            case 3:
                alert("第四张图链接");
                break;
            case 4:
                alert("第五张图链接");
                break;
        }
    })
    
    function initCourseCategory(){
    	$.ajax({
    		url : "${ctx}/ajax/category/getCourseCategory",
    		async : false,
    		dataType : 'json',
    		success : function(result) {
    			$("#navCourse").html(courseCategoryFn(result));
    		}
    	});
    }
    function initSeries(pageNo){
    	$.ajax({
    		url : "${ctx}/ajax/series/getSeries",
    		async : false,
    		dataType : 'json',
    		data:{
    			pageNo:pageNo
    		},
    		success : function(result) {
    			$("#list-series").html(thumbnailsFn(result));
    			if(result.hasMore){
   				 	$("#loadMoreSeries").removeClass("hide");
   				}else{
   				 	$("#loadMoreSeries").addClass("hide");
   				}
    		}
    	});
    }
    function initCouese(type,pageNo){
    	$.ajax({
    		url : "${ctx}/ajax/course/getAllCoursesIndexInfo",
    		async : false,
    		dataType : 'json',
    		data:{
    			userId:"${userId}",
    			type:type,
    			pageNo:pageNo,
    		},
    		success : function(result) {
    			$("#list-course").html(thumbnailsFn(result));
    			if(result.hasMore){
    				 $("#loadMoreCourse").removeClass("hide");
    			}else{
    				 $("#loadMoreCourse").addClass("hide");
    			}
    		}
    	});
    }
</script>
</body>
</html>
