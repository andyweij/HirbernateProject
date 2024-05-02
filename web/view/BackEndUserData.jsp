<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" charset="utf-8" href="css/jquery-ui-1.9.2.custom.css">
    <link type="text/css" rel="stylesheet" charset="utf-8" href="css/bootstrap.css"/>
    <link type="text/css" rel="stylesheet" charset="utf-8" href="css/style.css"/>
    <link rel="stylesheet" type="text/css" href="css/daterangepicker.css"/>
    <script type="text/javascript" src="js/jquery.min.js"></script>
    <script type="text/javascript" src="js/moment.min.js"></script>
    <script type="text/javascript" src="js/daterangepicker.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.9.2.custom.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <title>BackEndUserData</title>
    <style>
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            $('input[id="date"]').daterangepicker({
                "locale": {
                    format: 'YYYY/MM/DD',
                    applyLabel: '確定',
                    cancelLabel: '取消',
                    "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
                    "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"]
                },
                singleDatePicker: true,
            });
        });
        //分頁資訊儲存欄位
        var sortableParams = {
            column: null,
            sortOrder: "asc"
        };
        //排序
        $(document).on('click', '.sortable', function () {
            var column = $(this).data('column'); // 獲取點選的欄位名稱
            var sortOrder = "asc"; // 初始排序順序，您可以在需要的時候切換

            // 如果欄位已經被點選過，則切換排序順序
            if ($(this).hasClass('asc')) {
                sortOrder = "desc";
                $(this).removeClass('asc').addClass('desc'); // 切換至 desc 並更新 class
            } else {
                $(this).removeClass('desc').addClass('asc'); // 切換至 asc 並更新 class
            }
            sortableParams.column = column;
            sortableParams.sortOrder = sortOrder;
            userList();
        });
        //分頁選擇
        $(document).on('click', 'a[name="page"]', function (event) {
            event.preventDefault();
            var clickedPage = $(this).data('page'); // 從data-page屬性中讀取分頁頁碼
            console.log("Clicked page:", clickedPage);
            userList(clickedPage);
        });
        //更新使用者編輯
        $(document).on('click', '.edit_btn', function () {
            var userId = $(this).data('userid'); // 獲取使用者 ID
            var iframeUrl = 'user_information_edit?editUserId=' + userId; // iframe 頁面的 URL
            console.log(userId);
            // // 在 Modal 中顯示 iframe
            // $('#editModal .modal-body').html('<iframe src="' + iframeUrl + '" frameborder="0" width="100%" height="100%"></iframe>');
            // var iframeUrl = "user_information_edit"; // 替換為你的 JSP 頁面的路徑
            var iframeElement = document.getElementById("myIframe");
            iframeElement.src = iframeUrl; // 設定 iframe 的 URL
            // 打開 Modal
            $('#editModal').modal('show');
        });
        //使用者列表與查詢
        function userList(page) {
            if (page == null) {
                page = 1;
            }
            var queryData = {
                queryDate: $('input[id="date"]').val(),
                curPage: page,
                column: sortableParams.column,
                sorted: sortableParams.sortOrder,
            }
            console.log(queryData.queryDate);
            $.ajax({
                url: 'usersListController?action=sortUser', // 指定要進行呼叫的位址
                method: "GET", // 請求方式 POST/GET
                data: queryData, // 傳送至 Server的請求資料(物件型式則為 Key/Value pairs)
                dataType: 'JSON', // Server回傳的資料類型
                success: function (data) { // 請求成功時執行函式
                    var userList = $("#userList");
                    userList.empty(); // 清空现有列表
                    var users = data.users;
                    var pages = data.pagination;
                    console.log(data)
                    // 遍历数据并添加到列表中
                    $.each(users, function (index, user) {
                        const formattedDate = moment(user.createTime.time).format('YYYY-MM-DD HH:mm:ss.SSS'); // 移除多餘的引號
                        console.log(index);
                        userList.append("<tr>")
                        userList.append("<td>" + user.id + "</td>" + "<td>" + user.account + "</td>" + "<td>" + user.name + "</td>" + "<td>" + user.age + "</td>" + "<td>" + formattedDate + "</td>");
                        var deleteLink = $("<a href='usersListController?action=deleteUser&userId=" + user.id + "'>刪除</a>");
                        var deleteCell = $("<td>").append(deleteLink);
                        userList.append('<td><a href="#" class="btnstyleA edit_btn" data-userid="' + user.id + '"><span>編輯</span></a></td>');
                        userList.append(deleteCell);
                        userList.append("</tr>");
                    });
                    var paginationList = $("#paginationList");
                    paginationList.empty();
                    $.each(pages.pagination, function (index, page) {
                        var pageButton = $("<span ><a href='#' name='page'>" + page + "\t" + "</a></span>");
                        pageButton.find('a').attr('data-page', page); // 將分頁頁碼存儲在data-page屬性中
                        if (index == 0 && pages.currentPageNo != 1) {
                            var prevPageButton = $("<span><a href='#' name='page'>&lt;</a></span>"); // 創建前一頁按鈕
                            prevPageButton.find('a').attr('data-page', pages.currentPageNo - 1);
                            paginationList.append(prevPageButton);
                            paginationList.append(pageButton);
                        } else if (index + 1 == pages.pagination.length && pages.currentPageNo != index + 1) {
                            paginationList.append(pageButton);
                            var nextPageButton = $("<span><a href='#' name='page'>&gt;</a></span>"); // 創建後一頁按鈕
                            nextPageButton.find('a').attr('data-page', pages.currentPageNo + 1);
                            paginationList.append(nextPageButton);
                        } else {
                            pageButton.find('a').attr('data-page', page);
                            paginationList.append(pageButton);
                        }

                    });
                },
                error: function (error) { // 請求發生錯誤時執行函式
                    alert("Ajax Error!");
                    console.log("Error Message:", error);
                }
            });
        }
        //新增使用者
        function insertUser() {
            var userName = document.getElementById("name").value;
            var userAge = document.getElementById("age").value;
            var userAccount = document.getElementById("account").value;
            console.log(userName, userAge);
            var formData = new FormData();
            formData.append("userName", userName);
            formData.append("userAge", userAge);
            formData.append("userAccount", userAccount);

            // 创建一个XMLHttpRequest对象
            const request = new XMLHttpRequest();

            // 配置请求
            request.open("POST", "usersListController?action=createUser", true);

            // 监听请求状态变化
            request.onreadystatechange = function () {
                if (request.readyState === XMLHttpRequest.DONE) {
                    if (request.status === 200) {
                        // 请求成功，您可以在这里处理响应
                        console.log(request.responseText);
                        $('#createModal').modal('hide');
                    } else {
                        // 请求失败，您可以在这里处理错误
                        alert("Error!");
                        console.error("Request failed with status: " + request.status);
                    }
                }
            };
            request.send(formData);
            // 发送请求
        }
        //刪除使用者
        function deleteUser(userId) {
            var request = new XMLHttpRequest();
            var formData = new FormData();
            formData.append("userId", userId);
            request.open("POST", "usersListController?action=deleteUser", true);
            // 监听请求状态变化
            request.onreadystatechange = function () {
                if (request.readyState === XMLHttpRequest.DONE) {
                    if (request.status === 200) {
                        // 请求成功，您可以在这里处理响应
                        console.log(request.responseText);
                    } else {
                        // 请求失败，您可以在这里处理错误
                        console.error("Request failed with status: " + request.status);
                    }
                }
            };
            request.send(formData);
        }

    </script>
