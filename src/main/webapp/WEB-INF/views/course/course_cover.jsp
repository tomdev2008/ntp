<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="j" uri="/WEB-INF/tags/formtag.tld" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<j:set name="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title></title>
    <link rel="stylesheet" href="${ctx}/resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctx}/resources/css/global.css?id=12"/>
    <link rel="stylesheet" href="${ctx}/resources/css/admin-default.css?id=12"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/uploadify/uploadify.css"/>
    <script src="${ctx}/resources/js/jquery.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctx}/resources/uploadify/jquery.uploadify-3.1.min.js?id=1211"></script>
</head>
<body>
<form class="form-horizontal" id="inputForm" method="post" action="${ctx}/course/cover" name="form">
<input name="courseId" id="courseId" type="hidden" value="141e8cfa2710a52f4f19ee243dcba0a9"><!--courseId--对应的课程id-->
    <img id="imgshow" style="width: 200px;height:300px;" src="${ctx}/resources/images/zht-main-img.jpg"></img>
    <br/>         
             名称：   <input type="text" id="fdName" name="fdName"/>
    <br/>

          备注：    <input type="text" name="fdContext">
    <br/>
<tags:simpleupload filename="fdName"
                       filevalue="" id="upMovie" exts="*.jpg;" imgshow="imgshow" attIdName="attId" attIdID="attIdID">
    </tags:simpleupload>
     <br/>
    <input type="submit" value="确定"/>
     </form>
<script src="${ctx}/resources/js/lib/bootstrap/bootstrap.min.js" type="text/javascript"></script>
</body>
</html>