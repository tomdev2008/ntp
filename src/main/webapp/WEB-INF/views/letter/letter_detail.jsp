<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
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
<link rel="stylesheet" type="text/css" href="${ctx}/resources/css/webim.css">
<!--[if lt IE 9]>
<script src="js/html5.js"></script>
<![endif]-->

    <!-- 聊天纪录列表 模板 -->
    <script id="listChatLogTemplate" type="text/x-dot-template">
        {{~it :item}}
        <div class="webim_chat_item">
            <div class="webim_chat_item_hd">
                <span class="webim_date">{{=item.date}}</span>
            </div>
            <div class="webim_chat_item_bd">
                {{~item.list :dia}}
                <div class="webim_dia_box {{?dia.isMe}}webim_dia_r{{??}}webim_dia_l{{?}}">
                    <img src="{{?dia.isMe}}http://img.staff.xdf.cn/Photo/06/3A/a911e1178bf3725acd75ddbb9c7e3a06_9494.jpg{{??}}./images/default-face-45.png{{?}}" class="dia_col webim_dia_face" alt=""/>
                    <div class="dia_col webim_dia_bg">
                        <div class="dia_caret"><b></b></div>
                        <div class="webim_dia_cont">
                            {{=dia.msg}}
                        </div>
                        <div class="va_bottm">
                            <span class="webim_date dia_col">{{=dia.time}}</span>
                            <a href="{{=dia.id}}" class="remove dia_col">删除</a>
                        </div>
                    </div>
                </div>
                {{~}}
            </div>
        </div>
        {{~}}
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
    <script src="${ctx}/resources/js/doT.min.js"></script>
</head>

<body>


<section class="container">
	<section class="clearfix mt20">
	  <section class="col-left pull-left">
    	<%@ include file="/WEB-INF/views/letter/menu.jsp" %>
	  </section>
		<section class="w790 pull-right" id="rightCont">
            <div class="page-header bder2" data-spy="affix" data-offset-top="20">
                <span class="muted">我正在看：</span>    我与 魏紫 的对话
                <div class="backHome">
                    <a href="${ctx}/letter/findLetterList"><span class="muted">返回</span>私信<span class="muted">首页</span> <i class="icon-home icon-white"></i> </a>
                </div>
            </div>
            <div class="page-body">
                <section class="section pd20 box-control">
                    <form class="formMsg" id="formMsg">
                        <div class="rowLabel">
                            <label for="chatMsg">
                                <span class="muted">给</span> 魏紫（weizi5@xdf.cn）    广州学校 国外考试部
                            </label>
                            <div class="btns">
                                <a href="mailto:weizi5@xdf.cn"><i class="icon-envelope"></i>给TA发邮件</a>
                                <span class="divider">|</span>
                                <a href="#" class="empty">清空所有对话</a>
                            </div>
                        </div>
                        <textarea name="chatMsg" id="chatMsg" required rows="3"></textarea>
                        <div class="rowLabel">
                            <span class="dt">TA的电话</span> <i class="icon-tel"></i><span class="muted">135 8165 1017</span>
                            <div class="btns">
                                <button class="btn btn-primary btn-large" type="submit">发送</button>
                            </div>
                        </div>
                    </form>
                    <div class="webim_chat_list" id="listtChatLog">
                    </div>
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
                </section>
            </div>
	    </section>
	</section>

</section>
<script type="text/javascript" src="${ctx}/resources/js/jquery.placeholder.1.3.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.jalert.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/messages_zh.js"></script>
<script type="text/javascript">
$(function(){
    $.Placeholder.init();
    //聊天纪录列表 模板函数
    var listChatLogFn = doT.template(document.getElementById("listChatLogTemplate").text);
    var listChatData = [
        {
            list: [
                {
                    id: "fdid3232323",
                    isMe: false,
                    msg: "每一个我身边在美国读硕、读博、工作的人看完《中国合伙人》都是这个感想，不知不觉我们都蜕变成了“自干五”每一个我身边在美国读硕、读博、工作的人看完《中国工作的人看完《中国合伙人》都是这个感想。",
                    time: "10:01 AM"
                },
                {
                    id: "fdid355434343",
                    isMe: true,
                    msg: "每一个我身边在美国读硕、读博、工作的人看完《中国合伙人》都是这个感想，不知不觉我们都蜕变成了“自干五”每一个我身边在美国读硕、读博、工作的人看完《中国工作的人看完《中国合伙人》都是这个感想。",
                    time: "10:01 AM"
                },
                {
                    id: "fdid3111110000",
                    isMe: true,
                    msg: "每一个我身边在美国读硕、读博、工作的人看完《中国合伙人》都是这个感想...",
                    time: "10:01 AM"
                },
                {
                    id: "fdid30003232344555555",
                    isMe: false,
                    msg: "每一个我身边在美国读硕、读博、工作的人看完《中国合伙人》都是这个感想，不知不觉我们都蜕变成了“自干五”每一个我身边在美国读硕、读博、工作的人看完《中国工作的人看完《中国合伙人》都是这个感想。",
                    time: "10:01 AM"
                }
            ],
            date: "2013/12/14"
        },
        {
            list: [
                {
                    id: "fdid3232323",
                    isMe: false,
                    msg: "每一个我身边在美国读硕、读博、工作的人看完《中国合伙人》都是这个感想，不知不觉我们都蜕变成了“自干五”每一个我身边在美国读硕、读博、工作的人看完《中国工作的人看完《中国合伙人》都是这个感想。",
                    time: "10:01 AM"
                },
                {
                    id: "fdid355434343",
                    isMe: true,
                    msg: "每一个我身边在美国读硕、读博、工作的人看完《中国合伙人》都是这个感想，不知不觉我们都蜕变成了“自干五”每一个我身边在美国读硕、读博、工作的人看完《中国工作的人看完《中国合伙人》都是这个感想。",
                    time: "10:01 AM"
                },
                {
                    id: "fdid3111110000",
                    isMe: true,
                    msg: "每一个我身边在美国读硕、读博、工作的人看完《中国合伙人》都是这个感想...",
                    time: "10:01 AM"
                },
                {
                    id: "fdid30003232344555555",
                    isMe: false,
                    msg: "每一个我身边在美国读硕、读博、工作的人看完《中国合伙人》都是这个感想，不知不觉我们都蜕变成了“自干五”每一个我身边在美国读硕、读博、工作的人看完《中国工作的人看完《中国合伙人》都是这个感想。",
                    time: "10:01 AM"
                }
            ],
            date: "2013/12/14"
        }
    ]

    $("#listtChatLog").html(listChatLogFn(listChatData))
            .find(".remove").click(function(e){//删除当前对话
                e.preventDefault();
                var $this = $(this);
                jalert("确定删除该消息吗？",function(){
                    jalert_tips("删除了ID为"+ $this.attr("href").substring(1)+"的消息");
                })
            });
});
</script>
</body>
</html>
