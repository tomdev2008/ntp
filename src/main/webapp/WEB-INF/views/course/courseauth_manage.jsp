<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<%
 String order = request.getParameter("order");
//String fdType = request.getParameter("fdType");

%>
 <html class=""> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新东方在线教师备课平台</title>
<link rel="stylesheet" href="${ctx}/resources/css/global.css" />
<link href="${ctx}/resources/css/DTotal.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${ctx}/resources/css/jquery.autocomplete.css">
<script src="${ctx}/resources/js/jquery.jalert.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.placeholder.1.3.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/messages_zh.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.autocomplete.pack.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.sortable.js"></script>

</head>
<body>
 <!--老师列表模板-->
    <script id="listTeacherTemplate" type="x-dot-template">
        <ul class="listTeacher unstyled">
        {{~it :item:index}}
            <li>
                <div class="hd"><a class="remove" href="#">&times;</a></div>
                <div class="bd">
                    <label class="pull-left" ><input type="checkbox" name="teacherGroup" id="{{=item.id}}"/></label>
                    <div class="clearfix box">
                            <div class="span3">
                            <div class="type">
                            <span>教  师</span>
                    </div>
                    <div class="person">
                            <a href="{{=item.teacher.link}}" class="pull-left"><img style="width:50px;height:50px;" src="{{=item.teacher.imgUrl || './images/face-placeholder.jpg'}}" alt=""/></a>
                            <span>{{=item.teacher.name}}（{{=item.teacher.mail}}）<br />  {{=item.teacher.org}} {{=item.teacher.department}}</span>
                    </div>
                    </div>
                    <div class="span2">
                        <div class="time">
                            <i class="icon-time"></i>
                            {{=item.time}}
                        </div>
                        <div class="divider">
                                <i class="icon-handshake"></i>
                        </div>
                    </div>
                    <div class="span3">
                        <div class="type">
                            <span>导  师</span>
                        </div>
                        <div class="person">
                            {{?item.mentor}}
                                <a href="{{=item.mentor.link}}" class="pull-left"><img  style="width:50px;height:50px;" src="{{=item.mentor.imgUrl || './images/face-placeholder.jpg'}}" alt=""/></a>
                                <span>{{=item.mentor.name}}（{{=item.mentor.mail}}）<br /> {{=item.mentor.org}} {{=item.mentor.department}}</span>
                            {{??}}
                                <p align="center">本课程不需要导师参与</p>
                            {{?}}
                        </div>
                    </div>
                    </div>
                </div>
            </li>
        {{~}}
        </ul>
    </script>
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
     <script src="${ctx}/resources/js/doT.min.js" type="text/javascript"></script>
		<section class="container">
			<section class="clearfix mt20">
			  <section class="col-left pull-left">
		    	 <%@ include file="/WEB-INF/views/group/menu.jsp" %>
			  </section>
				<section class="w790 pull-right" id="rightCont">
			        <div class="page-header">
		                <span class="muted">授权学习</span> 
		                <div class="backHome">
		                    <a href="#"><span class="muted">返回</span>主管<span class="muted">首页</span> <i class="icon-home icon-white"></i> </a>
		                </div>
			        </div>
			        <div class="page-body authorizedBody">
			       <section class="section mt20">
                    <div class="media">
                        <div class="pull-left"><img class="media-object" id="coursepage" src="" alt=""/></div>
                        <div class="media-body">
                            <div class="media-heading">
                                <h2 id="courseName"></h2>
                            </div>
                            <p id="courseAuthor"></p>
                            <div class="rating-view">
                                <span class="rating-all" id="cavg">
                                    <i class="icon-star "></i><i class="icon-star "></i><i class="icon-star "></i><i class="icon-star "></i><i class="icon-star"></i>
                                 </span>
                                        <b class="text-warning" id="courseavg">0</b>
                            </div>
                            <div class="media-foot">
                                <a class="btn btn-primary btn-large" id="coursedetail" href="javascript:void(0);">查看课程详情</a>
                                <input type="hidden" id="courseId"/>
                            </div>
                        </div>
                    </div>
                </section>
                <section class="section mt20">
                    <form action="#rightCont" id="formAddTeacher">
                        <input type="text" required name="inputTeacher" id="inputTeacher" class="autoComplete input-block" placeholder="授权某位老师学习本课程">
                        <input type="hidden" id="teacher">
                        <div class="divider">
                            <i class="icon-handshake"></i>
                        </div>
                        <input type="text"  name="inputMentor" id="inputMentor" class="autoComplete input-block" placeholder="为该课程指定一位导师"/>
                        <input type="hidden" id="mentor"/>
                        <div class="divider">
                            <button class="btn btn-primary btn-large" type="submit">添加</button>
                            </div>
                    </form>
                </section>
			          <%-- <div class="page-body" id="page list">
			        	<%@ include file="/WEB-INF/views/course/divcourseauth.jsp" %>
			        </div> --%>
			      <section class="section box-control">
                    <div class="hd">
                        <div class="btn-toolbar">
                            <div class="btn-group">
                                <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">操作 <span class="caret"></span></a>
                                <ul class="dropdown-menu">
                                    <li><a href="#rightCont">导出列表</a></li>
                                    <li><a href="#rightCont">打包下载</a></li>
                                    <li><a href="#rightCont">批量删除</a></li>
                                </ul>
                            </div>
                            <form class="toolbar-search">
                                <input type="text" class="search" placeholder="搜索条目">
                                <i class="icon-search"></i>
                            </form>
                            <span class="showState">
                                <span class="muted">当前显示：</span>含“<a href="#">杨</a>”的用户
                            </span>
                            <a class="btn btn-link" href="#rightCont">清空搜索结果</a>
                        </div>
                    </div>
                    <div class="bd">
                        <div class="btn-toolbar">
                            <label class="muted">排序</label>
                            <div class="btn-group btns-radio" data-toggle="buttons-radio">
                                <button class="btn btn-large active" type="button">时间</button>
                                <button class="btn btn-large" type="button">教师</button>
                                <button class="btn btn-large" type="button">导师</button>
                            </div>
                            <label class="radio inline" for="selectCurrPage"><input type="radio" id="selectCurrPage" name="selectCheckbox" checked />选中本页</label>
                            <label class="radio inline" for="selectAll"><input type="radio" id="selectAll" name="selectCheckbox" />选中全部</label>
                            <div class="pages pull-right">
                                <div class="span2">
                                    第<span> 1 - 10</span> / <span>109</span> 条
                                </div>
                                <div class="btn-group">
                                    <button class="btn btn-primary btn-ctrl" type="button" disabled><i class="icon-chevron-left icon-white"></i></button>
                                    <button class="btn btn-primary btn-ctrl" type="button"><i class="icon-chevron-right icon-white"></i></button>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
                <section id="listTeacher">
                </section>
                <div class="pages">
                    <div class="btn-group dropup">
                        <button class="btn btn-primary btn-ctrl" type="button" disabled><i class="icon-chevron-left icon-white"></i></button>          
                        <button class="btn btn-primary btn-num" type="button">1</button>
                        <button class="btn btn-primary btn-num" type="button">2</button>
                        <button class="btn btn-primary btn-num" type="button">3</button>
                        <button class="btn btn-primary btn-num" type="button">4</button>
                        <button class="btn btn-primary btn-num active" type="button">5</button>
                        <button class="btn btn-primary btn-num" type="button">6</button>
                        <button class="btn btn-primary btn-num" type="button">7</button>
                        <button class="btn btn-primary btn-num" type="button">8</button>
                        <button class="btn btn-primary btn-num" type="button">9</button>
                        <button class="btn btn-primary btn-num" type="button">10</button>
                        <button class="btn btn-primary btn-num  dropdown-toggle"  data-toggle="dropdown" type="button">
                            <span class="caret"></span></button>
                        <ul class="dropdown-menu pull-right">
                            <li><a href="#">11-20</a></li>
                            <li><a href="#">21-30</a></li>
                            <li><a href="#">31-40</a></li>
                        </ul>
                        <button class="btn btn-primary btn-ctrl" type="button"><i class="icon-chevron-right icon-white"></i></button>
                    </div>
                </div>
            </div>
	    </section>
	</section>
            <!-- 缓存插叙关键字 -->
			<input type="hidden" id="coursekey" name="coursekey">
			<input type="hidden" id="allFlag" >
			<input type="hidden"  id="cousetype" value="${param.fdType}">
            <input type="hidden" id="cachorder"/>
