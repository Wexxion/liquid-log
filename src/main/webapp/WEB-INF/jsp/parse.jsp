<%--
  Created by IntelliJ IDEA.
  User: wexxi
  Date: 30.10.18
  Time: 22:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<style>
    #myProgress {
        width: 100%;
        background-color: #ddd;
    }

    #myBar {
        width: 0;
        height: 30px;
        background-color: #4CAF50;
    }
</style>
<head>
    <title>Title</title>
</head>
<body>
<hr>
<form action="${pageContext.request.contextPath}/parse/upload" method="POST" enctype="multipart/form-data">
    <label>DataBase name: <input type="text" name="dbName"></label><br>
    <label>
        Parsing mode:
        <select name="parseMode">
            <option value="sdng">sdng</option>
            <option value="gc">gc</option>
            <option value="top">top</option>
        </select>
    </label><br>
    <label>File: <input type="file" name="file"></label><br>
    <label>Time Zone: <input type="text" value="GMT" name="timeZone"></label><br>
    <label>Log Trace Result <input type="checkbox" name="logTrace"></label><br>
    <label><input type="submit" value="PARSE!" onclick="move()"></label>
</form>
<hr>
<div id="myProgress">
    <div id="myBar"></div>
</div>
<hr>
<div align="center" id="done" style="display:none;font-size: 5em;">Just a couple secs...</div>
<script>
    function move() {
        var file = document.getElementsByName("file")[0];
        var size = file.files[0].size / 1024 / 1024;
        var elem = document.getElementById("myBar");
        var width = 1;
        var id = setInterval(frame, size * 10);
        function frame() {
            if (width >= 100) {
                clearInterval(id);
                document.getElementById('done').style.display = 'block';
            } else {
                width++;
                elem.style.width = width + '%';
            }
        }
    }
</script>
</body>
</html>
