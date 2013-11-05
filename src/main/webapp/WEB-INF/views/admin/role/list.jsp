<%@page import="org.apache.commons.lang3.ArrayUtils"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%><%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%><j:set name="ctx" value="${pageContext.request.contextPath}" /><%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%><!DOCTYPE html><html lang="zh_CN"><head><script type="text/javascript">	$(function() {		$('#filterSelect').change(function() {							var fv = $(this).children('option:selected').val();							document.filterForm.method = "get";							document.filterForm.action = '${ctx}/admin/role/list?fdType="'									+ fv + '"';							document.filterForm.submit();							return;						});	});	function delSel() {		if (!confirm('您确定要批量删除吗？')) {			return false;		}		var chk_value = [];		$('input[name="ids"]:checked').each(function() {			chk_value.push($(this).val());		});		if (chk_value.length == 0) {			alert('请选择需要批量删除的内容。');			return false;		}		document.filterForm.method = "post";		document.filterForm.action = '${ctx}/admin/role/delAll';		document.filterForm.submit();		return;	}	function delById(fdId) {		if (!confirm('您确定要删除该用户？')) {			return false;		}		document.filterForm.method = "post";		document.filterForm.action = '${ctx}/admin/role/delete/' + fdId;		document.filterForm.submit();		return;	}</script></head><body>  <div class="container">	<div class="col-right">    	    	<div class="section">        <div class="page-header">        	<div class="tit-icon_bg"><i class="icon-pencil icon-white"></i><i class="icon-sj"></i></div>        	<h5>角色管理</h5>            <a href="${ctx}/dashboard" class="replyMybk" title="返回主页" ><i class="icon-home icon-white"></i></a>        </div>               <div class="page-body">     <j:autoform>      <form class="form-inline" name="filterForm">        <p class="page-intro">在本模块中，您可以配置平台的所有用户角色信息。</p>        <div class="btn-group">          <a href="${ctx}/admin/role/add" class="btn btn-primary">添加</a>          <a id="delAll" onclick="delSel()" class="btn btn-primary">批量删除</a>        </div>        <span style="margin: 0 2px 0 10px">查看</span> <select name="fdType" id="filterSelect">          <option value="">全部角色</option>          <option value="admin">系统管理员</option>          <option value="group">主管</option>          <!--            <option value="campus">学校主管</option>          -->          <option value="guidance">导师</option>          <option value="trainee">新教师</option>        </select>        <p/>        <table class="table table-striped">          <thead>            <tr>              <th><input type="checkbox" name="select" id="selectAll" /></th>              <th width="10px">#</th>              <th width="10%">姓名</th>              <th>角色</th>               <th width="40%">部门</th>              <th width="15%">操作</th>            </tr>          </thead>          <tbody>            <j:iter items="${page.list}" var="bean" status="vstatus">              <tr>                <td style="width: 10px;"><label> <input type="checkbox" name="ids" class="check"                    value="${bean.fdId}" />                </label></td>                <td>${vstatus.index + 1}</td>                <td>${bean.sysOrgPerson.realName}</td>                <td>${bean.roleEnum.value}</td>                <td>${bean.sysOrgPerson.hbmParent.fdName}</td>                <td><j:if test="${bean.fdId!='13b0cc641357059ccbf0b484a8da92dc'}">                    <a href="${ctx}/admin/role/edit/${bean.fdId}">修改</a>&nbsp;				    <a href="#" onclick="delById('${bean.fdId}')">删除</a>                  </j:if></td>              </tr>            </j:iter>          </tbody>        </table>        <tags:pagination page="${page}" searchParams="fdType=${fdType }" paginationSize="5" />      </form>    </j:autoform>  </div>        </div>    </div></div>  <script src="${ctx}/resources/js/common.js" type="text/javascript"></script></body></html>