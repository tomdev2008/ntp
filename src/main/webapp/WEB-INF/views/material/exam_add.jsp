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
       <td><input type="checkbox" {{?it.tissuePreparation!=false}}checked{{?}}  class="tissuePreparation" /></td>
        <td><input type="checkbox" {{?it.editingCourse!=false}}checked{{?}} class="editingCourse" /></td>
        <td><a href="#" class="icon-remove-blue"></a></td>
    </tr>
</script>

<!-- 试题详情编辑 模板 -->
<script id="examDetailTemplate" type="text/x-dot-template">
    <div class="page-header">
        <a href="${ctx}/material/materialAddFoward?fdId=${param.fdId}&fdType=${param.fdType}" class="backParent">返回当前试卷</a>
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
                                                    name="examStem">{{=it.examStem || ''}}</textarea><label id="examStemErr" for="examStem" class="error"></label>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" >试题总分 <small>(单位分)</small></label>
                    <div class="controls">
                        <input name="examScore" id="examScore" value="{{=it.examScore || ''}}" type="hidden"/>
                            <div class="timeLine scoreLine">
                                <div class="num">0</div>
                                {{ for(var i=1; i<=20; i++){ }}
                                <a title="{{=i}}分" href="#" style="width: {{=(670-20-1)/20 + 'px'}}"
                                   class="{{?i==1}}first {{?}}{{?it.examScore && i<=it.examScore}}active{{?}}"><span class="num">{{=i}}</span></a>
                                {{ } }}
                            </div>
					<label id="questionScoreErr" class="error" style="display: none;"></label>
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
<label id="answerErr" class="error" style="display: block;"></label>
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
		    <td><input type="text" onblur="initScore()" value="{{=it.score}}" data-toggle="tooltip" title="输入数字做为分值" class="itemScore input-mini">分</td>
		    <td><a href="#" class="icon-remove-blue"></a></td>
		</tr>
    </script>

    <script src="${ctx}/resources/js/doT.min.js"></script>
</head>

