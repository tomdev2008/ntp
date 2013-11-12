<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<!--[if lt IE 7]>      <html class="lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class=""> <!--<![endif]-->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新东方在线教师备课平台</title>
<link href="${ctx}/resources/theme/default/css/global.css" rel="stylesheet" type="text/css">
<link href="${ctx}/resources/theme/default/css/layout.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${ctx}/resources/uploadify/uploadify.css"/>
<style type="text/css">
.uploadify-button {
    background-color:rgb(67,145,187);
	background-image: -webkit-gradient(
		linear,
		left bottom,
		left top,
		color-stop(0, rgb(67,145,187)),
		color-stop(1, rgb(67,145,187))
	);
	max-width:70px;
	max-height:30px;
	border-radius: 1px;
	border: 0px;
	font: bold 12px Arial, Helvetica, sans-serif;
	display: block;
	text-align: center;
	text-shadow: 0 0px 0 rgba(0,0,0,0.25);
    
}
.uploadify:hover .uploadify-button {
    background-color:rgb(67,145,187);
	background-image: -webkit-gradient(
		linear,
		left bottom,
		left top,
		color-stop(0, rgb(67,145,187)),
		color-stop(1, rgb(67,145,187))
	);
}
.uploadify-queue-item {
	background-color: #FFFFFF;
	-webkit-border-radius: 3px;
	-moz-border-radius: 3px;
	border-radius: 3px;
	font: 11px Verdana, Geneva, sans-serif;
	margin-top: 1px;
	max-width: 1000px;
	padding: 5px;
}
.uploadify-progress {
	background-color: #E5E5E5;
	margin-top: 10px;
	width: 100%;
}
.uploadify-progress-bar {
	background-color: rgb(67,145,187);
	height: 27px;
	width: 1px;
}
</style>
<!--[if lt IE 9]>
<script src="js/html5.js"></script>
<![endif]-->


    <!--页面左侧模板-->
    <script id="pageLeftTemplate" type="text/x-dot-template">
            <div id="sidebar" class="sidebar" >
                {{#def.sidenav:it.sidenav}}
                <div class="bdbt2 leftBox mt20">
                    <a href="#"><i class="icon-disc-lg-bg"><i class="icon-medal"></i></i>
                        结业证书</a>
                </div>
            </div>
            {{##def.sidenav:param:
            <ul class="bdbt2 sidenav" id="sidenav">
                {{ for(var i=0; i < param.chapter.length+param.lecture.length; i++){ }}
                    {{~param.chapter :chapter:index}}
                        {{?chapter.index == i}}
                            <li class="nav-hd">
                                <span class="dt">章<b class="icon-circle-white-large">{{=chapter.num}}</b></span>
                                <span class="name">{{=chapter.name}}</span>
                            </li>
                        {{?}}
                    {{~}}
                    {{~param.lecture :lecture:index}}
                        {{?lecture.index == i}}
                           
                            <li{{?lecture.id == param.currentId}} class="active"{{?}}>
                                <a {{?lecture.status != 'untreated'||param.isOrder==false}}href="#" {{?}}data-fdid="{{=lecture.id}}" data-type="{{=lecture.baseType}}" data-toggle="popover" data-content="{{=lecture.intro || ''}}" title="{{=lecture.name || ''}}">
                                    <span class="dt">节{{=lecture.num}} <b class="icon-circle-progress">
                                        {{?lecture.status != 'untreated'}}<i class="icon-progress{{?lecture.status == 'doing'}} half{{?}}"></i>{{?}}
                                    </b></span>
                                    <span class="name"><i class="icon-{{=lecture.type}}"></i>
                                    {{=lecture.name || ''}}</span>
                                </a>
                            </li>
                        {{?}}
                    {{~}}
                {{ } }}
            </ul>
            #}}
    </script>

    <!--页面右侧模板-->
    <script id="pageRightTemplate" type="text/x-dot-template">
            {{#def.pageHeader}}
            <div class="page-body">
                {{#def.pageIntro}}
                {{?it.type == 'exam' || it.type == 'task'}}
                    {{#def.examContent}}
                {{??it.type == 'video'}}
                    {{#def.videoContent}}
                {{?}}
            </div>
    </script>



    <!--页面主内容区header-->
    <script id="pageRightHeaderTemplate" type="x-dot-template">
        <div class="page-header" id="pageHeader" data-spy="affix" data-offset-top="10">
                <div class="hd clearfix">
                <a class="btn" href="#button" id="prevLecture">
                <i class="icon-chevron-left"></i>
                <span>上一节</span>
                </a>
                <h1>{{=it.courseName}}   节{{=it.num}} {{=it.lectureName}}
                <span class="labelPass{{?it.status != "pass"}} disabled{{?}}"{{?it.isOptional}} id="btnOptionalLecture"{{?}}>
                <b class="caret"></b>
                <span class="iconWrap"><i class="icon-right"></i></span>
                <span class="tit">学习通过</span>
                </span>
                </h1>
                <a class="btn disabled" href="#button" id="nextLecture">
                        <i class="icon-chevron-right"></i>
                        <span>下一节</span>
                </a>
                </div>
                <div class="bd" id="headToolsBar">

                </div>
        </div>
    </script>

    <!--页面主内容区简介-->
    <script id="pageRightIntroTemplate" type="x-dot-template">
        <ul class="listIntro bdbt2">
            <li>
                <div class="line">
                    <div class="line">
                        <span class="label-intro" >学习任务</span>
                        <small>建议您认真完成所有{{?it.type=='exam'}}题目后提交试卷{{??it.type=='video'}}视频后提交{{??it.type=='task'}}作业后提交作业包{{??it.type=='doc'}}文档后提交{{??it.type=='ppt'}}幻灯片后提交{{?}}： </small></div>
                </div>
            </li>
            <li>
                <div class="line">
                    <div class="line">{{=it.lectureIntro || ''}}</div>
                </div>
            </li>
            <li>
                <div class="line">
                    <div class="line">
                        本{{?it.type=='exam'}}测试{{??it.type=='video'}}视频{{??it.type=='task'}}作业包{{??it.type=='doc'}}文档{{??it.type=='ppt'}}幻灯片{{?}}环节为
                        <b class="text-warning">{{?it.isOptional}}选{{??}}必{{?}}修</b> 环节，
                        您必须通过全部{{?it.type=='exam'}}试卷{{??it.type=='video'}}视频{{??it.type=='task'}}作业包{{??it.type=='doc'}}文档{{??it.type=='ppt'}}幻灯片{{?}}，
                        才可以进入下一关继续学习。</div>
                </div>
            </li>
        </ul>
    </script>

    <!--页面主内容区测试类内容-->
    <script id="pageRightExamTemplate" type="x-dot-template">
        <ul class="unstyled listExamPaper" id="listExamPaper">
            {{~it.listExamPaper :paper:index}}
                <li class="accordion-group">
                    <a class="titBar" data-toggle="collapse" data-parent="#listExamPaper" href="#examPaper{{=index+1}}">
                        <h2>{{?it.type == 'exam'}}试卷{{??it.type == 'task'}}作业包{{?}}{{=index+1}}. {{=paper.name}}</h2>
                        <p class="muted">共计{{=paper.examCount}}{{?it.type=='exam'}}题{{??it.type=='task'}}个作业{{?}}，满分{{=paper.fullScore}}分，建议{{?it.type=='exam'}}答题{{??it.type=='task'}}完成{{?}}时间为{{=paper.examPaperTime}}分钟。</p>
                        <span class="icon-state-bg{{?paper.examPaperStatus == 'fail'}} error">未通过
                        {{??paper.examPaperStatus == 'pass'}} success">通过
                        {{??paper.examPaperStatus == 'finish'}} info">答完
                        {{??paper.examPaperStatus == 'unfinish'}}">待答{{?}}</span>
                    </a>
                    <div id="examPaper{{=index+1}}" data-fdid="{{=paper.id}}" class="accordion-body collapse">

                    </div>
                </li>
            {{~}}


        </ul>
    </script>

    <!--试卷详情模板-->
    <script id="examPaperDetailTemplate" type="x-dot-template">
        <div class="accordion-inner">
                <div class="hd">
                <h2><span class="icon-state-bg{{?it.examPaperStatus == 'fail'}} error">未通过
                        {{??it.examPaperStatus == 'pass'}} success">通过
                        {{??it.examPaperStatus == 'finish'}} info">答完
                        {{??it.examPaperStatus == 'unfinish'}}">待答{{?}}</span> {{?it.type=='exam'}}试卷{{??it.type=='task'}}作业包{{?}}{{=it.num}} {{=it.name}} 共计 <span class="total">{{=it.examCount}}</span>{{?it.type=='exam'}}题{{??it.type=='task'}}个作业{{?}}，满分{{=it.fullScore}}分，建议{{?it.type=='exam'}}答题{{??it.type=='task'}}完成{{?}}时间为{{=it.examPaperTime}}分钟。</h2>
        <p class="muted">{{=it.examPaperIntro}}</p>
        <a class="btn btn-link" data-toggle="collapse" data-parent="#listExamPaper" href="#examPaper{{=it.num}}">收起<b class="caret"></b></a>
        </div>
        <form action="{{=it.action || '#'}}" post="post" id="formExam">
            <input name="fdid" value="{{=it.id}}" type="hidden" />
			<input name="bamId" value="{{=it.bamId}}" type="hidden" />
			<input name="catalogId" value="{{=it.catalogId}}" type="hidden" />
			<input name="fdMtype" value="{{=it.fdMtype}}" type="hidden" />
            {{?it.type=='exam'}}
                <div class="bd">
                <dl class="listExam">
                {{~it.listExam :exam1:index2}}
                    {{~it.listExam :exam:index}}
                    {{?index2 == exam.index}}
                        <dt class="examStem" id="examStem{{=index2+1}}">
                            {{=index2+1}}. {{=exam.examStem}} （{{=exam.examScore}}分）
                        </dt>
                        <dd>
                            <ul class="attachList unstyled">
                                {{~exam.listAttachment :att1:index1}}
                                {{~exam.listAttachment :att:index}}
                                {{?index1 == att.index}}
                                    <li><a href="{{=att.url}}"><i class="icon-paperClip"></i>{{=att.name}}</a></li>
                                {{?}}
                                {{~}}
                                {{~}}
                            </ul>
                            {{~exam.listExamAnswer :ans1:index1}}
                            {{~exam.listExamAnswer :ans:index}}
                            {{?index1 == ans.index}}
                                <label class="{{?exam.examType == 'single'}}radio{{??}}checkbox{{?}}" >
                                    <input type="{{?exam.examType == 'single'}}radio{{??}}checkbox{{?}}" {{?ans.isChecked}}checked{{?}} value="{{=exam.id}}:{{=ans.id}}" name="examAnswer" />
                                    {{=ans.name}}
                                </label>
                            {{?}}
                            {{~}}
                            {{~}}
                        </dd>
                    {{?}}
                    {{~}}
                {{~}}
                </dl>
                </div>
            {{??it.type=='task'}}
                {{~it.listExam :task1:index2}}
                {{~it.listExam :task}}
                {{?index2 == task.index}}
                    <div class="bd">
                        <div class="pd20">
                            <div id="examStem{{=index2+1}}">{{=index2+1}}. {{=task.examName}}（{{=task.examScore}}分）</div>
                            <div>{{=task.examStem}}</div>
                            <ul class="attachList unstyled">
                                {{~task.listAttachment :att1:index1}}
                                {{~task.listAttachment :att}}
                                {{?index1 == att.fdOrder}}
                                <li><a href="${ctx}/common/file/download/{{=att.fdId}}"><i class="icon-paperClip"></i>{{=att.fdFileName}}</a></li>
                                {{?}}
                                {{~}}
                                {{~}}
                            </ul>
                        </div>
                        <div class="bdt1 pd20">
                            {{?task.examType == 'uploadWork'}}
                                <label>上传作业（建议小于2G）</label>
                                <div class="control-upload">

                                  <div class="upload-fileName"><span id="attName"></span><i class="icon-paperClip"></i></div>
 				    			  <div id="taskAttSeq" style="height:20px;width:637px;display:block;"> </div>
					              <div style="margin-left:637px;margin-top: 8px;height:40px;width:600px;display:block;">
						            <button id="answerAtt" class="btn btn-primary btn-large" type="button" >上传</button>
					              </div>
					              <input type="hidden" value="" name="attach_{{=task.id}}" id="answerAttId">

                                </div>
                                <ul class="attachList unstyled" id="listTaskAttachment">
                                    {{~task.listTaskAttachment :att2}}
                                        <li id="attach{{=att2.id}}">
                                            <a href="${ctx}/common/file/download/{{=att2.fdId}}">
												<i class="icon-paperClip"></i>{{=att2.fdFileName}}</a>
                                            <a href="#" class="icon-remove-blue"></a>
                                        </li>
                                    {{~}}
                                </ul>
                            {{??task.examType == 'onlineAnswer'}}
                                {{?task.answer}}
                                    {{=task.answer}}
                                {{?}}
                                {{?task.status != 'success'}}
                                    <label>答题</label>
                                    <textarea name="answer{{=task.id}}" required class="input-block-level" placeholder="请必务填写" rows="4"></textarea>
                                {{?}}
                            {{?}}
                        </div>
                        {{?task.status == 'success' || task.status == 'error'}}
                            <div class="bdt1 pd20">
                                <div class="media teacherRating">
                                    <div class="pull-left">
                                        <a href="#"><img class="media-object" src="{{=task.teacherRating.teacher.imgUrl}}" alt="指导老师"/></a>
                                        <h3>指导老师</h3>
                                    </div>
                                    <div class="media-body">
                                        <div class="media-heading"><label class="label {{?task.status == 'success'}}label-success">通过{{??}}label-important">未通过{{?}}</label>｜ 满分 {{=task.examScore}} 分  ｜ 成绩 {{=task.teacherRating.score}} 分</div>
                                        <p>{{=task.teacherRating.comment || ''}}</p>
                                    </div>
                                </div>
                            </div>
                        {{?}}
                    </div>
                {{?}}
                {{~}}
                {{~}}
            {{?}}
        <div class="ft">
                <button class="btn btn-primary btn-large" type="submit">提交答案</button>
        <button class="btn btn-link" type="button" data-toggle="collapse" data-parent="#listExamPaper" data-target="#examPaper{{=it.num}}">收起<b class="caret"></b></button>
        </div>
        </form>

        </div>
    </script>

    <!--试卷状态栏模板-->
    <script id="examPaperStatusBarTemplate" type="x-dot-template">
        <div class="examPaperStat">
            <div class="dt">{{?it.type == 'exam'}}试卷{{??it.type == 'task'}}作业包{{?}}{{=it.num}}</div>
            你已完成<span class="succ">{{=it.successCount}}</span>{{?it.type == 'exam'}}道题目{{??it.type == 'task'}}个作业{{?}}，
            共计<span class="total">{{=it.examCount}}</span>{{?it.type == 'exam'}}题{{??it.type == 'task'}}个作业{{?}}
            <a href="#navExams" data-toggle="collapse" class="showList"><span class="txtShow">展开</span><span class="txtHide">收起</span>{{?it.type == 'exam'}}试题{{??it.type == 'task'}}作业{{?}}列表<b class="caret"></b></a>
        </div>
        <div id="navExams" class="collapse in">
            <div class="collapse-inner">
                {{~it.listExam :exam1:index1}}
                {{~it.listExam :exam:index}}
                {{?index1 == exam.index}}
                    <a class="num{{?exam.status != 'null'}} active"{{??}}" title="待答"{{?}} {{?exam.status == 'error'}}title="答错"{{??exam.status == 'success'}}title="答对"{{??exam.status == 'finish'}}title="答过"{{?}} href="#examStem{{=index1+1}}">
                    {{=index1+1}}
                    <i class="icon-circle-{{?exam.status == 'error'}}error{{??exam.status == 'success'}}success{{?}}"></i></a>
                {{?}}
                {{~}}
                {{~}}
            </div>
        </div>
    </script>

    <script src="${ctx}/resources/js/doT.min.js"></script>
</head>

<body>

<section class="container pr">
    <section class="clearfix" >
        <section class="col-left" id="sideBar">
        </section>
        <section class="col-right" id="mainContent">
        </section>
    </section>
</section>

<script type="text/javascript" src="${ctx}/resources/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/messages_zh.js"></script>
<script type="text/javascript" src="${ctx}/resources/uploadify/jquery.uploadify-3.1.min.js?id=1211"></script>
<script type="text/javascript">

    $(function(){
        //页面左侧模板函数
        var pageLeftBarFn = doT.template(document.getElementById("pageLeftTemplate").text);
        
        //页面右侧模板函数
        var rightContentFn = doT.template(document.getElementById("pageRightTemplate").text,undefined,{
            pageHeader: document.getElementById("pageRightHeaderTemplate").text,
            pageIntro: document.getElementById("pageRightIntroTemplate").text,
            examContent: document.getElementById("pageRightExamTemplate").text
        });

        /*试卷详情模板函数*/
        var examPaperDetailFn = doT.template(document.getElementById("examPaperDetailTemplate").text);

        /*试卷状态栏模板函数*/
        var examPaperStatusBarFn = doT.template(document.getElementById("examPaperStatusBarTemplate").text);

				//课程进程Id
        var bamId = "${param.bamId}";
        
        //课程进程中的节Id
        var catalogId = "${param.catalogId}";
        
        //课程进行中节的内容类型
        var fdMtype = "${param.fdMtype}";
      //课程是否有序		
        var isOrder ="${param.isOrder}";
        
		var leftData = {};
				//ajax获取左侧章节展示树
		    $.ajax({
					  url: "${ctx}/ajax/passThrough/getBamCatalogTree",
					  async:false,
					  data:{bamId:bamId},
					  dataType:'json',
					  success: function(rsult){
						  leftData = rsult;
						  leftData.sidenav.currentId = catalogId;
						  leftData.sidenav.isOrder=isOrder;
						  $("#sideBar").html(pageLeftBarFn(leftData));
					  },
				});
				
        //左侧菜单定位
        setTimeout(function(){
            $("#sidebar").affix({
                offset: {
                    bottom: 100
                }
            }).parent().height($("#sidebar").height());
        },100);
				
        $("#sidenav>li>a").popover({
            trigger: "hover"
        })
                .click(function(e){
                    e.preventDefault();
                    if($(this).attr("href")){//已通章节可点
                        loadRightCont($(this).attr("data-fdid"),$(this).attr("data-type"));
                        $(this).parent().addClass("active").siblings().removeClass("active");
                    }
                });

        loadRightCont(catalogId,fdMtype);//默认加载章节 参数：节id

        function loadRightCont(fdid,type){
        	catalogId=fdid;
		    fdMtype=type;
	        $.ajax({
		  			  url: "${ctx}/ajax/passThrough/getCourseContent",
		  			  async:false,
		  			  data:{
		  				  catalogId:fdid,
		  				  bamId:bamId,
		  				  fdMtype:fdMtype
		  			  },
		  			  dataType:'json',
		  			  success: function(result){
		  				$("#mainContent").html(rightContentFn(result));
		  				if(result.type == "exam" || result.type == "task"){
		  					afterLoadExamOrTaskPage(result);
		  	            } else if(result.type == "video"){
		  	              //  afterLoadVideoPage(result);
		  	            //} else if(result.type == "doc"){
		  	            //    afterLoadDocPage(result);
		  	            } 
		  				
		  			  },
	  			});
	        

            //可选章节按钮
            $("#btnOptionalLecture").css("cursor","pointer")
                    .click(function(e){
                        $(this).removeClass("disabled");
                        $("#nextLecture").removeClass("disabled");
                    });
            }

        
        /*测试页加载完成后执行方法*/
        function afterLoadExamOrTaskPage(data){
        	
            var $window = $(window);
            //试卷列表折叠 手风琴事件
            $("#listExamPaper>li>.collapse")
                    .bind("show",function(){
                        var $this = $(this);
                        var tempData = {};
                        $this.prev(".titBar").addClass("hide");
                        $.ajax({
         		  			  url: "${ctx}/ajax/passThrough/getSubInfoByMaterialId",
         		  			  async:false,
         		  			  data:{
         		  				materialId: $this.attr("data-fdid"),
         		  				catalogId:catalogId,
         		  				bamId:bamId,
         		  				sourceType:fdMtype,
         		  			  },
         		  			  dataType:'json',
         		  			  success: function(result){
         		  				tempData=result;
         		  			  }
         	  			}); 
                        tempData.type = data.type;
                        tempData.num = $this.parent().index() + 1;
                        tempData.examCount = tempData.listExam.length;
                        var count = 0;
                        for( var j in tempData.listExam){
                            if(tempData.listExam[j].status == "success"){
                                count++;
                            }
                        }
                        tempData.successCount = count;

                        $("#headToolsBar").html(examPaperStatusBarFn(tempData));
                        
                        //试题列表序号控制
                        $("#navExams>.collapse-inner>.num").click(function(e){
                            e.preventDefault();
                            var $this = $(this);
                            var id = $this.attr("href");
                            $window.scrollTop($(id).offset().top - $("#pageHeader").height() - 60);
                        })
                                .tooltip({
                                    placement: "bottom"
                                });

                        tempData.action = "submitExamOrTask";
                        tempData.bamId=bamId ;
                        tempData.catalogId=catalogId ;
                        tempData.fdMtype=fdMtype ;
                        $this.html(examPaperDetailFn(tempData));

                        $("#listTaskAttachment>li>.icon-remove-blue").click(function(e){
                            e.preventDefault();
                            $(this).parent().remove();
                        })
                        

                    })
                    .bind("shown",function(){
                        var $this = $(this);
                        var pos = $this.parent("li").offset();
                        var sTop = 0;
                        var $pageHead = $("#pageHeader");
                        if($pageHead.hasClass("affix")){
                            sTop = pos.top - 60 - $pageHead.height();
                        } else{
                            sTop = pos.top - 60 - $pageHead.height() - $pageHead.children(".hd").height() -$("#headToolsBar").height();
                        }
                        $window.scrollTop(sTop);
                       
                        jQuery("#answerAtt").uploadify({
                            'height' : 27,
                            'width' : 80,
                            'multi' : false,// 是否可上传多个文件
                            'simUploadLimit' : 1,
                            'swf' : '${ctx}/resources/uploadify/uploadify.swf',
                            'buttonText' : '上 传',
                            'uploader' : '${ctx}/common/file/o_upload',
                            'auto' : true,// 选中后自动上传文件
                            'queueID': 'taskAttSeq',// 文件队列div
                            'fileSizeLimit':2097152,// 限制文件大小为2G
                            'queueSizeLimit':1,
                            'onUploadStart' : function (file) {
                                jQuery("#answerAtt").uploadify("settings", "formData");
                            },
                            'onUploadSuccess' : function (file, datas, Response) {
                                if (Response) {
                                    var objvalue = eval("(" + datas + ")");
                                    //answerAttId
                                    jQuery("#${answerAttId}").val(file.name+",");
                                    jQuery("#attName").html(objvalue.fileName);
                                    var html = "<li id='attach'><a><i class='icon-paperClip'></i>"+objvalue.fileName+"<a href='#' class='icon-remove-blue'></a></li>";
                                     $("#listTaskAttachment").append(html);
                                }
                            },
                            'onSelect':function(file){
                            	// 选择新文件时,先清文件列表,因为此处是课程封页,所以只需要一个图片附件
                            	var queuedFile = {};
                    			for (var n in this.queueData.files) {
                    					queuedFile = this.queueData.files[n];
                    					if(queuedFile.id!=file.id){
                    						delete this.queueData.files[queuedFile.id]
                    						$('#' + queuedFile.id).fadeOut(0, function() {
                    							$(this).remove();
                    						});
                    					}
                    				}
                            },
                            'removeCompleted':false // 进度条不消失
                        });
		  				
		  				
                    })
                    .bind("hide",function(){
                        $(this).empty().prev(".titBar").removeClass("hide");
                        $("#headToolsBar").empty();
                    });
        }
    });
</script>
</body>
</html>
