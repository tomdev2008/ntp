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
    <!--心情相关模板-->
    <script id="moodTemplate" type="text/x-dot-template">
        {{?it.list}}
            {{~it.list :item}}
                {{#def.dayMood:item}}
            {{~}}
        {{??}}
            {{#def.item:it.item}}
        {{?}}
        {{##def.dayMood:param:
            <dt>
            <div class="timeBox">
                <span class="date">{{=param.date}}</span>
                <b class="caret"></b>
            </div>
            <i class="icon-disc-blue"></i>
            </dt>
            <dd>
                {{~param.items :item}}
                    {{#def.item:item}}
                {{~}}
            </dd>
        #}}
        {{##def.item:param:
            <div class="item" data-fdid="{{=param.id}}">
                <div class="mood">{{=param.mood}}</div>
                <div class="toolbar clearfix">
                    <div class="pull-left">
                        <div class="time"><i class="icon-time"></i>{{=param.time}}</div>
                    </div>

                    <div class="pull-right">
                        <div class="btn-ctrl">
                            <a href="#" class="praise{{?param.praise.did}} active{{?}}" title="{{?param.praise.did}}您已赞过{{??}}点击赞{{?}}"><i class="icon-thumbs-up"></i><span class="num">{{=param.praise.count}}</span></a>
                            <a href="#" class="weak{{?param.weak.did}} active{{?}}" title="{{?param.weak.did}}您已踩过{{??}}点击踩{{?}}"><i class="icon-thumbs-down"></i><span class="num">{{=param.weak.count}}</span></a>
                            <a href="#" class="comment" title="点击评论"><i class="icon-dialog"></i><span class="num">{{=param.comment.count}}</span></a>
                        </div>
                    </div>
                </div>
                <div id="commentBox{{=param.id}}" class="commentBox collapse in">
                    {{?param.comment.count!=0}}
                        <div class="collapse-inner">
                            <a href="#commentBox{{=param.id}}" class="comment-toggle" data-toggle="collapse" title="点击收起评论">
                                <b class="caret"></b>
                                <span class="txt">收起评论</span>
                            </a>
                            {{~param.comment.list :item}}
                                {{#def.moodComment}}
                            {{~}}
                        </div>
                    {{?}}
                </div>
            </div>
        #}}
    </script>

    <!--评论心情表单模板-->
    <script id="formCommentTemplate" type="x-dot-template">
        <div class="formBox">
                <form action="#">
                    <textarea class="input-block-level" name="comment" rows="2" required></textarea>
                    <div class="formActions">
                        <button class="btn btn-primary" type="submit">评论</button>
                        <button class="btn btn-cancel" type="button">取消</button>
                    </div>
                </form>
        </div>
    </script>

    <!--心情的评论模板-->
    <script id="moodCommentTemplate" type="x-dot-template">
        {{ if(!item)var item=it; }}
        <div class="item2 media" >
            <a class="pull-left" href="${ctx}/{{!item.issuer.link}}">
<img src="{{?item.issuer.imgUrl.indexOf('http')>-1}}{{=item.issuer.imgUrl}}{{??}}${ctx}/{{=item.issuer.imgUrl}}{{?}}" class="media-object" alt="头像" /></a>
</a>
            <div class="media-body">
                <div class="media-heading">
                    {{=item.issuer.name}}({{=item.issuer.mail}}) &nbsp;&nbsp;来自 {{=item.issuer.org}}
                    <span class="time"><i class="icon-time"></i>{{=item.time}}</span>
                </div>
                <p>{{=item.comment}}</p>

            </div>
        </div>
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
        QQ  {{=it.qq}} <br/>
                        血型  {{=it.bool}}
                    </p>
                </div>
            </div>
            <div class="pull-right box1">
                <div class="mainCourse">
                    <img src="{{?it.courseImg!=""}}${ctx}/common/file/image/{{=it.courseImg}}{{??}}${ctx }/resources/images/temp-newClass.jpg{{?}}" alt="">
                    <a href="##" class="courseLink">
                        <h1>{{=it.courseName}}</h1>
                        <p class="sub">{{=it.courseAuther}}</p>
                    </a>
                    <a class="myAll" href="${ctx}/course/courseIndex">我的全部课程</a>
                </div>
                <div class="well">
                    <i class="icon-shyhl"></i>
                    <span class="txt">
{{?it.selfIntroduction==""}}这家伙很懒，也不好好介绍一下自己~ :-({{??}}
{{=it.selfIntroduction}}
{{?}}</span>
                    <i class="icon-shyhr"></i>
                </div>
            </div>
        </div>
</script>
<script id="activeTemplate" type="text/x-dot-template">
<dl class="dl-horizontal">
                        <dt>最近登录</dt>
                        <dd class="text-info">{{=it.lastTime}}</dd>
                        <dt>在线统计</dt>
                        <dd class="text-info">{{=it.onlineDay}}天</dd>
                        <dt>备课进度</dt>
                        <dd class="text-info">{{=it.currLecture}}</dd>
                        <dt>心路历程</dt>
                        <dd class="text-info">{{=it.messageCount}}条记录</dd>
                    </dl>
</script>
<script id="scheduleTemplate" type="text/x-dot-template">
                <div class="progress">
                    <div class="bar" style="width: {{=it.width}};"></div>
                </div>
                <div class="statusBar">
                    我共完成 {{=it.sums}} 个<span class="text-info">备课环节</span>，下一节：<span class="text-info">{{=it.nextCatalog}}</span>
                </div>
</script>
    <script src="${ctx}/resources/js/doT.min.js"></script>
</head>
<body>
<section class="container">
	<section class="mt20 section" id="userDiv">

	</section>

    <section class="mt20 clearfix" id="scheduleSec" >
        <div class="pull-left section">
            <div class="course-progress" id="scheduleDiv">

            </div>
        </div>
        <div class="pull-right section">
            <div class="box-nextBtn">
                <a class="icon-circle-lg-bg" href="${ctx}/passThrough/getCourseHome/${courseId}"><i class="icon-chevron-right"></i></a>
            </div>
        </div>
    </section>

    <section class="mt20 clearfix">
        <section class="pull-left w760" id="feelingDiv">
            <div class="section box-pd20">
                <form id="formAddMood" action="##">
                	<c:if test="${isMe==true}">
                    <textarea name="field-mood" required id="field-mood" class="input-block-level textarea"  rows="3"></textarea>
                    <div class="clearfix">
                        <button type="submit" class="btn btn-primary pull-right">写备课心情</button>
                    </div>
                    </c:if>
                </form>
                    <dl class="list-mood" id="listMood">
                    </dl>
            </div>
        </section>
        <section class="pull-right w225">
            <div class="section statistical">
                <div class="hd">
                    <h5>活跃指数</h5>
                </div>
                <div class="bd" id="activeDiv">
                    
                </div>
            </div>
<!--             <div class="section mt20 list-face">
                <div class="hd">
                    <h5>最近访客</h5>
                </div>
                <div class="bd">
                    <ul class="thumbnails">
                        <li><a href="#" class="thumbnail"><img src="images/temp-face36.jpg" alt=""></a>
                            <h6>韩梅梅</h6>
                        </li>
                        <li><a href="#" class="thumbnail"><img src="images/temp-face36.jpg" alt=""></a>
                            <h6>韩梅梅</h6>
                        </li>
                        <li><a href="#" class="thumbnail"><img src="images/temp-face36.jpg" alt=""></a>
                            <h6>韩梅梅</h6>
                        </li>
                        <li><a href="#" class="thumbnail"><img src="images/temp-face36.jpg" alt=""></a>
                            <h6>韩梅梅</h6>
                        </li>
                        <li><a href="#" class="thumbnail"><img src="images/temp-face36.jpg" alt=""></a>
                            <h6>韩梅梅</h6>
                        </li>
                        <li><a href="#" class="thumbnail"><img src="images/temp-face36.jpg" alt=""></a>
                            <h6>韩梅梅</h6>
                        </li>
                        <li><a href="#" class="thumbnail"><img src="images/temp-face36.jpg" alt=""></a>
                            <h6>韩梅梅</h6>
                        </li>
                        <li><a href="#" class="thumbnail"><img src="images/temp-face36.jpg" alt=""></a>
                            <h6>韩梅梅</h6>
                        </li>
                        <li><a href="#" class="thumbnail"><img src="images/temp-face36.jpg" alt=""></a>
                            <h6>韩梅梅</h6>
                        </li>
                        <li><a href="#" class="thumbnail"><img src="images/temp-face36.jpg" alt=""></a>
                            <h6>韩梅梅</h6>
                        </li>
                        <li><a href="#" class="thumbnail"><img src="images/temp-face36.jpg" alt=""></a>
                            <h6>韩梅梅</h6>
                        </li>
                        <li><a href="#" class="thumbnail"><img src="images/temp-face36.jpg" alt=""></a>
                            <h6>韩梅梅</h6>
                        </li>
                        <li><a href="#" class="thumbnail"><img src="images/temp-face36.jpg" alt=""></a>
                            <h6>韩梅梅</h6>
                        </li>
                        <li><a href="#" class="thumbnail"><img src="images/temp-face36.jpg" alt=""></a>
                            <h6>韩梅梅</h6>
                        </li>
                        <li><a href="#" class="thumbnail"><img src="images/temp-face36.jpg" alt=""></a>
                            <h6>韩梅梅</h6>
                        </li>
                    </ul>
                    <div class="page-group clearfix">
                        <a href="#" class="btn-prev">上一页</a>
                        <a href="#" class="btn-next">下一页</a>
                    </div>
                </div>
            </div> -->
<!--             <div class="section newClass mt20">
                <div class="hd">
                    <h5>我的课程</h5>
                    <a href="#" class="ab_r">全部</a>
                </div>
                <div class="bd">
                    <div class="list-class">
                        <a href="#">
                            <img src="images/temp-newClass.jpg" alt="">
                            <span class="mask"></span>
                    			<span class="caption">
                                	<h6>集团雅思基础口语新教师</h6>
                                    <span class="text-warning">集团国外考试推广管理中心</span>
                                </span>
                        </a>
                        <a href="#">
                            <img src="images/temp-newClass.jpg" alt="">
                            <span class="mask"></span>
                    			<span class="caption">
                                	<h6>集团雅思基础口语新教师</h6>
                                    <span class="text-warning">集团国外考试推广管理中心</span>
                                </span>
                        </a>
                        <a href="#">
                            <img src="images/temp-newClass.jpg" alt="">
                            <span class="mask"></span>
                    			<span class="caption">
                                	<h6>集团雅思基础口语新教师</h6>
                                    <span class="text-warning">集团国外考试推广管理中心</span>
                                </span>
                        </a>
                        <a href="#">
                            <img src="images/temp-newClass.jpg" alt="">
                            <span class="mask"></span>
                    			<span class="caption">
                                	<h6>集团雅思基础口语新教师</h6>
                                    <span class="text-warning">集团国外考试推广管理中心</span>
                                </span>
                        </a>
                        <a href="#">
                            <img src="images/temp-newClass.jpg" alt="">
                            <span class="mask"></span>
                    			<span class="caption">
                                	<h6>集团雅思基础口语新教师</h6>
                                    <span class="text-warning">集团国外考试推广管理中心</span>
                                </span>
                        </a>
                    </div>
                </div>
            </div> -->
             <!-- 发现课程之最新课程列表 -->
                <c:import url="/WEB-INF/views/passThrough/new_course_list.jsp"></c:import>
        </section>
    </section>
</section>
<script type="text/javascript" src="${ctx}/resources/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/messages_zh.js"></script>
<script src="${ctx}/resources/js/jquery.jalert.js" type="text/javascript"></script>

<script type="text/javascript">	
	var moodFn = doT.template(document.getElementById("moodTemplate").text,undefined,{
        moodComment: document.getElementById("moodCommentTemplate").text
    });
    var formCommentFn = doT.template(document.getElementById("formCommentTemplate").text);
    var moodCommentFn = doT.template(document.getElementById("moodCommentTemplate").text);
    var userFn = doT.template(document.getElementById("userTemplate").text);
    var activeFn = doT.template(document.getElementById("activeTemplate").text);
    var scheduleFn = doT.template(document.getElementById("scheduleTemplate").text);
    
    initUser();
    initmoodData();
    initActive();
    initSchedule();
    $("#listMood").delegate("dd .btn-ctrl>a","click",function(e){
        e.preventDefault();
        var $this = $(this);
        if(!$this.hasClass("active")){
            var tips = "";
            if($this.hasClass("comment")){
                tips = "点击评论";
                var $form = $(formCommentFn());
                var dataRetuen={};
                $this.closest(".toolbar").after($form);
                $form.find("form").validate({
                    submitHandler: function(){
                        /*评论心情*/
						$.ajax({
			        		url : "${ctx}/ajax/message/addFeelingMessagesMessage",
			        		async : false,
			        		dataType : 'json',
			        		data:{
			        			fdContent:$form.find("textarea").val(),
			        			messageId: $this.closest(".item").attr("data-fdid")
			        		},
			        		success : function(result) {
			        			dataRetuen=result;
			        		}
			        	});
						 var $inner = $('<div class="collapse-inner"></div>');
	                        var $num = $this.children(".num");
	                        if($form.next(".commentBox").children(".collapse-inner").length){
	                            $inner = $form.next(".commentBox").children(".collapse-inner");
	                        } else {
	                            $form.next(".commentBox").append($inner);
	                        }
	                        $inner.prepend(moodCommentFn(dataRetuen));
	                        $form.remove();
	                        $this.removeClass("active");
	                        $num.text(parseInt($num.text())+1);
                    }
                });
                $form.find(".btn-cancel").click(function(e){
                    $form.remove();
                    $this.removeClass("active");
                });
            }else {
            	
                if($this.hasClass("weak")){
                	var pushok;
                    tips = "您已踩过";
                    $.ajax({
                		url : "${ctx}/ajax/message/supportOrOpposeMessage",
                		async : false,
                		data:{
                			messageId:$this.closest(".item").attr("data-fdid"),
                			fdType:"02",
                		},
                		success : function(result) {
                			if(result=='"cannot"'){
                  				pushok=false;
                  			}else{
                  				pushok=true;
                  			}
                		}
                	});
                    if(pushok){
                      	var $num = $this.children(".num");
                           $num.text(parseInt($num.text())+1);
                           $this.addClass("active").attr("data-original-title",tips);
                  	}else{
                  		$.fn.jalert2("不能支持和反对自己的评论");
                  	}
                } else if($this.hasClass("praise")){
                	var pushok1=false;
                    tips = "您已赞过";
                    $.ajax({
                		url : "${ctx}/ajax/message/supportOrOpposeMessage",
                		async : false,
                		data:{
                			messageId:$this.closest(".item").attr("data-fdid"),
                			fdType:"01",
                		},
                		success:function(result) {
                			if(result=='"cannot"'){
                  				pushok1=false;
                  			}else{
                  				pushok1=true;
                  			}
                		}
                	});

                    if(pushok1){
                   	 var $num = $this.children(".num");
                        $num.text(parseInt($num.text())+1);
                        $this.addClass("active").attr("data-original-title",tips);
               		}else{
               			$.fn.jalert2("不能支持和反对自己的评论");
               		}
                }
            }
            
        }
    })
        .find("dd .btn-ctrl>a").tooltip()
        .end().find(".commentBox").on("hide",function(){
                var $btn = $(this).find(".comment-toggle").detach();
                $btn.children(".txt").text("展开评论");
                $(this).prev().find(".btn-ctrl").append($btn);
        })
            .on("show",function(){
                var $btn = $(this).prev().find(".comment-toggle").detach();
                $btn.children(".txt").text("收起评论");
                $(this).children().prepend($btn);
            });

    /*发表心情表单*/
    $("#formAddMood").validate({
        submitHandler: function(form){
            var lastDate = $("#listMood>dt:first .date").text();
            var currDate = formatDate(new Date());
            $.ajax({
        		url : "${ctx}/ajax/message/addMessageFeeling",
        		async : false,
        		dataType : 'json',
        		data:{
        			userId:"${userId}",
        			courseId:"${courseId}",
        			fdContent: $("#field-mood").val()
        		},
        		success : function(result) {
        			initmoodData();
        		}
        	});
            $("#field-mood").val("");
        }
    })

    function formatDate(d){
        return ((d.getMonth()+1)<10 ? "0"+(d.getMonth()+1) : d.getMonth()+1) + " " + (d.getDate()<10 ? "0"+ d.getDate() : d.getDate()) + " "
                + d.getFullYear();
    }
    //初始化用户
    function initUser(){
    	$.ajax({
    		url : "${ctx}/ajax/passThrough/getUserCourseInfo",
    		async : false,
    		dataType : 'json',
    		data:{
    			userId:"${userId}",
    			courseId:"${courseId}"
    		},
    		success : function(result) {
    			$("#userDiv").html(userFn(result));
    		}
    	});
    }
    //初始化心情列表 
    function initmoodData(){
    	$.ajax({
    		url : "${ctx}/ajax/passThrough/getMessageFeeling",
    		async : false,
    		dataType : 'json',
    		type: "post",
    		data:{
    			userId:"${userId}",
    			courseId:"${courseId}"
    		},
    		success : function(result) {
    			$("#listMood").html(moodFn(result));
    			if(result.list.length==0){
    				$("#feelingDiv").addClass("hide");
    			}
    		}
    	});
    	$("#listMood dd .btn-ctrl>a").tooltip()
        .end().find(".commentBox").on("hide",function(){
            var $btn = $(this).find(".comment-toggle").detach();
            $btn.children(".txt").text("展开评论");
            $(this).prev().find(".btn-ctrl").append($btn);
    })
        .on("show",function(){
            var $btn = $(this).prev().find(".comment-toggle").detach();
            $btn.children(".txt").text("收起评论");
            $(this).children().prepend($btn);
        });
    }
    
  	//初始化活跃指数
    function initActive(){
    	$.ajax({
    		url : "${ctx}/ajax/passThrough/getCourseFeelingActive",
    		async : false,
    		dataType : 'json',
    		type: "post",
    		data:{
    			userId:"${userId}",
    			courseId:"${courseId}"
    		},
    		success : function(result) {
    			$("#activeDiv").html(activeFn(result));
    		}
    	});
  	}
  	
  //初始化进度条
    function initSchedule(){
	  if("${isMe}"=="true"){
		  $.ajax({
	    		url : "${ctx}/ajax/passThrough/getCourseFeelingSchedule",
	    		async : false,
	    		dataType : 'json',
	    		type: "post",
	    		data:{
	    			userId:"${userId}",
	    			courseId:"${courseId}"
	    		},
	    		success : function(result) {
	    			$("#scheduleDiv").html(scheduleFn(result));
	    		}
	    	}); 
	  }else{
		  $("#scheduleSec").addClass("hide");
	  }
    	
  	}

</script>
</body>
</html>