<body>
<input type='hidden' id='materIalId' value='${param.fdId}' />
<section class="container">
	<section class="clearfix mt20">
	  <section class="col-left pull-left">
    	 <%@ include file="/WEB-INF/views/group/menu.jsp" %>
	  </section>
		<section class="w790 pull-right" id="rightCont">
	        <div class="page-header">
                <a href="${ctx}/material/findList?fdType=08" class="backParent">返回测试列表</a>
                <h4 id="examMainName"></h4>
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
                                       name="examPaperName" type="text"> <label id="examPaperNameErr" for="examPaperName" class="error" style="display: none;"></label><span class="date" id="createTime"></span>
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
                                    <div id="mainTimeLine" class="timeLine">
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
                                试题列表（共计<span id="questionCount"></span>题，满分<span id="questionScore"></span>分，及格 <input class="input-mini" required number="true" id="passScore" name="passScore" type="text"/>      分）
                            </label>
                            <label for="passScore" id="passScoreErr" class="error"></label>
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
                                <input value="" id="author" required class="input-block-level"
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
                            <li class="active"><a id="open1" data-toggle="tab" href="#open">公开</a></li>
                            <li ><a id="close1" data-toggle="tab" href="#encrypt">加密</a></li>
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
                $("#questionScoreErr").css("display","none");
            });
	//初始化页面
	if("${param.fdId}"!=null&&"${param.fdId}"!=""){
		 $.ajax({
			  url: "${ctx}/ajax/material/getMaterial?materialId=${param.fdId}",
			  async:false,
			  dataType : 'json',
			  success: function(result){
				  $("#examPaperIntro").val(result.description);
				  $("#passScore").val(result.score);
				  $("#author").val(result.fdAuthor);
				  $("#authorIntro").val(result.fdAuthorDescription);
				  $("#createTime").html(result.createTime);
				  
				  if(result.isPublish==true){
					  $("#open1").trigger("click");
					  $("#permission").val("open");
				  }else{
					  $("#close1").trigger("click");
					  $("#permission").val("encrypt");
				  }
				  var n = result.time/15;
				  $("#mainTimeLine a :lt("+n+")").attr("class","active");
			  }
		});
	    //初始化试题列表
	   initExamQuestions();
	   //初始化权限列表
	   var creater="";
	   var url="";
	    $.ajax({
			  url: "${ctx}/ajax/material/getCreater?materialId=${param.fdId}",
			  async:false,
			  dataType : 'json',
			  success: function(result){
				  creater = result.name+"（"+result.email+"），"+result.dept;
				  url=result.url;
			  }
		});
	    var listUserKinguserFn = doT.template(document.getElementById("listUserKinguserTemplate").text);
	    $.ajax({
			  url: "${ctx}/ajax/material/getAuthInfoByMaterId?MaterialId=${param.fdId}",
			  async:false,
			  dataType : 'json',
			  success: function(result){
				  var html = "<tr data-fdid='creater' draggable='true'> "+
				  " <td class='tdTit'> <div class='pr'> <div class='state-dragable'><span class='icon-bar'></span><span class='icon-bar'></span><span class='icon-bar'></span><span class='icon-bar'></span><span class='icon-bar'></span></div> "+
				  "<img src='"+url+"' alt=''>"+creater+" </div> </td>"+
				  " <td><input type='checkbox' onclick='return false' checked='' class='tissuePreparation'></td> <td>"+
				  "<input type='checkbox' onclick='return false' checked='' class='editingCourse'></td> <td></a>"+
				  "</td> </tr>";
				  for(var i in result.user){
					  html += listUserKinguserFn(result.user[i]);
				  }
				  $("#list_user").html(html); 
				  
			  }
		});
	}
	
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
    	$.fn.jalert("您确认要删除当前测试？",function(){
    		$.ajax({
  			  url: "${ctx}/ajax/material/deleteMaterial?materialId=${param.fdId}",
  			  async:false,
  			  success: function(result){
  				  window.location.href="${ctx}/material/findList?order=FDCREATETIME&fdType=08";
  			  }
  		});
    	});
    	
    });

    /*提交表单函数*/
    function submitForm(form){
    	if(parseInt($("#passScore").val())>parseInt($("#questionScore").html())){
    		$("#passScoreErr").css("display","block");
    		$("#passScoreErr").html("及格分数不能大于满分");
    		return;
    	}
        var data = {
        	id:"${param.fdId}",
            examPaperName: $("#examPaperName").val(),
            examPaperIntro: $("#examPaperIntro").val(),
            author: $("#author").val(),
            authorIntro: $("#authorIntro").val(),
            permission:$("#permission").val(),
            score:$("#passScore").val(),
            studyTime:$("#examPaperTime").val(),
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
            data.listExam = JSON.stringify(data.listExam);
        }
        if(data.permission === "encrypt"){
            $("#list_user>tr").each(function(i){
                data.kingUser.push({
                    id: $(this).attr("data-fdid"),
                    index: i,
                    tissuePreparation: $(this).find(".tissuePreparation").is(":checked"),
                    editingCourse: $(this).find(".editingCourse").is(":checked")
                });
            });
            data.kingUser = JSON.stringify(data.kingUser);
        }
        if(data.listExam.length==0){
        	$.fn.jalert2("请输入试题");
        }
        if(data.permission === "encrypt"&&data.kingUser.length==0){
        	$.fn.jalert2("请输入用户");
        }
        $.ajax({
			  url:"${ctx}/ajax/examquestion/UpdateExamQuestionAndMaterial",
			  async:false,
			  data:data,
			  dataType:'json',
			  success: function(rsult){
				  window.location.href="${ctx}/material/findList?order=FDCREATETIME&fdType=08";
			  },
		});
    }
    
    
    $('#formEditDTotal a[data-toggle="tab"]').on('click', function (e) {
        var href = 	e.target.href.split("#").pop();
        $("#permission").val(href);
    });
    $("#list_user,#list_exam").sortable({
        handle: '.state-dragable'
    })
            .find("a.icon-remove-blue").bind("click",function(e){
                e.preventDefault();
                $(this).closest("tr").remove();
                initScore();
            });


    $("#addUser").autocomplete("${ctx}/ajax/user/findByName",{
        formatMatch: function(item) {
            return item.name + item.mail + item.org + item.department;
        },
        formatItem: function(item) {
            return '<img src="'
                    + (item.imgUrl || 'images/temp-face36.jpg') + '" alt="">'
                    + item.name + '（' + item.mail + '），'
                    + item.org + '  ' + item.department;
        },
        parse : function(data) {
        	var rows = [];
			for ( var i = 0; i < data.length; i++) {
				rows[rows.length] = {
					data : data[i],
					value : data[i].name,
					result : data[i].name
				//显示在输入文本框里的内容 ,
				};
			}
			return rows;
		},
		dataType : 'json',
		matchContains:true ,
		max: 10,
		scroll: false,
		width:688
    }).result(function(e,item){
		var flag = true;
		$("#addUser").next(".help-block").remove();
		$("#list_user>tr").each(function(){
			if($(this).attr("data-fdid")==item.id){
				$("#addUser").after('<span class="help-block">不能添加重复的用户！</span>');;
				$("#addUser").val("");
				flag = false;
			}
		});
		if(flag){
			$(this).val(item.name);
			$("#list_user").append(listUserKinguserFn(item))
			.sortable({
				handle: '.state-dragable',
				forcePlaceholderSize: true
			})
			.find("a.icon-remove-blue").bind("click",function(e){
				e.preventDefault();
				$(this).closest("tr").remove();
				initScore();
			});
			$("#addUser").val("");
		}

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
        	$("#examPaperNameErr").html("请先设置试卷名称");
        	$("#examPaperNameErr").css("display","block");
        	return;
        }
    	var data = {};
        if( !fdid){//添加试题数据
            data = {
                examScoreTotal: 20,
                examType: "multiple"
            };
        } else {
        	
			 $.ajax({
				  url:"${ctx}/ajax/examquestion/getExamsByMaterialId",
				  async:false,
				  data:{id: fdid},
				  dataType:'json',
				  success: function(rsult){
						 data = rsult;
				  },
			});

        } 
        data.examPaperName = $("#examPaperName").val();  //当前试卷名称
        $("#rightCont").html(examDetailFn(data));

        //应用拖放效果
        $(".list-attachment").sortable({
            handle: '.state-dragable'
        })
                //移除和编辑列表项
                .delegate(".item-ctrl>.icon-remove-blue","click",function(e){
                    e.preventDefault();
                    $(this).closest("li").remove();
                    initScore();                    
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
                    $("#questionScoreErr").css("display","none");
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
        	if($("#examType").val()=="completion"){
        		var examSub=$("#examStem").val();
            	if((examSub.split("#").length%2==0)){
            		$("#examStemErr").html("题干输入有误，请仔细查看修改后再提交");
            		$("#examStemErr").css("display","block");
            		return;
            	}
        	}
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
                        url: $(this).find(".name").attr("href"),
                        name:$(this).find(".name").html(),
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
            	$("#questionScoreErr").html("请设置分数");
            	$("#questionScoreErr").css("display","block");
            	return;
            }
            if(JSON.stringify(data.examType)!='"completion"'&&JSON.stringify(data.listExamAnswer)=="[]"){
            	$("#answerErr").html("请输入试题答案");
            	$("#answerErr").css("display","block");
            	return;
            }
            //ajax
        	$.ajax({
				  url: "${ctx}/ajax/examquestion/saveOrUpdateExamQuestion?questionId="+fdid+"&materIalId=${param.fdId}&materialName="+materialName,
				  async:false,
				  data:data,
				  type: "POST",
				  dataType:'json',
				  success: function(rsult){
					 window.location.href="${ctx}/material/materialFoward?fdId="+rsult.materIalId+"&fdType=08";
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
                        url: objvalue.filePath
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
        if(fdid==null||fdid==""){
        	$("#delExam").remove();
        }else{
        	$("#delExam").bind("click",function(){
        		$.fn.jalert("确认删除试题？",function(){
	        			$.ajax({
	          			  url: "${ctx}/ajax/examquestion/deleQuestionToExam",
	          			  async:false,
	          			  dataType : 'json',
	          			  data:{
	          				  questionId : fdid,
	          				  examId :"${param.fdId}",
	          			  },
	          			  success: function(result){
	          				  
	          			  }
	          			});
					    window.location.href="${ctx}/material/materialFoward?fdId=${param.fdId}&fdType=08";
				  });
        		
        	});
        }
    }
});
    
function initExamQuestions(){
	var id = $("#materIalId").val();
	if(id!==null&&id!=""){
		var examQuestionTemplate = doT.template(document.getElementById("examQuestionTemplate").text);
	    $.ajax({
			  url: "${ctx}/ajax/material/getExamQuestionByMaterId?materialId=${param.fdId}",
			  async:false,
			  dataType : 'json',
			  success: function(result){
				  var html = "";
				  
				  for(var i in result.qusetions){
					  html += examQuestionTemplate(result.qusetions[i]);
				  }
				  $("#list_exam").html(html); 
				  $("#examPaperName").val(result.name);
				  $("#examMainName").html(result.name);
				  initScore();
				  
			  }
		});
	}
	  
}

function initScore(){
	var totalScore = 0;
	var count = 0;
	for(var i=0 ; i<$("#list_exam input").length ;i++){
		count++;
		totalScore = totalScore +parseInt($("#list_exam input :eq("+i+")").val());
	}
	 $("#questionCount").html(count);
	  $("#questionScore").html(totalScore);
}

</script>
</body>
</html>
