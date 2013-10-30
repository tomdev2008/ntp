<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="j" uri="/WEB-INF/tags/formtag.tld"%>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html class="">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新东方在线教师备课平台</title>
<link href="${ctx}/resources/css/DTotal.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${ctx}/resources/css/jquery.autocomplete.css">

<!-- 授权管理 用户列表 模板 -->
<script id="listUserKinguserTemplate" type="text/x-dot-template">
    <tr data-fdid="{{=it.id}}">
        <td class="tdTit">
          <div class="pr">
            <div class="state-dragable"><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></div>
            <img src="{{=it.imgUrl || 'images/temp-face36.jpg'}}" alt="">{{=it.name}}（{{=it.mail}}），{{=it.org}} {{=it.department}}
          <div>
         </td>
        <td><input type="checkbox" checked class="tissuePreparation" /></td>
        <td><input type="checkbox" class="editingCourse" /></td>
        <td><a href="#" class="icon-remove-blue"></a></td>
    </tr>
</script>

    <script src="${ctx}/resources/js/doT.min.js"></script>
</head>

<body>

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
	            <c:if test="${param.fdType==1000}">
	            <li class="active">
	             </c:if>
	             <c:if test="${param.fdType!=1000}">
	            <li>
	             </c:if>
	             
	              <a href="${ctx}/course/findcourseInfos?fdType=1000" id="courseInfos">
	              <i class="icon-course"></i>我的课程</a>
	             </li>
	             <li class="nav-header">
                     <span>课程素材库</span>
	            </li>
                <c:if test="${param.fdType==01}">
                <li class="active">
                </c:if>
                <c:if test="${param.fdType!=01}">
                <li>
                </c:if>
                  <a href="${ctx}/material/findList?fdType=01"><i class="icon-video">
                </i>视频</a></li>
                </li>
	            <c:if test="${param.fdType==04}">
                <li class="active">
                </c:if>
                <c:if test="${param.fdType!=04}">
                <li>
                </c:if>
                  <a href="${ctx}/material/findList?fdType=04"><i class="icon-doc">
                </i>文档</a></li>
                <c:if test="${param.fdType==05}">
                <li class="active">
                </c:if>
                <c:if test="${param.fdType!=05}">
                <li>
                </c:if>
                  <a href="${ctx}/material/findList?fdType=05"><i class="icon-ppt">
                </i>幻灯片</a></li>
                <c:if test="${param.fdType==08}">
                <li class="active">
                </c:if>
                <c:if test="${param.fdType!=08}">
                <li>
                </c:if>
                  <a href="${ctx}/material/findList?fdType=08"><i class="icon-exam">
                </i>测试</a></li>
                
                <c:if test="${param.fdType==10}">
                 <li class="active">
                </c:if>
                <c:if test="${param.fdType!=10}">
                 <li>
                </c:if>
                  <a href="${ctx}/material/findList?fdType=10"><i class="icon-task"></i>作业</a></li>
	    </ul>
	  </section>
		<section class="w790 pull-right" id="rightCont">
	        <div class="page-header">
                <a href="素材库-视频.html" class="backParent">返回视频列表</a>
                <h4>${materialInfo.fdName}</h4>
                <div class="btn-group">
                    <button class="btn btn-large btn-primary" type="button">保存</button>
                    <button class="btn btn-large btn-primary" type="button">下载</button>
                </div>
	        </div>
            <div class="page-body editingBody">
                <form action="${ctx}/material/addOrUpdateVideo" id="formEditDTotal" class="form-horizontal" method="post">
                    <section class="section">
                        <div class="control-group">
                            <label class="control-label" for="videoName">视&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;频</label>
                            <div class="controls">
                                <input value="" placeholder="请输入素材名称"
                                    id="videoName" required class="span6" name="videoName" type="text">
                                <span class="date"></span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="videoUrl">播放地址</label>
                            <div class="controls">
                             <input value=""  placeholder="请认真填写该章节的 建议学习时长"
                                id="videoUrl" class="input-block-level" name="fdLink" type="text">
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="videoIntro">视频简介</label>
                            <div class="controls">
                              <textarea placeholder="非必填项" rows="4"
                                        class="input-block-level" id="videoIntro"
                                        name="fdDescription"></textarea>
                            </div>
                        </div>
                    </section>
                    <section class="section mt20">
                        <label>上传视频（支持MP4、AVI、WMV格式的视频，建议小于10G）：成功上传的视频将会显示在下面的视频列表中。</label>
                        <div class="control-upload">
                            <span class="progress"> <div class="bar" style="width:20%;"></div> </span>
                            <span class="txt"><span>20%</span>，剩余时间：<span>00:00:29</span></span>
                            <button class="btn btn-primary btn-large" type="button">上传</button>
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
                                       name="fdAuthorDescription" ></textarea>
                            </div>
                        </div>
                    </section>
                    <div class="page-header mt20"> <h4>权限设置</h4> </div>
                    <section class="section">
                        <label>权限设置<input type="hidden" id="permission" name="permission" value="open"></label>
                        <ul class="nav nav-pills">
                            <li class="active">
                               <a data-toggle="tab" href="#open">
                                                                                     公开
                               </a>
                           </li>
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
                                        <th>可使用</th>
                                        <th>可编辑</th>
                                        <th>删除</th>
                                    </tr>
                                    </thead>
                                    <tbody id="list_user">
                                  <!--   <tr data-fdid="fdid325" draggable="true">
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
                                        <td><input type="checkbox" class="editingCourse"></td>
                                        <td><a href="#" class="icon-remove-blue"></a></td>
                                    </tr>  -->        
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
</section>
<script type="text/javascript" src="${ctx}/resources/js/jquery.placeholder.1.3.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/messages_zh.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.autocomplete.pack.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.sortable.js"></script>
<script type="text/javascript">
$(function(){
    $.Placeholder.init();
    //授权管理 用户列表 模板函数
    var listUserKinguserFn = doT.template(document.getElementById("listUserKinguserTemplate").text);
    
    $("#formEditDTotal").validate({
        submitHandler: function(form){
            var data = {
                videoName: $("#videoName").val(),
                fdId: $("#fdId").val(),
                videoUrl: $("#videoUrl").val(),
                videoIntro: $("#videoIntro").val(),
                author: $("#author").val(),
                authorIntro: $("#authorIntro").val(),
                permission:$("#permission").val(),
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
            //console.log(JSON.stringify(data));
            //ajax
            $.post("${ctx}/ajax/material/saveOrUpdateVideo",data)
             .success(function(){
            	 $.fn.jalert2("保存成功!");
             }); 
        }
    });
    $('#formEditDTotal a[data-toggle="tab"]').on('shown', function (e) {
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
</script>
</body>
</html>
