<%@ page language="java" contentType="text/html;charset=UTF-8"	pageEncoding="UTF-8"%><%@page import="org.apache.commons.lang3.ArrayUtils"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%><%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%><j:set name="ctx" value="${pageContext.request.contextPath}" /><%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%><!DOCTYPE html><html lang="zh_CN"><head><meta http-equiv="Content-Type" content="text/html; charset=utf-8" /><link rel="stylesheet" href="${ctx}/resources/css/global.css" /><link href="${ctx}/resources/css/DTotal.css" rel="stylesheet"	type="text/css">	<link href="${ctx}/resources/css/template_detail.css" rel="stylesheet" type="text/css"><script src="${ctx}/resources/js/jquery.jalert.js"	type="text/javascript"></script><!-- 学校信息 模板 --><script id="sectionDirectoryTemplate" type="text/x-dot-template">		<div class="sortableWrap">          <ul class="sortable" id="sortable">			    	{{  for(var i=1; i<(it.list.length)+1; i++){  }} 						{{~it.list :lec:order}}							{{?lec.order == i}}								<li class="lecture" data-fdid="{{=lec.id}}">									{{#def.sectionbar:lec}}								</li>							{{?}}						{{~}}					{{ } }}	      	</ul>	   </div></script><!--条模板--><script id="sectionBarTemplate" type="text/x-dot-template">	{{##def.sectionbar:param:		<div class="sortable-bar">			<span class="title"><i class="icon-doc"></i>				<span class="index">{{=param.order}}</span>				<span class="name">{{=param.name || ''}}</span>				<span class="name">{{=param.content || ''}}</span>			</span>			<a class="icon-pencil2 icon-white btn-ctrls" href="#"></a>			<a class="icon-remove icon-white btn-ctrls" href="#"></a>			<a href="#" class="btn-edit">编辑内容</a>			<div class="state-dragable">				<span class="icon-bar"></span>				<span class="icon-bar"></span>				<span class="icon-bar"></span>				<span class="icon-bar"></span>				<span class="icon-bar"></span>			</div>		</div>	#}}</script><script src="${ctx}/resources/js/doT.min.js"></script></head><body>	<div class="w790 pull-right" id="rightCont">		<div class="page-body">				<c:if test="${ptype eq '02' }">				<%@ include file="/WEB-INF/views/admin/page/divschoollist.jsp"%>			</c:if>			<c:if test="${ptype eq '01' }">				<%@ include file="/WEB-INF/views/admin/page/divteacherlist.jsp"%>			</c:if> 			</div>	</div>	<script type="text/javascript"		src="${ctx}/resources/js/jquery.validate.min.js"></script>	<script type="text/javascript" src="${ctx}/resources/js/messages_zh.js"></script>	<script type="text/javascript"		src="${ctx}/resources/js/jquery.sortable.js"></script>	<script type="text/javascript"		src="${ctx}/resources/uploadify/jquery.uploadify.js?id=1211"></script>	<script type="text/javascript"		src="${ctx}/resources/js/jquery.autocomplete.pack.js"></script>	<script type="text/javascript">		$(function() {			ptype = "${ptype}";			if (ptype == '02') {				// 展示章节模板函数				var sectionsFn = doT.template(document						.getElementById("sectionBarTemplate").text);				// load目录模板函数				var loadsectionsDirectoryFn = doT.template(document						.getElementById("sectionDirectoryTemplate").text						+ document.getElementById("sectionBarTemplate").text);				//加载学校信息				result = getPages(ptype);				//alert(JSON.stringify(result));				$("#listdata").html(loadsectionsDirectoryFn(result));				var $sections = $('#sortable');				$sections.sortable({							handle : '.sortable-bar',							forcePlaceholderSize : true				})		.bind("sortupdate", changIndex)		// 绑定拖放元素改变次序事件		// 绑定编辑标题按钮事件		.delegate(				".sortable-bar>.icon-pencil2",				"click",				function(e) {					e.preventDefault();					var $tit = $(this).prev(".title");					var data = rtnSectionData($tit.closest("li").hasClass("chapter"),$tit.children(".index").text(),$tit.children(".name").text());					$(this).closest(".sortable-bar").addClass("hide").after(editTitleFn(data));				})		// 绑定删除章节按钮事件		.delegate(".sortable-bar>.icon-remove","click",				function(e) {					e.preventDefault();					var $li = $(this).closest("li");					$.post($('#ctx').val()+ '/ajax/catalog/deleteCatalogById',							{fdid : $li.attr("data-fdid")							}).success(function() {								$li.remove();								changIndex();							});				})		// 绑定编辑节内容按钮事件		.delegate(".sortable-bar>.btn-edit","click",			function(e) {				e.preventDefault();				if ($(this).parent().next(".lecture-content").length) {					$(this).addClass("hide").parent().next(".lecture-content").removeClass("hide");				}			})		// 绑定节内容关闭按钮事件		.delegate(".lecture-content>.hd>a.icon-remove-sign","click",			function(e) {				e.preventDefault();				$(this).closest(".lecture-content").addClass("hide").prevAll(".sortable-bar").children(".btn-edit").removeClass("hide");			})		// 绑定保存章节标题按钮事件		.delegate(				".form-edit-title .btn-primary",				"click",				function(e) {					e.preventDefault();					var $form = $(this).closest(".form-edit-title");					var $tit = $form.find("input.input-block-level");					var $li = $form.parent("li");					var data, fdId;					if ($tit.val()) {						if ($form.prev().hasClass("sortable-bar")) {// 编辑已有章节							$.post($('#ctx').val()+ '/ajax/catalog/updateCatalogNameById',									{fdid : $li.attr("data-fdid"),									title : $tit.val()									}, 									function(data) {									}, "json"							).success(function() {								$li.children(".sortable-bar").removeClass("hide").find(".name").text($tit.val());							});						} else {							$.ajax({								url : $('#ctx').val()+ "/ajax/catalog/addCatalog",								async : false,								data : {									courseid : $("#courseId").val(),									ischapter : $li.hasClass("chapter"),									fdtotalno : parseInt($sections.children("li").length) - 1,									fdno : $form.find(".index").text(),									title : $tit.val()								},								dataType : 'json',								success : function(result) {									var data = rtnSectionData($li.hasClass("chapter"),$form.find(".index").text(),$tit.val(),$li.length,"none",result.id);									$("#courseId").val(result.courseid);									$li.attr("data-fdid",result.id);									$form.before(sectionsFn(data));									$sections.sortable({										handle : '.sortable-bar',										forcePlaceholderSize : true									});								},							});						}						$form.remove();					} else {						$form.find(".control-group:first").addClass("warning").find(":text").after('<span class="help-block">请填写标题！</span>');					}				})				// 更新章节顺序方法				function changIndex() {					var $items = $sections.children("li"), i_ch = 1, i_le = 1, 					data = {						lecture : []					}, $item;					$items.each(function(i) {						$item = $(this);						$item.find(".title>.index").text(i_le);						data.lecture.push({							index : i,							num : i_le++,							id : $item.attr("data-fdid")						});					});					$.post(							$('#ctx').val()									+ "/ajax/page/updatePageOrder", {								lecture : JSON.stringify(data.lecture)							}, function(res) {							}, 'json');// ajax					// 更新所有章节排序				}			}		})		function getPages(ptype) {			var result;			$.ajax({				url : "${ctx}/ajax/page/getPages?ptype=" + ptype,				async : false,				dataType : 'json',				success : function(data) {					result = data;				}			});			return result;		}	</script></body></html>