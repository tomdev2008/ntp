<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
<input id="selectTypeHidden" type="hidden"  value="${param.selectType}" name="selectType"/>
    <!--老师列表模板-->
    <script id="listTeacherTemplate" type="x-dot-template">
            {{~it :item:index}}
            <li class="media" data-fdid="{{=item.id}}">
                <div class="pull-left">
                    <a href="${ctx}/course/courseIndex?userId={{=item.user.userId}}" class="face" target="_blank">
                        <img src="{{?item.user.imgUrl.indexOf('http')>-1}}{{=item.user.imgUrl}}{{??}}${ctx}/{{=item.user.imgUrl}}{{?}}" class="media-object img-polaroid" alt="头像"/>
                    </a>
                    <a href="#" class="send msg" ><i class="icon-msg"></i>发私信</a>
                    <a href="mailto:{{=item.user.mail}}" class="send" ><i class="icon-envelope"></i>发邮件</a>
                </div>
                <div class="media-body">
                    <div class="media-heading">
                        <span class="name">{{=item.user.name}}</span>
                        <span class="muted">{{=item.user.department}}</span>
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
                    <div class="box">
                        <div class="hd">
                            <div class="caret-group">
                                <b class="caret0"></b>
                                <b class="caret1"></b>
                            </div>
                        </div>
                        <div class="bd">
                            <p>{{=item.passMsg}}</p>
                            <div class="time"><i class="icon-time"></i>{{=item.passTime}}</div>
                        </div>
                    </div>
                </div>
            </li>
            {{~}}
    </script>
    <script id="pageTopTemplate" type="x-dot-template">
        <div class="span2">
           	    第<span> {{=it.startLine}} - {{=it.endLine}}</span> / <span>{{=it.totalSize}}</span> 条
        </div>
        <div class="btn-group">
              <button id="gotoBefore1" class="btn btn-primary btn-ctrl" type="button"><i class="icon-chevron-left icon-white"></i></button>
              <button id="gotoNext1"   class="btn btn-primary btn-ctrl" type="button"><i class="icon-chevron-right icon-white"></i></button>
        </div>            
    </script>

<script id="pageBottomTemplate" type="x-dot-template">
	<div class="btn-group dropup">
            <button id="gotoBefore2" class="btn btn-primary btn-ctrl" type="button" disabled><i class="icon-chevron-left icon-white"></i></button>
            {{ for(var i=it.startPage; i <=it.endPage ; i++){ }}
				{{?it.pageNo == i}}
					<button class="btn btn-primary btn-num active" onclick="refreshTrackList('{{=it.type}}',{{=i}},{{=it.pageSize}},'{{=it.order}}')" type="button">{{=i}}</button>
				{{??}}
					<button class="btn btn-primary btn-num" onclick="refreshTrackList('{{=it.type}}',{{=i}},{{=it.pageSize}},'{{=it.order}}')" type="button">{{=i}}</button>
				{{?}}
				
			{{ } }}
            
            <button class="btn btn-primary btn-num  dropdown-toggle"  data-toggle="dropdown" type="button">
                <span class="caret"></span></button>
            <ul class="dropdown-menu pull-right">
				{{ for(var i=1; i <=it.totalPage ; i++){ }}
					<li><a href="javascript:void(0)" onclick="refreshTrackList('{{=it.type}}',{{=i}},{{=it.pageSize}},'{{=it.order}}')">{{=(i-1)*it.pageSize+1}} - {{=(i-1)*it.pageSize+it.pageSize}}</a></li>
				{{ } }}

            </ul>
            <button id="gotoNext2" class="btn btn-primary btn-ctrl" type="button"><i class="icon-chevron-right icon-white"></i></button>
        </div>
	</script>
