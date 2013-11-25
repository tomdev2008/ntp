<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新东方在线教师备课平台</title>
<link href="${ctx}/resources/css/global.css" rel="stylesheet" type="text/css">
<link href="${ctx}/resources/css/iAmMentor.css" rel="stylesheet" type="text/css">


    <!--老师列表模板-->
    <script id="listTeacherTemplate" type="x-dot-template">
            {{~it.list :item:index}}
            <li class="media" data-fdid="{{=item.id}}">
               <input type="checkbox" name="noteIds" value="{{=item.id}}" style="display: none;"/>
                <div class="pull-left">
                    <a href="{{!item.user.link || '#'}}" class="face" target="_blank">
                        <img src="{{?item.user.imgUrl.indexOf('http')>-1}}{{=item.user.imgUrl}}{{??}}${ctx}/{{=item.user.imgUrl}}{{?}}" class="media-object img-polaroid" alt="头像"/>
                    </a>
                    <a href="#" class="send msg" ><i class="icon-msg"></i>发私信</a>
                    <a href="mailto:{{=item.user.mail}}" class="send" ><i class="icon-envelope"></i>发邮件</a>
                </div>
                <div class="media-body">
                    <div class="media-heading">
                        <span class="name">{{=item.user.name}}</span>
                        <span class="muted">{{=item.user.org}} {{=item.user.department}}</span>
                                            <span class="muted right">
                                                <i class="icon-tel"></i>{{=item.user.phone || ''}}
                                                <i class="icon-envelope"></i>{{=item.user.mail}}
                                            </span>
                    </div>
                    <dl>
                        <dt>课程：</dt>
                        <dd>{{=item.courseName}}</dd>
                        <dt>导师：</dt>
                        <dd>{{=item.mentor}}</dd>
                        <dt>当前环节：</dt>
                        <dd>{{=item.currLecture}}</dd>
                    </dl>
                </div>
                <div class="media-foot">
                    {{?it.status == 'unchecked'}}
                        <div class="toolbar">
                            <a href="#" class="onlineCorrecting"><i class="icon-checkbox"></i>在线批改</a>
                            <em>|</em>
                            <a href="javascript:void(0)" onclick="downloadAtt('{{=item.downloadBoxUrl}}','{{=item.zipname}}');"  class="downloadBox"><i class="icon-downloadbox"></i>打包下载</a>
                        </div>
                    {{??}}
                        <div class="statebar">作业包总分 <strong>{{=item.scoreTotal}}</strong>  分<em>|</em>及格分 <strong>{{=item.scorePass}}</strong>  分<em>|</em>当前批改的总得分 <strong>{{=item.score}}</strong>  分</div>
                        <span class="isPass{{?item.isPass}} pass{{?}}">{{?item.isPass}}通过{{??}}未通过{{?}}</span>
                    {{?}}
                </div>
            </li>
            {{~}}
    </script>
<script id="pageheardTemplate" type="text/x-dot-template">
	<div class="span2">
 	   第<span > {{=it.startNum}} - {{=it.endNum}}</span> / <span >{{=it.totalCount}}</span> 条
	</div>
	<div class="btn-group">
    <button class="btn btn-primary btn-ctrl" type="button" {{?it.currentPage <= 1}} disabled {{?}} onclick='pageNavClick({{=it.currentPage-1}})'><i class="icon-chevron-left icon-white"></i></button>
    <button class="btn btn-primary btn-ctrl" type="button" {{?it.currentPage == it.totalPage}} disabled {{?}} onclick='pageNavClick({{=it.currentPage+1}})'><i class="icon-chevron-right icon-white"></i></button>
	</div>
