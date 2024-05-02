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
    <title>userInformationEdit</title>
    <script type="text/javascript">
        $(document).ready(function () {
            // 獲取 editUserId 從 URL 查詢參數
            var editUserId = getParameterByName('editUserId');
            $('#editUserId').val(editUserId); // 在隱藏欄位中設定 editUserId

            $('#submitForm').on('click', function (event) {
                event.preventDefault();
                var editAccount = $('#editAccount').val();
                var editName = $('#editName').val();
                var editAge = $('#editAge').val();
                // 使用獲取的 editUserId 和輸入的參數組成資料物件
                var dataToSend = {
                    userAccount: editAccount,
                    userId: editUserId,
                    userName: editName,
                    userAge: editAge
                };

                $.ajax({
                    url: 'usersListController?action=updateUser', // 替換為後端處理請求的 URL
                    method: 'POST',
                    data: dataToSend,
                    dataType: 'json',
                    success: function (response) {
                        console.log(response);
                        window.parent.$('#editModal').modal('hide');
                        window.parent.userList();
                        // 可以執行一些處理，例如更新介面等
                    },
                    error: function (error) {
                        console.error('AJAX Error:', error);
                    }
                });
            });
        });

        function getParameterByName(name) {
            var url = window.location.href;
            name = name.replace(/[\[\]]/g, '\\$&');
            var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)');
            var results = regex.exec(url);
            if (!results) return null;
            if (!results[2]) return '';
            return decodeURIComponent(results[2].replace(/\+/g, ' '));
        }

    </script>
</head>
<body>
<form role="form">
    <!-- 隱藏欄位用於儲存 editUserId -->
    <input type="hidden" id="editUserId">

    <div class="form-horizontal clearfix">
        <div class="control-group">
            <label class="control-label">帳號：</label>
            <div class="controls"><input type="text" id="editAccount" onKeyUp="value=value.replace(/[\W]/g,'')"
                                         class="input-large textInput" placeholder="輸入帳號..." required>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">姓名：</label>
            <div class="controls"><input type="text" id="editName"
                                         class="input-large textInput" placeholder="輸入名稱..." required>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">年齡：</label>
            <div class="controls"><input type="number" id="editAge"
                                         class="input-large textInput" placeholder="輸入年齡..." required>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="submitForm">儲存修改</button>
    </div>
</form>
</body>
</html>
