<%@tag import="cn.me.xdf.utils.DateUtil"%>
<%@tag import="org.apache.commons.lang3.StringUtils"%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="value" type="java.lang.String" required="true"%>
<%
	if(StringUtils.isNotBlank(value)){
		String [] lis = value.split(",");
		for(String li:lis){
%>
<li><%=li%></li>
<%
		}
	}
%>