<div class="page-body">
    <section class="section box-control">
        <div class="hd">
            <div class="btn-toolbar">
                <div class="btn-group">
                    <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                        操作
                        <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="javascript:void(0)" id="expStudyTrackA">导出列表</a></li>
                    </ul>
                </div>
                <form class="toolbar-search">
                    <input type="text" id="serach" class="search" onkeydown="showSearch();" onkeyup="showSearch();">
                    <i class="icon-search" id="selectButton"></i>
                </form>
            <span class="showState">
                <span class="muted">当前显示：</span>         
            		 <span id="markshow" ><a id="containkey" href="#">全部条目</a></span>   
            </span>
                <a class="btn btn-link" href="#rightCont" id="resetSelect">清空搜索结果</a>
            </div>
        </div>
        <div class="bd">
            <div class="btn-toolbar">
                <label class="muted">排序</label>
                <div class="btn-group btns-radio" data-toggle="buttons-radio" id="divOrder"> 
               		<button  date-orderType="time" class="btn btn-large active" type="button">时间</button>
                    <button  date-orderType="course" class="btn btn-large"  type="button">课程</button>
                    <button  date-orderType="newTeacher" class="btn btn-large" type="button">新教师</button>
                    <button  date-orderType="teacher" class="btn btn-large" type="button">导师</button>
                </div>
                <label class="radio inline" for="selectCurrPage"><input value="noPage" type="radio" id="selectCurrPage" name="selectCheckbox" checked />选中本页</label>
                <label class="radio inline" for="selectAll"><input value="all" type="radio" id="selectAll" name="selectCheckbox" />选中全部</label>
                <div class="pages pull-right" id="pageTop" >

                </div>
            </div>
        </div>
    </section>    
    <section class="listWrap">
        <ul class="listTeacher media-list" id="listTeacher">

        </ul>
    </section>
    <div class="pages" id="pagebottom">
    </div>
</div>
<script type="text/javascript">
refreshTrackList("",1,10,"time");
$("#resetSelect").bind("click",function(){
	$("#serach").val("");
	$("#markshow").html('<a id="containkey"href="#">全部条目</a>');
	refreshTrackList("",1,10,"time");
});

$("#selectButton").bind("click",function(){
	refreshTrackList("",1,10,"time",$("#serach").val());
	
});

$("#divOrder button").bind("click",function(){
	refreshTrackList('',1,10,$(this).attr("date-orderType"),$("#serach").val());
});

