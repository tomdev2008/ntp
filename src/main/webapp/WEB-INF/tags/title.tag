<%@tag import="cn.me.xdf.utils.DateUtil"%>
<%@tag import="org.apache.commons.lang3.StringUtils"%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="value" type="java.lang.String" required="true"%>
<%@ attribute name="size" type="java.lang.Integer" required="true"%>
<%
	String title = value;
	if(StringUtils.isNotBlank(title)){
		if(title.length()>size){
			title = title.substring(0, size)+"...";
		}
	}
%>
<%= title%>