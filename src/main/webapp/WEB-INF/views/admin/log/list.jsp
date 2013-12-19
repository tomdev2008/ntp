<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<j:set name="ctx" value="${pageContext.request.contextPath}" />
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html lang="zh_CN">
<head>
<script type="text/javascript" src="${ctx}/resources/js/jquery.jalert.js"></script>
<script type="text/javascript">
	$(function() {
		$('#filterSelect').change(function() {
			var fv = $(this).children('option:selected').val();
			document.filterForm.method = "get";
			document.filterForm.action = '${ctx}/admin/log/list?fdType="'
					+ fv + '"';
			document.filterForm.submit();
			return; 
		});
	});

/* 	function delSel() {
		if (!confirm('您确定要批量删除吗？')) {
			return false;
		}
		var chk_value = [];
		$('input[name="ids"]:checked').each(function() {
			chk_value.push($(this).val());
		});
		if (chk_value.length == 0) {
			alert('请选择需要批量删除的内容。');
			return false;
		}
		document.filterForm.method = "post";
		document.filterForm.action = '${ctx}/admin/role/delAll';
		document.filterForm.submit();
		return;
	}
 */
	function delById(fdId,fdType){
		$.fn.jalert("您确定要删除该日志？",function(){
			document.filterForm.method = "post";
			document.filterForm.action = '${ctx}/admin/log/delete?fdId='+fdId+'&fdType='+fdType;
			document.filterForm.submit();
		});
	}
</script>
</head>
<body>
    <div class="page-body"> 
    <j:autoform>
      <form class="form-inline" name="filterForm">
       </form>
        <p class="page-intro">在本模块中，您可以查看或删除日志信息</p>
        <div class="btn-group">
          <a id="delAll" onclick="delSel()" class="btn btn-primary">批量删除</a>
        </div>
        <span style="margin: 0 2px 0 10px">查看</span> <select name="fdType" id="filterSelect">
          <option value="LogLogin">登录日志</option>
          <option value="LogLogout">登出日志</option>
          <option value="LogApp">操作日志</option>
        </select>
        <p/>
        <table class="table table-striped">
          <thead>
            <tr>
              <th><input type="checkbox" name="select" id="selectAll" /></th>
              <th width="10px">#</th>
              <th width="10%">操作人</th>
              <th width="30%">部门</th>
              <th width="10%">日志类型</th>
              <th width="30%">操作时间</th>
              <th width="10%">操作</th>
            </tr>
          </thead>
          <tbody>
            <j:iter items="${list}" var="bean" status="vstatus">
              <tr>
                <td style="width: 10px;"><label> <input type="checkbox" name="ids" class="check"
                    value="${bean.fdId}" />
                </label></td>
                <td>${vstatus.index+1}</td>
                <td>${bean.fdUserName}</td>
                <td>${bean.fdUserDep}</td>
                <td>${bean.logType}</td>
                <td>${bean.time}</td>
                <td>
                <a href="${ctx}/admin/log/view?logId=${bean.fdLogId}&logType=${fdType}">查看</a>
                <a href="javascript:void(0)" onclick="delById('${bean.fdLogId}','${fdType}')">删除</a>
                </td>
              </tr>
            </j:iter>
          </tbody> 
        </table>
        <tags:pagination page="${page}" searchParams="fdType=${fdType}" paginationSize="10" />
     
    </j:autoform>
  </div>
</body>
</html>