</script>
<script id="pageEndTemplate" type="text/x-dot-template">
	<div class="btn-group dropup">
	 <button class="btn btn-primary btn-ctrl" type="button" {{?it.currentPage<=1}}disabled{{?}} onclick='pageNavClick({{=it.currentPage-1}})'>
	 <i class="icon-chevron-left icon-white"></i></button>
	{{ for(var i=it.StartPage;i<=it.EndPage;i++){ }}
			{{?i==it.currentPage}}
				<button class="btn btn-primary btn-num active" type="button">{{=i}}</button>
			{{??}}
				<a  onclick="pageNavClick({{=i}})">
				<button class="btn btn-primary btn-num" type="button">{{=i}}</button>
				</a>
			{{?}}
	{{}}}
	 <button class="btn btn-primary btn-num  dropdown-toggle"  data-toggle="dropdown" type="button">
                            <span class="caret"></span></button>
	     <ul class="dropdown-menu pull-right">
		{{ for(var j=it.StartOperate;j<=it.EndOperate;j++){ }}
			<li><a href="javascript:void(0)" onclick="pageNavClick({{=j}})">{{=j*10-10+1}}-{{=j*10}}</a></li>
		{{}}}
		</ul>
	 <button class="btn btn-primary btn-ctrl" type="button" {{?it.currentPage == it.totalPage}} disabled {{?}} onclick='pageNavClick({{=it.currentPage+1}})'><i class="icon-chevron-right icon-white"></i></button>
</div>
</script>
    <script src="${ctx}/resources/js/doT.min.js" type="text/javascript"></script>
</head>

<body>


<!--主体 S-->
<section class="container">	
		<div class="clearfix mt20">
	        <div class="pull-left w760">
	        
                <div class="page-body">

                    <section class="section box-control">
                        <ul class="nav nav-tabs" id="navTabs">
                            <li class="active"><a href="#unchecked" >未批改的</a></li>
                            <li><a href="#checked" >批改过的</a></li>
                        </ul>
                        <div class="hd">
                            <div class="btn-toolbar">
                                <div class="btn-group">
                                    <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                                     		   操作
                                        <span class="caret"></span></a>
                                    <ul class="dropdown-menu">
                                        <li><a href="#rightCont">导出列表</a></li>
                                        <li><a href="#rightCont" onclick="batchDownload();">打包下载</a></li>
                                    </ul>
                                </div>
                                <form class="toolbar-search">
                                    <input type="text" class="search" id="search" onkeydown="showSearch();" onkeyup="showSearch();">
                                    <i class="icon-search" onclick="pageNavClick('1');"></i>
                                </form>
                            <span class="showState">
                                <span class="muted">当前显示：</span>
                                <span id="markshow"><a id="containkey"href="#">全部条目</a></span>
                            </span>
                                <a class="btn btn-link" href="#rightCont" href="javaScript:void(0);" onclick="clearserach();">清空搜索结果</a>
                            </div>
                        </div>
                        <div class="bd">
                            <div class="btn-toolbar">
                                <label class="muted">排序</label>
                                <div class="btn-group btns-radio" data-toggle="buttons-radio">
                                <a onclick="getDataOrderBy('1','FDCREATETIME');">
                                  <c:if test="${param.order=='FDCREATETIME'}">
								    <button class="btn btn-large active" type="button">时间</button>
							      </c:if>
							      <c:if test="${param.order!='FDCREATETIME'}">
								    <button class="btn btn-large" type="button">时间</button>
							      </c:if>
							     </a>
							     <a onclick="getDataOrderBy('1','fdname');">
							      <c:if test="${param.order=='fdname'}">
							        <button class="btn btn-large active" type="button">课程</button>
							      </c:if>
							      <c:if test="${param.order!='fdname'}">
							        <button class="btn btn-large" type="button">课程</button>
							      </c:if>
							     </a>
							     <a onclick="getDataOrderBy('1','user');">
							      <c:if test="${param.order=='user'}">
							        <button class="btn btn-large active" type="button">新教师</button>
							      </c:if>
							      <c:if test="${param.order!='user'}">
							        <button class="btn btn-large" type="button">新教师</button>
							      </c:if>
							     </a>
                                </div>
                                <label class="radio inline" for="selectCurrPage">
                                <input type="radio" id="selectCurrPage" onclick="checkcurrpage()" name="selectCheckbox"/>选中本页</label>
                                <label class="radio inline" for="selectAll">
                                <input type="radio" id="selectAll" onclick="selectAll()" name="selectCheckbox" />选中全部</label>
                                <div class="pages pull-right" id="pageheard">
                                    
                                </div>
                            </div>
                        </div>
                    </section>
                    <section class="listWrap">
                        <ul class="listTeacher media-list" id="listTeacher">

                        </ul>
                    </section>
                    <div class="pages" id="pageEnd">
                       
                    </div>
                </div>
			</div>
			<div class="pull-right w225">
                <div class="section">
                    <div class="profile">
                        <a href="#"><img src="${ctx}/resources/images/face-placeholder.png" class="face" alt="头像"/></a>
                        <h5>杨义锋 <i class="icon-male"></i></h5> <!-- 女人用.icon-female -->
                        <p class="muted">
                                                             集团总公司 知识管理中心 <br/>
                                                            最近登录    3 天前<br/>
                                                            在线统计    35 天
                        </p>
                    </div>
                </div>
                <div class="section navTeacher" data-spy="affix" data-offset-top="384">
                	<div class="hd">
                		<h5>我是导师</h5>
                	</div>
                    <div class="bd">
                    	<div class="listImg">
                        	<a href="#">
                    			<img src="${ctx}/resources/images/iAmTeacher/track.jpg" alt="">
                    			<span class="mask"></span>
                    			<span class="caption">
                                	<h6>学习跟踪</h6>
                                </span>
                            </a>
                            <a href="#">
                    			<img src="${ctx}/resources/images/iAmTeacher/checkwork.jpg" alt="">
                    			<span class="mask"></span>
                    			<span class="caption">
                                	<h6>批改作业</h6>
                                </span>
                            </a>
                            <a href="#">
                    			<img src="${ctx}/resources/images/iAmTeacher/schedule.jpg" alt="">
                    			<span class="mask"></span>
                    			<span class="caption">
                                	<h6>安排日程</h6>
                                </span>
                            </a>
                        </div>
                    </div>
                </div>
	        </div>
        </div>
