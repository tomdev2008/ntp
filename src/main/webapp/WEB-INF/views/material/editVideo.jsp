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
<title>新东方在线教师备课平台</title>
<link href="${ctx}/resources/css/DTotal.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${ctx}/resources/css/jquery.autocomplete.css">
<link rel="stylesheet" type="text/css" href="${ctx}/resources/uploadify/uploadify.css"/>
<!--上传附件的"浏览"按钮样式-->
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
          <div>
         </td>
        <td><input type="checkbox" {{?it.tissuePreparation!=false}}checked{{?}} class="tissuePreparation" /></td>
        <td><input type="checkbox" {{?it.editingCourse!=false}}checked{{?}} class="editingCourse" /></td>
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
                <a href="${ctx}/material/findList?fdType=${materialInfo.fdType}" class="backParent">
                <span id="back"></span>
               </a>
                <h4>${materialInfo.fdName}</h4>
                <div class="btn-group">
                    <button class="btn btn-large btn-primary" type="button" onclick="saveMater();">保存</button>
                    <c:if test="${materialInfo.fdId!=null}">
                    <button class="btn btn-large btn-primary" type="button" onclick="downloadMater();">下载</button>
                    <button class="btn btn-white btn-large " type="button" onclick="confirmDel();">删除</button>
                    </c:if> 
               </div>
	        </div>
            <div class="page-body editingBody">
                <form action="#" id="formEditDTotal" class="form-horizontal" method="post">
                    <section class="section">
                        <div class="control-group">
                            <label class="control-label" for="videoName" id="typeTxt"></label>
                            <div class="controls">
                                <input value="${materialInfo.fdName}" 
                                    id="videoName" required class="span6" name="videoName" type="text">
                                <span class="date">
                                <fmt:formatDate value="${materialInfo.fdCreateTime}" pattern="yyyy/MM/dd hh:mm aa"/>
                                </span>
                                <input type="hidden" id="fdId" value="${materialInfo.fdId}">
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" >统&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;计</label>
                            <div class="controls controls-txt">
                                <span class="rating-view">
                                    <span class="rating-all">
                                       <c:if test="${score.fdAverage!=null}">
                                         <c:forEach var="i" begin="1" end="${score.fdAverage}">
                                           <i class="icon-star active"></i>
                                         </c:forEach>
                                         <c:forEach var="i" begin="1" end="${5-score.fdAverage}">
                                           <i class="icon-star"></i>
                                         </c:forEach>
                                       </c:if>
                                       <c:if test="${score.fdAverage==null}">
                                         <c:forEach var="i" begin="1" end="5">
					   					   <i class="icon-star"></i>
					  				     </c:forEach>
                                       </c:if>
                                       
                                     </span>
                                      <b class="text-warning">
                                       <c:if test="${score.fdAverage==null}">
                                          0
                                       </c:if>
                                       <c:if test="${score.fdAverage!=null}">
                                          ${score.fdAverage}
                                       </c:if>
                                      </b>
                                </span>
                                <span class="btns-handle">
                                    <button type="button" class="btn btn-link"><i class="icon-eye"></i>
                                       <c:if test="${materialInfo.fdPlays==null}">
                                          0
                                       </c:if>
                                       <c:if test="${materialInfo.fdPlays!=null}">
                                          ${materialInfo.fdPlays}
                                       </c:if>
                                     </button>
                                    <button type="button" class="btn btn-link"><i class="icon-thumbs-up"></i>
                                      <c:if test="${materialInfo.fdLauds==null}">
                                          0
                                       </c:if>
                                       <c:if test="${materialInfo.fdLauds!=null}">
                                          ${materialInfo.fdLauds}
                                       </c:if>
                                    </button>
                                    <button type="button" class="btn btn-link"><i class="icon-download"></i>0</button>
                                </span>
                            </div>
                        </div>
                        <div class="control-group" id="videoText">
                            <label class="control-label" for="videoUrl">播放地址</label>
                            <div class="controls">
                             <input value="${materialInfo.fdLink}" 
                                  placeholder="请认真填写该章节的 建议学习时长"
                                id="videoUrl" class="input-block-level" name="fdLink" type="text">
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="videoIntro" id="materialIntro"></label>
                            <div class="controls">
                              <textarea placeholder="非必填项" rows="4"
                                        class="input-block-level" id="videoIntro"
                                        name="fdDescription">${materialInfo.fdDescription}</textarea>
                            </div>
                        </div>
                    </section>
                    <section class="section mt20">
                        <label id="uploadIntro"></label>
                        <div class="control-upload">
                           <div id="upMaterialDiv" style="height:20px;width:650px;display:block;"></div>
						   <div style="margin-left:670px;margin-top: 8px;height:40px;width:600px;display:block;">
						     <button id="upMaterial" class="btn btn-primary btn-large" type="button" >上传</button>
						  </div>
						  <input type="hidden"  name="attId" id="attId" value="">
                        </div>
                    </section>
                    <section class="section mt20">
                        <div class="media-placeholder">
                           		 格式转换中 ... ...
                        </div>
                    </section>
                    <section class="section">
                        <div class="control-group">
                            <label class="control-label" for="author">作&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;者</label>
                            <div class="controls">
                                <input value="${materialInfo.fdAuthor}" id="author" required class="input-block-level"
                                       name="fdAuthor" type="text">
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="authorIntro">作者简介</label>
                            <div class="controls">
                             <textarea placeholder="非必填项" rows="4"
                                       class="input-block-level" id="authorIntro"
                                       name="fdAuthorDescription" >${materialInfo.fdAuthorDescription}</textarea>
                            </div>
                        </div>
                    </section>
                    <div class="page-header mt20"> <h4>权限设置</h4> </div>
                    <section class="section">
                        <label>权限设置</label>
                        <ul class="nav nav-pills">
                        <c:if test="${materialInfo.isPublish!=false}">
                            <li class="active"><a data-toggle="tab" href="#open">公开</a></li>
                            <li><a data-toggle="tab" href="#encrypt">加密</a></li>
                            <input type="hidden" id="permission" name="permission" value="open">
                         </c:if>
                         <c:if test="${materialInfo.isPublish==false}">
                            <li><a data-toggle="tab" href="#open">公开</a></li>
                            <li class="active"><a data-toggle="tab" href="#encrypt">加密</a></li>
                            <input type="hidden" id="permission" name="permission" value="encrypt">
                         </c:if>
                        </ul>
                        <div class="tab-content">
                          <c:if test="${materialInfo.isPublish!=false}">
                             <div class="tab-pane active" id="open">
                                	提示：“公开”素材将允许所有主管在管理课程的过程中使用，而“加密”素材将允许您手动授权某些主管使用本课程素材。
                             </div>
                             <div class="tab-pane" id="encrypt">
                                <table class="table table-bordered">
                                    <thead><tr><th>授权用户</th><th>可使用</th>
                                     <th>可编辑</th><th>删除</th></tr></thead>
                                    <tbody id="list_user"></tbody>
                                </table>
                                <div class="pr">
                                    <input type="text" id="addUser" class="autoComplete input-block-level" placeholder="请输入人名、邮箱、机构或者部门查找用户">
                                    <i class="icon-search"></i>
                                </div>
                            </div>
                          </c:if>
                          <c:if test="${materialInfo.isPublish==false}">
                             <div class="tab-pane" id="open">
                                	提示：“公开”素材将允许所有主管在管理课程的过程中使用，而“加密”素材将允许您手动授权某些主管使用本课程素材。
                             </div>
                             <div class="tab-pane active" id="encrypt">
                                <table class="table table-bordered">
                                    <thead><tr><th>授权用户</th><th>可使用</th>
                                     <th>可编辑</th><th>删除</th></tr></thead>
                                    <tbody id="list_user"></tbody>
                                </table>
                                <div class="pr">
                                    <input type="text" id="addUser" class="autoComplete input-block-level" placeholder="请输入人名、邮箱、机构或者部门查找用户">
                                    <i class="icon-search"></i>
                                </div>
                            </div>
                          </c:if>
                            
                        </div>
                    </section>
                    <button class="btn btn-block btn-submit btn-inverse" type="submit">保存</button>
                </form>
            </div>
	    </section>
	</section>
