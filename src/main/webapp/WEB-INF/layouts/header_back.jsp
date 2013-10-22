<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="j" uri="/WEB-INF/tags/formtag.tld"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
<div class="navbar navbar-inverse navbar-fixed-top">
  <div class="navbar-inner">
    <div class="container pr">
      <div class="logo">
        <a href="#">XDF</a>
      </div>
      <div class="span4 top-nav">
        <ul class="nav">
          <li><a href="${ctx}/trainee/welcome">首页</a></li>
          <li><a>频道</a></li>
          <li><a>广场</a></li>
          <li><a>应用</a></li>
        </ul>
      </div>
      
      <div class="message">
        <span class="span_01">
          <a href="${ctx}/dashboard" style="color:#fefefe;"><shiro:principal/></a>&nbsp;老师，欢迎回来！
        </span>
        <span class="span_02">
          <a href="${ctx}/dashboard" rel="tooltip" class="icon-th icon-white" data-original-title="我的桌面" data-placement="bottom"></a>
          <a id="notify" href="${ctx}/notify/list/1/ALL" rel="tooltip" class="icon-envelope icon-white" data-original-title="查看消息" data-placement="bottom"></a>
          <a href="${ctx}/logout" rel="tooltip" class="icon-off icon-white" data-original-title="退出" data-placement="bottom"></a>
        </span>
      </div>
      <form class="search">
        <input type="text" name="search" class="inp_01" placeholder="搜索知识、找人" />
        <input type="button" name="go" class="inp_02" />
      </form>
      <div id="notify_box" class="hide"></div>
    </div>
  </div>
</div>
<script type="text/javascript">
function getUnreadNotifyCount(){
 $.ajax({
    type: "post",
    dataType: "json",
    url: "${ctx}/ajax/notify/notifyCount",
    data: {},
    success: function(data){
    	if(data>0){
    		$("#notify_box").html('<a href="#" class="close">×</a>'+data+'条未读消息，<a href="${ctx}/notify/list/1/ALL" >查看信息</a>')
        	.show().find(".close").click(function(){
        		$(this).parent().remove();
        	});
        	$("#notify").append('<i class="icon-new"></i>');
    	}else{
    		$("#notify .icon-new").remove();
    	}
    }
 }); 
}
getUnreadNotifyCount();
</script>