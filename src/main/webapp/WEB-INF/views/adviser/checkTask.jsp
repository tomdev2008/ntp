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
                <div class="pull-left">
                    <a href="{{!item.user.link || '#'}}" class="face" target="_blank">
                        <img src="{{!item.user.imgUrl || './images/face-placeholder.png'}}" class="media-object img-polaroid" alt="头像"/>
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
                            <a href="{{!item.downloadBoxUrl || '#'}}" class="downloadBox"><i class="icon-downloadbox"></i>打包下载</a>
                        </div>
                    {{??}}
                        <div class="statebar">作业包总分 <strong>{{=item.scoreTotal}}</strong>  分<em>|</em>及格分 <strong>{{=item.scorePass}}</strong>  分<em>|</em>当前批改的总得分 <strong>{{=item.score}}</strong>  分</div>
                        <span class="isPass{{?item.isPass}} pass{{?}}">{{?item.isPass}}通过{{??}}未通过{{?}}</span>
                    {{?}}
                </div>
            </li>
            {{~}}
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
                                        <li><a href="#rightCont">打包下载</a></li>
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
                                    <button class="btn btn-large active" type="button">课程</button>
                                    <button class="btn btn-large" type="button">新教师</button>
                                    <button class="btn btn-large" type="button">导师</button>
                                    <button class="btn btn-large" type="button">时间</button>
                                </div>
                                <label class="radio inline" for="selectCurrPage"><input type="radio" id="selectCurrPage" name="selectCheckbox" checked />选中本页</label>
                                <label class="radio inline" for="selectAll"><input type="radio" id="selectAll" name="selectCheckbox" />选中全部</label>
                                <div class="pages pull-right">
                                    <div class="span2">
                                                                                                  第<span> ${page.startNum} - ${page.endNum}</span> / <span>${page.totalCount}</span> 条
                                    </div>
                                    <div class="btn-group">
                                        <button class="btn btn-primary btn-ctrl" type="button" disabled><i class="icon-chevron-left icon-white"></i></button>
                                        <button class="btn btn-primary btn-ctrl" type="button"><i class="icon-chevron-right icon-white"></i></button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                    <section class="listWrap">
                        <ul class="listTeacher media-list" id="listTeacher">

                        </ul>
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

<!--底部 S-->
	
<!--底部 E-->
</section>
<!--主体 E-->
<script type="text/javascript">
    $(function(){
        /*老师列表模板函数*/
        var listTeacherFn = doT.template(document.getElementById("listTeacherTemplate").text);
        var unCheckedData = {},checkedData= {} ;

        $("#navTabs>li>a").on("click",function(e){
            e.preventDefault();
            $(this).parent().addClass("active").siblings().removeClass("active");
            loadList($(this).attr("href").slice(1));
        });

        initlistTeacher();
        
        function initlistTeacher(){
        	$.ajax({
          	  type: "POST",
        		  url: "${ctx}/ajax/adviser/findCheckTaskList",
        		  async:false,
        		  dataType : 'json',
        		  success: function(result){
        			  alert(JSON.stringify(result));
        			  checkedData.list = result;
        		  }
        	});
        }
        loadList("unchecked");//默认加载未批改的
        /*加载列表*/
        function loadList(type){
            switch (type){
                case "checked":
                    checkedData.list = [//ajax
                        {
                            id: "fdid903243324",
                            user: {
                                name: "杨义锋",
                                imgUrl: "${ctx}/resources/images/temp-face70.jpg",
                                org: "集团总公司",
                                department: "知识管理中心",
                                phone: "135 8165 1017",
                                mail: "username@xdf.cn ",
                                link: "#profile"
                            },
                            courseName: "集团英联邦项目雅思强化口语备课课程 ",
                            mentor: "覃晔",
                            currLecture: "第二关 学术准备 2.2 提交学术作业",
                            scoreTotal: 50,
                            scorePass:40,
                            score: 45,
                            isPass: true
                        },
                    ];
                    checkedData.status = "checked";
                    $("#listTeacher").html(listTeacherFn(checkedData));
                    break;
                case "unchecked":
                default :
                	
                checkedData.status = "unchecked";
                $("#listTeacher").html(listTeacherFn(checkedData));

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

    });
    

</script>
</body>
</html>
