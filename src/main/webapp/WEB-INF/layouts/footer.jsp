<%@ page language="java" pageEncoding="UTF-8"%><%@ taglib prefix="j" uri="/WEB-INF/tags/formtag.tld"%><div class="container">	<div class="footer">		<div class="navbar clearfix">			<div class="nav">				<li><a href="http://www.xdf.cn/" target="_blank">新东方网</a></li>				<li><a href="http://me.xdf.cn/" target="_blank">知识管理平台</a></li>				<li><a href="${ctx }/login">登录</a></li>				<li><a href="${ctx }/self_register">注册</a></li>				<li class="last"><a href="mailto:yangyifeng@xdf.cn">帮助</a></li>			</div>            <p>Copyright (C) 2013 New Oriental Education and Technology Group</p>		</div>			</div></div><%/**<j:if test="${!empty vms }"><!--聊天窗口 start--><div id="WB_webim" class="WB_webim"	style="right: 0px; bottom: 0px; position: fixed;">	<div class="wbim_chat_box wbim_chat_box_s"		style="position: absolute; right: 20px; bottom: 0px; z-index: 9500; display: none;">		<div class="wbim_chat_con">			<div class="wbim_tit2">				<div class="wbim_titin">					<div class="wbim_tit2_lf">						<p class="tit2_lf_con" style="">							<a target="_blank" class="webim_list_head webim_head_50"								href="##"><span class="head_pic"><img alt=""									src="${member.guid.poto}"></span></a><span								class="chat_name"></span>						</p>					</div>					<div class="wbim_tit2_rt">						<a href="javascript:void(0);" class="rt_icon WBIM_icon_minY"							title="最小化"></a><a href="javascript:void(0);"							class="rt_icon WBIM_icon_closeY" title="关闭"></a>					</div>				</div>			</div>			<div class="wbim_chat_wrap">				<div class="wbim_chat_rt" style="position: relative;">					<div class="wbim_chat_up" style="height: 249px; overflow: hidden;">						<div class="wbim_chat_list">							<div class="webim_chat_dialogue"								style="overflow: auto; position: relative; height: 249px;">								<div style="top: 0px; z-index: 1000;" class="wbim_chat_scroll">									<em class="scroll_top"></em><em style="height: 0;"										class="scroll_main"></em><em class="scroll_bottom"></em>								</div>								<div class="webim_dia_list"									style="overflow: hidden; padding: 5px; display: block; visibility: visible;">										<j:iter items="${vms }" var="vm">										<j:ifelse test="${vm.todoType==1 }">											<j:then>											<div class="webim_dia_box webim_dia_r " node-type="msgRoot">												<div class="dia_info">													<span class="info_date">${vm.msgTime }</span>												</div>												<div class="webim_dia_bg">													<div class="dia_con" node-type="conRoot">														<p class="dia_txt">${vm.msg }</p>													</div>													<div class="msg_arr"></div>												</div>											</div>											</j:then>											<j:else>											<div class="webim_dia_box webim_dia_l " node-type="msgRoot">												<div class="dia_info">													<span class="info_date">${vm.msgTime }</span>												</div>												<div class="webim_dia_bg">													<div class="dia_con" node-type="conRoot">														<p class="dia_txt">${vm.msg }</p>													</div>													<div class="msg_arr"></div>												</div>											</div>											</j:else>										</j:ifelse>									</j:iter>								</div>							</div>						</div>					</div>					<div class="webim_chat_sendbox">						<div class="sendbox_bar clearfix" node-type="wbim_chat_toolbar"							style="position: relative; z-index: 109;">							<div class="sendbox_ac"></div>							<div class="sendbox_show ">								<span class="sendbox_oth"><a href="##" target="_blank"									class="sendbox_his" title="查看私信记录"><em										class="WBIM_icon_com WBIM_iconsend_his"></em>聊天记录</a></span>							</div>						</div>						<div class="sendbox_box clearfix">							<textarea style="overflow-y: auto; height: 58px;"								class="sendbox_area"></textarea>						</div>						<div class="sendbox_btn clearfix" node-type="_tipRoot"							style="position: relative">							<div class="sendbox_btn_l"></div>							<div class="sendbox_btn_r">								<div class="">									<a href="javascript:;" class="btn btn-warning btn-small btn-send">发送</a>								</div>							</div>						</div>					</div>				</div>			</div>		</div>	</div>	<div class="webim_win_minD wbim_min_chat wbim_min_chat_msg"		style="cursor: pointer; position: absolute; bottom: 0px; right: 20px; display: block; visibility: visible;">你有		${todoCount } 条新私信</div></div><iframe id="chatFrame" name="chatFrame" style="border: 0;height: 0px;width: 0px;" src="${ctx }/ajax/chat.jsp?receiver=${member.guid.fdId}&userId=${member.newteachId}"></iframe><script type="text/javascript">function showTime(){                                    var today = new Date();    var y=today.getFullYear()+"-";    var month=today.getMonth()+"-";    var td=today.getDate()+"-";    var h=today.getHours();    var m=today.getMinutes();       return y+month+td+' '+h+':'+m;}    	$(function() {		var $wbim = $("#WB_webim");		var $chat = $wbim.children(".wbim_chat_box");		var $minChat = $wbim.children(".wbim_min_chat");		var $sendbox = $chat.find(".sendbox_area");		var $dia_list = $chat.find(".webim_dia_list");		$minChat.bind("click", function() {			$minChat.hide();			$(window.parent.document).contents().find("#chatFrame")[0].contentWindow.register();			$chat.show();		});		$chat.find(".WBIM_icon_minY").bind("click", function() {			$chat.hide();			var chatName = $chat.find(".chat_name > a").text();			$minChat.removeClass('wbim_min_chat_msg').text(chatName).show();		});		$chat.find(".WBIM_icon_closeY").bind("click", function() {			$chat.hide();		});		$chat.find(".btn-send").bind("click", function() {			if($sendbox.val() != ""){				//$("#chatFrame").sendMsg($sendbox.val());				 $(window.parent.document).contents().find("#chatFrame")[0].contentWindow.send($sendbox.val());				$dia_list.append('<div class="webim_dia_box webim_dia_r " node-type="msgRoot">\						<div class="dia_info">\						<span class="info_date">'+showTime()+'</span>\					</div>\					<div class="webim_dia_bg">\						<div class="dia_con" node-type="conRoot">\							<p class="dia_txt">'+ $sendbox.val() +'</p>\						</div>\						<div class="msg_arr"></div>\					</div>\				</div>');				$sendbox.val("").focus();				$chat.find(".webim_chat_dialogue").scrollTop($dia_list.height() - 239);			}		});	});	function callBackMsg(msg){		try{			var $wbim = $("#WB_webim");			var $chat = $wbim.children(".wbim_chat_box");			var $sendbox = $chat.find(".sendbox_area");			var $dia_list = $chat.find(".webim_dia_list");			$dia_list.append('<div class="webim_dia_box webim_dia_l " node-type="msgRoot">\					<div class="dia_info">\					<span class="info_date">'+showTime()+'</span>\				</div>\				<div class="webim_dia_bg">\					<div class="dia_con" node-type="conRoot">\						<p class="dia_txt">'+ msg +'</p>\					</div>\					<div class="msg_arr"></div>\				</div>\			</div>');			$sendbox.val("").focus();			$chat.find(".webim_chat_dialogue").scrollTop($dia_list.height() - 239);					}catch(e){			alert(e.message);		}		}	</script></j:if>**/%><!--聊天窗口 start-->