</section>
<input type="hidden" id="fdType" value="${param.fdType}">
<input type="hidden" id="fdattId" value="${attId}">
<script type="text/javascript" src="${ctx}/resources/js/jquery.placeholder.1.3.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/messages_zh.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.autocomplete.pack.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.sortable.js"></script>
<script type="text/javascript" src="${ctx}/resources/uploadify/jquery.uploadify-3.1.min.js?id=1211"></script>
<script src="${ctx}/resources/js/jquery.jalert.js" type="text/javascript"></script>
<script type="text/javascript">
function confirmDel(){
	$.fn.jalert("您确认要删除该素材吗？",deleteMaterial);
}
function deleteMaterial(){
	 $.ajax({
		type: "post",
		url: "${ctx}/ajax/material/deleteMaterial",
		data : {
			"materialId":$("#fdId").val(),
		},
		success:function(){
			window.location.href="${ctx}/material/findList?fdType="+$("#fdType").val();
		}
	}); 
}
//下载素材
function downloadMater(){
  var attId = $("#fdattId").val();
  if(attId!=null&&attId!=""){
	  window.location.href="${ctx}/common/file/download/"+attId;
	  $.ajax({
			type: "post",
			url: "${ctx}/ajax/material/updateDownloadNum",
			data : {
				"materialId":$("#fdId").val(),
			},
			success:function(){
				
			}
		}); 
  } else {
	  $.fn.jalert2("您好！该视频没有对应附件");
  } 
}
</script>
<script type="text/javascript">
$(function(){
	var data_uploadIntro;
	var uptype;
	switch($("#fdType").val()){
  	case "01":
  		$("#materialIntro").html("视频简介");
  		$("#back").html("返回视频列表");
  		$("#typeTxt").html("视&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;频");
  		data_uploadIntro = "上传视频（支持MP4、AVI、WMV格式的视频，建议小于10G）：成功上传的视频将会显示在下面的视频列表中。";
  		$("#uploadIntro").html(data_uploadIntro);
  		uptype='*.mp4;*.avi;*.wmv;';
  		break;
    case "02":
    	$("#materialIntro").html("音频简介");
    	$("#back").html("返回音频列表");
    	$("#typeTxt").html("音&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;频");
    	data_uploadIntro = "上传音频（支持MP3、MV格式的音频，建议小于10G）：成功上传的视频将会显示在下面的音频列表中。";
    	$("#uploadIntro").html(data_uploadIntro);
    	uptype='*.mp3;*.mv;';
        break;
    case "04":
    	$("#materialIntro").html("文档简介");
    	$("#back").html("返回文档列表");
    	$("#typeTxt").html("文&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;档");
    	$("#videoText").html("");
    	data_uploadIntro = "上传文档（支持DOC、EXCEL格式的文档，建议小于10G）：成功上传的视频将会显示在下面的文档列表中。";
    	$("#uploadIntro").html(data_uploadIntro);
    	uptype='*.doc;*.xls;';
        break;
    case "05":
    	$("#materialIntro").html("幻灯片简介");
    	$("#back").html("返回幻灯片列表");
    	$("#typeTxt").html("幻&nbsp;&nbsp;灯&nbsp;&nbsp;片");
    	$("#videoText").html("");
    	data_uploadIntro = "上传幻灯片（建议小于10G）：成功上传的视频将会显示在下面的幻灯片列表中。";
    	$("#uploadIntro").html(data_uploadIntro);
    	uptype='*.ppt;';
        break;
  }

  jQuery("#upMaterial").uploadify({
    'height' : 27,
    'width' : 80,
    'multi' : true,
    'simUploadLimit' : 1,
    'swf' : '${ctx}/resources/uploadify/uploadify.swf',
    'buttonText' : '上传',
    'uploader' : '${ctx}/common/file/o_upload',
    'auto' : true,
    'queueID': 'upMaterialDiv',
    'fileTypeExts' : '*.AVI;*.MP4;*.WMV;',
    'onUploadStart' : function (file) {
        jQuery("#upMaterial").uploadify("settings", "formData");
    },
    'onUploadSuccess' : function (file, data, Response) {
        if (Response) {
            var objvalue = eval("(" + data + ")");
            jQuery("#attId").val(objvalue.attId);
        }
    },
    
    'onSelect':function(file){
    	// 选择新文件时,先清文件列表
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
    'removeCompleted':false
  });
});

</script>
<script type="text/javascript">
$(function(){
    $.Placeholder.init();
    //授权管理 用户列表 模板函数
    var listUserKinguserFn = doT.template(document.getElementById("listUserKinguserTemplate").text);
    //初始化创建者
    var creator="";
	var url="";
    if("${materialInfo.fdId!=null}"){
	   $.ajax({
		 url: "${ctx}/ajax/material/getCreater?materialId=${materialInfo.fdId}",
		 async:false,
		 dataType : 'json',
		 success: function(result){
		    creator = result.name+"（"+result.email+"），"+result.dept;
				  url=result.url;
		 }
	   });
    }
    //初始化权限列表
    $.ajax({
		  url: "${ctx}/ajax/material/getAuthInfoByMaterId?MaterialId=${materialInfo.fdId}",
		  async:false,
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
    
    $("#formEditDTotal").validate({
        submitHandler:saveMaterial
    });
    
    $('#formEditDTotal a[data-toggle="tab"]').on('click', function (e) {
        var href = 	e.target.href.split("#").pop();
        $("#permission").val(href);
    });
    $("#list_user").sortable({
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
			});
			$("#addUser").val("");
		}
	});
});
function saveMaterial(){
	if(!$("#formEditDTotal").valid()){
		return;
	}
    var data = {
        videoName: $("#videoName").val(),
        fdId: $("#fdId").val(),
        videoUrl: $("#videoUrl").val(),
        videoIntro: $("#videoIntro").val(),
        author: $("#author").val(),
        authorIntro: $("#authorIntro").val(),
        permission:$("#permission").val(),
        fdType:$("#fdType").val(),
        attId:$("#attId").val(),
        kingUser: null
    };
    if(data.permission === "encrypt"){
        //push人员授权数据
        data.kingUser = [];
        $("#list_user>tr").each(function(){
            data.kingUser.push({
                id: $(this).attr("data-fdid"),
                index: $(this).index(),
                tissuePreparation: $(this).find(".tissuePreparation").is(":checked"),
                editingCourse: $(this).find(".editingCourse").is(":checked")
            });
        });
        data.kingUser = JSON.stringify(data.kingUser);
    }
    $.post("${ctx}/ajax/material/saveOrUpdateVideo",data)
     .success(function(){
    	 window.location.href="${ctx}/material/findList?order=FDCREATETIME&fdType="+$("#fdType").val();
     }); 
}
function saveMater(){
	$("#formEditDTotal").trigger("submit");
}
</script>
</body>
</html>
