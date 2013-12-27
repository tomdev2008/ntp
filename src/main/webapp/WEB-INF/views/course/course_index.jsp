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
<link href="${ctx}/resources/theme-profile/default/css/global.css" rel="stylesheet" type="text/css">
<link href="${ctx}/resources/theme-profile/default/css/home-course.css" rel="stylesheet" type="text/css">
<!--[if lt IE 9]>
<script src="js/html5.js"></script>
<![endif]-->
<script id="thumbnailsTemplate" type="text/x-dot-template">
        {{~it.list :item:index}}
            <li>
                <div class="thumbnail">
					<img src="{{?item.imgUrl!=""}}${ctx}/common/file/image/{{=item.imgUrl}}{{??}}${ctx }/resources/images/default-cover.png{{?}}" alt="">
					{{?it.type == "series"}}
                        <div class="hd2">
                                    <span>
                                        <strong>{{=item.docNum}}&nbsp;个</strong>课程
                                    </span>
                                     <span>
                                        <strong>{{=item.learnerNum}}&nbsp;位</strong>老师在学习
                                    </span>
                        </div>
                    {{?}}
                    <div class="bd2">
                        <h3>
						{{?item.name==null}}
							不详
						{{??}}
							{{?item.name.length>17}}
								{{=item.name.substring(0,17)}}...
							{{??}}
								{{=item.name}}
							{{?}}
						{{?}}
						</h3>
                        <p>
						{{?item.issuer==null}}
							不详
						{{??}}
							{{?item.issuer.length>17}}
								{{=item.issuer.substring(0,17)}}...
							{{??}}
								{{=item.issuer}} 
							{{?}}
						{{?}}
						</p>
 						{{?it.type == "single"}}
                        <p class="rating">
                                    <span class="rating-view">
                                        <span class="rating-all">
                                            {{ for(var i=1; i<= 5; i++){ }}
                                                <i class="icon-star{{?i<=item.score}} active{{?}}"></i>
                                            {{ } }}
                                         </span>
                                        <b class="text-warning">
										{{?(item.score+"").length==1}}
											{{=item.score}}.0
										{{??}}
											{{=item.score}}
										{{?}}														
										</b>
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
                        	<a class="btn btn-primary pull-right" href="${ctx}/series/studayfoward?seriesId={{=item.dataId}}">
 						{{?}}
						{{?it.type == "single"}}
                        	<a class="btn btn-primary pull-right" href="${ctx}/passThrough/getCourseHome/{{=item.dataId}}">
 						{{?}}
							{{?it.type == "series"}}
								<i class="{{?item.isLearning}}icon-progress-shadow"></i>继续学习{{??}}icon-book-shadow"></i>开始学习{{?}}
 							{{?}}
							{{?it.type == "single"}}
								{{?item.isme == true}}
									<i class="{{?item.isLearning}}icon-progress-shadow"></i>{{?item.isThrough}}再次学习{{??}}继续学习{{?}}{{??}}icon-book-shadow"></i>开始学习{{?}}
								{{??}}
									<i class="icon-book-shadow"></i>进入课程
								{{?}}
							{{?}}
							</a>
                        {{?it.type == 'single'}}
                            <div class="pull-right"><strong>{{=item.learnerNum}}</strong>&nbsp;位老师在学习</div>
                        {{?}}
                    </div>
                </div>
            </li>
        {{~}}
{{?it.list.length<30}}
        	<li>
 			{{?it.type == 'single'}}
            	<div class="moreCourse">更多课程，敬请期待</div>
 			{{?}}
			{{?it.type == 'series'}}
            	<div class="moreCourse">更多系列，敬请期待</div>
 			{{?}}
        	</li>
		{{?}}
</script>

<script id="courseCategoryTemplate" type="text/x-dot-template">
    <li class="active"><a href="javascript:void(0)" data-id="all">全部课程</a></li>
	{{~it.list :item:index}}
	<li ><a href="javascript:void(0)" data-id="{{=item.courseCategoryId}}">{{=item.courseCategoryName}}</a></li>
	{{~}}
</script>

<script id="userTemplate" type="text/x-dot-template">
            <div class="clearfix row1">
            <div class="pull-left media profile">
                <div class="pull-left">
<img src="{{?it.img.indexOf('http')>-1}}{{=it.img}}{{??}}${ctx}/{{=it.img}}{{?}}" class="media-object" alt="头像"/>
</div>
                <div class="media-body">
                    <div class="media-heading">
                        {{=it.name}} 
						{{?it.sex=='M'}}
							<i class="icon-male"></i></h5>
						{{??}}
							<i class="icon-female"></i></h5>
						{{?}}
						{{?it.isme==true}}
							 <a class="icon-circle-bg" href="${ctx}/register/updateTeacher"><i class="icon-pencil-mini"></i></a>
                       		 <a class="icon-circle-bg" href="#"><i class="icon-cloth"></i></a>
						{{?}}
                    </div>
                    <p class="muted">
                        机构 {{=it.org}}  <br/>
                        部门  {{=it.dep}} <br/>
                        电话  {{=it.tel}} <br/>
                        生日  {{=it.bird}} <br/>
                        血型  {{=it.bool}}
                    </p>
                </div>
            </div>
            <div class="pull-right box1">
                <div class="clearfix">
                    <div class="pull-left">
                        完成学习 <strong class="num">{{=it.finishSum}}</strong>个课程
                    </div>
                    <div class="pull-right">
                        正在学习 <strong class="num">{{=it.unfinishSum}}</strong>个课程
                    </div>
                </div>
                <div class="well">
                    <i class="icon-shyhl"></i>
                    <a href="/ntp/register/updateTeacher" class="txt">
{{?it.selfIntroduction==""}}这家伙很懒，也不好好介绍一下自己~ :-({{??}}
							{{?it.selfIntroduction.length>20}}
								{{=it.selfIntroduction.substring(0,20)}}...
							{{??}}
								{{=it.selfIntroduction}}
							{{?}}
{{?}}
					
					</a>
                    <i class="icon-shyhr"></i>
                </div>
            </div>
        </div>
</script>
    <script src="${ctx}/resources/js/doT.min.js"></script>
</head>

<body>
<section class="container">
	<section class="mt20 section" id="userDiv">

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

</section>

<script type="text/javascript">	
	var thumbnailsFn = doT.template(document.getElementById("thumbnailsTemplate").text);
	var courseCategoryFn = doT.template(document.getElementById("courseCategoryTemplate").text);
	var userFn = doT.template(document.getElementById("userTemplate").text);

	//初始化用户
	initUser();
	//初始化课程分类
	initCourseCategory();
	//初始化课程
	initCouese("all",1);
	//初始化系列
	initSeries(1);
	
    $("#loadMoreSeries").click(function(e){//点击加载更多
    	 e.preventDefault();
         var pageNo = $("#list-series li").length/30+1;
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
        var pageNo = $("#list-course li").length/30+1;
        var type = $("#navCourse .active a").attr("data-id");
        $.ajax({
    		url : "${ctx}/ajax/course/getMyCoursesIndexInfo",
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
    
    function initCouese(type,pageNo){
    	$.ajax({
    		url : "${ctx}/ajax/course/getMyCoursesIndexInfo",
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
    
    function initUser(){
    	$.ajax({
    		url : "${ctx}/ajax/course/getUserCourseInfo",
    		async : false,
    		dataType : 'json',
    		data:{
    			userId:"${userId}"
    		},
    		success : function(result) {
    			$("#userDiv").html(userFn(result));
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
</script>
</body>
</html>
