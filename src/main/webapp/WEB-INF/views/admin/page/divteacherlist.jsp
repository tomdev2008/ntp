<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
<section class="section box-control">
	<div class="bd">
		<ul class="nav nav-tabs" id="navTabs">
			<li><a href="#unchecked">学习联盟</a></li>
			<li class="active"><a href="#checked">平台寄语</a></li>
		</ul>
		<div class="btn-toolbar">
			<a class="btn" href="${ctx }/admin/page/add?ptype=01">添加平台寄语</a>
			<div class="btn-group">
				<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
					操作 <span class="caret"></span>
				</a>
				<ul class="dropdown-menu">
					<li><a href="#">批量删除</a></li>
				</ul>
			</div>
			<form class="toolbar-search">
				<input type="text" id="serach" class="search" placeholder="机构/部门">
				<i class="icon-search"></i>
			</form>
			<span class="showState"> <span class="muted">当前显示：</span> <span
				id="markshow"> <a id="containkey" href="#">全部条目</a>
			</span>
			</span> <a class="btn btn-link" href="#" onclick="clearserach();">清空搜索结果</a>
		</div>
	</div>
</section>
<section class="section listWrap">
	<ul class="nav list">
	</ul>
</section>
<div class="pages">
	<div class="btn-group dropup">

		<button class="btn btn-primary btn-ctrl" type="button" disabled>
			<i class="icon-chevron-left icon-white"></i>
		</button>

	</div>
</div>