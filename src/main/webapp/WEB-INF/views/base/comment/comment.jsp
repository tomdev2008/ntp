<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
<link href="${ctx}/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="${ctx}/resources/css/emigrated.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${ctx}/resources/js/jquery.js"></script>
<!-- 评论条目模板 -->
<script id="commentLineTemplate" type="text/x-dot-template">
       <li class="media" dataId="{{=it.fdId}}">
            <a href="#" class="pull-left"><tags:image href="{{=it.fdUserURL}}" clas="media-object"/></a>
            <div class="media-body">
              <div class="media-heading"> {{=it.no}}# {{=it.fdUserName}}（{{=it.fdUserEmail}}）    来自 {{=it.fdUserDept}}
              </div>
              <p class="comt-content">{{=it.content}}</p>
              <div class="media-footing clearfix">
              	<span class="pull-left"><i class="icon-time"></i>{{=it.fdCreateTime}}</span>
                <div class="pull-right btns-comt">
                	<a href="javascript:void(0)" dataId="{{=it.fdId}}" class="suport" ><i class="icon-thumbs-up"></i>0</a>
                	<a href="javascript:void(0)" dataId="{{=it.fdId}}" class="oppose" ><i class="icon-thumbs-down"></i>0</a>
                	<a href="#" ><i class="icon-share-alt"></i>0</a>
                </div>
              </div>
            </div>
       </li> 
</script>
<script src="${ctx}/resources/js/doT.min.js"></script>
<div class="main">  
    <div class="section">
        <div class="page-header">
        	<div class="tit-icon_bg"><i class="icon-white-info"></i><i class="icon-sj"></i></div>
        	<h5>全部评论</h5>
            <div class="pages">
            	<div class="span2">
                	第<span id="pageLine1"></span> / <span id="pageTotal1"></span> 条
                </div>
                <div class="btn-group">
                	<button id="gotoBefore1" class="btn btn-primary"><i class="icon-chevron-left icon-white"></i></button>
                    <button id="gotoNext1" class="btn btn-primary"><i class="icon-chevron-right icon-white"></i></button>
                </div>
            </div>
        </div>
        <ul class="media-list comment-list" id="commentListUl">
        </ul>
         <div class="comment-list-bottom clearfix">
         	<div class="pages pull-right">
            	<div class="span2">
                	第<span id="pageLine2"></span> / <span id="pageTotal2"></span> 条
                </div>
                <div class="btn-group">
                	<button id="gotoBefore2" class="btn btn-primary"><i class="icon-chevron-left icon-white"></i></button>
                    <button id="gotoNext2" class="btn btn-primary"><i class="icon-chevron-right icon-white"></i></button>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
////////////////////////初始化开始
var pageSize=10;
initCommentLines("${param.modelName}","${param.modelId}",1);
initCommentPageInfo("${param.modelName}","${param.modelId}",1);
$(".suport").bind("click",function(){
	$.ajax({
		  url: "${ctx}/ajax/message/supportOrOpposeMessage",
		  async:false,
		  dataType : 'json',
		  data:{
			  messageId :$(this).attr("dataId"),
			  fdType :"01",
		  },
		  success: function(result){
			  alert("成功");
		  }
	});
});
$(".oppose").bind("click",function(){
	alert($(this).attr("dataId"));
});
////////////////////////初始化结束
//初始评论列表化页面
function initCommentLines(modelName,modelId,pageNo){
	$.ajax({
		  url: "${ctx}/ajax/message/findCommentByModelId",
		  async:false,
		  dataType : 'json',
		  data:{
			  modelName :modelName,
			  modelId :modelId,
			  pageNo:pageNo,
			  pageSize:pageSize,
		  },
		  success: function(result){
			  var html ="";
			  var commentLineTemplate = doT.template(document.getElementById("commentLineTemplate").text);
			  for(var i=0;i<result.listComments.length;i++){
				  html = html + commentLineTemplate(result.listComments[i]);
			  }
			  $("#commentListUl").html(html);
		  }
	});
}
//初始化分页信息
function initCommentPageInfo(modelName,modelId,pageNo){
	$.ajax({
		  url: "${ctx}/ajax/message/getCommentPageInfo",
		  async:false,
		  dataType : 'json',
		  data:{
			  modelName:modelName,
			  modelId :modelId,
			  pageNo:pageNo,
			  pageSize:pageSize,
		  },
		  success: function(result){
			 //alert(JSON.stringify(result));
			 $("#pageLine1").html(" "+result.startLine+" - "+result.endLine+" ");
			 $("#pageLine2").html(" "+result.startLine+" - "+result.endLine+" ");
			 $("#pageTotal1").html(result.totalSize);
			 $("#pageTotal2").html(result.totalSize);
			 
			 /* $("#gotoBefore1").bind("click",function (){gotoBefore(result.pageNo);});
			 $("#gotoBefore2").bind("click",function (){gotoBefore(result.pageNo);});
			 $("#gotoNext1").bind("click",function (){gotoNext(result.pageNo);});
			 $("#gotoNext2").bind("click",function (){gotoNext(result.pageNo);}); */
			
			 //第一页
			 if(result.pageNo==1){
				 $("#gotoBefore1").unbind();
				 $("#gotoBefore2").unbind();
				 $("#gotoNext1").bind("click",function (){gotoNext(result.pageNo);});
				 $("#gotoNext2").bind("click",function (){gotoNext(result.pageNo);});
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
				 $("#gotoBefore1").bind("click",function (){gotoBefore(result.pageNo);});
				 $("#gotoBefore2").bind("click",function (){gotoBefore(result.pageNo);});
				 $("#gotoBefore1").removeAttr("disabled");
				 $("#gotoBefore2").removeAttr("disabled");
			 }
			 //中间
			 if(result.pageNo!=result.totalPage&&result.pageNo!=1){
				 $("#gotoNext1").bind("click",function (){gotoNext(result.pageNo);});
				 $("#gotoNext2").bind("click",function (){gotoNext(result.pageNo);});
				 $("#gotoBefore1").bind("click",function (){gotoBefore(result.pageNo);});
				 $("#gotoBefore2").bind("click",function (){gotoBefore(result.pageNo);});
				 $("#gotoBefore1").removeAttr("disabled");
				 $("#gotoBefore2").removeAttr("disabled");
				 $("#gotoNext1").removeAttr("disabled");
				 $("#gotoNext2").removeAttr("disabled");
			 }
			 
		  }
	});
}
//进入下一页方法
function gotoBefore(pageNo){
	initCommentLines("${param.modelName}","${param.modelId}",pageNo-1);
	initCommentPageInfo("${param.modelName}","${param.modelId}",pageNo-1);
}
//进入上一页方法
function gotoNext(pageNo){
	initCommentLines("${param.modelName}","${param.modelId}",pageNo+1);
	initCommentPageInfo("${param.modelName}","${param.modelId}",pageNo+1);
}

</script>

