<%@page pageEncoding="UTF-8"%><%@ taglib prefix="j" uri="/WEB-INF/tld/formtag.tld"%><j:set name="ctx" value="${pageContext.request.contextPath}" /><!DOCTYPE html><html lang="zh_CN"><head><link rel="stylesheet" href="${ctx}/resources/css/jquery.autocomplete.css" /><script type="text/javascript" src="${ctx }/resources/js/jquery.autocomplete.pack.js"></script><script type="text/javascript" src="${ctx }/resources/js/jquery.jalert.js"></script><script type="text/javascript" src="${ctx}/resources/js/jquery.validate.min.js"></script><style type="text/css">#adduser {  background-color: #FFF;  padding: 10px 20px;}#adduser .u_list>tbody>tr a {  cursor: pointer;  text-decoration: none}#inpUser {  width: 400px;}</style><script type="text/javascript">	jQuery(document).ready(function() {		jQuery("#inputForm").validate();		return;	});</script><script type="text/javascript">	$()			.ready(					function() {						var name = "";						var id = "";						$("#inpUser").autocomplete(								"${ctx}/ajax/user/findByName", {									matchContains : true,									max : 100,									width : 410,									dataType : 'json', // 加入对返回的json对象进行解析的函数，函数返回一个数组									parse : function(data) {										var rows = [];										for ( var i = 0; i < data.length; i++) {											rows[rows.length] = {												data : data[i],												value : data[i].name,												result : data[i].name, // 显示在输入文本框里的内容											};										}										return rows;									},									formatItem : function(item) {										var photo;						    			if(item.imgUrl.indexOf("http")>-1){						    				photo=item.imgUrl;						    			}else{						    				photo="${ctx}/"+item.imgUrl;						    			}						    			return '<img src="'				                        + (photo) + '" alt="">'				                        + item.name + '&nbsp;&nbsp;&nbsp;&nbsp;'				                        + item.org + '&nbsp;&nbsp;' + item.department;									}								}).result(function(event, item) {							name = item.name;							id = item.id;						});						$("#u_list tbody a").click(function() {							$(this).closest("tr").remove();						});						$("#addUser")								.click(										function() {											$("#inpUser").val("");											var rows = 0;											$("#u_list tbody tr input")													.each(															function() {																rows = $(																		"#u_list tbody")																		.children(																				"tr").length;															});											if (rows == 1) {												jalert("每次只能添加一个用户！");											} else {												$("#u_list tbody")														.append(																'<tr><td>'																		+ name																		+ '<input type="hidden" name="personId" value="' + id			+ '" /></td><td><a class="text-error">移除</a></td></tr>')														.find("a")														.click(																function() {																	$(this)																			.closest(																					"tr")																			.remove();																});											}										});					});</script></head><body>  <div class="page-body">   <form class="form-horizontal" method="post" id="inputForm" action="${ctx}/admin/role/save" name="form">    <section id="set-exam">      <div>        <p class="page-intro">在本模块中，您可以配置平台的所有用户角色信息。</p>        <br />        <table class="table table-striped">          <tr>            <th width="20%">角色</th>            <td><select id="roleType" name="fdType" class="required">                <option value="">选择角色</option>                <option value="admin">系统管理员</option>                <option value="group">主管</option>            </select></td>          </tr>          <tr>            <th>人员</th>            <td>              <table class="table " id="u_list">                <tbody>                </tbody>              </table>            </td>          </tr>          <tr>            <th>选择</th>            <td colspan="2"><input id="inpUser" type="text" class="span3" />              <button id="addUser" type="button" class="btn">添加</button></td>          </tr>          <tr>            <td colspan="4"><button class="btn btn-primary" type="submit" value="提交" id="saveBtn">确定</button></td>          </tr>        </table>      </div>    </section>  </form>  </div></body></html>