<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html class=""> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="${ctx}/resources/css/DTotal.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${ctx}/resources/css/jquery.autocomplete.css">
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

<!-- 作业详情编辑 模板 -->
<script id="examDetailTemplate" type="text/x-dot-template">
    <div class="page-header">
        <a href="${ctx}/material/materialFoward?fdId=${materialInfo.fdId}&fdType=10" class="backParent">返回当前作业包</a>
        <h4>{{=it.examPaperName}}</h4>
        <div class="btn-group">
            <button class="btn btn-large btn-primary" id="saveExam" type="button">保存</button>
            <button class="btn btn-large btn-primary" disabled id="exportExam" type="button">导出</button>
            <button class="btn btn-white btn-large " id="delExam" type="button">删除</button>
        </div>
    </div>
    <div class="page-body editingBody">
        <form action="{{=it.action || '#'}}" id="formEditDTotal" class="form-horizontal" method="post">
            <section class="section">
                <div class="control-group">
                    <label class="control-label" >题型设置</label>
                    <div class="controls">
                        <input name="examType" id="examType" value="{{=it.examType || 'uploadWork'}}" type="hidden" />
                        <div class="btn-group btns-radio" data-toggle="buttons-radio">
                            <button class="btn btn-large{{?!it.examType || it.examType == 'uploadWork'}} active{{?}}" id="uploadWork" type="button">上传作业</button>
                            <button class="btn btn-large{{?it.examType == 'onlineAnswer'}} active{{?}}" id="onlineAnswer" type="button">在线作答</button>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="examName">作&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业</label>
                    <div class="controls">
                        <input value="{{=it.examName || ''}}" id="examName" placeholder="请输入作业名称" required class="span6"
                               name="examName" type="text"><span class="date"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="examStem">作业简介</label>
                    <div class="controls"><textarea placeholder="请使用#...#标记填空题的答案，例如：新教师在线备课课程的第三章学习内容是#标准化教案#" rows="4"
                                                    class="input-block-level" required id="examStem"
                                                    name="examStem">{{=it.examStem || ''}}</textarea>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" >作业总分 <small>(单位分)</small></label>
                    <div class="controls">
                        <input name="examScore" id="examScore" value="{{=it.examScore || ''}}" type="hidden"/>
                            {{#def.timeLine:it.timeLine}}
                    </div>
                </div>
            </section>
            <section class="section mt20">
                <label>辅助材料（上传辅助材料，建议小于2G）</label>
                <div class="control-upload">
                    <div class="upload-fileName"><span id="attName"></span><i class="icon-paperClip"></i></div>
                   
 				    <div id="qdiv" style="height:20px;width:650px;display:block;"> </div>
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

            <button class="btn btn-block btn-submit btn-inverse" type="submit">保存</button>
        </form>
    </div>
</script>

    <!-- 时间轴 模板 -->
    <script id="timeLineTemplate" type="text/x-dot-template">
        {{?!it.timeLine}}
            {{#def.timeLine:it}}
        {{?}}
        {{##def.timeLine:param:
            <div class="timeLine">
                    <div class="num">0</div>
            {{ for(var i=1; i <= param.total; i++){ }}
            <a title="{{=i*param.span}}{{=it.unit || ''}}" href="#" style="width: {{=(param.width-param.total-1)/param.total}}px"
            class="{{?i*param.span==param.span}}first {{?}}{{?param.curPos && i*param.span<=param.curPos}}active{{?}}"><span class="num">{{=i*param.span}}</span></a>
            {{ } }}
            </div>
        #}}
    </script>

    <!-- 作业详情 列表项 模板 -->
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
                    <label class="{{?it.examType == 'uploadWork'}}checkbox{{??it.examType == 'onlineAnswer'}}radio{{??}}checkbox{{?}} inline">
                        <input type="{{?it.examType == 'uploadWork'}}checkbox{{??it.examType == 'onlineAnswer'}}radio{{??}}checkbox{{?}}"
                        {{?param.isAnswer}}checked{{?}} name="isAnswer" />
                        我是答案</label>
                    <a class="icon-pencil-blue" href="#"></a>
                    {{?}}
                    <a href="#" class="icon-remove-blue"></a>
                </div>
            </li>
        #}}
    </script>
    <script id="taskPaperListTemplate" type="text/x-dot-template">
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

<section class="container">
	<section class="clearfix mt20">
	  <section class="col-left pull-left">
	  <%@ include file="/WEB-INF/views/group/menu.jsp" %>
	  </section>
		<section class="w790 pull-right" id="rightCont">
	        <div class="page-header">
                <a href="${ctx}/material/findList?fdType=10&order=FDCREATETIME" class="backParent">返回作业包列表</a>
                <h4 id="fdName"></h4>
                <div class="btn-group">
                    <button class="btn btn-large btn-primary" id="saveExamPaper" type="button">保存</button>
                    <button class="btn btn-large btn-primary" disabled id="exportExamPaper" type="button">导出</button>
                    <button class="btn btn-white btn-large " id="delExamPaper" type="button">删除</button>
                </div>
	        </div>
            <div class="page-body editingBody">
                <form action="#" id="formEditDTotal" class="form-horizontal" method="post">
                    <section class="section">
                        <div class="control-group">
                            <label class="control-label" for="examPaperName">作业包</label>
                            <div class="controls">
                                <input  id="examPaperName" required class="span6"
                                       name="examPaperName" type="text">
                                <span class="date" id="createTime"></span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="examPaperIntro">作业包<div>简介</div></label>
                            <div class="controls">
                            <textarea placeholder="非必填项" rows="4"
                                      class="input-block-level" id="examPaperIntro"
                                      name="examPaperIntro"></textarea>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" >建议时间 <small>(单位天)</small></label>
                            <div class="controls">
                                <input name="examPaperTime" id="examPaperTime"  type="hidden"/>
                            </div>
                        </div>

                    </section>
                    <section class="section mt20">
                        <div class="hd">
                            <label for="passScore" class="miniInput-label">
                                                                       作业列表（共计<span id="count"></span>题，满分<span id="totalScore"></span>分，及格 
                                <input class="input-mini" id="passScore" number="true" required="required"  name="passScore"  type="text"/>      分）
                            </label>
                            <label for="passScore" id="passScoreErr" class="error"></label>
                            <button class="btn btn-primary btn-large" id="addExam" type="button">添加作业</button>
                        </div>
                        <div class="bd">
                            <table class="table table-bordered">
                                <thead>
                                <tr>
                                    <th>作业</th>
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
                                <input id="author" required class="input-block-level"
                                       name="author" type="text">
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="authorIntro">作者简介</label>
                            <div class="controls">
                            <textarea placeholder="非必填项" rows="4"
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
<input type="hidden" id="materialId" value="${materialInfo.fdId}"/>
</section>
<script type="text/javascript" src="${ctx}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.placeholder.1.3.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/messages_zh.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.autocomplete.pack.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.sortable.js"></script>
<script type="text/javascript" src="${ctx}/resources/uploadify/jquery.uploadify-3.1.min.js?id=1211"></script>
<script src="${ctx}/resources/js/jquery.jalert.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
    $.Placeholder.init();
    $('.itemScore[data-toggle="tooltip"]').tooltip({
        trigger: "focus"
    });
  //初始化页面
  //时间轴 模板函数
    var timeLineFn = doT.template(document.getElementById("timeLineTemplate").text);
	if("${param.fdId}"!=null&&"${param.fdId}"!=""){
		 $.ajax({
			  url: "${ctx}/ajax/material/getMaterial?materialId=${param.fdId}",
			  async:false,
			  dataType : 'json',
			  success: function(result){
				  $("#examPaperIntro").val(result.description);
				  $("#fdName").html(result.fdName);
				  $("#examPaperName").val(result.fdName);
				  $("#passScore").val(result.score);
				  $("#examPaperTime").val(result.time);
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
				  $("#examPaperTime").after(timeLineFn({//时间轴控件 配置数据
				        width: 670, //时间轴控件 宽度
				        total: 7, //总格数
				        curPos: result.time, //当前位置
				        span: 1, //每格的进制
				        unit: "天"
				    }));
			  }
		});
	}
});
/**
 * Created by wqh on 13-11-1.
 */
$(function(){
    //授权管理 用户列表 模板函数
    var listUserKinguserFn = doT.template(document.getElementById("listUserKinguserTemplate").text);
    //时间轴 模板函数
    var timeLineFn = doT.template(document.getElementById("timeLineTemplate").text);
    //初始化作业列表
    initExamPaperList();
    //初始化授权人员列表
    //初始化权限列表
	var creator="";
	var url="";
	$.ajax({
		url: "${ctx}/ajax/material/getCreater?materialId=${param.fdId}",
		async:true,
		dataType : 'json',
		success: function(result){
		  creator = result.name+"（"+result.email+"），"+result.dept;
				  url=result.url;
		}
	});
    $.ajax({
		url: "${ctx}/ajax/material/getAuthInfoByMaterId?MaterialId=${param.fdId}",
		async:true,
		dataType : 'json',
		success: function(result){
			var html = "<tr data-fdid='creator' draggable='true'> "+
			  " <td class='tdTit'> <div class='pr'> <div class='state-dragable'><span class='icon-bar'></span><span class='icon-bar'></span><span class='icon-bar'></span><span class='icon-bar'></span><span class='icon-bar'></span></div> "+
			  "<img src='"+url+"' alt=''>"+creator+" </div> </td>"+
			  " <td><input type='checkbox' onclick='return false' checked='' class='tissuePreparation'></td> <td>"+
			  "<input type='checkbox' onclick='return false' checked='' class='editingCourse'></td> <td></a>"+
			  "</td> </tr>";
			for(var i in result.user){
				  html += listUserKinguserFn(result.user[i]);
			}
			$("#list_user").html(html); 
		  }
	});
   
    $(".timeLine>a").tooltip()
            .click(function(e){
                e.preventDefault();
                $(this).prevAll("a").add(this).addClass("active");
                $(this).nextAll("a").removeClass("active");
                $("#examPaperTime").val( $(this).children(".num").text());
            });

    //作业详情编辑页面 模板函数
    var examDetailFn = doT.template(document.getElementById("examDetailTemplate").text
            + document.getElementById("itemExamDetailTemplate").text
            + document.getElementById("timeLineTemplate").text);

    //作业详情 列表项 模板函数
    var itemExamDetailFn = doT.template(document.getElementById("itemExamDetailTemplate").text);

    $("#formEditDTotal").validate({
        submitHandler: submitForm
    });
    $("#saveExamPaper").click(function(e){
        $("#formEditDTotal").trigger("submit");
    });
    //删除作业包事件
    $("#delExamPaper").click(function(e){
    	$.ajax({
			  url: "${ctx}/ajax/material/deleteMaterial?materialId=${param.fdId}",
			  async:false,
			  success: function(result){
				  $.fn.jalert2("删除成功");
				  window.location.href="${ctx}/material/findList?order=FDCREATETIME&fdType="+'${param.fdType}';
			  }
		});
    });

    /*提交表单函数*/
    function submitForm(form){
    	if(parseInt($("#passScore").val())>parseInt($("#totalScore").html())){
    		$("#passScoreErr").css("display","block");
    		$("#passScoreErr").html("及格分数不能大于满分");
    		return;
    	}
        var data = {
            materialId:$("#materialId").val(),
        	examPaperName: $("#examPaperName").val(),
            examPaperIntro: $("#examPaperIntro").val(),
            author: $("#author").val(),
            authorIntro: $("#authorIntro").val(),
            permission:$("#permission").val(),
            passScore:$("#passScore").val(),
            studyTime:$("#examPaperTime").val(),
            listExam: [],
            kingUser: []
        };
        if($("#list_exam").children("tr").length){
            //push 作业列表数据
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
            //push人员授权数据
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
        	$.fn.jalert2("请输入人员信息");
        }
        ///保存作业包素材的方法
        $.ajax({
			  url:"${ctx}/ajax/taskPaper/updateTaskPaperMaterial",
			  async:false,
			  data:data,
			  dataType:'json',
			  success: function(rsult){
				  window.location.href="${ctx}/material/findList?order=FDCREATETIME&fdType="+'${param.fdType}';
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
            });

    var allUserData ;

    
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


    //添加作业事件
    $("#addExam").bind("click",function(e){
        loadExamPage();
    });

    //打开作业列表中的作业详情
    $("#list_exam>tr>.tdTit .pr a").click(function(e){
        e.preventDefault();
        var id = $(this).closest("tr").attr("data-fdid");
        loadExamPage(id);
        //
    });
    /*加载作业页面*/
    function loadExamPage(fdid){
    	var materialName = $("#examPaperName").val();
        if(materialName==""||materialName==null){
        	$.fn.jalert2("请先作业包名称");
        	return;
        }
        var data = {};
        if( !fdid){//添加作业数据
            data = {
                examScoreTotal: 20,
                examType: "uploadWork"
            };
        } else {
        	//当点击list页面的作业项时，初始化作业数据
        	$.ajax({
				  url:"${ctx}/ajax/taskPaper/getTaskByTaskId",
				  async:false,
				  data:{id: fdid},
				  dataType:'json',
				  success: function(rsult){
						 data = rsult;
				  },
			});
        }
        data.examPaperName = $("#examPaperName").val();  //当前作业包名称
        data.timeLine = {//时间轴控件 配置数据
            width: 670, //时间轴控件 宽度
            total: 20, //总格数
            curPos: data.examScore, //当前位置
            span: 1, //每格的进制
            unit: "分"
        }
        $("#rightCont").html(examDetailFn(data));

        //应用拖放效果
        $(".list-attachment").sortable({
            handle: '.state-dragable'
        })
            //移除列表项
                .delegate(".item-ctrl>.icon-remove-blue","click",function(e){
                    e.preventDefault();
                    $(this).closest("li").remove();
                });


        $(".timeLine>a").tooltip()
                .click(function(e){//分数控制
                    e.preventDefault();
                    $(this).prevAll("a").add(this).addClass("active");
                    $(this).nextAll("a").removeClass("active");
                    $("#examScore").val( $(this).children(".num").text());
                });

        $("#formEditDTotal").validate({
            submitHandler: submitForm
        });
        $("#saveExam").click(function(e){
            $("#formEditDTotal").trigger("submit");
        });

        //作业类型选择
        $('[data-toggle="buttons-radio"]>.btn').click(function(){
            $("#examType").val(this.id);
        });

        /*提交作业详情表单函数*/
        function submitForm(form){
            var data = {
            	materialId:$("#materialId").val(),
            	materialName:materialName,
                examType: $("#examType").val(),
                examName: $("#examName").val(),
                examStem: $("#examStem").val(),
                examScore: $("#examScore").val(),
                listAttachment: []
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
            //console.log(data);
            //ajax
            $.ajax({
				  url: "${ctx}/ajax/taskPaper/saveOrUpdateTaskPaper?taskId="+fdid+"&materialId="+$("#materialId").val(),
				  async:false,
				  data:data,
				  type: "POST",
				  dataType:'json',
				  success: function(result){
					  window.location.href="${ctx}/material/materialFoward?fdId="+result.materialId;
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
            'fileSizeLimit':20480,// 限制文件大小为2m
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
    }
});
    
function initExamPaperList(){
	var id = $("#materialId").val();
	if(id!==null&&id!=""){
		var itemExamDetailFn = doT.template(document.getElementById("taskPaperListTemplate").text);
	    $.ajax({
			  url: "${ctx}/ajax/taskPaper/getTaskByMaterialId?materialId="+id,
			  async:false,
			  dataType : 'json',
			  success: function(result){
				  var html = "";
				 for(var i in result){
					  html += itemExamDetailFn(result[i]);
				  }
				  $("#list_exam").html(html); 
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
	$("#count").html(count);
	$("#totalScore").html(totalScore);
}
</script>
</body>
</html>