function refreshTrackList(type,pageNo,pageSize,order){
	if(!order){
		order=$("#divOrder .active").attr("date-ordertype");
	}
	resetOrderImg(order);
	type=$("#selectTypeHidden").val();
	/*老师列表模板函数*/
	var listTeacherFn = doT.template(document.getElementById("listTeacherTemplate").text);
	var pageTopFn = doT.template(document.getElementById("pageTopTemplate").text);
	var pageBottomFn = doT.template(document.getElementById("pageBottomTemplate").text);
	var data = {};
	var pageDate={};
	$.ajax({
		url : "${ctx}/ajax/studyTrack/getTracks",
		async : false,
		dataType : 'json',
		type: "post",
		data:{
			selectType:type,
			pageNo:pageNo,
			pageSize:pageSize,
			order:order,
			key:$("#serach").val(),
		},
		success : function(result) {
			data = result.list;
			pageDate = result.pageInfo;
		}
	});
	$("#expStudyTrackA").bind("click",function(){
		var isAll=$(':radio[name="selectCheckbox"]:checked').val();
		var chk_value = [];
		$("#listTeacher li").each(function() {
			chk_value.push($(this).attr("data-fdid"));
		});
		var serach = $("#serach").val();
		window.location.href="${ctx}/common/exp/getExpStudyTrack?modelIds="+chk_value+"&selectType="+type+"&key="+serach+"&order="+order+"&isAll="+isAll;
	});
	$("#pageTop").html(pageTopFn(pageDate));
	pageDate.type=type;
	pageDate.order=order;
	$("#pagebottom").html(pageBottomFn(pageDate));
	 if(pageDate.pageNo==1){
		 $("#gotoBefore1").unbind();
		 $("#gotoNext1").bind("click",function (){refreshTrackList(type,pageDate.pageNo+1,pageDate.pageSize);});
		 $("#gotoBefore1").attr("disabled",true);
		 $("#gotoNext1").removeAttr("disabled"); 
		 $("#gotoBefore2").unbind();
		 $("#gotoNext2").bind("click",function (){refreshTrackList(type,pageDate.pageNo+1,pageDate.pageSize);});
		 $("#gotoBefore2").attr("disabled",true);
		 $("#gotoNext2").removeAttr("disabled"); 
	 }
	 //最后一页
	 if(pageDate.pageNo==pageDate.totalPage){
		 $("#gotoNext1").unbind();
		 $("#gotoNext1").attr("disabled",true);
		 $("#gotoBefore1").bind("click",function (){refreshTrackList(type,pageDate.pageNo-1,pageDate.pageSize);});
		 $("#gotoBefore1").removeAttr("disabled");
		 $("#gotoNext2").unbind();
		 $("#gotoNext2").attr("disabled",true);
		 $("#gotoBefore2").bind("click",function (){refreshTrackList(type,pageDate.pageNo-1,pageDate.pageSize);});
		 $("#gotoBefore2").removeAttr("disabled");
	 }
	 //只有一页
	 if(pageDate.pageNo==pageDate.totalPage&&pageDate.pageNo==1){
		 $("#gotoNext1").unbind();
		 $("#gotoBefore1").unbind();
		 $("#gotoNext1").attr("disabled",true);
		 $("#gotoBefore1").attr("disabled",true);
		 $("#gotoNext2").unbind();
		 $("#gotoBefore2").unbind();
		 $("#gotoNext2").attr("disabled",true);
		 $("#gotoBefore2").attr("disabled",true);
	 }
	 //中间
	 if(pageDate.pageNo!=pageDate.totalPage&&pageDate.pageNo!=1){
		 $("#gotoNext1").bind("click",function (){refreshTrackList(type,pageDate.pageNo+1,pageDate.pageSize);});
		 $("#gotoBefore1").bind("click",function (){refreshTrackList(type,pageDate.pageNo-1,pageDate.pageSize);});
		 $("#gotoBefore1").removeAttr("disabled");
		 $("#gotoNext1").removeAttr("disabled");
		 $("#gotoNext2").bind("click",function (){refreshTrackList(type,pageDate.pageNo+1,pageDate.pageSize);});
		 $("#gotoBefore2").bind("click",function (){refreshTrackList(type,pageDate.pageNo-1,pageDate.pageSize);});
		 $("#gotoBefore2").removeAttr("disabled");
		 $("#gotoNext2").removeAttr("disabled");
	 }
	 
	$("#listTeacher").html(listTeacherFn(data))
	.children(".media").bind({
	    "mouseover": function(e){
	        $(this).addClass("active");
	    },
	    "mouseout": function(e){
	        $(this).removeClass("active");
	    },
	    "click": function(e){
	        var $target = $(e.target);
	        if(!$target.hasClass("send")){
	            window.open("http://ntp.xdf.cn/" + $(this).attr("data-fdid"));//打开老师页面
	        } else if($target.hasClass("msg")){
	            alert("发私信");
	        }
	    }
	});	
}

function showSearch(){
	var fdTitle = document.getElementById("serach").value;
	$("#markshow").html('含“<a id="containkey" href="#"></a>”的用户');
	if(fdTitle==''){
		$("#markshow").html('<a id="containkey" href="#">全部条目</a>');
	}else if(fdTitle.length>2){
		$("#containkey").html(fdTitle.substr(0,6)+"...");
	}else{
		$("#containkey").html(fdTitle);
	}
}

function resetOrderImg(order){
	$("#divOrder button").removeClass("active");
	$("#divOrder button").each(function(i){
		if($(this).attr("date-orderType")==order){
			$(this).addClass("active");
		}
	});
}
</script>