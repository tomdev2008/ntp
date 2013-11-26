<%@page import="cn.me.xdf.model.material.MaterialInfo"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
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
<!--[if lt IE 9]>
<script src="js/html5.js"></script>
<![endif]-->


    <!--页面左侧模板-->
    <script id="pageLeftTemplate" type="text/x-dot-template">
            <div id="sidebar" class="sidebar" >
                {{#def.sidenav:it.sidenav}}
                <div class="bdbt2 leftBox mt20" >
                    <a href="javascript:void(0)" id="courseOverBtn"><i class="icon-disc-lg-bg"><i class="icon-medal"></i></i>
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
                                <a {{?lecture.fdStatus=='true'}}href="#" {{?}}data-fdid="{{=lecture.id}}" data-type="{{=lecture.baseType}}" data-toggle="popover" data-content="{{=lecture.intro || ''}}" title="{{=lecture.name || ''}}">
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
            <div class="page-body" id="pageBody">
                {{#def.pageIntro}}
                {{?it.type == 'exam' || it.type == 'task'}}
                    {{#def.examContent}}
                {{?}}
            </div>
    </script>



    <!--页面主内容区header-->
    <script id="pageRightHeaderTemplate" type="x-dot-template">
        <div class="page-header" id="pageHeader" data-offset-top="10">
                <div class="hd clearfix">
             <a class="btn {{?!it.pstatus}} disabled{{?}}" {{?it.pstatus}}href="#"{{?}} id="prevLecture" data-fdid="{{=it.prevc}}" data-type="{{=it.prevBaseType || ''}}">
                <i class="icon-chevron-lg-left"></i>
                <span>上一节</span>
                </a>
                <h1>{{=it.courseName}}   节{{=it.num}} {{=it.lectureName}}
                <span class="labelPass{{?it.status != "pass"}} disabled{{?}}"{{?it.isOptional}} id="btnOptionalLecture"{{?}}>
                <b class="caret"></b>
                <span class="iconWrap"><i class="icon-right"></i></span>
                <span class="tit">学习通过</span>
                </span>
                </h1>
				{{?it.isLast}}
				<a class="btn btn-back" id="nextOver" > <i class="icon-disc-lg-bg"><i class="icon-home icon-white"></i></i> </a>
				{{??}}
				 <a class="btn {{?!it.nstatus}} disabled{{?}}" {{?it.nstatus}}href="#"{{?}}  id="nextLecture" data-fdid="{{=it.nextc}}" data-type="{{=it.nextBaseType || ''}}">
                        <i class="icon-chevron-lg-right"></i>
                        <span>下一节</span>
                </a>
				{{?}}
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
                    <a class="titBar"  examPaperStatus="{{=paper.examPaperStatus}}" {{?it.type == 'task' && paper.examPaperStatus == 'pass'}}{{??}}data-toggle="collapse" data-parent="#listExamPaper" {{?}}href="#examPaper{{=index+1}}">
                        <h2>{{?it.type == 'exam'}}试卷{{??it.type == 'task'}}作业包{{?}}{{=index+1}}. {{=paper.name}}</h2>
                        <p class="muted">共计{{=paper.examCount}}{{?it.type=='exam'}}题{{??it.type=='task'}}个作业{{?}}，满分{{=paper.fullScore}}分，建议{{?it.type=='exam'}}答题{{??it.type=='task'}}完成{{?}}时间为{{=paper.examPaperTime}}{{?it.type=='exam'}}分钟{{??it.type=='task'}}天{{?}}。</p>
                        <span class="icon-state-bg{{?paper.examPaperStatus == 'fail'}} error">未通过
                        {{??paper.examPaperStatus == 'pass'}} success">通过
                        {{??paper.examPaperStatus == 'finish'}} info">答完
                        {{??paper.examPaperStatus == 'unfinish'}}">待答{{?}}</span>
                    </a>
                    <div id="examPaper{{=index+1}}" examPaperStatus="{{=paper.examPaperStatus}}" data-fdid="{{=paper.id}}" class="accordion-body collapse">

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
        <p class="muted">{{=it.examPaperIntro||''}}</p>
        <a class="btn btn-link" data-toggle="collapse" data-parent="#listExamPaper" href="#examPaper{{=it.num}}">收起<b class="caret"></b></a>
        </div>
        <form action="{{=it.action || '#'}}" post="post" id="formExam">
			{{?it.listExam.length==0}}
				该素材已被删除
			{{??}}
            <input name="fdid" value="{{=it.id}}" type="hidden" />
			<input name="bamId" value="{{=it.bamId}}" type="hidden" />
			<input name="catalogId" value="{{=it.catalogId}}" type="hidden" />
			<input name="fdMtype" value="{{=it.fdMtype}}" type="hidden" />
			<input name="startTime" value="{{=it.startTime}}" type="hidden" />
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
                            {{?exam.listAttachment!=null}}
                            <ul class="attachList unstyled">
                                {{~exam.listAttachment :att1:index1}}
                                {{~exam.listAttachment :att:index}}
                                {{?index1 == att.fdOrder}}
                                    <li><a href="${ctx}/common/file/download/{{=att.fdId}}"><i class="icon-paperClip"></i>{{=att.fdFileName}}</a></li>
                                {{?}}
                                {{~}}
                                {{~}}
                            </ul>
                            {{?}}
 							{{?exam.examType == 'completion'}}
                                {{~exam.listExamAnswer :ans:indexno}}
                                <label class="input">
                                    <b class="icon-circle-bg blue">{{=indexno+1}}</b>
                                    <input type="text" required class="input-xxlarge" value="{{=ans}}" exam-id="{{=exam.id}}" name="examAnswer_completion{{=exam.id}}{{=indexno+1}}" putId="examAnswer_completion"/>
									<input type="hidden" value=""  name="examAnswer"/>
                                </label>
                                {{~}}
                            {{??}}
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
							{{?}}
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
                                    <div class="upload-fileName" id="attName"></div>
                   					<div class="pr">
						             <button name="answerAtt" id="{{=task.id}}" class="btn btn-primary btn-large" type="button" >上传</button>
                                    </div>
								</div>
                                <ul class="attachList unstyled" id="listTaskAttachment_{{=task.id}}">
                                    {{~task.listTaskAttachment :att2}}
                                        <li id="attach{{=att2.id}}">
                                            <input type="hidden" value='{{=att2.fdId}}' name='attach_{{=task.id}}' id='answerAttId_{{=task.id}}'>
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
                                    <textarea name="answer_{{=task.id}}" required class="input-block-level" placeholder="请必务填写" rows="4"></textarea>
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
        {{?}}
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

    <!-- 视频、文档等媒体信息模板-->
    <script id="mediaInfoTemplate" type="x-dot-template">
        {{#def.mediaInfo:it.defaultMedia}}
        {{##def.mediaInfo:param:
            <div class="section mt20">
                <div class="mediaWrap bdbt2">
                    <div class="mediaObject">
                        <div id="flashcontent"></div>
                    </div>
                </div>
                <div class="mediaToolbarWrap">
                    <div class="mediaToolbar" id="mediaToolbar" data-fdid="{{=param.id}}">
                        <div class="btn-group">
                            <a id="btnPraise" class="btn btn-link{{?!param.mePraised}} active{{?}}" href="javascript:void(0)" title="{{?param.mePraised}}取消赞{{??}}赞{{?}}" praisedstatus="{{=param.mePraised}}"><i class="icon-heart-blue"></i><span class="num">{{=param.praiseCount || 0 }}</span></a>
                           <a id="btnDownload" title="{{?param.canDownload}}点击{{??}}无权{{?}}下载" canDownload="{{=param.canDownload}}" class="btn btn-link" {{?param.canDownload}}href="javascript:void(0)" data-fdid="{{=param.url}}" {{??}} disabled{{?}}><i class="icon-download-blue"></i><span class="num" id="sdowncount">{{=param.downloadCount || 0 }}</span></a>
                        </div>
                        <span class="playCount">{{?it.type == 'video'}}播放{{??}}阅读{{?}}  <strong class="num">{{=param.readCount || 0 }}</strong>  次</span>
                      <button id="btnDoPass" class="btn btn-success"{{?param.isPass}} disabled{{?}}><i class="icon-right"></i>我学会了</button>
                    </div>
                    {{#def.listMedia}}
                </div>
                <div class="hd">
                    <div class="tit-icon_bg"><i class="icon-video-intro"></i></div>
                    <h5>{{?it.type == 'video'}}视频{{??it.type == 'doc'}}文档{{??it.type == 'ppt'}}幻灯片{{?}}信息</h5>
                    <div class="pos-right" id="ratingTotal">
                        <span>综合评分</span>
                            <span class="rating-all" >
                                {{ for(var i=0; i<5; i++){ }}
                                    <i class="icon-star{{?i < param.rating.average}} active{{?}}"></i>
                                {{ } }}
                             </span>
                        <b class="text-warning">{{=param.rating.average || 0}}</b>
                    </div>
                </div>
                <div class="clearfix mt20">
                    <div class="pull-left video-info">
                        <h5>{{?it.type == 'video'}}视频{{??it.type == 'doc'}}文档{{??it.type == 'ppt'}}幻灯片{{?}}名称  <span class="name" id="mediaName">{{=param.name}}</span></h5>
                        <p class="mediaIntro" id="mediaIntro">
                            {{=param.intro}}
                        </p>
                    </div>
                    <div class="pull-right" id="pullrightInfo">
                        <dl class="info-rating">

                            <dd id="ratingFive">
                                <div class="rating span2">
                                    <i class="icon-star"></i><i class="icon-star"></i><i class="icon-star"></i><i class="icon-star"></i><i class="icon-star"></i>
                                </div>
                                <div class="progress-gray span1">
                                    <div class="bar" style="width: {{=(param.rating.five/param.rating.total)*100}}%;"></div>
                                </div>
                                <span class="fs9">{{=param.rating.five}}</span>
                            </dd>
                            <dd id="ratingFour">
                                <div class="rating span2">
                                    <i class="icon-star"></i><i class="icon-star"></i><i class="icon-star"></i><i class="icon-star"></i>
                                </div>
                                <div class="progress-gray span1">
                                    <div class="bar" style="width: {{=(param.rating.four/param.rating.total)*100}}%;"></div>
                                </div>
                                <span class="fs9">{{=param.rating.four}}</span>
                            </dd>
                            <dd id="ratingThree">
                                <div class="rating span2">
                                    <i class="icon-star"></i><i class="icon-star"></i><i class="icon-star"></i>
                                </div>
                                <div class="progress-gray span1">
                                    <div class="bar" style="width: {{=(param.rating.three/param.rating.total)*100}}%;"></div>
                                </div>
                                <span class="fs9">{{=param.rating.three}}</span>
                            </dd>
                            <dd id="ratingTwo">
                                <div class="rating span2">
                                    <i class="icon-star"></i><i class="icon-star"></i>
                                </div>
                                <div class="progress-gray span1">
                                    <div class="bar" style="width: {{=(param.rating.two/param.rating.total)*100}}%;"></div>
                                </div>
                                <span class="fs9">{{=param.rating.two}}</span>
                            </dd>
                            <dd id="ratingOne">
                                <div class="rating span2">
                                    <i class="icon-star"></i>
                                </div>
                                <div class="progress-gray span1">
                                    <div class="bar" style="width: {{=(param.rating.one/param.rating.total)*100}}%;"></div>
                                </div>
                                <span class="fs9">{{=param.rating.one}}</span>
                            </dd>
                        </dl>
                    </div>
                </div>
            </div>
        #}}
        {{##def.listMedia:
            <ul class="mediaList nav nav-pills" id="listMedia">
                {{~it.listMedia :media:index}}
                <li class="{{?it.defaultMedia.id == media.id}}active{{?}} {{?media.isPass}}pass{{?}}">
                    <a href="{{=media.url}}" data-fdid="{{=media.id}}" title="{{=media.name}}">
                        <i class="icon-circle-success"></i>{{?it.type == 'video'}}视频{{??it.type == 'doc'}}文档{{??it.type == 'ppt'}}幻灯片{{?}}{{=index+1}}
                    </a>
                </li>
                {{~}}
            </ul>
        #}}
    </script>

    <!-- 视频、文档等媒体页面 评论模板-->
    <script id="mediaCommentTemplate" type="x-dot-template">
        <div class="section" id="mediaComment">
            <div class="edit-box">
                    <div class="box-rating">
                        <span>评分</span>
                        <span class="rating-do" id="ratingDo">
                            {{ for(var i=0; i<5; i++ ){ }}
                                <i class="icon-star{{?i<it.meRating}} active{{?}}"></i>
                            {{ } }}
                         </span>
                        <span class="point" id="ratingDoScore"></span>
                    </div>
                <form action="javascript:void(0)" id="formMakeComments" data-fdid="{{=it.mediaComment.id}}" >
                    <textarea  id="textComment" name="textComment" required class="input-block-level" placeholder="有什么想吐槽的吗？随便写两句文评吧~ : )" ></textarea>
                    <div class="comm-rating">
                        <label for="isAnonymity" class="checkbox span1">
                            <input id="isAnonymity" name="isAnonymity" type="checkbox">匿名发表
                        </label>
                        <button class="btn btn-primary" type="submit">发表</button>
                    </div>
                </form>
            </div>
			<div id="commentDiv">
            <div class="hd">
                <div class="tit-icon_bg"><i class="icon-white-info"></i></div>
                <h5>全部评论</h5>
                <div class="pages">
                    <div class="span2">
                        第<span id="pageLine1"> 1 - 10</span> / <span id="pageTotal1"></span> 条
                    </div>
                    <div class="btn-group">
                        <button id="gotoBefore1" class="btn btn-primary"><i class="icon-chevron-left icon-white"></i></button>
                        <button id="gotoNext1" class="btn btn-primary"><i class="icon-chevron-right icon-white"></i></button>
                    </div>
                </div>
            </div>
            <ul class="media-list comment-list" id="listComment">
            </ul>
            <div class="comment-list-bottom clearfix">
                <div class="pages pull-right">
                    <div class="span2" >
                        第<span id="pageLine2"> 1 - 10</span> / <span id="pageTotal2"></span> 条
                    </div>
                    <div class="btn-group">
                        <button id="gotoBefore2" class="btn btn-primary"><i class="icon-chevron-left icon-white"></i></button>
                        <button id="gotoNext2" class="btn btn-primary"><i class="icon-chevron-right icon-white"></i></button>
                    </div>
                </div>
            </div>
			</div>
        </div>
    </script>

    <!--评论列表 模板-->
    <script id="listCommentTemplate" type="x-dot-template">
        {{~it :item:index}}
        <li class="media" data-fdid="{{=item.fdId}}">
			<a href="#" class="pull-left">
				{{?item.isAnonymous}}
				<img class="media-object" src="/JensProject/resources/images/face-placeholder.png" />
				{{??}}
				<img class="media-object" src="{{?item.fdUserURL.indexOf('http')>-1}}{{=item.fdUserURL}}{{??}}${ctx}/{{=item.fdUserURL}}{{?}}" />
				{{?}}
			</a>
            <div class="media-body">
                <div class="media-heading">
                    <span class="name">{{=item.no}}# {{?item.isAnonymous}}匿名{{??}}{{=item.fdUserName}}</span>（<span class="mail">{{=item.fdUserEmail}}</span>）    来自 <span class="org">{{=item.fdUserDept}}</span>{{?}}
                    {{?item.isShowScore}}
                        <div class="rating-view">
                                    <span class="rating-all">
                                        {{ for(var i=0; i<5; i++){ }}
                                            <i class="icon-star{{?i < item.score}} active{{?}}"></i>
                                        {{ } }}
                                    </span>
                            <b class="text-warning">{{=item.score}}</b>
                        </div>
                    {{?}}
                </div>
                <p class="comt-content">
                    {{=item.content}}
                </p>
                <div class="media-footing">
                    <div class="clearfix">
                        <span class="pull-left"><i class="icon-time"></i>{{=item.fdCreateTime}}
                        </span>
                        <div class="pull-right btns-comt">
                            <a href="#" class="btnPraise{{?!item.canSport}} active{{?}}"><i class="icon-thumbs-up"></i><span class="num">{{=item.supportCount}}</span></a>
                            <a href="#" class="btnWeak{{?!item.canOppose}} active{{?}}"><i class="icon-thumbs-down"></i><span class="num">{{=item.opposeCount}}</span></a>
                            <a href="#" class="btnComment"><i class="icon-dialog"></i><span class="num">{{=item.replyCount}}</span></a>
                        </div>
                    </div>
                </div>
            </div>
        </li>
        {{~}}
    </script>

    <!--回复评论表单 模板-->
    <script id="formReplyCommentTemplate" type="x-dot-template">
        <div class="form-reply">
            <form id="formReply">
                    <textarea class="input-block-level" required id="replyComm" name="replyComm" rows="3" >回复 （{{=it.name}}）：</textarea>
                <div class="form-action">
                     <div class="btn-group">
                        <button class="btn btn-primary" type="submit">回复</button>
                        <button class="btn btn-cancel" type="button">取消</button>
                    </div>
                </div>
            </form>
        </div>
    </script>
				<script id="scoreInfo" type="x-dot-template">
 						<dl class="info-rating">
						 	<dd id="ratingFive">
                                <div class="rating span2">
                                    <i class="icon-star"></i><i class="icon-star"></i><i class="icon-star"></i><i class="icon-star"></i><i class="icon-star"></i>
                                </div>
                                <div class="progress-gray span1">
                                    <div class="bar" style="width: {{=(it.fdFiveScoreNum/it.fdScoreNum)*100}}%;"></div>
                                </div>
                                <span class="fs9">{{=it.fdFiveScoreNum}}</span>
                            </dd>
                            <dd id="ratingFour">
                                <div class="rating span2">
                                    <i class="icon-star"></i><i class="icon-star"></i><i class="icon-star"></i><i class="icon-star"></i>
                                </div>
                                <div class="progress-gray span1">
                                    <div class="bar" style="width: {{=(it.fdFourScoreNum/it.fdScoreNum)*100}}%;"></div>
                                </div>
                                <span class="fs9">{{=it.fdFourScoreNum}}</span>
                            </dd>
                            <dd id="ratingThree">
                                <div class="rating span2">
                                    <i class="icon-star"></i><i class="icon-star"></i><i class="icon-star"></i>
                                </div>
                                <div class="progress-gray span1">
                                    <div class="bar" style="width: {{=(it.fdThreeScoreNum/it.fdScoreNum)*100}}%;"></div>
                                </div>
                                <span class="fs9">{{=it.fdThreeScoreNum}}</span>
                            </dd>
                            <dd id="ratingTwo">
                                <div class="rating span2">
                                    <i class="icon-star"></i><i class="icon-star"></i>
                                </div>
                                <div class="progress-gray span1">
                                    <div class="bar" style="width: {{=(it.fdTwoScoreNum/it.fdScoreNum)*100}}%;"></div>
                                </div>
                                <span class="fs9">{{=it.fdTwoScoreNum}}</span>
                            </dd>
                            <dd id="ratingOne">
                                <div class="rating span2">
                                    <i class="icon-star"></i>
                                </div>
                                <div class="progress-gray span1">
                                    <div class="bar" style="width: {{=(it.fdOneScoreNum/it.fdScoreNum)*100}}%;"></div>
                                </div>
                                <span class="fs9">{{=it.fdOneScoreNum}}</span>
                            </dd>
                        </dl>
    		</script>
   <!-- 结业证书页面  -->
   <script id="pageContentTemplate" type="text/x-dot-template">
       <div class="page-header" id="pageHeader" data-offset-top="10">
                <div class="hd clearfix">
             <a class="btn" href="#" id="firstC" >
                <i class="icon-chevron-lg-left"></i>
                <span>第一节</span>
                </a>
                <h1>{{=it.courseName}}
                <span class="labelPass{{?it.status != "pass"}} disabled{{?}}"{{?it.isOptional}} id="btnOptionalLecture"{{?}}>
                </span>
                </h1>
				<a class="btn" href="#" id="lastC" >
                <i class="icon-chevron-lg-right"></i>
                <span>最后节</span>
                </a>
                </div>
                <div class="bd" id="headToolsBar">
                </div>
        </div>
        <section class="page-body" >
            <section class="bdbt2 box-certificate" id="cardId">
                <div class="hd"><h2>新东方认证教师</h2></div>
                <div class="bd">
                    <div class="media">
                        <a class="pull-left" href="{{=it.user.link}}">
								<img src="{{?it.user.imgUrl.indexOf('http')>-1}}{{=it.user.imgUrl}}{{??}}${ctx}/{{=it.user.imgUrl}}{{?}}" class="media-object img-polaroid" alt=""/>
                            	<i class="icon-medal-lg"></i>
                        </a>
                        <div class="media-body">
                            <div class="media-heading"><em>{{=it.user.name}}</em>老师，</div>
                            <p>已于 {{=it.passTime}} 完成《<em>{{=it.courseName}}</em>》，特此认证。
                                This is to certify MR/MS <span class="upper">{{=it.user.enName}}</span> 's successful completion of the New Oriental Teacher Online Training.</p>
                            <p class="muted">颁发者：{{=it.issuer}}</p>
                        </div>
                    </div>
                </div>
                <div class="ft">
                    <button class="btn btn-primary btn-large" type="button" id="downloadCertificate">下载证书</button>
                </div>
            </section>
        </section>
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
<script type="text/javascript" src="${ctx}/resources/uploadify/jquery.uploadify.js?id=1211"></script>
<script src="${ctx}/resources/js/jquery.jalert.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function(){
    	$.Placeholder.init();
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

        /*视频、文档等媒体信息 模板函数*/
        var mediaInfoFn = doT.template(document.getElementById("mediaInfoTemplate").text);
        var rightMaterialContentFn = doT.template(document.getElementById("pageRightTemplate").text,undefined,{
            pageHeader: document.getElementById("pageRightHeaderTemplate").text,
            pageIntro: document.getElementById("pageRightIntroTemplate").text,
            examContent: document.getElementById("mediaInfoTemplate").text
        });
        /*视频、文档等媒体评论 模板函数*/
        var mediaCommentFn = doT.template(document.getElementById("mediaCommentTemplate").text);
        /*视频、文档等媒体评论列表 模板函数*/
        var listCommentFn = doT.template(document.getElementById("listCommentTemplate").text);
        /*视频、文档等媒体回复评论表单 模板函数*/
        var formReplyCommentFn = doT.template(document.getElementById("formReplyCommentTemplate").text);

		//课程进程Id
        var bamId = "${param.bamId}";
        
        //课程进程中的节Id
        var catalogId = "${param.catalogId}";
        
        //课程进行中节的内容类型
        var fdMtype = "${param.fdMtype}";
        
		var leftData = {};
				//ajax获取左侧章节展示树
				
	     loadLeftData(bamId);//加载左边栏目
				
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
		$("#courseOverBtn").bind("click",function(){
			loadOverCard();
		});
        loadRightCont(catalogId,fdMtype);//默认加载章节 参数：节id
        
        function loadOverCard(){
        	
        	 var pageContentFn = doT.template(document.getElementById("pageContentTemplate").text);
        	 var pageData={};
        	 var passed;
        	 $.ajax({
	  			  url: "${ctx}/ajax/passThrough/getCourseOverByBamId",
	  			  async:false,
	  			  data:{
	  				  bamId:bamId
	  			  },
	  			  dataType:'json',
	  			  type:"post",
	  			  success: function(result){
	  				  if(result=="notThrough"){
	  					passed=false;
	  				  }else{
	  					passed=true; 
	  					 pageData=result;
	  				  }
	  			  }
 			});
        	 if(passed){
        		 $("#mainContent").html(pageContentFn(pageData));
        		 $("#downloadCertificate").bind("click",function(){
                	 window.location.href = "${ctx}/common/file/downloadImg?bamId="+bamId;
                 });
        		 $("#firstC").click(function (e){
                  	window.location.href = "${ctx}/passThrough/getStudyContent?bamId="+bamId+"&catalogId="+pageData.firstCId+"&fdMtype="+pageData.firstCType;
                 }); 
        		 $("#lastC").click(function (e){
                   	window.location.href = "${ctx}/passThrough/getStudyContent?bamId="+bamId+"&catalogId="+pageData.listCId+"&fdMtype="+pageData.listCType;
                 }); 
        	 }
        }
		
		 function loadLeftData(bamId){
			  $.ajax({
				  url: "${ctx}/ajax/passThrough/getBamCatalogTree",
				  async:false,
				  data:{bamId:bamId},
				  dataType:'json',
				  success: function(rsult){
					  leftData = rsult;
					  leftData.sidenav.currentId = catalogId;
					  $("#sideBar").html(pageLeftBarFn(leftData));
				  },
			});
			 
		 }
        
		function loadRightCont(fdid,type){
        	catalogId=fdid;
		    fdMtype=type;
	        $.ajax({
		  			  url: "${ctx}/ajax/passThrough/getCourseContent",
		  			  async:false,
		  			  cache:false,
		  			  data:{
		  				  catalogId:fdid,
		  				  bamId:bamId,
		  				  fdMtype:fdMtype
		  			  },
		  			  dataType:'json',
		  			  success: function(result){
		  				if(result.type == "exam" || result.type == "task"){
		  					$("#mainContent").html(rightContentFn(result));
		  					afterLoadExamOrTaskPage(result);
		  	            } else if(result.type == "video" || result.type == "doc"||result.type == "ppt"){
		  	            	$("#mainContent").html(rightMaterialContentFn(result));
		  	                afterLoadMediaPage(result);
		  	            }
		  				
		  			  },
	  			});

            //可选章节按钮
            $("#btnOptionalLecture").css("cursor","pointer")
                    .click(function(e){
                        //$(this).removeClass("disabled");
                        //$("#nextLecture").removeClass("disabled");
                     $.ajax({
      		  			  url: "${ctx}/ajax/passThrough/updateCatalogThrough",
      		  			  async:false,
      		  			  data:{
      		  				  catalogId:catalogId,
      		  				  bamId:bamId,
      		  			  },
      		  			  dataType:'json',
      		  			  success: function(result){
      		  				loadLeftData(bamId);
      		  				loadRightCont(catalogId,fdMtype);
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
      		  			  },
      	  			}); 
                        
          });
            
          //上一节
            $("#prevLecture").click(function (e){
            	if($(this).attr("href")){
            		catalogId = $(this).attr("data-fdid");
            		loadLeftData(bamId);
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
            	}
                 
            });
            //下一节
            $("#nextLecture").click(function (e){
            	if($(this).attr("href")){
            		catalogId = $(this).attr("data-fdid");
            		loadLeftData(bamId);
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
            		loadRightCont($(this).attr("data-fdid"),$(this).attr("data-type"));
            	}
                
            }); 
            $("#nextOver").click(function (e){
            	loadOverCard();
            }); 
            $("#pageHeader").affix({
                offset: {
                    top: 10
                }
            });
        }
        /*视频、文档等媒体页加载头部后执行方法*/
        function afterLoadMediaPage(data){
            var $body = $("#pageBody");
            $body.append(mediaInfoFn(data))
                    .append(mediaCommentFn(data.defaultMedia));
            //$("#listComment").html(listCommentFn(data.defaultMedia.mediaComment.listComment));

            /*评分效果*/
            $body.delegate("#ratingDo>i","click",function(e){
                var $this = $(this),
                        index = $this.index();
                    $this.addClass("active").prevAll().addClass("active");
                    $this.nextAll().removeClass("active");
                    $this.parent().nextAll(".point").text(index+1);  
                    $.ajax({
  		  			  url: "${ctx}/ajax/score/pushMaterialToCourse",
  		  			  async:false,
  		  			  data:{
  		  					fdModelId :$("#formMakeComments").attr("data-fdid"),
  		  					fdScore : index + 1,
  		  			  },
  		  			  dataType:'json',
  		  			  success: function(result){
  		  				$("#ratingTotal").find(".rating-all>.icon-star").each(function(i){
  		                    if(i < result[0].fdAverage){
  		                        $(this).addClass("active");
  		                    } else {
  		                        $(this).removeClass("active");
  		                    }
  		                }).end().children("b.text-warning").text(result[0].fdAverage);
  		  				var scoreInfoHtml = doT.template(document.getElementById("scoreInfo").text);
  		  				$("#pullrightInfo").html(scoreInfoHtml(result[0]));
  		  			  },
  	  				});
            })  /*评论列表中按钮事件*/
                .delegate("#listComment>.media .btns-comt>a","click",function(e){
                    e.preventDefault();
                        var $this = $(this);
                        var itemId = $this.closest(".media").attr("data-fdid");
                        var $num = $this.children(".num");
                        if(!$this.hasClass("active")){
                            if($this.hasClass("btnPraise")){//赞
                            	var pushok;
                            	$.ajax({
	                          		  url: "${ctx}/ajax/message/supportOrOpposeMessage",
	                          		  async:false,
	                          		  data:{
	                          			messageId :itemId,
	                          			fdType :"01",
	                          		  },
	                          		  success: function(result){
	                          			  if(result=='"err"'){
	                          				pushok=false;
	                          			  }else{
	                          				pushok=true;
	                          			  }
	                          		  }
                          		});
                            	if(pushok){
                            		$num.text(parseInt($num.text()) + 1);
                            		$this.addClass("active");
                            	}else{
                            		$.fn.jalert2("不能支持和反对自己的评论");
                            	}
                            } else if($this.hasClass("btnWeak")){//踩
                            	var pushok;
                            	$.ajax({
	                          		  url: "${ctx}/ajax/message/supportOrOpposeMessage",
	                          		  async:false,
	                          		  data:{
	                          			messageId :itemId,
	                          			fdType :"02",
	                          		  },
	                          		  success: function(result){
	                          			  if(result=='"err"'){
	                          				pushok=false;
	                          			  }else{
	                          				pushok=true;
	                          			  }
	                          		  }
                          		});
                            	if(pushok){
                            		$num.text(parseInt($num.text()) + 1);
                            		$this.addClass("active");
                            	}else{
                            		$.fn.jalert2("不能支持和反对自己的评论");
                            	}
                            } else if($this.hasClass("btnComment")){//评论
                                if($("#formReply").length){
                                	$.fn.jalert2("请先保存其它回复");
                                } else {
                                    var $mediaBody = $this.closest(".media-body");
                                    var toName =  $mediaBody.find(".media-heading>.name").text();
                                    var toMail =  $mediaBody.find(".media-heading>.mail").text();
                                    var toOrg =  $mediaBody.find(".media-heading>.org").text();
                                    var toLink = $mediaBody.prev("a").attr("href");
                                    $this.closest(".clearfix").after(formReplyCommentFn({
                                        name: toName
                                    }));
                                    /*评论列表中回复表单验证*/
                                    $("#formReply").validate({
                                        submitHandler: function(form){
                                           $.ajax({
          	                          		  url: "${ctx}/ajax/message/addMaterialMessagesMessage",
          	                          		  async:false,
          	                          		  data:{
          	                          			materialId :$("#formMakeComments").attr("data-fdid"),
          	                          			fdContent :$("#replyComm").val(),
          	                          			messageId :itemId,
          	                          		  },
          	                          		  success: function(result){
          	                          			resetComment(1,10);
          	                          		  }
                                    		});
                                        }
                                    });
                                    $("#formReply .btn-cancel").click(function(e){
                                        $(this).closest(".form-reply").remove();
                                        $this.removeClass("active");
                                    });

                                }
                            }
                            
                        }
                });
			resetComment(1,10);
			resetScoreInfo();
            /*评分表单*/
            $("#formMakeComments").validate({
                submitHandler: submitFormComment
            });
            
            function resetScoreInfo(){
            	$.ajax({
          		  url: "${ctx}/ajax/score/canPushScoreToMaterial",
          		  async:false,
          		  dataType:'json',
          		  data:{
          			  fdModelId:$("#formMakeComments").attr("data-fdid"),
          		  },
          		  success: function(result){
          			  var score = (result==-1)?0:result;
          			  $("#ratingDo").find(".icon-star").each(function(i){
  		                    if(i < score){
  		                        $(this).addClass("active");
  		                    } else {
  		                        $(this).removeClass("active");
  		                    }
  		                });
          			  $("#ratingDoScore").text(score);
          		  }
      		});
            	$.ajax({
            		  url: "${ctx}/ajax/score/getScoreStatisticsByfdModelId",
            		  async:false,
            		  dataType:'json',
            		  data:{
            			  fdModelId:$("#formMakeComments").attr("data-fdid"),
            			  fdModelName:"<%=MaterialInfo.class.getName()%>",
            		  },
            		  success: function(result){
            			  $("#ratingTotal").find(".rating-all>.icon-star").each(function(i){
    		                    if(i < result[0].fdAverage){
    		                        $(this).addClass("active");
    		                    } else {
    		                        $(this).removeClass("active");
    		                    }
    		                }).end().children("b.text-warning").text(result[0].fdAverage);
    		  				var scoreInfoHtml = doT.template(document.getElementById("scoreInfo").text);
    		  				$("#pullrightInfo").html(scoreInfoHtml(result[0]));
            		  }
        		});
            }

            function resetComment(pageNo,pageSize){
                $.ajax({
            		  url: "${ctx}/ajax/message/findCommentByModelId",
            		  async:false,
            		  dataType : 'json',
            		  type:"post",
            		  data:{
            			  modelName:"<%=MaterialInfo.class.getName()%>",
            			  modelId:$("#formMakeComments").attr("data-fdid"),
            			  pageNo:pageNo,
            			  pageSize:pageSize,
                	  },
            		  success: function(result){
            			  $("#listComment").html(listCommentFn(result.listComments));
            			  if(result.listComments.length==0){
            				  $("#commentDiv").addClass("hide");
            			  }else{
            				  $("#commentDiv").removeClass("hide");
            			  }
            		  }
            	});
              //刷新评论页码
                $.ajax({
          		  url: "${ctx}/ajax/message/getCommentPageInfo",
          		  async:false,
          		  dataType : 'json',
          		  data:{
          			  modelName:"<%=MaterialInfo.class.getName()%>",
          			  modelId:$("#formMakeComments").attr("data-fdid"),
          			  pageNo:pageNo,
          			  pageSize:pageSize,
              	  },
          		  success: function(result){
         			 $("#pageLine1").html(" "+result.startLine+" - "+result.endLine+" ");
         			 $("#pageLine2").html(" "+result.startLine+" - "+result.endLine+" ");
         			 $("#pageTotal1").html(result.totalSize);
         			 $("#pageTotal2").html(result.totalSize);
         			 //第一页
         			 if(result.pageNo==1){
         				 $("#gotoBefore1").unbind();
         				 $("#gotoBefore2").unbind();
         				 $("#gotoNext1").bind("click",function (){resetComment(pageNo+1,pageSize);});
         				 $("#gotoNext2").bind("click",function (){resetComment(pageNo+1,pageSize);});
         				 $("#gotoBefore1").attr("disabled",true);
         				 $("#gotoBefore2").attr("disabled",true);
         				 $("#gotoNext1").removeAttr("disabled");
         				 $("#gotoNext2").removeAttr("disabled");
         			 }
         			 //最后一页
         			 if(result.pageNo==result.totalPage){
         				 $("#gotoNext1").unbind();
         				 $("#gotoNext2").unbind();
         				 $("#gotoNext1").attr("disabled",true);
         				 $("#gotoNext2").attr("disabled",true);
         				 $("#gotoBefore1").bind("click",function (){resetComment(pageNo-1,pageSize);});
         				 $("#gotoBefore2").bind("click",function (){resetComment(pageNo-1,pageSize);});
         				 $("#gotoBefore1").removeAttr("disabled");
         				 $("#gotoBefore2").removeAttr("disabled");
         			 }
         			 //只有一页
         			 if(result.pageNo==result.totalPage&&result.pageNo==1){
         				 $("#gotoNext1").unbind();
         				 $("#gotoNext2").unbind();
         				 $("#gotoBefore1").unbind();
         				 $("#gotoBefore2").unbind();
         				 $("#gotoNext1").attr("disabled",true);
         				 $("#gotoNext2").attr("disabled",true);
         				 $("#gotoBefore1").attr("disabled",true);
         				 $("#gotoBefore2").attr("disabled",true);
         			 }
         			 //中间
         			 if(result.pageNo!=result.totalPage&&result.pageNo!=1){
         				 $("#gotoNext1").bind("click",function (){resetComment(pageNo+1,pageSize);});
         				 $("#gotoNext2").bind("click",function (){resetComment(pageNo+1,pageSize);});
         				 $("#gotoBefore1").bind("click",function (){resetComment(pageNo-1,pageSize);});
         				 $("#gotoBefore2").bind("click",function (){resetComment(pageNo-1,pageSize);});
         				 $("#gotoBefore1").removeAttr("disabled");
         				 $("#gotoBefore2").removeAttr("disabled");
         				 $("#gotoNext1").removeAttr("disabled");
         				 $("#gotoNext2").removeAttr("disabled");
         			 }
         			 
          		  }
          		});
            
            }
            
            /*提交评论表单*/
            function submitFormComment(form){
               $.ajax({
          		  	url: "${ctx}/ajax/message/addMaterialMessage",
          		  	async:false,
          		  	dataType : 'json',
          		  	data:{
          				materialId:$("#formMakeComments").attr("data-fdid"),
                    	isAnonymity: $("#isAnonymity").is(":checked"),
                    	fdContent: $("#textComment").val(),
              	  	},
          		});
               resetComment(1,10);
                //刷新评论列表信息
                /* resetComment(1,10); */
              return false;
            }

            $("head").loadJS({name: "SWFobject.js"});

            /**************************  如下代码把 videoplayer.swf 嵌入到 id 为 flashcontent 的div中  *********************/
            var flashvars =
            {
                //flash接收的参数
                autoPlay:'true',
                skin:"http://me.xdf.cn:80/iportal/sys/attachment/video/videoPlayerSkin.swf",
                video: data.defaultMedia.url,
                fms:"rtmp://video.xdf.cn/V3/"
            };
            var params =
            {
                allowScriptAccess: "always",
                allowFullScreen:"true",
                wmode:"transparent",
                quality:"high"
            };
            var attributes =
            {
                id: "myflash"
            };
            swfobject.embedSWF("http://me.xdf.cn:80/iportal/sys/attachment/video/videoplayer.swf", "flashcontent",
                    "100%", "510", "6.0.0", "expressInstall.swf", flashvars, params, attributes);
            var $mediaToolbar = $("#mediaToolbar");

            $("#listMedia>li>a").bind("click",function(e){
                e.preventDefault();
                var $this = $(this);
                $this.parent("li").addClass("active").siblings().removeClass("active");
                flashvars.video = $this.attr("href");
                swfobject.embedSWF("http://me.xdf.cn:80/iportal/sys/attachment/video/videoplayer.swf", "myflash",
                        "100%", "510", "6.0.0", "expressInstall.swf", flashvars, params, attributes);
                var result;
                $.ajax({
                	  cache:false,
		  			  url: "${ctx}/ajax/passThrough/getCourseContent",
		  			  async:false,
		  			  data:{
		  				  materialId:$this.attr("data-fdid"),
		  				  catalogId:catalogId,
		  				  bamId:bamId,
		  				  fdMtype:fdMtype
		  			  },
		  			  dataType:'json',
		  			  success: function(data){
		  				result = data.defaultMedia;
		  			  },
                });
                if(result.mePraised){
                	$("#btnPraise").removeClass("active").children(".num").text(result.praiseCount);
                }else{
                	 $("#btnPraise").addClass("active").children(".num").text(result.praiseCount);
                }
               
                $("#btnDownload").addClass(result.canDownload ? "active" : "disabled").attr("data-original-title",result.canDownload ? '点击下载' : '无权下载')
                        .children(".num").text(result.downloadCount);
                $("#btnDoPass").attr("disabled",result.isPass ? true : false);
                $mediaToolbar.find(".playCount>.num").text(result.readCount);
                $("#mediaToolbar").attr("data-fdid",result.id);
                $("#btnDownload").attr("data-fdid",result.url);
                $("#btnDownload").attr("canDownload",result.canDownload);
                $("#btnPraise").attr("praisedstatus",result.mePraised);
                $("#mediaName").text($this.attr("title"));
                $("#mediaIntro").text(result.intro);
                $("#ratingTotal").find(".rating-all>.icon-star").each(function(i){
                    if(i < result.rating.average){
                        $(this).addClass("active");
                    } else {
                        $(this).removeClass("active");
                    }
                }).end().children("b.text-warning").text(result.rating.average);
                $("#ratingFive").find(".progress-gray>.bar")
                        .width(result.rating.five/result.rating.total*100 + "%").end().children(".fs9").text(result.rating.five);
                $("#ratingFour").find(".progress-gray>.bar")
                        .width(result.rating.four/result.rating.total*100 + "%").end().children(".fs9").text(result.rating.four);
                $("#ratingThree").find(".progress-gray>.bar")
                        .width(result.rating.three/result.rating.total*100 + "%").end().children(".fs9").text(result.rating.three);
                $("#ratingTwo").find(".progress-gray>.bar")
                        .width(result.rating.two/result.rating.total*100 + "%").end().children(".fs9").text(result.rating.two);
                $("#ratingOne").find(".progress-gray>.bar")
                        .width(result.rating.one/result.rating.total*100 + "%").end().children(".fs9").text(result.rating.one);
                $("#mediaComment").after(mediaCommentFn(result)).remove();
                //$("#listComment").html(listCommentFn(result.mediaComment.listComment));

                /*评分表单*/
                $("#formMakeComments").validate({
                    submitHandler: submitFormComment,
                });
                resetComment(1,10);
    			resetScoreInfo();
            });

            $mediaToolbar.find(".btn-group>.btn").tooltip();

            /*点赞事件*/
            $("#btnPraise").on("click",function(e){
                e.preventDefault();
                var $this = $(this);
               /*  if($this.hasClass("active")){
                    $this.removeClass("active").attr("data-original-title","赞").children(".num").text(parseInt($this.text())-1);
                } else {
                    $this.addClass("active").attr("data-original-title","取消赞").children(".num").text(parseInt($this.text())+1);
                } */
                //$.post("url",{id: $mediaToolbar.attr("data-fdid")})
                if($this.attr("praisedstatus")=='true'){
	                $.ajax({
	         			type: "post",
	         			url: "${ctx}/ajax/material/saveLaud",
	         			data : {
	         				"materialId":$mediaToolbar.attr("data-fdid"),
	         			},
	         			success:function(data){
	         				 $this.addClass("active").attr("data-original-title","赞").children(".num").text(data); 
	         				 $this.attr("praisedstatus",'false');
	         			}
	         		}); 
                }else{
                	$.fn.jalert2("您已经赞过该资源!");
                }
            });

            /*点下载事件*/
            $("#btnDownload").on("click",function(e){
                var $this = $(this);
                if($this.attr("canDownload")=='false'){
                    e.preventDefault();
                    return;
                } else {
                    //$.post("url",{id: $mediaToolbar.attr("data-fdid")});
                	 if($this.attr("data-fdid")!=null&&$this.attr("data-fdid")!=""){
                		 $.ajax({
                  			type: "post",
                  			url: "${ctx}/ajax/material/updateDownloadNum",
                  			data : {
                  				"materialId":$mediaToolbar.attr("data-fdid"),
                  			},
                  			success:function(data){
                  				 window.location.href="${ctx}/common/file/download/"+$this.attr("data-fdid");
                  				 $("#btnDownload").find("span").html(data);
                  			}
                  		}); 
                     	 
                       } else {
                     	  $.fn.jalert2("您好！该视频没有对应附件");
                       } 
                }
            });

            $("#btnDoPass").on("click",function(e){
                $("#listMedia>li.active").addClass("pass");
                $(this).attr("disabled", true);
                $.ajax({
          			type: "post",
          			url: "${ctx}/passThrough/submitExamOrTask?bamId="+bamId+"&catalogId="+catalogId+"&fdMtype="+fdMtype+"&fdid="+$mediaToolbar.attr("data-fdid"),
          			data : {
          				"materialId":$mediaToolbar.attr("data-fdid"),
          			},
          			success:function(data){
          				loadRightCont(catalogId,fdMtype);
          				loadLeftData(bamId);
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
          				
          			}
          		}); 
                //$.post("url",{id: $mediaToolbar.attr("data-fdid")})
            });
        }


        /*测试页加载完成后执行方法*/
        function afterLoadExamOrTaskPage(data){

            var $window = $(window);

            $("#listExamPaper>li>a").click(function(e){
            	var $athis = $(this);
            	var examPaperStatus_a = $athis.attr("examPaperStatus");
                if((data.type == "task") && (examPaperStatus_a=="finish")){
                  	$.fn.jalert2("您已提交当前作业，请耐心等待导师审批！");
                  	return;
                 } 
                e.preventDefault();
                if($(this).parent().siblings().find(".collapse").hasClass("in")){
                	 var $this2 = $(this);
                 	$this2.attr("data-toggle","");
                 	$.fn.jalert("确定要关闭未保存的表单？",function(){
                 		$this2.parent().siblings().find(".collapse").each(function(i){
                 			if($(this).hasClass("in")){
                 				$(this).collapse("hide");
                 			}
                 		});
                 		$this2.attr("data-toggle","collapse");
                 		$athis.trigger("click");
                 	});
                }
            });

            //试卷列表折叠 手风琴事件
            $("#listExamPaper>li>.collapse")
                    .bind("show",function(){
                        var $this = $(this);
                        var examPaperStatus = $this.attr("examPaperStatus");
                        if((data.type == "task") && (examPaperStatus=="finish")){
                        	$.fn.jalert2("您已提交当前作业，请耐心等待导师审批！");
                        	return;
                        }
                        var tempData = {};
                        $this.prev(".titBar").addClass("hide");
                        $.ajax({
                        	  type:"post",
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
                        var date = new Date();
                        //tempData.startTime=date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                        tempData.startTime=date.getTime();
                        $this.html(examPaperDetailFn(tempData));

                        $this.find("[data-toggle='collapse']").click(function(e){
                        	var $this2 = $(this);
                        	$this2.attr("data-toggle","");
                        	$.fn.jalert("确定要关闭未保存的表单？",function(){
                        		$this2.attr("data-toggle","collapse");
                        		$this.collapse("hide");
                        	});
                        	
                        })
                        
				
                        $("button[name='answerAtt']").each(function(){
                        	//var fileid = $(this).attr('id'); 
                        	var temp = "#listTaskAttachment_"+$(this).attr('id')+">li>.icon-remove-blue";
                        	 $(temp).click(function(e){
                                 e.preventDefault();
                                 $(this).parent().remove();
                             })
                        	
                         });

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
                        
                        var $progress ,flag = true,pct,interval,countdown = 0,byteUped = 0;
                        $("button[name='answerAtt']").each(function(){
                        	var fileid = $(this).attr('id');
                        	
                        	$('#'+fileid).uploadify({
                        		'height' : 40,
                                'width' : 68,
                                'multi' : false,
                                'simUploadLimit' : 1,
                                'swf' : '${ctx}/resources/uploadify/uploadify.swf',
                                "buttonClass": "btn btn-primary btn-large",
                                'buttonText' : '上传',
                                'uploader' : '${ctx}/common/file/o_upload',
                                'auto' : true,
                                'fileTypeExts' : '*.*',
                                'fileSizeLimit':2097152,// 限制文件大小为2G
                                'onInit' : function(){
                                	$progress = $('<span class=\"progress\"><div class=\"bar\" style=\"width:0%;\"></div> </span><span class=\"txt\"><span class=\"pct\">0%</span><span class=\"countdown\"></span></span>');
                                	$('#'+fileid).next(".uploadify-queue").remove();
                                },
                                'onUploadStart' : function (file) {
                                	$('#'+fileid).before($progress);
                                    //$uploadBtn.uploadify("settings", "formData");
                                },
                                'onUploadSuccess' : function (file, datas, Response) {
                                    if (Response) {
                                    	$progress.find(".countdown").empty();
                                        var objvalue = eval("(" + datas + ")");
                                        jQuery("#attName").html(objvalue.fileName+"<i class=\"icon-paperClip\"></i>");
                                        var html = "<li id='attach"+objvalue.attId+"'><input type='hidden' value='"+objvalue.attId+"' name='attach_"+fileid+"' id='answerAttId_"+fileid+"'><a><i class='icon-paperClip'></i>"
                                        +objvalue.fileName+"</a><a href='#' class='icon-remove-blue'></a></li>";
                                        $("#listTaskAttachment_"+fileid).append(html);
                                    }
                                },
                                'onUploadProgress' : function(file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) {
                                	pct = Math.round((bytesUploaded/bytesTotal)*100)+'%';
                                	byteUped = bytesUploaded;
                                	if(flag){
                                		interval = setInterval(uploadSpeed,100);
                                		flag = false;
                                	}
                                	if(bytesUploaded == bytesTotal){
                                		clearInterval(interval);
                                		
                                	}
                                	$progress.find(".bar").width(pct).end().find(".pct").text(pct);
                                	countdown>0 && $progress.find(".countdown").text(secTransform((bytesTotal-bytesUploaded)/countdown));
                                }
                            });
        						
        				});
                        function uploadSpeed(){
                    		countdown = byteUped - countdown;
                    	}
                    	function secTransform(s){
                    		if( typeof s == "number"){
                    			s = Math.ceil(s);
                    			var t = "";
                    			if(s>3600){
                    				t= Math.ceil(s/3600) + "小时" + Math.ceil(s%3600/60) + "分钟" + s%3600%60 + "秒";
                    			} else if(s>60){
                    				t= Math.ceil(s/60) + "分钟" + s%60 + "秒";
                    			} else {
                    				t= s + "秒";
                    			}
                    			return "，剩余时间：" + t;
                    		}else{
                    			return null;
                    		}		
                    	}
                    	
                        $("#formExam").validate({
                            submitHandler: function(form){
	                            $("input[putId='examAnswer_completion']").each(function(){
	                            	$(this).nextAll("input:first").val($(this).attr("exam-id")+":"+$(this).attr("value"));
	                           	});
	                            form.submit();
                            }
                        });
                    })
                    .bind("hide",function(){
                    	$(".uploadify").each(function(){
                    		$(this).uploadify('destroy'); 
                    	});
                        $(this).empty().prev(".titBar").removeClass("hide");
                        $("#headToolsBar").empty();
                        $("#listExamPaper>li>a").each(function(){
                            if($(this).attr("data-toggle") == ""){
                                $(this).attr("data-toggle","collapse");
                            }
                        })
                    });
        }


    });



</script>
</body>
</html>
