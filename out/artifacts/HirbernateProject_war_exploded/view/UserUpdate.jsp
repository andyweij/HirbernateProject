<%--
  Created by IntelliJ IDEA.
  User: weichiehhsu
  Date: 2023/8/9
  Time: 下午 05:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>

    <title>UserUpdate</title>
    <script src="js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#userId").bind("change", function () {
                //# 取id
                var userID = $("#userId option:selected").val();

                var userParam = {userID: userID};

                if (userID != "") {
                    $.ajax({
                        url: 'usersListController?action=updateUserView', // 指定要進行呼叫的位址
                        type: "GET", // 請求方式 POST/GET
                        // data: {id : accountID}, // 傳送至 Server的請求資料(物件型式則為 Key/Value pairs)
                        data: userParam,
                        dataType: 'JSON', // Server回傳的資料類型
                        success: function (userInfo) { // 請求成功時執行函式

                            $("#userAccount").val(userInfo.account);//id
                            $("#userName").val(userInfo.name);
                            $("#userAge").val(userInfo.age);
                        },
                        error: function (error) { // 請求發生錯誤時執行函式
                            alert("Ajax Error!");
                        }
                    });
                } else {
                    $("#userAccount").val('');
                    $("#userName").val('');
                    $("#userAge").val('');
                }
            });

        })
        ;
    </script>
</head>
<body>
<jsp:include page="UserManagement"></jsp:include>
<hr>
<form action="usersListController?action=updateUser" method="POST">
    <p>
        <select id="userId" name="userId">
            <option value="">----- 請選擇 -----</option>
            <c:forEach items="${users}" var="user">
                <option
                        <c:if test="${user.id eq updateUserId}">selected</c:if>
                        value="${user.id}">
                        ${user.name}
                </option>
            </c:forEach>
        </select>
    </p>
    <br>
    帳戶:<input type="text" id="userAccount" name="userAccount"><BR>
    名稱:<input type="text" id="userName" name="userName"><BR>
    年齡:<input type="number" id="userAge" name="userAge">
    <button type="submit">修改</button>
</form>
</body>
</html>
