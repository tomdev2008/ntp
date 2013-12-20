<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<section class="section box-control">
	<div class="hd">
		<ul class="nav nav-tabs" id="navTabs">
			<li class="active"><a href="#unchecked">学习联盟</a></li>
			<li><a href="#checked">平台寄语</a></li>
		</ul>
		<div class="btn-toolbar">
			<a class="btn" href="${ctx }/admin/page/add?ptype=02">添加机构/部门</a>
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
			</span> <a class="btn btn-link" href="#">清空搜索结果</a>
		</div>
	</div>
</section>
<section class="section listWrap">
<div id="listdata">

</div>
</section>