</section>
<script type="text/javascript">	
function pressEnter(){//回车事件
	if(event.keyCode==13){
		findeCoursesByKey(1,$('#cachorder').val());
	}
}
function clearserach(){//清理搜索栏并显示数据列表
	//alert('ss');
	$("#serach").attr("value","");
	$("#markshow").html('<a id="containkey"href="#">全部条目</a>');
	findeCoursesByKey(1,'fdcreatetime');
}

function showSearch(){
	var fdTitle = document.getElementById("serach").value;
	$("#markshow").html('含“<a id="containkey"href="#"></a>”的条目');
	if(fdTitle.length>2){
		$("#containkey").html(fdTitle.substr(0,2)+"...");
		}else{
			$("#containkey").html(fdTitle);
		}
}
function findeCoursesByKey(pageNo,order){
	var fdTitle = document.getElementById("serach").value;
	if($('input[name="selectCheckbox"]:checked').val()==1){
		$("#allkey").attr("value",1);
	}
	$('#cachorder').attr('value',order);
	$("#coursekey").attr("value",fdTitle);//关键字赋值
	$("#pageBody").html("");
	$.ajax({
		type: "post",
		 url: "${ctx}/ajax/course/getCoureAuthInfosOrByKey",
		data : {
			"fdTitle" : fdTitle,
			"pageNo" : pageNo,
			"order" : order,
		},
		cache: false, 
		dataType: "html",
		success:function(data){
			//alert(data);
			var serachkey=$("#coursekey").val();
			$("#pageBody").html(data);
			if(fdTitle!=""&&fdTitle!=null){
				$("#markshow").html('含“<a id="containkey"href="#"></a>”的条目');
				if(fdTitle.length>2){
					$("#containkey").html(fdTitle.substr(0,2)+"...");
					}else{
						$("#containkey").html(fdTitle);
					}
			}
			else{
				$("#containkey").html('<a id="containkey"href="#">全部条目</a>');
				
			}
			
			$("#serach").attr("value",serachkey);
			if($("#allFlag").val()=='true'){
				document.getElementById("selectAll").checked=true;
				selectAll();
			}
		}
	}); 
}
/**********************************************************************/
    $.Placeholder.init();
    $(function(){
        $("#formAddTeacher").validate({
            submitHandler: function(form){
            	 $.post("${ctx}/ajax/course/saveCourseParticipateAuth",{
                 	courseId:'${param.courseId}',//课程id
                 	teacher:$("#teacher").val(),//教师id
                 	mentor:$("#mentor").val()//导师id
               		  }).success(function(){
               			window.location.href="${ctx}/course/getSingleCourseAuthInfo?courseId="+$("#courseId").val()+"&fdType=13";
                     });
            }
        });
        /*老师列表模板函数*/
        var listTeacherFn = doT.template(document.getElementById("listTeacherTemplate").text);
        var result;
        $.ajax({
        	type: "post",
        	url: "${ctx}/ajax/course/getSingleCourseAuths",
        	data : {
        		"courseId" : '${param.courseId}',
        		"pageNo" : 1
        	},
        	async:false,
        	cache: false, 
        	dataType: "json",
        	success:function(data){
        		$("#leaningCount").html(data.totalCount);
        		//$("#leaningTeachers").html(leaningTeacherPageFn(data));
        		 loadListTeacherMentor(data.list);
        		 result=data.course;
        		
        		 
        	}
        }); 
        //课程封面
        if(result.coverUrl!=""){
        	$("#coursepage").attr('src','${ctx}/common/file/image/'+result.coverUrl);
        }else{
        	$("#coursepage").attr('src','${ctx}/resources/images/zht-main-img.jpg');
        }
        //课程评分
        for(var i=0;i<result.courseScore;i++){
        $("#cavg").find("i")[i].addClass("active");
        }
        //课程评分
        $("#courseavg").html(result.courseScore);
        //课程名称
        $("#courseName").html(result.courseName);
        //课程作者
        $("#courseAuthor").html(result.courseAuthor);
        //详细课程
        $("#coursedetail").attr('href',"${ctx}/course/pagefoward?courseId="+result.courseId);
        $("#courseId").val(result.courseId);
        /*加载老师列表函数*/
        function loadListTeacherMentor(arrObj){
            $("#listTeacher").html(listTeacherFn(arrObj));
        }
        /*******************************选择教师 导师**************************************/
        var listUserKinguserFn = doT.template(document.getElementById("listUserKinguserTemplate").text);
        $("#inputTeacher").autocomplete("${ctx}/ajax/user/findByName",{
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
        	$("#inputTeacher").val(item.name + '（' + item.mail + '），' + item.org + '  ' + item.department);
        	$("#teacher").val(item.id);
        	alert($("#teacher").val());
    	});
        /****************************************导师信息***************************/
        $("#inputMentor").autocomplete("${ctx}/ajax/user/findByName",{
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
        	$("#inputMentor").val(item.name + '（' + item.mail + '），' + item.org + '  ' + item.department);
        	$("#mentor").val(item.id);
        	alert($("#mentor").val());
    	});
    });
    //翻页
    function pageNavClick(pageNo){
    	 $.ajax({
         	type: "post",
         	url: "${ctx}/ajax/course/getSingleCourseAuths",
         	data : {
         		"courseId" : '${param.courseId}',
         		"pageNo" : pageNo
         	},
         	cache: false, 
         	dataType: "json",
         	success:function(data){	
         		alert(JSON.stringify(data));
         		$("#leaningCount").html(data.totalCount);
         		//$("#leaningTeachers").html(leaningTeacherPageFn(data));
         		 loadListTeacherMentor(data.list);
         		 
         	}
         }); 
    }
</script>
</body>
</html>
