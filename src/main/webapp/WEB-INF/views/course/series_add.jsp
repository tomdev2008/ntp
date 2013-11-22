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
<link href="${ctx}/resources/css/template_detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${ctx}/resources/css/jquery.autocomplete.css">
<script type="text/javascript" src="${ctx}/resources/js/jquery.autocomplete.pack.js"></script>
<script src="${ctx}/resources/js/jquery.jalert.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.placeholder.1.3.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/messages_zh.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.sortable.js"></script>
<script type="text/javascript" src="${ctx}/resources/uploadify/jquery.uploadify.js?id=1211"></script>

<!--[if lt IE 9]>
<script src="js/html5.js"></script>
<![endif]-->

<!-- 模板详情_右侧内容区标题 模板 -->
<script id="contHeaderTemplate" type="text/x-dot-template">
 	<div class="page-header">        	
		<h4>{{=it.pageTitle || ''}}</h4>           
	</div>
</script>

<!-- 模板详情_系列目录 模板 -->
<script id="sectionDirectoryTemplate" type="text/x-dot-template">
	{{#def.pageHeader}}
    	<div class="page-body">
	    	<div class="section" id="progress_courses">          
	       		
	            	<div class="progress progress-course">
	            		<div class="bar" style="width:20%;"></div>
	            	</div>
	                您已经完成了本系列的 <span class="num_comp"></span> 个阶段内容，共计 <span class="num_all">{{=it.lecture.length}}</span> 阶段。
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
			    <button id="addSeries" class="btn btn-block" type="button">添加阶段</button>
	        </div>
	      </div>
      </div> 
</script>

<!--添加系列表单模板-->
<script id="addSectionsTemplate" type="text/x-dot-template">
	<li class="{{?it.chapter}}chapter{{??it.lecture}}lecture{{?}}" data-fdid="">
		{{#def.edittitle || ''}}		
	</li>
</script>

<!--编辑系列标题表单模板-->
<script id="formEditSectionTitle" type="text/x-dot-template">
	<div class="form-edit-title form-horizontal">		
			<div class="control-group">
				<label class="control-label">第<span class="index">{{?it.chapter}}{{=it.chapter.num}}</span>阶段{{??it.lecture}}{{=it.lecture.num}}</span>课程{{?}}</label>
				<div class="controls">
                   {{?it.chapter}}<input type="text" maxlength="20" class="input-block-level" placeholder="请输入标题内容" value="{{=(it.chapter ? it.chapter.title : it.lecture.title) || ''}}" />
					{{??it.lecture}}<input type="text" id="courses" name="courses" class="autoComplete input-block-level" placeholder="请选择课程信息">
                    {{?}}
					<span class="count">20字</span>
				</div>
			</div>
			<div class="controls">
				<button class="btn btn-primary btn-large" type="button">保存</button>
				<button class="btn btn-link" type="button">取消</button>
			</div>		
	</div>
</script>

<!--展示系列模板-->
<script id="sectionsTemplate" type="text/x-dot-template">
		{{?it.chapter}}
			{{#def.sectionbar:it.chapter}}
		{{??it.lecture}}
			{{#def.sectionbar:it.lecture}}
			{{#def.lecturecontent:it.lecture}}
		{{?}}		              
</script>

<!--系列条模板-->
<script id="sectionBarTemplate" type="text/x-dot-template">
	{{##def.sectionbar:param:
		<div class="sortable-bar">
			<span class="title">{{?param.type}}<i class="icon-{{=param.type}}"></i>{{?}}
				第<span class="index">{{=param.num}}</span>阶段
				<span class="name">{{=param.title || ''}}</span>
			</span>
			<a class="icon-pencil2{{?!param.type}} icon-white{{?}} btn-ctrls" href="#"></a>
			<a class="icon-remove{{?!param.type}} icon-white{{?}} btn-ctrls" href="#"></a>
			<a href="#" class="btn-edit">编辑内容</a>
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

<!--阶段内容模板-->
<script id="SeriesContentTemplate" type="text/x-dot-template">
	{{##def.lecturecontent:param:
		<div class="lecture-content hide">
			<div class="hd">
			编辑内容
			<a href="#" class="icon-remove-sign"></a>
			</div>
			<div class="bd">
				<a class="btn-type" href="#"><i class="icon-doc-lg"></i><h5>课程</h5></a>
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
				<a class="btn-type" href="#"><i class="icon-doc-lg"></i><h5>课程</h5></a>
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
                    	<label for="seriesTitle">系列名称</label>
                        <input type="text" id="seriesTitle" name="seriesTitle" required minlength="6" class="input-block-level" value="{{=it.seriesTit || ''}}"  />
                        <label for="seriesDesc">系列描述</label>
                        <textarea name="seriesDesc" id="seriesDesc" required minlength="12" class="input-block-level" rows="3">{{=it.seriesDesc || ''}}</textarea>   
                    </fieldset>
						<label for="sectionOrder"></label>
						<div class="btn-group btns-radio" data-toggle="buttons-radio">
                            <button class="btn btn-large{{?it.isavailable==null || it.isavailable}} active{{?}}" id="true" type="button">有效</button>
                            <button class="btn btn-large{{?it.isavailable!=null && !it.isavailable}} active{{?}}" id="false" type="button">无效</button>
                        </div>
	       </div>
           <button class="btn btn-block btn-submit btn-inverse" type="button" onClick="saveBaseInfo()">保存</button>
       </form>
       </div>	  
	 </div> 
</script>
<!-- 系列推广 模板 -->
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
						     <button id="upMovie" class="btn btn-primary btn-large" type="button" >上传</button>
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
<!-- 删除系列 模板 -->
<script id="deleteSeriesTemplate" type="text/x-dot-template">
	{{#def.pageHeader}} 
    <div class="page-body deleteCourse-content">   	        
	    	<div class="section" >
					<div class="well">
						警告：您点击下面的删除系列按钮以后，将会出现以下结果：<br />
1. 本系列将不复存在；<br />
2. 与系列相关的课程记录也将一并删除；<br />
3. 本系列中的课程资料 请前往 课程信息 进行查阅。
					</div>
	       </div>		 
           <button class="btn btn-block btn-warning btn-submit" id="deleteCourse" type="button">删除</button>         
	 </div> 	
</script>
<script id="listUserKinguserTemplate" type="text/x-dot-template">
    <tr data-fdid="{{=it.id}}">
        <td class="tdTit">
          <div class="pr">
            <div class="state-dragable"><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></div>
            <img src="{{=it.imgUrl || '${ctx}/resources/images/temp-face36.jpg'}}" alt="">{{=it.name}}（{{=it.mail}}），{{=it.org}} {{=it.department}}
          <div>
         </td>
        <td><input type="checkbox" {{?it.tissuePreparation!=false}}checked{{?}} class="tissuePreparation" /></td>
        <td><input type="checkbox" {{?it.editingCourse!=false}}checked{{?}} class="editingCourse" /></td>
        <td><a href="#" class="icon-remove-blue"></a></td>
    </tr>
</script>
<script src="${ctx}/resources/js/doT.min.js"></script>
<script src="${ctx}/resources/js/templSeriesPages.js"></script>

</head>

<body>

<input type="hidden" id="ctx" value="${pageContext.request.contextPath}"/>
<section class="container">
	<div class="clearfix">
		<div class="tit-bar">    	
	        <div class="page-title section" id="page-title">
	        	<input type='hidden' id='seriesId' value='${series.fdId}' />
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
	                系列内容
	            </li>
	            <li class="active"><a href="#sectionsDirectory">阶段目录</a></li>
	             <li class="nav-header">
	                <i class="icon-setting"></i>
	                系列设置
	            </li>
	            <li ><a href="#basicInfo">系列信息</a></li> 
	            <li><a href="#promotion">系列推广</a></li>  
	            <li><a href="#deleteSeries">系列删除</a></li>       
	    </ul>
	  </div>
		<div class="w790 pull-right" id="rightCont">    
	   
	    </div>
	    <!--  <input type="text" required name="inputTeacher" id="inputTeacher" class="autoComplete input-block" placeholder="授权某位老师学习本课程">
	   -->
	</div>
</section>


<script type="text/javascript">	
$.Placeholder.init();
	


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
	  			//	if($('#seriesId').val()!=null &&  $('#seriesId').val()!=''){
	  					rightCont.loadBasicInfoPage("系列信息");
	  			//	}else{
	  			//		$.fn.jalert2("请先设置系列信息");
	  			//		urlRouter("basicInfo");
	  			//	}
	  				break;
	  			case "promotion":
	  			//	if($('#seriesId').val()!=null &&  $('#seriesId').val()!=''){
	  					rightCont.loadPromotionPage("系列推广");
	  			//	}else{
	  			//		$.fn.jalert2("请先设置系列信息");
	  			//		urlRouter("basicInfo");
	  			//	}
	  				break;
				case "deleteSeries":
				//	if($('#seriesId').val()!=null &&  $('#seriesId').val()!=''){
						rightCont.loadDeleteCoursePage("删除课程",$("#seriesId").val());
	  			//	}else{
	  			//		$.fn.jalert2("请先设置系列信息");
	  			//		urlRouter("basicInfo");
	  			//	}
	  				break;
	  			case "sectionsDirectory":
	  			default:
	  				rightCont.loadSectionDirectoryPage("系列目录");			
	  		}
		},10);
		
		
	}
	urlRouter();

	//ajax保存课程基本信息
	function saveBaseInfo(){
		if(!$("#formBasicInfo").valid()){
			return;
		}
		$.post('${ctx}/ajax/series/saveSeries',{
			 seriesTitle: $("#seriesTitle").val(),
			 seriesDesc:  $("#seriesDesc").val()
			},
			function(data){
				$("#seriesId").val(data.seriesId);
			},"json")
		.success(function(){
			//提交成功跳转到阶段信息
       	    urlRouter("sectionsDirectory");
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

	//系列封页图片保存
    function saveSeriesPic(){
    	$.post('${ctx}/ajax/series/cover',{
			seriesId : $("#seriesId").val(),
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
