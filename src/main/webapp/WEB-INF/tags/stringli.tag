<%@tag import="cn.me.xdf.utils.DateUtil"%>
<%@tag import="org.apache.commons.lang3.StringUtils"%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="value" type="java.lang.String" required="true"%>
<%@ attribute name="sign" type="java.lang.String" required="false"%>
<%
	String split = ",";
	if(StringUtils.isNotBlank(sign)){
		split = sign;
	}
	if(StringUtils.isNotBlank(value)){
		String [] lis = value.split(split);
		for(String li:lis){
%>
<li><%=li%></li>
<%
		}
	}
%>
