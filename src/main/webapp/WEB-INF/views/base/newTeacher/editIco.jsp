<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html>
<head>
<link rel="stylesheet" href="${ctx}/resources/css/global.css" />
<link href="${ctx}/resources/css/settings.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${ctx}/resources/uploadify/uploadify.css"/> 
<style>
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
	max-height:20px;
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
</style>
<script src="${ctx}/resources/js/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/resources/uploadify/jquery.uploadify-3.1.min.js?id=12"></script>


</head>

<body>

       <div class="page-body"> 
         <p class="reg_form-intro">请确认您要修改的图像。</p>
          <form method="post" id="inputForm" action="${ctx}/register/updateTeacherPoto" class="reg_form form-horizontal">
          <input type="hidden" id="fdIcoUrl" name="fdIcoUrl" value="${fdIcoUrl}" />
        	<div>
        		<div>
        		   <label for="face" class="control-label">头像</label>
        		    <div>
        		    
        		  <table>
        		  <tr>
        		   <td>
                       <div id="potoImg">
                	 <a href="#" class="face pull-left">
                         <tags:image href="${fdIcoUrl}" id="imgshow" clas="media-object img-face" />
                        <h6>
                        </h6>
                             <div style="position: relative;top:-20px;">
                                 <tags:simpleupload filename="fdName"
                                                    filevalue="" id="upPic" exts="*.jpg;*.JPEG;*.png;*.bmp;" callBack="callBackMethod" imgshow="imgshow" attIdName="attId" attIdID="attIdID">
                                 </tags:simpleupload>
                             </div>
                       </div>
                     
                     </a>
                       <!--图片剪切-->
                       <div class="cutimg-box no" style="display:none;">
                           <iframe id="iframeimg" width="600" height="500" id="win" name="win" frameborder="0" scrolling="no"
                                   src=""></iframe>
                       </div>
                    <div class="pull-left support-img">支持JPG\JPEG、PNG、BMP格式的图片<br />建议文件小于2M</div>
                      </td>
                </tr>
                <tr>
                <td>

                </td>
                </tr>
               </table>
               
                   </div>
                </div>
        	</div> 
        	<div>
            	<div>
                	<button type="submit" class="submit btn btn-primary btn-large" >确认修改</button>
                </div>
            </div>
        </form>
        </div>
<script type="text/javascript">

    function callBackMethod(attId,fileName){
        $("#potoImg").hide();
        $("#upPic").next(".uploadify-queue").remove();
        $(".cutimg-box").show();
        var preImg = '${ctx}/common/file/image/' + attId;
        var imgSrc = escape(preImg);
        $("#iframeimg").attr("src","${ctx}/common/imageCut/page?imgSrcPath="+imgSrc+"&zoomWidth=180&zoomHeight=180&imgId="+attId);
    }

    //图片剪切成功
    function successSelectArea(imgSrc){
        var now=new Date();
        var number = now.getSeconds();
        jQuery("#imgshow").attr('src',  imgSrc+"?n="+number);
        $(".cutimg-box").hide();
        //imgshow
        $("#potoImg").show();
    }
</script>
</body>
</html>
