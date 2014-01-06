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
<link rel="stylesheet" type="text/css" href="css/jquery.autocomplete.css">
<!--[if lt IE 9]>
<script src="js/html5.js"></script>
<![endif]-->
</head>

<body>


<section class="container">
	<section class="clearfix mt20">
	  <section class="col-left pull-left">
    	<%@ include file="/WEB-INF/views/letter/menu.jsp" %>
	  </section>
		<section class="w790 pull-right" id="rightCont">
	        <div class="page-header">
                <a href="私信-我的私信.html" class="backParent">返回</a>
                <h4>发邮件</h4>
	        </div>
            <div class="page-body editingBody">
                <form action="#" id="formEditDTotal" class="form-horizontal" method="post">
                    <section class="section">
                        <div class="control-group">
                            <label class="control-label" for="addUser">发  给</label>
                            <div class="controls">
                                <input value="" id="addUser" required class="span6"
                                       name="addUser" type="text"><span class="date">2013/02/14 10:01 AM</span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="videoIntro">内  容</label>
                            <div class="controls"><textarea required placeholder="不要超过200字。" rows="4"
                                                            class="input-block-level" id="videoIntro"
                                                            name="videoIntro"></textarea>
                            </div>
                        </div>
                    </section>
                    <button class="btn btn-block btn-submit btn-inverse" type="submit">发送</button>
                </form>
            </div>
	    </section>
	</section>


</section>
<script type="text/javascript" src="${ctx}/resources/js/jquery.placeholder.1.3.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/messages_zh.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.autocomplete.pack.js"></script>
<script type="text/javascript">
$(function(){
    $.Placeholder.init();


    $("#formEditDTotal").validate({
        submitHandler: function(form){
            //
        }
    });

    var allUserData ;

    $("#addUser").autocomplete(allUserData,{
        formatMatch: function(item) {
            return item.name + item.mail + item.org + item.department;
        },
        formatResult: function(item) {
            return item.name + "(" + item.mail + ")";
        },
        formatItem: function(item) {
            return '<img src="'
                    + (item.imgUrl || 'images/temp-face36.jpg') + '" alt="">'
                    + item.name + '（' + item.mail + '），'
                    + item.org + '  ' + item.department;
        },
        matchContains:true ,
        max: 10,
        scroll: false,
        width:500
    }).result(function(e,item){
                //item.id  可在此存入隐藏域
            });
});
</script>
</body>
</html>