<input type="hidden" id="fdType"/>
<input type="hidden" id="fdOrder" value="${param.order}"/>
<!--底部 S-->
	
<!--底部 E-->
</section>
<!--主体 E-->
<script src="${ctx}/resources/js/jquery.jalert.js" type="text/javascript"></script>
<script type="text/javascript">
var unCheckedData = {},checkedData= {} ;

/*老师列表模板函数*/
var listTeacherFn = doT.template(document.getElementById("listTeacherTemplate").text);
//头部翻页
var pageheardFn=doT.template(document.getElementById("pageheardTemplate").text);
//底部翻页
var pageendFn= doT.template(document.getElementById("pageEndTemplate").text);

    $(function(){
       
        initlistTeacher("unchecked");//初始化列表
        
        //loadList("unchecked");//默认加载未批改的

        $("#navTabs>li>a").on("click",function(e){
            e.preventDefault();
            $(this).parent().addClass("active").siblings().removeClass("active");
            initlistTeacher($(this).attr("href").slice(1));
            loadList($(this).attr("href").slice(1));
        });

        
        function initlistTeacher(fdType){
        	$.ajax({
          	  	type: "POST",
        		url: "${ctx}/ajax/adviser/findCheckTaskList",
        		async:false,
        		cache: false, 
        		data : {
                  	"fdType":fdType,
                },
        		dataType : 'json',
        		success: function(result){
        			//alert(JSON.stringify(result.checkedData));
        			if(fdType=="checked"){
        				checkedData.list = result.checkedData;
        			}else{
        				unCheckedData.list = result.checkedData;
        			}
        			$("#pageheard").html(pageheardFn(result.paging));
             		$("#pageEnd").html(pageendFn(result.paging));
        			loadList(fdType);
        			$("#fdType").val(fdType);
        		}
        	});
        }

});
//批量下载或者下载全部
function batchDownload(){
	if(document.getElementById("selectAll").checked){
		 var keyword=$("#search").val();
		 var fdType=$("#fdType").val();
		 $.fn.jalert("您下载全部数据吗？",function(){
			  window.location.href="${ctx}/common/file/allDownloadTaskZip/"+fdType+"/作业?keyword="+keyword;
			  return;
		});
	} else if(document.getElementById("selectCurrPage").checked){
		var chk_value = [];
		$('input[name="noteIds"]:checked').each(function() {
			chk_value.push($(this).val());
	    });
		$.fn.jalert("您下载本页数据吗？",function(){
			  window.location.href="${ctx}/common/file/batchDownloadTaskZip/"+chk_value+"/作业";
			  return;
		});
	} else{
		 $.fn.jalert2("您好!您没有选择要下载的数据！");
		  return;
	}
}
//全部选中
function selectAll(){
  $('input[name="noteIds"]').each(function(){
	   $(this).attr("checked",false);// 
  });
}
//选中当前页
function checkcurrpage(){
    $('input[name="noteIds"]').each(function(){
    	$(this).attr("checked",true);// 
   });
}
//下载
function downloadAtt(attIds,zipname){
	if(attIds==null||attIds==''){
		 $.fn.jalert2("您好!该作业包没有数据可下载！");
		   return;
	}
    window.location.href="${ctx}/common/file/downloadZipsByArrayIds/"+attIds+"/"+zipname;
}
//翻页
function pageNavClick(pageNo){
  var keyword=$("#search").val();
  var fdType=$("#fdType").val();
  var order=$("#fdOrder").val();
  if(order==''){
	  order='FDCREATETIME';
  }
  $.ajax({
        type: "post",
        url: "${ctx}/ajax/adviser/findCheckTaskList",
        data : {
          	"order":order,
          	"pageNo" : pageNo,
          	"keyword":keyword,
          	"fdType":fdType,
        },
        cache: false, 
        dataType: "json",
        success:function(result){	
        	//alert(fdType);
        	if(fdType=="checked"){
				checkedData.list = result.checkedData;
			}else{
				unCheckedData.list = result.checkedData;
			}
			$("#pageheard").html(pageheardFn(result.paging));
     		$("#pageEnd").html(pageendFn(result.paging));
			loadList(fdType);
			$("#fdType").val(fdType);
        }
  }); 
}
function getDataOrderBy(pageNo,order){
	 $("#orderBy").val(order);
	 pageNavClick(pageNo);
}
function clearserach(){//清理搜索栏并显示数据列表
	$("#search").val("");
	$("#markshow").html('<a id="containkey"href="#">全部条目</a>');
}

