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
<link href="${ctx}/resources/css/DTotal.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${ctx}/resources/css/jquery.autocomplete.css">
<!--[if lt IE 9]>
<script src="js/html5.js"></script>
<![endif]-->

<!-- 授权管理 用户列表 模板 -->
<script id="listUserKinguserTemplate" type="text/x-dot-template">
    <tr data-fdid="{{=it.id}}">
        <td class="tdTit">
            <div class="pr">
                <div class="state-dragable"><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></div>
                <img src="{{=it.imgUrl || 'images/temp-face36.jpg'}}" alt="">{{=it.name}}（{{=it.mail}}），{{=it.org}} {{=it.department}}
            </div>
        </td>
        <td><input type="checkbox" checked class="tissuePreparation" /></td>
        <td><input type="checkbox" class="editingCourse" /></td>
        <td><a href="#" class="icon-remove-blue"></a></td>
    </tr>
</script>

<!-- 试题详情编辑 模板 -->
<script id="examDetailTemplate" type="text/x-dot-template">
    <div class="page-header">
        <a href="./素材库-试卷详情页.html" class="backParent">返回当前试卷</a>
        <h4>{{=it.examPaperName}}</h4>
        <div class="btn-group">
            <button class="btn btn-large btn-primary" id="saveExam" type="button">保存</button>
            <button class="btn btn-white btn-large " id="delExam" type="button">删除</button>
        </div>
    </div>
    <div class="page-body editingBody">
        <form action="{{=it.action || '#'}}" id="formEditDTotal" class="form-horizontal" method="post">
            <section class="section">
                <div class="control-group">
                    <label class="control-label" >题型设置</label>
                    <div class="controls">
                        <input name="examType" id="examType" value="{{=it.examType || 'multiple'}}" type="hidden" />
                        <div class="btn-group btns-radio" data-toggle="buttons-radio">
                            <button class="btn btn-large{{?!it.examType || it.examType == 'multiple'}} active{{?}}" id="multiple" type="button">多项选择题</button>
                            <button class="btn btn-large{{?it.examType == 'single'}} active{{?}}" id="single" type="button">单项选择题</button>
                            <button class="btn btn-large{{?it.examType == 'completion'}} active{{?}}" id="completion" type="button">填空题</button>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="examStem">试题题干</label>
                    <div class="controls"><textarea placeholder="请使用#...#标记填空题的答案，例如：新教师在线备课课程的第三章学习内容是#标准化教案#" rows="4"
                                                    class="input-block-level" required id="examStem"
                                                    name="examStem">{{=it.examStem || ''}}</textarea>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" >试题总分 <small>(单位分)</small></label>
                    <div class="controls">
                        <input name="examScore" id="examScore" value="{{=it.examScore || ''}}" type="hidden"/>
                            <div class="timeLine scoreLine">
                                <div class="num">0</div>
                                {{ for(var i=1; i<=it.examScoreTotal; i++){ }}
                                <a title="{{=i}}分" href="#" style="width: {{=(670-it.examScoreTotal-1)/it.examScoreTotal + 'px'}}"
                                   class="{{?i==1}}first {{?}}{{?it.examScore && i<=it.examScore}}active{{?}}"><span class="num">{{=i}}</span></a>
                                {{ } }}
                            </div>
                    </div>
                </div>
            </section>
            <section class="section mt20">
                <label>辅助材料（上传辅助材料，建议小于2G）</label>
                <div class="control-upload">
                    <div class="upload-fileName"><span id="attName"><span><i class="icon-paperClip"></i></div>

						<div id="qdiv" style="height:20px;width:650px;display:block;"> 
 						 </div>
						 <div style="margin-left:670px;margin-top: 8px;height:40px;width:600px;display:block;">
						     <button id="upMovie" class="btn btn-primary btn-large" type="button" >上传</button>
						 </div>
							<input type="hidden"  name="attId" id="attId">


                </div>
                <ul class="unstyled list-attachment" id="listAttachment">
                    {{~it.listAttachment :att:index}}
                        {{~it.listAttachment :att2:index2}}
                            {{?index == att2.index}}
                                {{#def.item:att2}}
                            {{?}}
                        {{~}}
                    {{~}}
                </ul>
            </section>
            {{#def.examAnswer}}
            <button class="btn btn-block btn-submit btn-inverse" type="submit">保存</button>
        </form>
    </div>
</script>

    <!-- 试题详情 列表项 模板 -->
    <script id="itemExamDetailTemplate" type="text/x-dot-template">
        {{?it.flag === "add"}}
             {{#def.item:it}}
        {{?}}
        {{##def.item:param:
            <li data-fdid="{{=param.id || 'temp'}}">
                <div class="state-dragable"><span class="icon-bar"></span><span
                        class="icon-bar"></span><span class="icon-bar"></span><span
                        class="icon-bar"></span><span class="icon-bar"></span></div>
                <{{?param.url}}a href="{{=param.url}}" target="_blank" {{??}}div{{?}} class="name">{{=param.name}}</{{?param.url}}a{{??}}div{{?}}>
                <div class="item-ctrl">
                    {{?param.isAnswer != undefined}}
                    <label class="{{?it.examType == 'multiple'}}checkbox{{??it.examType == 'single'}}radio{{??}}checkbox{{?}} inline">
                        <input type="{{?it.examType == 'multiple'}}checkbox{{??it.examType == 'single'}}radio{{??}}checkbox{{?}}"
                        {{?param.isAnswer}}checked{{?}} name="isAnswer" />
                        我是答案</label>
                    <a class="icon-pencil-blue" href="#"></a>
                    {{?}}
                    <a href="#" class="icon-remove-blue"></a>
                </div>
            </li>
        #}}
    </script>

    <!-- 试题选项答案 模板 -->
    <script id="examAnswerDetailTemplate" type="text/x-dot-template">
        <section class="section mt20 {{?it.examType == 'completion'}}hide{{?}}" id="examAnswer">
            <div class="hd">
                <h5>
                    试题选项答案
                </h5>
                 <button class="btn btn-primary btn-large pos-right" id="addExamItem" type="button">填加选项</button>
            </div>
            <div class="bd">
                <div class="formAddItem form-horizontal hide" id="formAddItem">
                    <div class="control-group">
                        <label class="control-label" for="nameExamItem">内&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;容</label>
                        <div class="controls">
                            <input type="text" id="nameExamItem" required />
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="">是否答案</label>
                        <div class="controls">
                            <label class="{{?it.examType == 'multiple'}}checkbox{{??it.examType == 'single'}}radio{{??}}checkbox{{?}}" >
                                <input type="{{?it.examType == 'multiple'}}checkbox{{??it.examType == 'single'}}radio{{??}}checkbox{{?}}"  name="isAnswer"/>我是答案</label>
                            <div class="action-btn">
                                <button class="btn btn-primary btn-large" type="button">确定</button>
                                <button class="btn btn-link btn-large" type="button">取消</button>
                            </div>
                        </div>
                    </div>
                </div>
                <ul class="unstyled list-attachment" id="listExamAnswer">
                    {{~it.listExamAnswer :answer:index}}
                        {{~it.listExamAnswer :answer2:index2}}
                            {{?index == answer2.index}}
                                {{#def.item:answer2}}
                            {{?}}
                        {{~}}
                    {{~}}
                </ul>
            </div>
        </section>
    </script>
    <script id="examQuestionTemplate" type="text/x-dot-template">
	    <tr data-fdid="{{=it.id}}" >
		    <td class="tdTit">
		        <div class="pr">
		            <div class="state-dragable"><span class="icon-bar"></span><span
		                    class="icon-bar"></span><span class="icon-bar"></span><span
		                    class="icon-bar"></span><span class="icon-bar"></span></div>
		            <a href="#">{{=it.subject}}</a>
		        </div>
		    </td>
		    <td><input type="text" value="{{=it.score}}" data-toggle="tooltip" title="输入数字做为分值" class="itemScore input-mini">分</td>
		    <td><a href="#" class="icon-remove-blue"></a></td>
		</tr>
    </script>

    <script src="${ctx}/resources/js/doT.min.js"></script>
</head>

<body>
<%-- <header class="navbar navbar-inverse navbar-fixed-top">
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
                	<span class="top-face"><img src="${ctx}/resources/images/temp-face.jpg" alt=""><i class="icon-disc"></i></span>
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
</header> --%>
<input type='hidden' id='materIalId' value='14220b965c8b55ef35ac3564953b7ca5' />
<section class="container">
	<section class="clearfix mt20">
	  <section class="col-left pull-left">
    	<ul class="nav nav-list sidenav" id="sideNav">
                <li class="nav-header first"><a href="#">学习跟踪</a></li>
                <li class="nav-header"><a href="#">授权学习</a></li>
	            <li class="nav-header">
                    <span>课程管理</span>
	            </li>
	            <li><a href="#"><i class="icon-course-series"></i>我的系列课程</a></li>
	            <li><a href="#"><i class="icon-course"></i>我的课程</a></li>
	             <li class="nav-header">
                     <span>课程素材库</span>
	            </li>
                <li><a href="#"><i class="icon-video"></i>视频</a></li>
                <li><a href="#"><i class="icon-doc"></i>文档</a></li>
                <li><a href="#"><i class="icon-ppt"></i>幻灯片</a></li>
                <li class="active"><a href="#"><i class="icon-exam"></i>测试</a></li>
                <li><a href="#"><i class="icon-task"></i>作业</a></li>
	    </ul>
	  </section>
		<section class="w790 pull-right" id="rightCont">
	        <div class="page-header">
                <a href="素材库-测试列表.html" class="backParent">返回测试列表</a>
                <h4>雅思口语强化课程教案解读试卷</h4>
                <div class="btn-group">
                    <button class="btn btn-large btn-primary" id="saveExamPaper" type="button">保存</button>
                    <button class="btn btn-white btn-large " id="delExamPaper" type="button">删除</button>
                </div>
	        </div>
            <div class="page-body editingBody">
                <form action="#" id="formEditDTotal" class="form-horizontal" method="post">
                    <section class="section">
                        <div class="control-group">
                            <label class="control-label" for="examPaperName">试&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;卷</label>
                            <div class="controls">
                                <input id="examPaperName" required class="span6"
                                       name="examPaperName" type="text"><span class="date">2013/02/14 10:01 AM</span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="examPaperIntro">试卷简介</label>
                            <div class="controls"><textarea placeholder="非必填项" rows="4"
                                                            class="input-block-level" id="examPaperIntro"
                                                            name="examPaperIntro"></textarea>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" >建议时间 <small>(单位分钟)</small></label>
                            <div class="controls">
                                <input name="examPaperTime" id="examPaperTime" value="30" type="hidden"/>
                                    <div class="timeLine">
                                        <div class="num">0</div>
                                        <a title="15分钟" href="#" class="first"><span class="num">15</span></a>
                                        <a title="30分钟" href="#" ><span class="num">30</span></a>
                                        <a title="45分钟" href="#"><span class="num">45</span></a>
                                        <a title="60分钟" href="#"><span class="num">60</span></a>
                                        <a title="75分钟" href="#"><span class="num">75</span></a>
                                        <a title="90分钟" href="#"><span class="num">90</span></a>
                                        <a title="105分钟" href="#"><span class="num">105</span></a>
                                        <a title="120分钟" href="#"><span class="num">120</span></a>
                                    </div>
                            </div>
                        </div>

                    </section>
                    <section class="section mt20">
                        <div class="hd">
                            <label for="passScore" class="miniInput-label">
                                试题列表（共计10题，满分50分，及格 <input class="input-mini" id="passScore" name="passScore" value="40" type="text"/>      分）
                            </label>
                            <button class="btn btn-primary btn-large" id="addExam" type="button">添加试题</button>
                        </div>
                        <div class="bd">
                            <table class="table table-bordered">
                                <thead>
                                <tr>
                                    <th>试题</th>
                                    <th>分数</th>
                                    <th>删除</th>
                                </tr>
                                </thead>
                                <tbody id="list_exam">
                               
                               
                               
                               
                                </tbody>
                            </table>
                        </div>
                    </section>
                    <section class="section mt20">
                        <div class="control-group">
                            <label class="control-label" for="author">作&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;者</label>
                            <div class="controls">
                                <input value="集团国外考试推广管理中心" id="author" required class="input-block-level"
                                       name="author" type="text">
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="authorIntro">作者简介</label>
                            <div class="controls"><textarea placeholder="非必填项" rows="4"
                                                            class="input-block-level" id="authorIntro"
                                                            name="authorIntro"></textarea>
                            </div>
                        </div>
                    </section>
                    <div class="page-header mt20"> <h4>权限设置</h4> </div>
                    <section class="section">
                        <label>权限设置<input type="hidden" id="permission" name="permission" value="open"></label>
                        <ul class="nav nav-pills">
                            <li class="active"><a data-toggle="tab" href="#open">公开</a></li>
                            <li><a data-toggle="tab" href="#encrypt">加密</a></li>
                        </ul>
                        <div class="tab-content">
                            <div class="tab-pane active" id="open">
                                提示：“公开”素材将允许所有主管在管理课程的过程中使用，而“加密”素材将允许您手动授权某些主管使用本课程素材。
                            </div>
                            <div class="tab-pane" id="encrypt">
                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th>授权用户</th>
                                        <th>组织备课</th>
                                        <th>编辑课程</th>
                                        <th>删除</th>
                                    </tr>
                                    </thead>
                                    <tbody id="list_user">
                                    <tr data-fdid="fdid325" draggable="true">
                                        <td class="tdTit">
                                            <div class="pr">
                                                <div class="state-dragable"><span class="icon-bar"></span><span
                                                        class="icon-bar"></span><span class="icon-bar"></span><span
                                                        class="icon-bar"></span><span class="icon-bar"></span></div>
                                                <img src="images/temp-face36.jpg" alt="">魏紫（weizi5@xdf.cn），广州学校 国外考试部
                                            </div>
                                        </td>
                                        <td><input type="checkbox" checked="" class="tissuePreparation"></td>
                                        <td><input type="checkbox" class="editingCourse"></td>
                                        <td><a href="#" class="icon-remove-blue"></a></td>
                                    </tr>
                                    <tr data-fdid="fdid324" draggable="true">
                                        <td class="tdTit">
                                            <div class="pr">
                                                <div class="state-dragable"><span class="icon-bar"></span><span
                                                        class="icon-bar"></span><span class="icon-bar"></span><span
                                                        class="icon-bar"></span><span class="icon-bar"></span></div>
                                                <img src="images/temp-face36.jpg" alt="">刘鹍（liukun@xdf.cn），集团总公司 知识管理中心
                                            </div>
                                        </td>
                                        <td><input type="checkbox" class="tissuePreparation"></td>
                                        <td><input type="checkbox" checked="" class="editingCourse"></td>
                                        <td><a href="#" class="icon-remove-blue"></a></td>
                                    </tr>
                                    <tr data-fdid="fdid323" draggable="true">
                                        <td class="tdTit">
                                            <div class="pr">
                                                <div class="state-dragable"><span class="icon-bar"></span><span
                                                        class="icon-bar"></span><span class="icon-bar"></span><span
                                                        class="icon-bar"></span><span class="icon-bar"></span></div>
                                                <img src="http://img.staff.xdf.cn/Photo/06/3A/a911e1178bf3725acd75ddbb9c7e3a06_9494.jpg"
                                                 alt="">杨义锋（yangyifeng@xdf.cn），集团总公司 知识管理中心
                                            </div>
                                        </td>
                                        <td><input type="checkbox" checked="" class="tissuePreparation"></td>
                                        <td><input type="checkbox" checked="" class="editingCourse"></td>
                                        <td><a href="#" class="icon-remove-blue"></a></td>
                                    </tr>
                                    </tbody>
                                </table>
                                <div class="pr">
                                    <input type="text" id="addUser" class="autoComplete input-block-level" placeholder="请输入人名、邮箱、机构或者部门查找用户">
                                    <i class="icon-search"></i>
                                </div>
                            </div>
                        </div>
                    </section>
                    <button class="btn btn-block btn-submit btn-inverse" type="submit">保存</button>
                </form>
            </div>
	    </section>
	</section>

<!--底部 S-->
<%-- 	<footer>
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
	</footer> --%>
<!--底部 E-->
</section>

<script type="text/javascript" src="${ctx}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.placeholder.1.3.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/messages_zh.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.autocomplete.pack.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.sortable.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/resources/uploadify/uploadify.css"/>
<script type="text/javascript" src="${ctx}/resources/uploadify/jquery.uploadify-3.1.min.js?id=1211"></script>
<script src="${ctx}/resources/js/jquery.jalert.js" type="text/javascript"></script>
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
<script type="text/javascript">
$(function(){
    $.Placeholder.init();
    $('.itemScore[data-toggle="tooltip"]').tooltip({
        trigger: "focus"
    });
    $(".timeLine>a").tooltip()
            .click(function(e){
                e.preventDefault();
                $(this).prevAll("a").add(this).addClass("active");
                $(this).nextAll("a").removeClass("active");
                $("#examPaperTime").val( $(this).children(".num").text());
            });

    //初始化试题列表
     var examQuestionTemplate = doT.template(document.getElementById("examQuestionTemplate").text);
   
    $.ajax({
		  url: "${ctx}/ajax/material/getExamQuestionByMaterId?materialId=14220b965c8b55ef35ac3564953b7ca5",
		  async:false,
		  dataType : 'json',
		  success: function(result){
			  var html = "";
			  for(var i in result.qusetions){
				  html += examQuestionTemplate(result.qusetions[i]);
			  }
			  $("#list_exam").html(html); 
			  alert(result.name);
			  $("#examPaperName").val(result.name);
		  }
	});
    
    
    //授权管理 用户列表 模板函数
    var listUserKinguserFn = doT.template(document.getElementById("listUserKinguserTemplate").text);

    //试题详情编辑页面 模板函数
    var examDetailFn = doT.template(document.getElementById("examDetailTemplate").text
            + document.getElementById("itemExamDetailTemplate").text,undefined,{
                examAnswer: document.getElementById("examAnswerDetailTemplate").text
            });

    //试题详情 列表项 模板函数
    var itemExamDetailFn = doT.template(document.getElementById("itemExamDetailTemplate").text);

    $("#formEditDTotal").validate({
        submitHandler: submitForm
    });
    $("#saveExamPaper").click(function(e){
        $("#formEditDTotal").trigger("submit");
    });
    //删除试卷事件
    $("#delExamPaper").click(function(e){
        //
    });

    /*提交表单函数*/
    function submitForm(form){
        var data = {
            examPaperName: $("#examPaperName").val(),
            examPaperIntro: $("#examPaperIntro").val(),
            author: $("#author").val(),
            authorIntro: $("#authorIntro").val(),
            permission:$("#permission").val(),
            listExam: [],
            kingUser: []
        };
        if($("#list_exam").children("tr").length){
            //push 试题列表数据
            $("#list_exam>tr").each(function(i){
                data.listExam.push({
                    id: $(this).attr("data-fdid"),
                    index: i,
                    editingCourse: $(this).find(".itemScore").val()
                });
            });
        }
        if(data.permission === "encrypt"){
            //push人员授权数据
            $("#list_user>tr").each(function(i){
                data.kingUser.push({
                    id: $(this).attr("data-fdid"),
                    index: i,
                    tissuePreparation: $(this).find(".tissuePreparation").is(":checked"),
                    editingCourse: $(this).find(".editingCourse").is(":checked")
                });
            });
        }
        alert(JSON.stringify(data));
        //console.log(JSON.stringify(data));
        //ajax
        /*$.post("url?updata",data)
         .success(function(){
         alert("保存成功");
         });*/
    }
    $('#formEditDTotal a[data-toggle="tab"]').on('shown', function (e) {
        var href = 	e.target.href.split("#").pop();
        $("#permission").val(href);
    });
    $("#list_user,#list_exam").sortable({
        handle: '.state-dragable'
    })
            .find("a.icon-remove-blue").bind("click",function(e){
                e.preventDefault();
                $(this).closest("tr").remove();
            });

    var allUserData ;

    allUserData = [
        {
            id: "fdid3232323",
            imgUrl: "http://img.staff.xdf.cn/Photo/06/3A/a911e1178bf3725acd75ddbb9c7e3a06_9494.jpg",
            name: "杨小锋",
            mail: "yangyifeng@xdf.cn",
            org: "集团总公司",
            department: "知识管理中心"
        },
        {
            id: "fdid32234",
            imgUrl: "",
            name: "刘小鹍",
            mail: "liukun@xdf.cn",
            org: "集团总公司",
            department: "知识管理中心"
        },
        {
            id: "fdid328",
            imgUrl: "",
            name: "魏小紫",
            mail: "weizi5@xdf.cn",
            org: "广州学校",
            department: "国外考试部"
        }
    ]
    /*$("#addUser").autocomplete("url.jsp",{
     dataType: "json",
     parse: function(data) {
     return $.map(data, function(row) {
     return {
     data: row,
     value: row.name,
     result: row.name + " <" + row.mail + ">"
     }
     });
     },*/
    $("#addUser").autocomplete(allUserData,{
        formatMatch: function(item) {
            return item.name + item.mail + item.org + item.department;
        },
        formatItem: function(item) {
            return '<img src="'
                    + (item.imgUrl || 'images/temp-face36.jpg') + '" alt="">'
                    + item.name + '（' + item.mail + '），'
                    + item.org + '  ' + item.department;
        },
        matchContains:true ,
        max: 10,
        scroll: false,
        width:748
    }).result(function(e,item){
                $(this).val(item.name);
                $("#list_user").append(listUserKinguserFn(item))
                        .sortable({
                            handle: '.state-dragable',
                            forcePlaceholderSize: true
                        })
                        .find("a.icon-remove-blue").bind("click",function(e){
                            e.preventDefault();
                            $(this).closest("tr").remove();
                        });

            });


    //添加试题事件
    $("#addExam").bind("click",function(e){
        loadExamPage();
    });

    //打开试题列表中的试题详情
    $("#list_exam>tr>.tdTit .pr a").click(function(e){
        e.preventDefault();
        var id = $(this).closest("tr").attr("data-fdid");
        loadExamPage(id);
        //
    });

    /*加载试题页面*/
    function loadExamPage(fdid){
        var materialName = $("#examPaperName").val();
        if(materialName==""||materialName==null){
        	$.fn.jalert2("请先设置试卷名称");
        	return;
        }
    	var data = {};
        if( !fdid){//添加试题数据
            data = {
                examScoreTotal: 20,
                examType: "multiple"
            };
        } else {
            
        	materIalId
        	// ajax 获取已存在的试题数据
            /*$.get("url",{id: fdid}).success(function(result){
             data = result;
             })*/

            data = {// ajax 后删除
                examType: "completion",//multiple, single, completion
                examScoreTotal: 20,
                examScore: 10,
                examStem: "2011年前十一个月，某省高新技术产业完成总产值3763.00亿元，实现增加值896.31亿元。增加值同比增长30.74%，" +
                        "比规模以上工业增加值高11.64个百分点，占规模以上工业增加值的比重达到25.32%。" +
                        "高新技术产业各领域的增加值如下图所示：  （5 分）  PASS ",
                listAttachment: [
                    {
                        id:"fdid4205325wef",
                        index: 0,
                        name: "高新技术产业各领域增加值饼形图（单位：亿元）.jpg",
                        url: "#"
                    },
                    {
                        id:"fdid97867",
                        index: 3,
                        name: "高新技术产业各领域咨询报告.pdf",
                        url: "#"
                    },
                    {
                        id:"fdid11111443432",
                        index: 2,
                        name: "高新技术产业各领域专家分析讲座.mp4",
                        url: "#"
                    },
                    {
                        id:"fdid9849284",
                        index: 1,
                        name: "高新技术产业各领域咨询报告2.pdf",
                        url: "#"
                    }
                ],
                listExamAnswer:[
                    {
                        id: "fdid12332323",
                        index: "0",
                        name: "第一课最开始的时候",
                        isAnswer: false
                    },
                    {
                        id: "fdid546565",
                        index: "1",
                        name: "第二课最开始的时候",
                        isAnswer: true
                    }
                ]
            };
        } 
        data.examPaperName = "雅思口语强化课程教案解读试卷";  //当前试卷名称
        $("#rightCont").html(examDetailFn(data));

        //应用拖放效果
        $(".list-attachment").sortable({
            handle: '.state-dragable'
        })
                //移除和编辑列表项
                .delegate(".item-ctrl>.icon-remove-blue","click",function(e){
                    e.preventDefault();
                    $(this).closest("li").remove();
                })
                .delegate(".item-ctrl>.icon-pencil-blue","click",function(e){
                    e.preventDefault();
                    var $this = $(this);
                    var $name = $this.hide().parent().prev(".name");
                    var _txt = $name.text();
                    var $inpt = $('<input type="text" required value="' + _txt + '" name="isAnswer" />');
                    var $btns = $('<div class="action-btn">\
                            <button class="btn btn-primary btn-large" type="button">确定</button>\
                            <button class="btn btn-link btn-large" type="button">取消</button>\
                            </div>');
                    var $checkbox = $this.prev("label").detach();
                    $name.html($inpt)
                            .append($checkbox.removeClass("inline"))
                            .append($btns);
                    $btns.find(".btn").click(function(e){
                        if($(this).hasClass("btn-primary")){
                            if(validator.element($inpt)){
                                $name.html($inpt.val());
                                $name.next(".item-ctrl").prepend($checkbox.addClass("inline"));
                                $this.show();
                            }
                        } else {
                            $name.html($inpt.val());
                            $name.next(".item-ctrl").prepend($checkbox.addClass("inline"));
                            $this.show();
                        }
                    });
                });

        $(".scoreLine>a").tooltip()
                .click(function(e){//分数控制
                    e.preventDefault();
                    $(this).prevAll("a").add(this).addClass("active");
                    $(this).nextAll("a").removeClass("active");
                    $("#examScore").val( $(this).children(".num").text());
                });





        var validator = $("#formEditDTotal").validate({
            submitHandler: submitForm
        });
        $("#saveExam").click(function(e){
            $("#formEditDTotal").trigger("submit");
        });
        var $formAdd = $("#formAddItem");
        $formAdd.find(".action-btn>.btn").click(function(e){
            if($(this).hasClass("btn-primary")){
                if(validator.element("#nameExamItem")){
                    $("#listExamAnswer").append(itemExamDetailFn({
                        flag: "add",
                        name: $("#nameExamItem").val(),
                        isAnswer: $formAdd.find("input[name='isAnswer']").is(":checked"),
                        examType: $("#examType").val()
                    })).sortable({
                            handle: ".state-dragable"
                        });
                    $formAdd.addClass("hide");
                    $formAdd.find("#nameExamItem").val("");
                    $formAdd.find("input[name='isAnswer']").removeAttr("checked");
                }
            } else {
                $formAdd.addClass("hide");
            }
        });

        $("#addExamItem").click(function(e){
            $formAdd.removeClass("hide");
        });

        //试题类型选择
        $('[data-toggle="buttons-radio"]>.btn').click(function(){
            $("#examType").val(this.id);
            if(this.id == "completion"){
                $("#examAnswer").addClass("hide");
            } else {
                $("#examAnswer").removeClass("hide");
                if(this.id == "single"){
                    $("#listExamAnswer>li .checkbox,#formAddItem .checkbox").removeClass("checkbox").addClass("radio")
                            .children(":checkbox").after('<input type="radio" name="isAnswer" />').remove();
                } else if(this.id == "multiple"){
                    $("#listExamAnswer>li .radio,#formAddItem .radio").removeClass("radio").addClass("checkbox")
                            .children(":radio").after('<input type="checkbox" name="isAnswer" />').remove();
                }
            }
        });

        /*提交试题详情表单函数*/
        function submitForm(form){
            alert("--------");
        	var data = {
                examType: $("#examType").val(),
                examStem: $("#examStem").val(),
                examScore: $("#examScore").val(),
                listAttachment: [],
                listExamAnswer: []
            };
            if($("#listAttachment>li").length){
                //push 附件列表数据
                $("#listAttachment>li").each(function(i){
                    data.listAttachment.push({
                        id: $(this).attr("data-fdid"),
                        index: i,
                        url: $(this).find(".name").attr("href")
                    });
                });
                data.listAttachment =  JSON.stringify(data.listAttachment);
            }
            if(data.examType != "completion" && $("#listExamAnswer>li").length){
                //push 试题答案列表数据
                $("#listExamAnswer>li").each(function(i){
                	data.listExamAnswer.push({
                        id: $(this).attr("data-fdid"),
                        index: i,
                        name: $(this).find(".name").text(),
                        isAnswer: $(this).find("input:checked").is(":checked")
                    });
                });
                data.listExamAnswer =  JSON.stringify(data.listExamAnswer);
            }
            if(JSON.stringify(data.examScore)=='""')
            {
            	$.fn.jalert2("请设置分数");
            	return;
            }
            if(JSON.stringify(data.examType)!='"completion"'&&JSON.stringify(data.listExamAnswer)=="[]"){
            	$.fn.jalert2("请输入试题答案");
            	return;
            }
            //ajax
        	$.ajax({
				  url: "${ctx}/ajax/examquestion/saveOrUpdateExamQuestion?materialName="+materialName,
				  async:false,
				  data:data,
				  type: "POST",
				  dataType:'json',
				  success: function(rsult){
					 //alert("保存修改成功");
					 alert(JSON.stringify(rsult));
					 $("#rightCont").html(itemExamDetailFn(data));
				  },
			});
        }
        jQuery("#upMovie").uploadify({
            'height' : 27,
            'width' : 80,
            'multi' : false,// 是否可上传多个文件
            'simUploadLimit' : 1,
            'swf' : '${ctx}/resources/uploadify/uploadify.swf',
            'buttonText' : '上 传',
            'uploader' : '${ctx}/common/file/o_upload',
            'auto' : true,// 选中后自动上传文件
            'queueID': 'qdiv',// 文件队列div
            'fileSizeLimit':2048,// 限制文件大小为2m
            'queueSizeLimit':1,
            'onUploadStart' : function (file) {
                jQuery("#upMovie").uploadify("settings", "formData");
            },
            'onUploadSuccess' : function (file, datas, Response) {
                if (Response) {
                    var objvalue = eval("(" + datas + ")");
                    jQuery("#attName").html(objvalue.fileName);
                    $("#listAttachment").append(itemExamDetailFn({
                   	 	flag: "add" ,
                   	 	id: objvalue.attId,
                        name: objvalue.fileName,
                        url: "#"
                   })).sortable({
                           handle: ".state-dragable"
                   });
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
    }
});
</script>
</body>
</html>
