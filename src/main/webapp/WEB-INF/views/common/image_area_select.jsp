<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>图片剪切</title>
<link href="${ctx }/resources/selectarea/imgareaselect.css" rel="stylesheet" type="text/css" />
<script src="${ctx }/resources/selectarea/jquery.js" type="text/javascript"></script>
<script src="${ctx }/resources/selectarea/jquery.imgareaselect.js" type="text/javascript"></script>
</head>
<body>
<div style="">
	<div  class="big-imgDiv">
		<img id="ferret" src="${imgSrcPath}" title="image select" alt="loading IMG ......" />
	</div>
	<div class="cutRight-box" >
		<div id="imgDiv" class="min-imgDiv" >
			<img id="minImg" src="${imgSrcPath}"/>
		</div>
		<div class="imgArea-box">
		<form  action="" target="hiddenIframe" method="post" id="cutImgForm" >
			<input type="hidden" id="imgSrcPath"  name="imgSrcPath" value="${imgSrcPath}"/>
			<input type="hidden" id="cutImageW"  name="imgWidth"/>
            <input type="hidden" id="imgId" name="imgId" value="${imgId}"/>
			<input type="hidden" id="cutImageH"  name="imgHeight"/>
			<input type="hidden" id="cutImageX"  name="imgTop"/>
			<input type="hidden" id="cutImageY"  name="imgLeft"/>
			<input type="hidden" id="imgScale"  name="imgScale" value="1"/>
			<input type="hidden" name="reMinWidth" id="reMinWidth" value="${zoomWidth}" size="5"/>
			<input type="hidden" name="reMinHeight" id="reMinHeight" value="${zoomHeight}" size="5" />
			<div class="imgArea-bigSize" >
				原图 &nbsp; &nbsp;宽: <span id="oimgW"></span> &nbsp; &nbsp;
				 高: <span id="oimgH"></span>
			 </div>
			<div class="imgArea-minSize">
			缩略图 宽: <span id="thumbWidth"></span>
			高：<span id="thumbHeight"></span>
			</div>
			<div class="imgArea-control" >
			<button type="button" class="btn"  onclick="submitCut()">确认裁剪</button>
			</div>
		</form>
		</div>
	</div>
</div>
<script type="text/javascript">
	var container=400;
	var imageW = 100;
	var imageH = 100;
	var minWidth = ${zoomWidth};
	var minHeight = ${zoomHeight};
	var scale = 1;
	var uploadImgStatus = 0;
	function submitCut(){
		var imgSrcPath = $("#imgSrcPath").val();
		if(uploadImgStatus==0){
			$.ajax({
				url : "${ctx }/common/imageCut/imageCut",
				type : "POST",
				dataType : "json",
				data : {
					imgSrcPath : imgSrcPath,
					imgWidth: $("#cutImageW").val(),
					imgHeight : $("#cutImageH").val(),
					imgTop : $("#cutImageX").val(),
					imgLeft : $("#cutImageY").val(),
					imgScale : $("#imgScale").val(),
					reMinWidth:$("#reMinWidth").val(),
					reMinHeight:$("#reMinHeight").val() ,
                    imgId:$("#imgId").val()
				},
				success : function(result) {
					if(result){
						uploadImgStatus++;
						window.parent.successSelectArea(imgSrcPath);
					}
				}
			});	
		}else{
			window.parent.successSelectArea(imgSrcPath);
		}
	}
	
	function preview(img, selection){
		showCut(selection.width,selection.height,selection.x1,selection.y1);
	}
	function showCut(w,h,x,y){   
		var scaleX = minWidth / w;
		var scaleY = minHeight / h;
		$('#minImg').css({ width: Math.round(scaleX * imageW * scale) + 'px', height: Math.round(scaleY * imageH * scale) + 'px', marginLeft: '-' + Math.round(scaleX * x ) + 'px', marginTop: '-' + Math.round(scaleY * y) + 'px' });
		$('input#cutImageW').val(w);
		$('input#cutImageH').val(h);
		$('input#cutImageX').val(x);
		$('input#cutImageY').val(y);
	}
	$(window).load(function () {
		imageW = $('#ferret').width();
		imageH = $('#ferret').height();
		$('#oimgW').html(imageW);
		$('#oimgH').html(imageH);
		$('#thumbWidth').html(minWidth);
		$('#thumbHeight').html(minHeight);
		var imgMax = imageW>imageH?imageW:imageH;
		if(imgMax>container) {
			scale = container/imgMax;
			$('#imgScale').val(scale);
			$('#ferret').css({width:Math.round(scale*imageW)+'px',height:Math.round(scale*imageH)+'px'});
		}
		//默认尺寸
		if(imageW<minWidth||imageH<minHeight) {
			alert("源图尺寸小于缩略图，请重设缩略图大小。");
			return;
		}
		if(imageW==minWidth&&imageH==minHeight) {
			alert("源图和缩略图尺寸一致，请重设缩略图大小。");
			return;
		}
		$('#imgDiv').css({'width':minWidth+'px','height':minHeight+'px'});
		var minSelW = Math.round(minWidth*scale);
		var minSelH = Math.round(minHeight*scale);
		$('img#ferret').imgAreaSelect({selectionOpacity:0,outerOpacity:'0.5',selectionColor:'blue',onSelectChange:preview,minWidth:minSelW,minHeight:minSelH,aspectRatio:minWidth+":"+minHeight,x1:0,y1:0,x2:minWidth,y2:minHeight});
		showCut(minWidth,minHeight,0,0);
	});
</script>
</body>
</html>