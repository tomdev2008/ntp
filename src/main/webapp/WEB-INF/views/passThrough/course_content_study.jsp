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
                            <li{{?lecture.status == 'doing'}} class="active"{{?}}>
                                <a href="#" data-fdid="{{=lecture.id}}" data-toggle="popover" data-content="{{=lecture.intro || ''}}" title="{{=lecture.name || ''}}">
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
                {{?it.type == 'exam'}}
                    {{#def.examContent}}
                {{?}}
                {{?it.type == 'video'}}
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
                    <div class="line"><span class="label-intro" >学习任务</span><small>建议您认真完成所有题目后提交试卷：</small></div>
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
                        <h2>试卷{{=index+1}}. {{=paper.name}}</h2>
                        <p class="muted">共计{{=paper.listExam.length}}题，满分{{=paper.fullScore}}分，建议答题时间为{{=paper.examPaperTime}}分钟。</p>
                        <span class="icon-state-bg{{?paper.examPaperStatus == 'fail'}} error">未通过
                        {{??paper.examPaperStatus == 'pass'}} success">通过
                        {{??paper.examPaperStatus == 'untreated'}}">待答{{?}}</span>
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
                        {{??it.examPaperStatus == 'untreated'}}">待答{{?}}</span> 试卷{{=it.num}} {{=it.name}} 共计 <span class="total">{{=it.listExam.length}}</span>题，满分{{=it.fullScore}}分，建议答题时间为{{=it.examPaperTime}}分钟。</h2>
        <p class="muted">{{=it.examPaperIntro}}</p>
        <a class="btn btn-link" data-toggle="collapse" data-parent="#listExamPaper" href="#examPaper{{=it.num}}">收起<b class="caret"></b></a>
        </div>
        <form action="{{=it.action || '#'}}" post="post" id="formExam">
            <input name="fdid" value="{{=it.id}}" type="hidden" />
                <div class="bd">
                <dl class="listExam">
                {{~it.listExam :exam1:index2}}
                    {{~it.listExam :exam:index}}
                    {{?index2 == exam.index}}
                        <dt class="examStem" id="examStem{{=index2+1}}">
                            {{=index2+1}}. {{=exam.examStem}}
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
                                    <input type="{{?exam.examType == 'single'}}radio{{??}}checkbox{{?}}" {{?ans.isChecked}}checked{{?}} value="{{=index1+1}}" name="examAnswer{{=exam.id}}" />
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
            <div class="dt">试卷{{=it.num}}</div>
            你已完成<span class="succ">{{=it.successCount}}</span>道题目，共计<span class="total">{{=it.examCount}}</span>题
            <a href="#navExams" data-toggle="collapse" class="showList"><span class="txtShow">展开</span><span class="txtHide">收起</span>试题列表<b class="caret"></b></a>
        </div>
        <div id="navExams" class="collapse in">
            <div class="collapse-inner">
                {{~it.listExam :exam1:index1}}
                {{~it.listExam :exam:index}}
                {{?index1 == exam.index}}
                    <a class="num{{?exam.status != 'null'}} active{{?}}" href="#examStem{{=index1+1}}">{{=index1+1}}<i class="icon-circle-{{?exam.status == 'error'}}error{{??exam.status == 'success'}}success{{?}}"></i></a>
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
        
        
        var leftData = {
            sidenav: {
                chapter:[
                    {
                        index: 4,
                        num: 2,
                        name: "业务学习"
                    },
                    {
                        index: 0,
                        num: 1,
                        name: "业务学习"
                    }
                ],
                lecture: [
                    {
                        id: "fdid345320432",
                        index: 1,
                        num: 1,
                        type: "video",
                        name: "学习入门视频",
                        intro: "本课程为您介绍在线考试流程和相关注意事项",
                        status: "pass"
                    },
                    {
                        id: "fdid0wfwef432",
                        index: 2,
                        num: 2,
                        type: "exam",
                        name: "在线测试",
                        intro: "本课程为您介绍在线考试流程和相关注意事项",
                        status: "doing"
                    },
                    {
                        id: "fdid9094858345",
                        index: 3,
                        num: 3,
                        type: "task",
                        name: "在线作业",
                        intro: "本课程为您介绍在线考试流程和相关注意事项",
                        status: "untreated"
                    },
                    {
                        id: "fdid000000000",
                        index: 5,
                        num: 4,
                        type: "doc",
                        name: "文档",
                        intro: "本课程为您介绍在线考试流程和相关注意事项",
                        status: "untreated"
                    },
                    {
                        id: "fdid32432432432",
                        index: 6,
                        num: 5,
                        type: "ppt",
                        name: "幻灯片",
                        intro: "本课程为您介绍在线考试流程和相关注意事项",
                        status: "untreated"
                    }
                ]
            }
        }
        $("#sideBar").html(pageLeftBarFn(leftData));
        //var leftData = {};
		//ajax获取左侧章节展示树
        /* $.ajax({
			  url: "${ctx}/ajax/passThrough/getBamCatalogTree",
			  async:false,
			  data:{bamId:bamId},
			  dataType:'json',
			  success: function(rsult){
				  leftData = rsult;
				  alert(JSON.stringify(leftData));
				  $("#sideBar").html(pageLeftBarFn(leftData));
			  },
		}); */
        //左侧菜单定位
        setTimeout(function(){
            $("#sidebar").affix({
                offset: {
                    bottom: 100
                }
            }).parent().height($("#sidebar").height());
        },100);

        var rightData = {// 测试用 数据 ，ajax后删除
            type: "exam",
            status: "doing",
            courseName: "雅思口语新教师培训课程",
            lectureName: "参加在线测试",
            lectureIntro: "为获得更好的测试效果，建议您关闭IM聊天工具、视频音频等干扰源。测试过程中尽量不要离开。请勿关闭浏览器，否则测试结果将无法保存。",
            num: 2,
            isOptional: true,
            listExamPaper: [//试卷列表数据结构
                {
                    id: "fdid08582300324",
                    name: "雅思口语强化课程教案解读主试卷",
                    fullScore: 50,
                    examPaperTime: 20,
                    examPaperIntro: "雅思口语强化课程教案解读主试卷 雅思口语强化课程教案解读主试卷雅思口语强化课程教 案解读主试卷 雅思口语强化课程教案解读主试卷 雅思口语强化课程教案解读主试卷",
                    examPaperStatus: "untreated",//untreated, pass, fail, finish
                    listExam: [
                        {
                            id: "fdid00000000322",
                            index: 0,
                            status: "null",//success,error,null
                            examType: "single",//single, multiple, completion
                            examStem: "2011年前十一个月，某省高新技术产业完成总产值3763.00亿元，实现增加值896.31亿元。增加值同比增长30.74%，比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%。高新技术产业各领域的增加值如下图所示： （5 分） PASS",
                            listExamAnswer: [
                                {
                                    index: 3,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%",
                                    isAnswer: true,
                                    isChecked: false
                                },
                                {
                                    index: 0,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比",
                                    isAnswer: false,
                                    isChecked:false
                                },
                                {
                                    index: 1,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%",
                                    isAnswer: false,
                                    isChecked:false
                                },
                                {
                                    index: 2,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比",
                                    isAnswer: false,
                                    isChecked:false
                                }
                            ],
                            listAttachment: [
                                {
                                    index: 0,
                                    name: "高新技术产业各领域增加值饼形图（单位：亿元）.jpg",
                                    url: "#"
                                },
                                {
                                    index: 3,
                                    name: "高新技术产业各领域咨询报告.pdf",
                                    url: "#"
                                },
                                {
                                    index: 2,
                                    name: "高新技术产业各领域专家分析讲座.mp4",
                                    url: "#"
                                },
                                {
                                    index: 1,
                                    name: "高新技术产业各领域咨询报告2.pdf",
                                    url: "#"
                                }
                            ]
                        },
                        {
                            id: "fdid000000003233",
                            index: 1,
                            status: "null",//success,error,null
                            examType: "multiple",//single, multiple, completion
                            examStem: "2011年前十一个月，某省高新技术产业完成总产值3763.00亿元，实现增加值896.31亿元。增加值同比增长30.74%，比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%。高新技术产业各领域的增加值如下图所示： （5 分） PASS",
                            listExamAnswer: [
                                {
                                    index: 3,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%",
                                    isAnswer: true,
                                    isChecked: false
                                },
                                {
                                    index: 0,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比",
                                    isAnswer: false,
                                    isChecked:false
                                },
                                {
                                    index: 1,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%",
                                    isAnswer: false,
                                    isChecked:false
                                },
                                {
                                    index: 2,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比",
                                    isAnswer: true,
                                    isChecked:false
                                }
                            ],
                            listAttachment: [
                                {
                                    index: 0,
                                    name: "高新技术产业各领域增加值饼形图（单位：亿元）.jpg",
                                    url: "#"
                                },
                                {
                                    index: 3,
                                    name: "高新技术产业各领域咨询报告.pdf",
                                    url: "#"
                                },
                                {
                                    index: 2,
                                    name: "高新技术产业各领域专家分析讲座.mp4",
                                    url: "#"
                                },
                                {
                                    index: 1,
                                    name: "高新技术产业各领域咨询报告2.pdf",
                                    url: "#"
                                }
                            ]
                        },
                        {
                            id: "fdid0003233",
                            index: 2,
                            status: "null",//success,error,null
                            examType: "single",//single, multiple, completion
                            examStem: "2011年前十一个月，某省高新技术产业完成总产值3763.00亿元，实现增加值896.31亿元。增加值同比增长30.74%，比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%。高新技术产业各领域的增加值如下图所示： （5 分） PASS",
                            listExamAnswer: [
                                {
                                    index: 3,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%",
                                    isAnswer: true,
                                    isChecked: false
                                },
                                {
                                    index: 0,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比",
                                    isAnswer: false,
                                    isChecked:false
                                },
                                {
                                    index: 1,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%",
                                    isAnswer: false,
                                    isChecked:false
                                },
                                {
                                    index: 2,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比",
                                    isAnswer: false,
                                    isChecked:false
                                }
                            ],
                            listAttachment: [
                                {
                                    index: 0,
                                    name: "高新技术产业各领域增加值饼形图（单位：亿元）.jpg",
                                    url: "#"
                                },
                                {
                                    index: 3,
                                    name: "高新技术产业各领域咨询报告.pdf",
                                    url: "#"
                                },
                                {
                                    index: 2,
                                    name: "高新技术产业各领域专家分析讲座.mp4",
                                    url: "#"
                                },
                                {
                                    index: 1,
                                    name: "高新技术产业各领域咨询报告2.pdf",
                                    url: "#"
                                }
                            ]
                        }
                    ]
                },
                {
                    id: "fdid08580000432",
                    name: "雅思口语强化课程教案解读主试卷2",
                    fullScore: 50,
                    examPaperTime: 20,
                    examPaperIntro: "雅思口语强化课程教案解读主试卷 雅思口语强化课程教案解读主试卷雅思口语强化课程教 案解读主试卷 雅思口语强化课程教案解读主试卷 雅思口语强化课程教案解读主试卷",
                    examPaperStatus: "pass",//untreated, pass, fail, finish
                    listExam: [
                        {
                            id: "fdid00000000322",
                            index: 0,
                            status: "success",//success,error,null
                            examType: "single",//single, multiple, completion
                            examStem: "2011年前十一个月，某省高新技术产业完成总产值3763.00亿元，实现增加值896.31亿元。增加值同比增长30.74%，比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%。高新技术产业各领域的增加值如下图所示： （5 分） PASS",
                            listExamAnswer: [
                                {
                                    index: 3,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%",
                                    isAnswer: true,
                                    isChecked: true
                                },
                                {
                                    index: 0,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比",
                                    isAnswer: false,
                                    isChecked:false
                                },
                                {
                                    index: 1,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%",
                                    isAnswer: false,
                                    isChecked:false
                                },
                                {
                                    index: 2,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比",
                                    isAnswer: false,
                                    isChecked:false
                                }
                            ],
                            listAttachment: [
                                {
                                    index: 0,
                                    name: "高新技术产业各领域增加值饼形图（单位：亿元）.jpg",
                                    url: "#"
                                },
                                {
                                    index: 3,
                                    name: "高新技术产业各领域咨询报告.pdf",
                                    url: "#"
                                },
                                {
                                    index: 2,
                                    name: "高新技术产业各领域专家分析讲座.mp4",
                                    url: "#"
                                },
                                {
                                    index: 1,
                                    name: "高新技术产业各领域咨询报告2.pdf",
                                    url: "#"
                                }
                            ]
                        },
                        {
                            id: "fdid000000003233",
                            index: 1,
                            status: "success",//success,error,null
                            examType: "multiple",//single, multiple, completion
                            examStem: "2011年前十一个月，某省高新技术产业完成总产值3763.00亿元，实现增加值896.31亿元。增加值同比增长30.74%，比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%。高新技术产业各领域的增加值如下图所示： （5 分） PASS",
                            listExamAnswer: [
                                {
                                    index: 3,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%",
                                    isAnswer: true,
                                    isChecked: true
                                },
                                {
                                    index: 0,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比",
                                    isAnswer: true,
                                    isChecked:true
                                },
                                {
                                    index: 1,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%",
                                    isAnswer: false,
                                    isChecked:false
                                },
                                {
                                    index: 2,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比",
                                    isAnswer: true,
                                    isChecked:true
                                }
                            ],
                            listAttachment: [
                                {
                                    index: 0,
                                    name: "高新技术产业各领域增加值饼形图（单位：亿元）.jpg",
                                    url: "#"
                                },
                                {
                                    index: 3,
                                    name: "高新技术产业各领域咨询报告.pdf",
                                    url: "#"
                                },
                                {
                                    index: 2,
                                    name: "高新技术产业各领域专家分析讲座.mp4",
                                    url: "#"
                                },
                                {
                                    index: 1,
                                    name: "高新技术产业各领域咨询报告2.pdf",
                                    url: "#"
                                }
                            ]
                        },
                        {
                            id: "fdid0003233",
                            index: 2,
                            status: "success",//success,error,null
                            examType: "single",//single, multiple, completion
                            examStem: "2011年前十一个月，某省高新技术产业完成总产值3763.00亿元，实现增加值896.31亿元。增加值同比增长30.74%，比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%。高新技术产业各领域的增加值如下图所示： （5 分） PASS",
                            listExamAnswer: [
                                {
                                    index: 3,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%",
                                    isAnswer: false,
                                    isChecked: false
                                },
                                {
                                    index: 0,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比",
                                    isAnswer: false,
                                    isChecked:false
                                },
                                {
                                    index: 1,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%",
                                    isAnswer: false,
                                    isChecked:false
                                },
                                {
                                    index: 2,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比",
                                    isAnswer: true,
                                    isChecked:true
                                }
                            ],
                            listAttachment: [
                                {
                                    index: 0,
                                    name: "高新技术产业各领域增加值饼形图（单位：亿元）.jpg",
                                    url: "#"
                                },
                                {
                                    index: 3,
                                    name: "高新技术产业各领域咨询报告.pdf",
                                    url: "#"
                                },
                                {
                                    index: 2,
                                    name: "高新技术产业各领域专家分析讲座.mp4",
                                    url: "#"
                                },
                                {
                                    index: 1,
                                    name: "高新技术产业各领域咨询报告2.pdf",
                                    url: "#"
                                }
                            ]
                        }
                    ]
                },
                {
                    id: "fdid085800324324",
                    name: "雅思口语强化课程教案解读主试卷3",
                    fullScore: 50,
                    examPaperTime: 20,
                    examPaperIntro: "雅思口语强化课程教案解读主试卷 雅思口语强化课程教案解读主试卷雅思口语强化课程教 案解读主试卷 雅思口语强化课程教案解读主试卷 雅思口语强化课程教案解读主试卷",
                    examPaperStatus: "fail",//untreated, pass, fail, finish
                    listExam: [
                        {
                            id: "fdid00000000322",
                            index: 0,
                            status: "success",//success,error,null
                            examType: "single",//single, multiple, completion
                            examStem: "2011年前十一个月，某省高新技术产业完成总产值3763.00亿元，实现增加值896.31亿元。增加值同比增长30.74%，比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%。高新技术产业各领域的增加值如下图所示： （5 分） PASS",
                            listExamAnswer: [
                                {
                                    index: 3,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%",
                                    isAnswer: false,
                                    isChecked: false
                                },
                                {
                                    index: 0,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比",
                                    isAnswer: false,
                                    isChecked:false
                                },
                                {
                                    index: 1,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%",
                                    isAnswer: false,
                                    isChecked:false
                                },
                                {
                                    index: 2,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比",
                                    isAnswer: true,
                                    isChecked:true
                                }
                            ],
                            listAttachment: [
                                {
                                    index: 0,
                                    name: "高新技术产业各领域增加值饼形图（单位：亿元）.jpg",
                                    url: "#"
                                },
                                {
                                    index: 3,
                                    name: "高新技术产业各领域咨询报告.pdf",
                                    url: "#"
                                },
                                {
                                    index: 2,
                                    name: "高新技术产业各领域专家分析讲座.mp4",
                                    url: "#"
                                },
                                {
                                    index: 1,
                                    name: "高新技术产业各领域咨询报告2.pdf",
                                    url: "#"
                                }
                            ]
                        },
                        {
                            id: "fdid000000003233",
                            index: 1,
                            status: "error",//success,error,null
                            examType: "multiple",//single, multiple, completion
                            examStem: "2011年前十一个月，某省高新技术产业完成总产值3763.00亿元，实现增加值896.31亿元。增加值同比增长30.74%，比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%。高新技术产业各领域的增加值如下图所示： （5 分） PASS",
                            listExamAnswer: [
                                {
                                    index: 3,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%",
                                    isAnswer: true,
                                    isChecked: false
                                },
                                {
                                    index: 0,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比",
                                    isAnswer: true,
                                    isChecked:false
                                },
                                {
                                    index: 1,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%",
                                    isAnswer: false,
                                    isChecked:false
                                },
                                {
                                    index: 2,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比",
                                    isAnswer: true,
                                    isChecked:true
                                }
                            ],
                            listAttachment: [
                                {
                                    index: 0,
                                    name: "高新技术产业各领域增加值饼形图（单位：亿元）.jpg",
                                    url: "#"
                                },
                                {
                                    index: 3,
                                    name: "高新技术产业各领域咨询报告.pdf",
                                    url: "#"
                                },
                                {
                                    index: 2,
                                    name: "高新技术产业各领域专家分析讲座.mp4",
                                    url: "#"
                                },
                                {
                                    index: 1,
                                    name: "高新技术产业各领域咨询报告2.pdf",
                                    url: "#"
                                }
                            ]
                        },
                        {
                            id: "fdid0003233",
                            index: 2,
                            status: "null",//success,error,null
                            examType: "single",//single, multiple, completion
                            examStem: "2011年前十一个月，某省高新技术产业完成总产值3763.00亿元，实现增加值896.31亿元。增加值同比增长30.74%，比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%。高新技术产业各领域的增加值如下图所示： （5 分） PASS",
                            listExamAnswer: [
                                {
                                    index: 3,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%",
                                    isAnswer: false,
                                    isChecked: false
                                },
                                {
                                    index: 0,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比",
                                    isAnswer: false,
                                    isChecked:false
                                },
                                {
                                    index: 1,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%",
                                    isAnswer: false,
                                    isChecked:false
                                },
                                {
                                    index: 2,
                                    name: "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比",
                                    isAnswer: true,
                                    isChecked:false
                                }
                            ],
                            listAttachment: [
                                {
                                    index: 0,
                                    name: "高新技术产业各领域增加值饼形图（单位：亿元）.jpg",
                                    url: "#"
                                },
                                {
                                    index: 3,
                                    name: "高新技术产业各领域咨询报告.pdf",
                                    url: "#"
                                },
                                {
                                    index: 2,
                                    name: "高新技术产业各领域专家分析讲座.mp4",
                                    url: "#"
                                },
                                {
                                    index: 1,
                                    name: "高新技术产业各领域咨询报告2.pdf",
                                    url: "#"
                                }
                            ]
                        }
                    ]
                }
            ]
        }

        $("#sidenav>li>a").popover({
            trigger: "hover"
        })
                .click(function(e){
                    e.preventDefault();
                    loadRightCont($(this).attr("data-fdid"));
                });

        loadRightCont("fdid0wfwef432");//默认加载章节 参数：节id

        function loadRightCont(fdid){
            //$.get("url",{id: fdid}).success(function(result){//  ajax
            result = rightData;// 测试用 数据 ，ajax后删除
            $("#mainContent").html(rightContentFn(result));
            if(result.type == "exam"){
                afterLoadExamPage(result);
            } else if(result.type == "video"){
                //afterLoadVideoPage(result);
            }
            //});

            //可选章节按钮
            $("#btnOptionalLecture").css("cursor","pointer")
                    .click(function(e){
                        $(this).removeClass("disabled");
                        $("#nextLecture").removeClass("disabled");
                    });
        }





        function afterLoadExamPage(data){
            var rightData = data;

            var $window = $(window);

            //试卷列表折叠 手风琴事件
            $("#listExamPaper>li>.collapse")
                    .bind("show",function(){
                        var $this = $(this);
                        var data = {};
                        $this.prev(".titBar").addClass("hide");

                        for(var i in rightData.listExamPaper){
                            if(rightData.listExamPaper[i].id == $this.attr("data-fdid")){
                                data = rightData.listExamPaper[i];
                                data.num = parseInt(i) + 1;
                                data.examCount = rightData.listExamPaper[i].listExam.length;
                                var count = 0;
                                for( var j in rightData.listExamPaper[i].listExam){
                                    if(rightData.listExamPaper[i].listExam[j].status == "success"){
                                        count++;
                                    }
                                }
                                data.successCount = count;
                            }
                        }

                        $("#headToolsBar").html(examPaperStatusBarFn(data));
                        //试题列表序号控制
                        $("#navExams>.collapse-inner>.num").click(function(e){
                            e.preventDefault();
                            var $this = $(this);
                            var id = $this.attr("href");
                            $window.scrollTop($(id).offset().top - $("#pageHeader").height() - 60);
                        })

                        data.action = "url";
                        $this.html(examPaperDetailFn(data));

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
