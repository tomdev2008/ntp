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
<link href="${ctx}/resources/css/template_detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${ctx}/resources/css/jquery.autocomplete.css">
<!--[if lt IE 9]>
<script src="js/html5.js"></script>
<![endif]-->

<!-- 模板详情_右侧内容区标题 模板 -->
<script id="contHeaderTemplate" type="text/x-dot-template">
 	<div class="page-header">        	
		<h4>{{=it.pageTitle || ''}}</h4>           
	</div>
</script>

<!-- 模板详情_章节目录 模板 -->
<script id="sectionDirectoryTemplate" type="text/x-dot-template">
	{{#def.pageHeader}}
    	<div class="page-body">
	    	<div class="section" id="progress_courses">          
	       		
	            	<div class="progress progress-course">
	            		<div class="bar" style="width:20%;"></div>
	            	</div>
	                您已经完成了本课程的 <span class="num_comp"></span> 个章节内容，共计 <span class="num_all">{{=it.lecture.length}}</span> 个章节。
	        </div>
	       
	      <div class="sortableWrap">
	      	<ul class="sortable" id="sortable">  
			    	{{  for(var i=0; i<(it.chapter.length+it.lecture.length); i++){  }}
						{{~it.chapter :chp:index}}
							{{?chp.index == i}}	
								<li class="chapter" data-fdid="{{=chp.id}}">		
								{{#def.sectionbar:chp}}	
								</li>			
							{{?}}
						{{~}}
						{{~it.lecture :lec:index}}
							{{?lec.index == i}}
								<li class="lecture" data-fdid="{{=lec.id}}">
								{{#def.sectionbar:lec}}
								{{#def.lecturecontent:lec}}
								</li>
							{{?}}
						{{~}}
					{{ } }}
	      	</ul>
	        <div class="section-add">
	        	<button id="addLecture" class="btn btn-block btn-lecture-add" type="button">添加节</button>
	            <button id="addChapter" class="btn btn-block" type="button">添加章</button>
	        </div>
	      </div>
      </div> 
</script>

<!--添加章节表单模板-->
<script id="addSectionsTemplate" type="text/x-dot-template">
	<li class="{{?it.chapter}}chapter{{??it.lecture}}lecture{{?}}" data-fdid="">
		{{#def.edittitle || ''}}		
	</li>
</script>

<!--编辑章节标题表单模板-->
<script id="formEditSectionTitle" type="text/x-dot-template">
	<div class="form-edit-title form-horizontal">		
			<div class="control-group">
				<label class="control-label">第<span class="index">{{?it.chapter}}{{=it.chapter.num}}</span>章{{??it.lecture}}{{=it.lecture.num}}</span>节{{?}}</label>
				<div class="controls">
					<input type="text" maxlength="20" class="input-block-level" placeholder="请输入课程标题" value="{{=(it.chapter ? it.chapter.title : it.lecture.title) || ''}}" />
					<span class="count">20字</span>
				</div>
			</div>
			<div class="controls">
				<button class="btn btn-primary btn-large" type="button">保存</button>
				<button class="btn btn-link" type="button">取消</button>
			</div>		
	</div>
</script>

<!--展示章节模板-->
<script id="sectionsTemplate" type="text/x-dot-template">
		{{?it.chapter}}
			{{#def.sectionbar:it.chapter}}
		{{??it.lecture}}
			{{#def.sectionbar:it.lecture}}
			{{#def.lecturecontent:it.lecture}}
		{{?}}		              
</script>

<!--章节条模板-->
<script id="sectionBarTemplate" type="text/x-dot-template">
	{{##def.sectionbar:param:
		<div class="sortable-bar">
			<span class="title">{{?param.type}}<i class="icon-{{=param.type}}"></i>{{?}}
				第<span class="index">{{=param.num}}</span>{{?param.type}}节{{??}}章{{?}}
				<span class="name">{{=param.title || ''}}</span></span>
			<a class="icon-pencil2{{?!param.type}} icon-white{{?}} btn-ctrls" href="#"></a>
			<a class="icon-remove{{?!param.type}} icon-white{{?}} btn-ctrls" href="#"></a>
			{{?param.type}}
			<a href="#" class="btn-edit">编辑内容</a>
			{{?}}
			<div class="state-dragable">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</div>
		</div>
	#}}
</script>

<!--节内容模板-->
<script id="lectureContentTemplate" type="text/x-dot-template">
	{{##def.lecturecontent:param:
		<div class="lecture-content hide">
			<div class="hd">
			编辑内容
			<a href="#" class="icon-remove-sign"></a>
			</div>
			<div class="bd">
				<a class="btn-type{{?param.type != 'none' && param.type != 'video'}} disabled{{?}}" href="#video"><i class="icon-video-lg"></i><h5>视频</h5></a>
				<a class="btn-type{{?param.type != 'none' && param.type != 'audio'}} disabled{{?}}" href="#audio"><i class="icon-audio-lg"></i><h5>音频</h5></a>
				<a class="btn-type{{?param.type != 'none' && param.type != 'doc'}} disabled{{?}}" href="#doc"><i class="icon-doc-lg"></i><h5>文档</h5></a>
				<a class="btn-type{{?param.type != 'none' && param.type != 'ppt'}} disabled{{?}}" href="#ppt"><i class="icon-ppt-lg"></i><h5>幻灯片</h5></a>
				<a class="btn-type{{?param.type != 'none' && param.type != 'img'}} disabled{{?}}" href="#img"><i class="icon-img-lg"></i><h5>图片</h5></a>
				<a class="btn-type{{?param.type != 'none' && param.type != 'exam'}} disabled{{?}}" href="#exam"><i class="icon-exam-lg"></i><h5>测试</h5></a>
				<a class="btn-type{{?param.type != 'none' && param.type != 'task'}} disabled{{?}}" href="#task"><i class="icon-task-lg"></i><h5>作业</h5></a>
				<!-- 
				<a class="btn-type{{?param.type != 'none' && param.type != 'calendar'}} disabled{{?}}" href="#calendar"><i class="icon-calendar-lg"></i><h5>日程安排</h5></a>
				-->
			</div>
		</div>
	#}}
</script>

<!-- 模板详情_基本信息 模板 -->
<script id="basicInfoTemplate" type="text/x-dot-template">
	{{#def.pageHeader}} 
    <div class="page-body">       
	<div class="info-templ-content">
        <form id="formBasicInfo" method="post" action="{{=it.action || '##'}}">  	
	    	<div class="section" >   
	            	<fieldset>
                    	<label for="courseTitle">标题</label>
                        <input type="text" id="courseTitle" name="courseTitle" required minlength="6" class="input-block-level" value="{{=it.courseTit || ''}}"  />
                        <label for="subTitle">副标题</label>
                        <textarea name="subTitle" id="subTitle" required minlength="12" class="input-block-level" rows="3">{{=it.subTit || ''}}</textarea>
                        
                        <label >关键词</label>
                        <div class="keywordWrap">
                        	<input type="hidden" id="keyword" name="keyword" value="{{= it.keyword || '' }}" />
							{{~ it.keyword :key:index}}
                         		 <span class="alert alert-tag"><span>{{=key}}</span><a href="#" data-dismiss="alert" class="close">&times;</a></span>
                         	{{~}}
                          <a href="#" class="btn-add">+</a>
                          
                        </div>
                    </fieldset>	
                    <label >分类</label>
                    <input type="hidden" id="courseType" name="courseType" value="{{=it.courseType || ''}}" />
                    <ul class="nav nav-pills courseType">
						{{~ it.courseTypeList :type:index}}
							 <li{{?type.id == it.courseType}} class="active"{{?}}><a href="#">{{=type.title}}</a><input type='hidden' value='{{=type.id}}'></li>
						{{~}}                    	                  
                    </ul>  
	       </div>
           <button class="btn btn-block btn-submit btn-inverse" type="button" onClick="saveBaseInfo()">保存</button>
       </form>
       </div>	  
	 </div> 
</script>

<!-- 模板详情_基本信息 添加关键词 模板 -->
<script id="addKeywordInfoTemplate" type="text/x-dot-template">
		<div class="inpKeyword form-inline control-group">
			<input type="text" class="span5" id="addKey" placeholder="请输入关键词" />
			<button class="btn btn-large btn-primary" type="button">确定</button>
			<button class="btn btn-large" type="button">取消</button>
		  </div>
</script>

<!-- 模板详情_基本信息 关键词标签 模板 -->
<script id="tagKeywordInfoTemplate" type="text/x-dot-template">
		<span class="alert alert-tag"><span>{{=it.keyword}}</span><a href="#" data-dismiss="alert" class="close">&times;</a></span>
</script>

<!-- 模板详情_详细信息 模板 -->
<script id="detailInfoTemplate" type="text/x-dot-template">
	{{#def.pageHeader}} 
    <div class="page-body detail-content">    	
        <form id="formDetailInfo" method="post" action="{{=it.action || '##'}}">  	
	    	<div class="section" >   
					<label for="courseAbstract">课程摘要</label>
					<textarea id="courseAbstract" name="courseAbstract" required minlength="20" class="input-block-level" rows="4">{{=it.courseAbstract || ''}}</textarea>
					<label for="courseAuthor">作者</label>
					<input type="text" id="courseAuthor" name="courseAuthor"  class="span5" value="{{=it.courseAuthor || ''}}">
					<label for="authorDescrip">作者描述</label>
					<textarea id="authorDescrip" name="authorDescrip"  class="input-block-level" rows="4">{{=it.authorDescrip || ''}}</textarea>
					<label for="learnObjectives">学习目标</label>
					<input type="hidden" id="learnObjectives" name="learnObjectives" value="{{= it.learnObjectives || '' }}" />
					<ul class="list_alert nav">						
						{{~ it.learnObjectives :obj:index}}
							 {{#def.item:obj}}
						{{~}}					  
					</ul>
					{{#def.formAddItem:"学习目标"}}
					<label for="suggestedGroup">建议群体</label>
					<input type="hidden" id="suggestedGroup" name="suggestedGroup" value="{{= it.suggestedGroup || '' }}" />
					<ul class="list_alert nav">						
						{{~ it.suggestedGroup :obj:index}}
							 {{#def.item:obj}}
						{{~}}					  
					</ul>
					{{#def.formAddItem:"建议群体"}}
                    <label for="courseRequirements">课程要求</label>
					<input type="hidden" id="courseRequirements" name="courseRequirements" value="{{= it.courseRequirements || '' }}" />
					<ul class="list_alert nav">						
						{{~ it.courseRequirements :obj:index}}
							 {{#def.item:obj}}
						{{~}}					  
					</ul>
					{{#def.formAddItem:"课程要求"}}
                    
	       </div>
           <button class="btn btn-block btn-submit btn-inverse" type="button" onClick="saveDetailInfo()">保存</button>
       </form>	  
	 </div> 
	 {{##def.formAddItem:param:
	 	<div class="formAdd form-inline control-group"><input type="text" placeholder="请填写{{=param}}" /><button class="btn btn-large btn-primary" type="button">添加</button></div>
	 #}}
	 {{##def.item:param:
	 	<li class="alert"><i class="icon-square"></i><span>{{=param}}</span><a href="#" data-dismiss="alert" class="close">&times;</a></li>
	 #}}
</script>
<!-- 模板详情_详细信息 列表项 模板 -->
<script id="itemDetailInfoTemplate" type="text/x-dot-template">	
		<li class="alert"><i class="icon-square"></i><span>{{=it}}</span><a href="#" data-dismiss="alert" class="close">&times;</a></li>
</script>

<!-- 课程推广 模板 -->
<script id="promotionTemplate" type="text/x-dot-template">
	{{#def.pageHeader}} 
    <div class="page-body promotion-content">    	
        <form id="formPromotion" method="post" action="{{=it.action || '##'}}">  	
	    	<div class="section" >              	
					<label for="CourseCover">课程封面</label>
					<input id="courseCover" name="courseCover" class="input-block-level" type="hidden" value="{{=it.coverUrl || 'images/zht-main-img.jpg'}}" />
												<!--图片预览-->
					<div class="courseCover"><img id="imgshow" name="imgshow" style="width: 300px;height:200px;"  src="{{=it.coverUrl || '${ctx}/resources/images/zht-main-img.jpg'}}" alt="" /></div>					
	       </div>
		   <div class="section" >              	
					<label>上传图片（支持JPG\JPEG、PNG、BMP格式的图片，建议小于2M）</label>
					<div class="control-upload">
						 <div id="qdiv" style="height:20px;width:650px;display:block;">
							 
 						 </div>
						 <div style="margin-left:670px;margin-top: 8px;height:40px;width:600px;display:block;">
						     <button id="upMovie" class="btn btn-primary btn-large" type="button" >上传</button>
						 </div>
							<input type="hidden"  name="attId" id="attIdID">
					</div>		
	       </div>		  
		   <div class="courseSkins">
		   		 <label >课程皮肤</label>
				<input type="hidden" id="courseSkin" name="courseSkin" value="{{=it.courseSkin.title || ''}}" />
				<ul class="nav courseSkinList clearfix">
					{{~ it.courseSkinList :skin:index}}
						 <li{{?skin.title == it.courseSkin.title}} class="active"{{?}}><a href="#"><img src="{{=skin.imgUrl}}" alt="{{=skin.title}}" /><i class="icon-right"></i></a><h5>{{=skin.title}}</h5></li>
					{{~}}                    	                  
				</ul>
			</div>
           <button class="btn btn-block btn-submit btn-inverse" type="button"  onclick="saveCoursePic();">保存</button>
       </form>	  
	 </div> 	
</script>

<!-- 访问权限 模板 -->
<script id="accessRightTemplate" type="text/x-dot-template">
	{{#def.pageHeader}} 
    <div class="page-body accessRight-content">    	
        <form id="formAccessRight" method="post" action="{{=it.action || '#accessRight'}}">  	
	    	<div class="section" >              	
					<label>权限设置
						<input type="hidden" id="permission" name="permission" value="{{=it.permission || 'open'}}" />						
					</label>
					<ul class="nav nav-pills">
							<li{{?it.permission == 'open' || it.permission == ''}} class="active"{{?}}><a data-toggle="tab" href="#open">公开</a></li>
							<li{{?it.permission == 'encrypt'}} class="active"{{?}}><a data-toggle="tab" href="#encrypt">加密</a></li>
					</ul>
					<div class="tab-content">
						<div class="tab-pane{{?it.permission == 'open' || it.permission == ''}} active{{?}}" id="open">
						提示："公开"课程将允许所有NTP用户（新东方集团教职员工）访问。而"私密"课程将提醒课程负责主管手动授权或者密码访问。
						</div>
						<div class="tab-pane{{?it.permission == 'encrypt'}} active{{?}}" id="encrypt">
							<label  class="radio" for="authorized"><input type="radio" onclick="removePass()" value="authorized" {{?it.encryptType == 'authorized' || it.encryptType == ''}}checked{{?}} name="encryptType" id="authorized" /><span class="labelTxt">授权组织备课</span>前往 <a href="#kinguser" onClick="urlRouter()" >授权管理</a> 本课程的用户列表</label>
							<label  class="radio" id="passRadio" for="passwordProtect"><input type="radio" value="passwordProtect" {{?it.encryptType == 'passwordProtect'}}checked{{?}} name="encryptType" id="passwordProtect" /><span class="labelTxt">密码保护</span><input type="password" id="coursePwd"  name="coursePwd" {{?it.encryptType !='passwordProtect'}}disabled{{?}} placeholder="请填写课程访问的密码" value="{{=it.coursePwd || ''}}" /></label>
						</div>
					</div>					
	       </div>		 
           <button class="btn btn-block btn-submit btn-inverse" type="button" onClick="saveIsPublish()">保存</button>
       </form>	  
	 </div> 	
</script>

<!-- 授权管理 模板 -->
<script id="kinguserTemplate" type="text/x-dot-template">
	{{#def.pageHeader}} 
    <div class="page-body kinguser-content">   	        
	    	<div class="section" >
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
									<tr data-fdid="creater">
										<td class="tdTit">
                                          <div class="pr">
											<div class="state-dragable"><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></div>
											<img src="{{=it.createrimgUrl}}" alt="">{{=it.creatername}}（{{=it.creatermail}}）， {{=it.createrdepartment}}
										 </div>
                                          </td>
										<td><input type="checkbox" checked onclick="return false" class="tissuePreparation" /></td>
										<td><input type="checkbox" checked onclick="return false" class="editingCourse" /></td>
										<td></td>
									</tr>
							{{~it.user :user:index}}
								{{~it.user :user:index2}}
									{{?index == user.index}}
									<tr data-fdid="{{=user.id}}">
										<td class="tdTit">
                                          <div class="pr">
											<div class="state-dragable"><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></div>
											<img src="{{=user.imgUrl || 'images/temp-face36.jpg'}}" alt="">{{=user.name}}（{{=user.mail}}），{{=user.org}} {{=user.department}}
										 </div>
                                          </td>
										<td><input type="checkbox" {{?user.tissuePreparation}}checked{{?}} class="tissuePreparation" /></td>
										<td><input type="checkbox" {{?user.editingCourse}}checked{{?}} class="editingCourse" /></td>
										<td><a href="#" class="icon-remove-blue"></a></td>
									</tr>
									{{?}}
								{{~}}
							{{~}}
						</tbody>
					</table>
					<input type="text" id="addUser" class="autoComplete" />
	       </div>		 
           <button class="btn btn-block btn-submit btn-inverse" id="submitUser" type="button">保存</button>         
	 </div> 	
</script>

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
	<td><input type="checkbox" checked class="editingCourse" /></td>
	<td><a href="#" class="icon-remove-blue"></a></td>
</tr>
</script>

<!-- 删除课程 模板 -->
<script id="deleteCourseTemplate" type="text/x-dot-template">
	{{#def.pageHeader}} 
    <div class="page-body deleteCourse-content">   	        
	    	<div class="section" >
					<div class="well">
						警告：您点击下面的删除课程按钮以后，将会出现以下结果：<br />
1. 本课程将不复存在；<br />
2. 与课程相关的历史学习记录也将一并删除；<br />
3. 本课程中的内容资料（包括视频、文档等）请前往 课程素材库 进行查阅。
					</div>
	       </div>		 
           <button class="btn btn-block btn-warning btn-submit" id="deleteCourse" type="button">删除</button>         
	 </div> 	
</script>

<!-- 视频,文档。。。页 模板 -->
<script id="mediaPageTemplate" type="text/x-dot-template">
    <div class="page-header">
        <h4>第{{=it.lectureIndex}}节 {{=it.pageTitle || ''}}</h4>
    </div>
    <div class="page-body mediaPage-content">
        <form id="formMedia" method="post" class="form-horizontal" action="{{=it.action || '##'}}">
	    	<div class="section" >
                <div class="control-group">
                    <label class="control-label" for="lectureName">章节名称</label>
                    <div class="controls"><input value="{{=it.pageTitle || ''}}" id="lectureName" required class="input-xlarge" name="lectureName" type="text" /></div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="learnTime">学习时长</label>
                    <div class="controls"><input value="{{=it.learnTime || ''}}" required placeholder="请认真填写该章节的建议学习时长" id="learnTime" class="input-xlarge" name="learnTime" type="text" /></div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="sectionsIntro">章节说明</label>
                    <div class="controls">
                        <textarea placeholder="请认真填写该章节的描述信息" rows="4" required minlength="20" class="input-xxlarge" id="sectionsIntro" name="sectionsIntro" >{{=it.sectionsIntro || ''}}</textarea>
                    </div>
                </div>
	       </div>
            <div class="mediaList">
                <label >{{=it.typeTxt}}列表（<span id="mediaCount">{{=it.mediaList.length || ''}}</span>  个）</label>
                <ul class="unstyled" id="listMedia">
                    {{~it.mediaList :item:index}}
                        {{~it.mediaList :item2:index2}}
                            {{?(index+1) == item2.index}}
                            <li data-fdid="{{=item.id}}"><span class="title">{{=it.typeTxt}} <span class="index">{{=item2.index}}</span>：<span class="name">{{=item.title}}</span></span>
                                <a class="icon-pencil2 btn-ctrls" href="#"></a>
                                <a class="icon-remove btn-ctrls" href="#"></a>
                                <div class="state-dragable"><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></div>
                            </li>
                            {{?}}
                        {{~}}
                    {{~}}
                </ul>
            </div>
			{{?it.type != 'exam' && it.type != 'task'}}
		    <div class="section" >
					<label>{{=it.uploadIntro || ''}}</label>
					<div class="control-upload">

						<div id="upMaterialDiv" style="height:20px;width:650px;display:block;">
							 
 						 </div>
						 <div  style="margin-left:670px;margin-top: 8px;height:40px;width:600px;display:block;">
						     <button id="upMaterial" class="btn btn-primary btn-large" type="button" >上传</button>
						 </div>


					</div>
	        </div>
			{{?}}
            <div class="section" >
                <label>或者从 <a id="gotoMaterial" href="#">课程素材库</a> 中选择{{=it.typeTxt}}</label>
                <div class="autoCompleteWrap">
					<input id="addMedia" type="text" />
					<!--
					<button class="btn btn-primary btn-large" type="button" >选择</button>
					-->
                </div>
            </div>
           <button class="btn btn-block btn-submit btn-inverse" type="submit">保存</button>
       </form>
	 </div>
</script>

<!-- 视频,文档。。。列表项 模板 -->
<script id="mediaListTemplate" type="text/x-dot-template">
    <li data-fdid="{{=it.id}}"><span class="title">{{=it.typeTxt}} <span class="index">{{=it.index}}</span>：<span class="name">{{=it.name}}</span></span>
        <a class="icon-pencil2 btn-ctrls" href="#"></a>
        <a class="icon-remove btn-ctrls" href="#"></a>
        <div class="state-dragable"><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></div>
    </li>
</script>

<!--编辑视频,文档。。。列表项 标题表单模板-->
<script id="formEditMediaTitle" type="text/x-dot-template">
    <li class="form-edit-title form-horizontal">
        <div class="control-group">
            <label class="control-label">{{=it.typeTxt}}<span class="index">{{=it.index}}</span></label>
            <div class="controls">
                <input type="text" class="input-block-level" placeholder="请输入标题" value="{{=it.name || ''}}" />
                <span class="count">20字</span>
            </div>
        </div>
        <div class="controls">
            <button class="btn btn-primary btn-large" type="button">保存</button>
            <button class="btn btn-link" type="button">取消</button>
        </div>
    </li>
</script>

<script src="${ctx}/resources/js/doT.min.js"></script>
</head>

<body>
<!-- <header class="navbar navbar-inverse navbar-fixed-top">
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
                	<span class="top-face"><img src="./images/temp-face.jpg" alt=""><i class="icon-disc"></i></span>
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
</header> -->
<input type="hidden" id="ctx" value="${pageContext.request.contextPath}"/>
<section class="container">
	<div class="clearfix">
		<div class="tit-bar">    	
	        <div class="page-title section" id="page-title">
	        	<input type='hidden' id='courseId' value='${course.fdId}' />
	        	<h5>${course.fdTitle}</h5>
	            <div class="btn-group">
	                <c:if test="${course.fdStatus==null || course.fdStatus=='00'}">
		            <button class="btn btn-primary btn-large" disabled type="button" onclick="previewCourse()">预览</button>
		            <button class="btn btn-primary btn-large" disabled type="button" onclick="releaseCourse()">发布</button>
		            </c:if>
	            </div>
	        </div>
	        <img src="${ctx}/resources/images/admin-leftImg.png" width="187" height="60">
	    </div>
	  <div class="col-left pull-left">
	<ul class="nav nav-list sidenav" id="sideNav">
	            <li class="nav-header">
	                <i class="icon-content"></i>
	                课程内容
	            </li>
	            <li class="active"><a href="#sectionsDirectory">章节目录</a></li> 
	             <li class="nav-header">
	                <i class="icon-info"></i>
	                课程信息
	            </li>
	            <li><a href="#basicInfo">基本信息</a></li>   
	            <li><a href="#detailInfo">详细信息</a></li>
	            <li><a href="#promotion">课程推广</a></li>  
	             <li class="nav-header">
	                <i class="icon-setting"></i>
	                课程设置
	            </li>
	            <li><a href="#accessRight">访问权限</a></li>   
	            <li><a href="#kinguser">授权管理</a></li>
	            <li><a href="#deleteCourse">删除课程</a></li>       
	    </ul>
	  </div>
		<div class="w790 pull-right" id="rightCont">    
	   
	    </div>
	</div>

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
<script type="text/javascript" src="${ctx}/resources/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/messages_zh.js"></script>
<script src="${ctx}/resources/js/jquery.sortable.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/resources/uploadify/uploadify.css"/>
<script type="text/javascript" src="${ctx}/resources/uploadify/jquery.uploadify-3.1.min.js?id=1211"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.autocomplete.pack.js"></script>
<script src="${ctx}/resources/js/jquery.jalert.js" type="text/javascript"></script>
<script src="${ctx}/resources/js/templDetailPages.js"></script>
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
<script type="text/javascript">	
	
	//点击左侧菜单事件
	$("#sideNav>li>a").bind("click",function(e){	
		if ($('#upMovie').length > 0) { //注意jquery下检查一个元素是否存在必须使用 .length >0 来判断
		     $('#upMovie').uploadify('destroy'); 
		}
		if ($('#upMaterial').length > 0) { //注意jquery下检查一个元素是否存在必须使用 .length >0 来判断
		     $('#upMaterial').uploadify('destroy'); 
		}
		urlRouter();		
	});
	
	//根据URL中‘#’后参数判断加载栏目
	function urlRouter(href,opt){
		setTimeout(function(){
			var param = href ? href : location.href.split("#").pop();			
			$("#sideNav>li>a[href='#" + param + "']").parent().addClass("active").siblings().removeClass("active");
			switch(param){			
	  			case "basicInfo":
	  				rightCont.loadBasicInfoPage("基本信息");
	  				break;
	  			case "detailInfo":
	  				if($('#courseId').val()!=null &&  $('#courseId').val()!=''){
	  					rightCont.loadDetailInfoPage("详细信息");
	  				}else{
	  					$.fn.jalert2("请先设置基本信息");
	  					urlRouter("basicInfo");
	  				}
	  				break;
	  			case "promotion":
	  				if($('#courseId').val()!=null &&  $('#courseId').val()!=''){
	  					rightCont.loadPromotionPage("课程推广");
	  				}else{
	  					$.fn.jalert2("请先设置基本信息");
	  					urlRouter("basicInfo");
	  				}
	  				break;
	  			case "accessRight":
	  				if($('#courseId').val()!=null &&  $('#courseId').val()!=''){
	  					rightCont.loadAccessRightPage("权限设置");
	  				}else{
	  					$.fn.jalert2("请先设置基本信息");
	  					urlRouter("basicInfo");
	  				}
	  				break;
	  			case "kinguser":
	  				if($('#courseId').val()!=null &&  $('#courseId').val()!=''){
	  					rightCont.loadKinguserPage("授权管理");
	  				}else{
	  					$.fn.jalert2("请先设置基本信息");
	  					urlRouter("basicInfo");
	  				}
	  				break;
				case "deleteCourse":
					if($('#courseId').val()!=null &&  $('#courseId').val()!=''){
						rightCont.loadDeleteCoursePage("删除课程",$("#courseId").val());
	  				}else{
	  					$.fn.jalert2("请先设置基本信息");
	  					urlRouter("basicInfo");
	  				}
	  				break;
                case "video":
                    if(opt) {
                        rightCont.loadVideoPage(opt,"01");
                        break;
                    }
                case "audio":
                    if(opt) {
                        rightCont.loadVideoPage(opt,"02");
                        break;
                    }
                case "doc":
                    if(opt) {
                        rightCont.loadVideoPage(opt,"04");
                        break;
                    }
                case "ppt":
                    if(opt) {
                        rightCont.loadVideoPage(opt,"05");
                        break;
                    }
                case "img":
                    if(opt) {
                        rightCont.loadVideoPage(opt,"03");
                        break;
                    }
                case "exam":
                    if(opt) {
                        rightCont.loadVideoPage(opt,"08");
                        break;
                    }
                case "task":
                    if(opt) {
                        rightCont.loadVideoPage(opt,"10");
                        break;
                    }
	  			case "sectionsDirectory":
	  			default:
	  				rightCont.loadSectionDirectoryPage("章节目录");			
	  		}
		},10);
		
		
	}
	urlRouter();

	//ajax保存课程基本信息
	function saveBaseInfo(){
		if(!$("#formBasicInfo").valid()){
			return;
		}
		$.post('${ctx}/ajax/course/saveBaseInfo',{
			 courseId : $("#courseId").val(),
			 courseTitle: $("#courseTitle").val(),
			 subTitle:  $("#subTitle").val(),
			 keyword: $("#keyword").val(),
			 courseType: $("#courseType").val(),
			},
			function(data){
				$("#courseId").val(data.courseid);
			},"json")
		.success(function(){
			//提交成功跳转到详细信息
       	    urlRouter("detailInfo");
		});
	}
	
	//ajax保存课程详细信息
	function saveDetailInfo(){
		if(!$("#formDetailInfo").valid()){
			return;
		}
		$.post('${ctx}/ajax/course/saveDetailInfo',{
			 courseId : $("#courseId").val(),
			 courseAbstract: $("#courseAbstract").val(),
			 learnObjectives:  $("#learnObjectives").val(),
			 suggestedGroup: $("#suggestedGroup").val(),
			 courseRequirements: $("#courseRequirements").val(),
			 courseAuthor: $("#courseAuthor").val(),
			 authorDescrip: $("#authorDescrip").val()
			})
		.success(function(){
			//提交成功跳转到详细信息
       	    urlRouter("promotion");
		});
	}
		
	//ajax保存课程详细信息
	function saveIsPublish(){
		if($(':radio[name="encryptType"]:checked').val()=="passwordProtect"&&!$("#formAccessRight").valid()){
			return;
		}
		if($(':radio[name="encryptType"]:checked').val()=="authorized"){
			 $("#coursePwd").val("");
		}
		$.post('${ctx}/ajax/course/updateIsPublish',{
			courseId : $("#courseId").val(),
			isPublish: $("#permission").val(),
			fdPassword:  $("#coursePwd").val(),
			})
		.success(function(){
			//提交成功跳转到详细信息
       	    urlRouter("kinguser");
			
		});
	}
	//清空密码input
	function removePass(){
		$("#coursePwd").attr("class", "valid");
		$("#passRadio label").remove();
		
		$("#coursePwd").val("");
	}
	
	//课程发布
	function releaseCourse(){
		window.location.href="${ctx}/course/releaseCourse?courseId="+$("#courseId").val();
	}
	
	//课程预览
	function previewCourse(){
		window.open("${ctx}/course/previewCourse?courseId="+$("#courseId").val(),'_blank');
	}
	
	//课程封页图片保存
    function saveCoursePic(){
    	$.post('${ctx}/ajax/course/cover',{
			courseId : $("#courseId").val(),
			attId: $("#attIdID").val(),
			})
		.success(function(){
			if ($('#upMovie').length > 0) { 
				//注意jquery下检查一个元素是否存在必须使用 .length >0 来判断
			     $('#upMovie').uploadify('destroy'); 
			}
       	    urlRouter("accessRight");
		});
    }

</script>
</body>
</html>