</head>
<body id="main" onload="userList();">
<div id="header" class="clearfix">
    <div id="content clearfix">
        <div class="logoBrand pull-left">

        </div>
        <header class="pull-left">

        </header>
    </div>
</div>
<div class="mainContainter">
    <div class="pageHeader">
        <h1><i class=""></i>User
            <ul class="breadcrumb">
                <li><span>Home</span> <span class="divider">/</span></li>
                <li><span>EDU</span> <span class="divider">/</span></li>
                <li class="active"><span>User</span></li>
            </ul><!--breadcrumb-->
        </h1>
    </div><!--pageHeader-->

    <div class="setDIV modelDIV clearfix">
        <div class="content clearfix">
            <div class="top"><h1>查詢區塊</h1></div>
            <div class="">
                <span>查詢<input type="text" id="date" name="queryDate" value="" placeholder="輸入日期...">以後的 User 資料</span>
                <a href="javascript:userList()" class="btn btn-info">search</a>
            </div>

            <div>
                <a data-toggle="modal" data-target="#createModal" id="create_btn" class="btn btn-danger">新增User</a>
            </div>

            <div id="paginationList">
                <ul class="pagination">
                </ul>
            </div>

        </div>
    </div>
    <div class="portlet" id="memberList">
        <div class="top clearfix">
            <h1 class="pull-left">User List</h1>
        </div>
        <!--top-->
        <div class="content">

            <div class="channelList" id="list">
                <table class="table channelTable">
                    <thead>

                    <tr>
                        <th class="sortable" data-column="id">#</th>
                        <th class="sortable" data-column="account">帳號</th>
                        <th class="sortable" data-column="name">姓名</th>
                        <th class="sortable" data-column="age">年齡</th>
                        <th class="sortable" data-column="createTime">建立時間</th>
                        <th>編輯</th>
                        <th>刪除</th>
                    </tr>
                    </thead>
                    <tbody id="userList">
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</div>
<div id="footer">
    <div class="first">
        <div>
            <p>練習網頁</p>
            <p>我是footer</p>
        </div>
    </div><!--first-->
    <div class="last">
        <a href="#" target="_blank" style="color:#F00;">說明</a>
    </div><!--last-->
    <!-- Hidden stuff for demos -->

</div><!--footer-->
<div class="modal fade" id="editModal" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4><span class="glyphicon glyphicon-lock"></span>編輯使用者</h4>
            </div>
            <div class="modal-body">
                <iframe id="myIframe" frameborder="0" width="100%" height="100%"></iframe>
            </div>

        </div>
    </div>
</div>
<div class="modal fade" id="createModal" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4><span class="glyphicon glyphicon-lock"></span>新增使用者</h4>
            </div>
            <div class="modal-body">
                <form role="form">
                    <div class="form-horizontal clearfix">
                        <div class="control-group">
                            <label class="control-label">帳號：</label>
                            <div class="controls"><input type="text" id="account"
                                                         onKeyUp="value=value.replace(/[\W]/g,'')"
                                                         class="input-large textInput" placeholder="輸入帳號..." required>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">姓名：</label>
                            <div class="controls"><input type="text" id="name"
                                                         class="input-large textInput" placeholder="輸入名稱..." required>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">年齡：</label>
                            <div class="controls"><input type="number" id="age"
                                                         class="input-large textInput" placeholder="輸入年齡..." required>
                            </div>
                        </div>
                    </div>
                    <div id="saveSet">
                        <a href="javascript:insertUser();" class="btn btn-default btn-success"><span
                                class="glyphicon glyphicon-off"></span>儲存</a>
                        <a class="btn btn-default btn-default" data-dismiss="modal"><span
                                class="glyphicon glyphicon-remove"></span>取消</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</div>
</body>
</html>
