<%--
  Created by IntelliJ IDEA.
  User: weichiehhsu
  Date: 2023/8/9
  Time: 下午 03:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>UserCreate</title>
</head>
<body>
<jsp:include page="UserManagement"></jsp:include>
<hr>
<form action="usersListController?action=createUser" method="post">
    帳戶:<input type="text" name="userAccount"><BR>
    名稱:<input type="text" name="userName"><BR>
    年齡:<input type="number" name="userAge">
    <button type="submit">新增</button>
</form>
<p>${createMsg}</p>
<% session.removeAttribute("createMsg"); %>
</body>
</html>
