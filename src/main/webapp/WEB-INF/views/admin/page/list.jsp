<%@ page language="java" contentType="text/html;charset=UTF-8"	pageEncoding="UTF-8"%><%@page import="org.apache.commons.lang3.ArrayUtils"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%><%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%><j:set name="ctx" value="${pageContext.request.contextPath}" /><%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%><!DOCTYPE html><html lang="zh_CN"><head><script src="${ctx}/resources/js/jquery.jalert.js"	type="text/javascript"></script><!-- 学校信息 模板 --><script id="sectionDirectoryTemplate" type="text/x-dot-template"> <ul class="nav list" id="sortable"> 		{{~it.list:lec:index}}			{{?lec.order-1==index}}			<li data-fdid="{{=lec.id}}">            	<a href="${ctx}/admin/page/edit?ptype={{=lec.type}}&pid={{=lec.id}}"  title="编辑"> 			   		<input type="checkbox" name="ids" value="{{=lec.id}}" />						<span class="index">{{=lec.order}}</span>. 						<span class="title">{{?lec.content.length<20}}{{=lec.content}}{{??}}{{=lec.content.substr(0,20)+"..."}}{{?}}</span>						<span class="date">							<i class="icon-time"></i>{{=lec.createtime||''}}							<span class="dt">发布者</span> <em>{{=lec.name || ''}}</em>						</span>					<div class="state-dragable"><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></div>         		</a>			</li>			{{?}}		{{~}} </ul></script><script src="${ctx}/resources/js/doT.min.js"></script></head><body>		<div class="page-body">			<section class="section box-control">			<ul class="nav nav-tabs" id="navTabs">						<li id="school"><a href="${ctx}/admin/page/list?ptype=02">学习联盟</a></li>						<li id="teacher"><a href="${ctx}/admin/page/list?ptype=01">平台寄语</a></li>					</ul>				<div class="hd">					 <div class="btn-toolbar">						<a class="btn btn-primary" href="${ctx }/admin/page/add?ptype=${ptype }">添加</a>						<div class="btn-group">							<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">								操作 <span class="caret"></span>							</a>							<ul class="dropdown-menu">								<li><a href="#" onclick="confirmDelePage()">批量删除</a></li>							</ul>						</div>						<!-- <form class="toolbar-search" >							<input type="text" id="search" class="search"  onkeydown="showSearch();" onkeyup="showSearch();">							<i class="icon-search"></i>						</form>						<span class="showState"> <span class="muted">当前显示：</span> 						<span id="markshow"> <a id="containkey" href="#">全部条目</a>						</span>						</span>						 <a class="btn btn-link" href="javascript:void(0);" onclick="clearserach();">清空搜索结果</a> -->					</div>				</div>				<div class="bd">					<div class="btn-toolbar">						<label class="checkbox" for="selectAll"><input type="checkbox" id="selectAll" name="selectCheckbox" onclick="selectAll()" value="1">选中全部</label>					</div>				</div>			</section>			<section class="section listWrap hide" id="listdata">			</section>		</div>	<script type="text/javascript"		src="${ctx}/resources/js/jquery.sortable.js"></script>	<script type="text/javascript">		$(function() {			ptype = "${ptype}";			if(ptype=='01'){				$("#teacher").addClass("active");			}else{				$("#school").addClass("active");			}				// load目录模板函数				var loadsectionsDirectoryFn = doT.template(document						.getElementById("sectionDirectoryTemplate").text);				//加载信息				$.ajax({       				url : "${ctx}/ajax/page/getPages",       				data : {       					ptype:ptype,       					fdkey:$("#search").val()       				},       				async : false,       				dataType : 'json',       				success : function(data) {       					result = data;       					if(result.fdkey!=""){       						$("#search").val(result.fdkey);//查询关键字       					}       					//alert(JSON.stringify(result));       					if(result.list.length>0){       						$("#listdata").removeClass("hide");       					}       					$("#listdata").html(loadsectionsDirectoryFn(result));       				}       			});				var $sections = $('#sortable');				$sections.sortable({					 		handle: '.state-dragable',							forcePlaceholderSize : true						})				.bind("sortupdate", changIndex);				// 绑定拖放元素改变次序事件				// 更新章节顺序方法				function changIndex() {					var $items = $sections.children("li"),i_le = 1, 						data = {							lecture : []						}, $item;					$items.each(function() {						$item = $(this);						$item.find(".index").text(i_le);						data.lecture.push({							order:i_le++,							id : $item.attr("data-fdid")						});					});					$.post("${ctx}/ajax/page/updatePageOrder", {						lecture : JSON.stringify(data.lecture)					}, function(res) {					}, 'json');// ajax					// 更新所有章节排序				};				                  $("#search").keypress( function(e) {  //回车事件                 	if(event.keyCode==13){        				var fdkey=$("#search").val();        				result =getPages("${ptype}");        				$("#listdata").html(loadsectionsDirectoryFn(result));        				return false;        			}             });                 //清空                $(".btn-link").click(function(){                	$("#search").attr("value","");        			$("#markshow").html('<a id="containkey"href="#">全部条目</a>');        			result=getPages("${ptype}");        			$("#listdata").html(loadsectionsDirectoryFn(result));                })               /*  $("")                 */        		});				//------------------------------查询----------------------		        function getPages(ptype) {        			//关键字        			var fdkey=$("#search").val();        			var result;        			$.ajax({        				url : "${ctx}/ajax/page/getPages",        				data : {        					ptype:ptype,        					fdkey:fdkey        				},        				async : false,        				dataType : 'json',        				success : function(data) {        					result = data;        					if(result.fdkey!=""){        						$("#search").val(result.fdkey);//查询关键字        					}        					        				}        			});        			return result;        		}  		/* ------------------------------------------------删除-- */		function confirmDelePage(){			var delekey="";			$('input[name="ids"]:checked').each(function() {				delekey+=$(this).val()+",";			}); 				if(delekey==""){				jalert("当前没有选择要删除的数据!");				return;			}			if($('input[name="selectCheckbox"]:checked').val()==1){//删除所有				jalert("您确认要删除所有信息？",deleteAll);			}else{				jalert("您确认要删除所选信息？",deleteOne);			}		}		function deleteOne(){			var delekey="";			$('input[name="ids"]:checked').each(function() {				delekey+=$(this).val()+",";			}); 				 $.ajax({				type: "post",				async:false,				 url: "${ctx}/ajax/page/deleteOnePage",				data : {					pId:delekey,					ptype:"${ptype}"				},				success:function(data){					window.location.href="${ctx}/admin/page/list?ptype=${ptype}";				}			}); 		}		function deleteAll(){			var delekey = $("#search").val();			 $.ajax({				type: "post",				async:false,				url: "${ctx}/ajax/page/deleAllPages",				data : {					fdkey:delekey,					ptype:"${ptype}"									},				success:function(data){					window.location.href="${ctx}/admin/page/list?ptype=${ptype}";				}			}); 		}		//---------------------搜索----------------------------------------	/* 	function pressEnter(){//回车事件 回车根据关键字查询;			if(event.keyCode==13){				var fdkey=$("#search").val();				alert(fdkey);				getPages("${ptype}");				return false;			}		} */		/* function clearserach222222(){//清理搜索栏并显示数据列表			alert('ss');			$("#search").attr("value","");			$("#markshow").html('<a id="containkey"href="#">全部条目</a>');			getPages("${ptype}");		} */		function showSearch(){//搜索内容操作			var fdkey =  $("#search").val();			$("#markshow").html('含“<a id="containkey"href="#"></a>”的条目');			if(fdkey==''){				$("#markshow").html('<a id="containkey" href="#">全部条目</a>');			}else if(fdkey.length>1){				$("#containkey").html(fdkey.substr(0,1)+".");			}else{				$("#containkey").html(fdkey);			}		}		//全部选中		function selectAll(){			if(document.getElementById("selectAll").checked){				$('input[name="ids"]').each(function(){					$(this).attr("checked",true);				});			} else {				$('input[name="ids"]').each(function(){					$(this).attr("checked",false);// 				});			}		}	</script></body></html>