function showSearch(){
	var search = $("#search").val();
	$("#markshow").html('含“<a id="containkey"href="#"></a>”的用户');
	if(search.length>8){
		$("#containkey").html(search.substr(0,8)+"...");
	}else{
		$("#containkey").html(search);
	}
}

 /*加载列表*/
function loadList(fdType){
    switch (fdType){
        case "checked":
            checkedData.status = "checked";
            $("#listTeacher").html(listTeacherFn(checkedData));
            break;
        case "unchecked":
            unCheckedData.status = "unchecked";
            $("#listTeacher").html(listTeacherFn(unCheckedData));
        default :
            unCheckedData.status = "unchecked";
            $("#listTeacher").html(listTeacherFn(unCheckedData));

        }
        $("#listTeacher>.media").bind({
                        "mouseover": function(e){
                            $(this).addClass("active");
                        },
                        "mouseout": function(e){
                            $(this).removeClass("active");
                        },
                        "click": function(e){
                            var $target = $(e.target)
                            ,$parent = $target.parent();
                            if($target.add($parent).hasClass("send") || $target.add($parent).hasClass("downloadBox")){
                                e.stopPropagation();
                                if($target.add($parent).hasClass("msg")){
                                    e.preventDefault();
                                    //发私信
                                    alert("发私信");
                                }
                            }  else{
                                window.open("http://ntp.xdf.cn/" + $(this).attr("data-fdid"));//打开详情页面
                            }
                        }
                    });
        }
</script>
</body>
</html>
