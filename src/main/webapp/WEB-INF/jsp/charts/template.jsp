<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: wexxi
  Date: 11.12.18
  Time: 23:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>SD40 Performance indicator</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.5/css/bootstrap.min.css"
          integrity="sha384-AysaV+vQoT3kOAXZkl02PThvDr8HYKPZhNT5h/CXfBThSRXQ6jW5DO2ekP5ViFdi" crossorigin="anonymous"/>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.5/js/bootstrap.min.js"
            integrity="sha384-BLiI7JTZm+JWlgKa0M0kGRpJbF2J8q+qreVrKBC47e3K6BW78kGLrCkeRX6I9RoK"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="/css/style.css"/>
</head>
<body>
<script src="http://code.highcharts.com/highcharts.js"></script>
<%
    //Prepare links
    String path;
    String custom = "";
    if (request.getAttribute("custom") == null) {
        Object year = request.getAttribute("year");
        Object month = request.getAttribute("month");
        Object day = request.getAttribute("day");
        String countParam = request.getParameter("count");

        String params = "";
        String datePath = "";

        StringBuilder sb = new StringBuilder();

        if (countParam != null) {
            params = sb.append("?count=").append(countParam).toString();
        } else {
            sb.append('/').append(year).append('/').append(month);
            if (!day.toString().equals("0")) {
                sb.append('/').append(day);
            }
            datePath = sb.toString();
        }
        path = datePath + params;
    } else {
        custom = "/custom";
        Object from = request.getAttribute("from");
        Object to = request.getAttribute("to");
        Object maxResults = request.getAttribute("maxResults");

        StringBuilder sb = new StringBuilder();
        path = sb.append("?from=").append(from).append("&to=").append(to).append("&maxResults=").append(maxResults).toString();
    }
%>
<div class="container">
    <br>
    <h1>Performance data for "${client}"</h1>
    <h3><a class="btn btn-success btn-lg" href="/">Client list</a></h3>
    <h4 id="date_range"></h4>
    <p>Feel free to hide/show specific data by clicking on chart's legend</p>
    <ul class="nav nav-pills">
        <li class="nav-item"><a class="nav-link active">Your chart type (active)</a></li>
        <c:forEach items="${charts}" var="chart">
            <li class="nav-item">
                <a class="btn btn-outline-primary"
                   href="/history/${client}<%=custom %>/${chart.prefix}<%=path%>">
                        ${chart.name}
                </a>
            </li>
        </c:forEach>
    </ul>
</div>
<!-- Your chart -->
<!-- goes here -->
</body>
</html>