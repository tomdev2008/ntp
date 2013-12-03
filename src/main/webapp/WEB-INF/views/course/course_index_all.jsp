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
                        <a class="btn btn-warning pull-right" href="{{!item.link}}"><i class="{{?item.isLearning}}icon-progress-shadow"></i>继续学习{{??}}icon-book-shadow"></i>开始学习{{?}}</a>
                        {{?it.type == 'single'}}
                            <div class="pull-right"><strong>{{=item.learnerNum}}</strong>位老师在学习</div>
                        {{?}}
                    </div>
                </div>
            </li>
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
                    <li><a href="#">全部课程</a></li>
                    <li class="active"><a href="#">国外考试</a></li>
                    <li><a href="#">国内考试</a></li>
                    <li><a href="#">英语学习</a></li>
                    <li><a href="#">优能中学</a></li>
                    <li><a href="#">泡泡少儿</a></li>
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
                link: "##",
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
                link: "##",
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
                link: "##",
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
</script>
</body>
</html>
