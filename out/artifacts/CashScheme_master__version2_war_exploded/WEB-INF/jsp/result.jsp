<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 10475
  Date: 2023/4/11
  Time: 22:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Result</title>
</head>
<body>
<h1>结果</h1>
<c:forEach var="t_item" items="${res}">
<tr>
    <td>文件名：${t_item.ind}</td>
    <td>权值：${t_item.weig}</td>
</tr>
</c:forEach>

<%--<c:forEach var="s" items="${res}">--%>
<%--    <h1>${s}</h1>--%>
<%--</c:forEach>--%>
</body>
</html>
