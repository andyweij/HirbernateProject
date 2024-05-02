<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<jsp:include page="UserManagement"></jsp:include>
<hr>
<a href="usersListController?action=queryAllUsers">查詢</a>
<table border="1">
    <th>ID</th>
    <th>account</th>
    <th>name</th>
    <th>age</th>
    <th>createTime</th>
    <th>remove</th>
    <c:forEach items="${users}" var="user">
        <tr>
            <td>${user.id} </td>
            <td>${user.account} </td>
            <td>${user.name} </td>
            <td>${user.age} </td>
            <td>${user.createTime} </td>
            <td><a href="usersListController?action=deleteUser&id=${user.id}"+>刪除</a> </td>
        </tr>
    </c:forEach>

    <br><br>
    <c:forEach items="${pagination.pagination}" var="page">
        <ul class="pagination" >
            <a href="${page}" class="page-link" href="#">${page}</a>
        </ul>
    </c:forEach>
</table>

</body>

</html>
