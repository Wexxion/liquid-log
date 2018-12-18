<%@ page import="ru.naumen.sd40.log.parser.parsers.front.FrontDataType" %>
<%@ page import="ru.naumen.perfhouse.statdata.Constants" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: wexxi
  Date: 18.12.18
  Time: 23:01
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
    Number times[] = (Number[])request.getAttribute(Constants.TIME);
    Number frontTimes[]=  (Number[])request.getAttribute(FrontDataType.FRONT_TIMES);
    Number renderMin[] = (Number[])request.getAttribute(FrontDataType.MIN_RENDER_TIME);
    Number renderAvg[] = (Number[])request.getAttribute(FrontDataType.AVERAGE_RENDER_TIME);
    Number renderMax[] = (Number[])request.getAttribute(FrontDataType.MAX_RENDER_TIME);

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
        <li class="nav-item"><a class="nav-link active">Front Render</a></li>
        <c:forEach items="${dataTypes}" var="dataType">
            <li class="nav-item">
                <a class="btn btn-outline-primary"
                   href="/history/${client}<%=custom %>/${dataType}<%=path%>">
                        ${dataType}
                </a>
            </li>
        </c:forEach>
    </ul>
</div>
<<!-- Front chart -->
<div class="container" id="front">
    <div id="front-chart-container" style="height: 600px"></div>
    <div class="scroll-container">
        <table class="table table-fixed header-fixed">
            <thead class="thead-inverse">
                <th class="col-xs-3">Time</th>
                <th class="col-xs-2">Number of Renders</th>
                <th class="col-xs-2">Min Render time, ms</th>
                <th class="col-xs-2">Average Render time, ms</th>
                <th class="col-xs-2">Max Render Time, ms</th>
            </thead>
            <tbody >
            <% for(int i=0;i<times.length;i++) {%>
            <tr class="row">
                <td class="col-xs-3" style="text-align:center;">
                    <%= new java.util.Date(times[i].longValue()).toString() %>
                </td>
                <td class="col-xs-2" >
                    <%= frontTimes[i].intValue() %>
                </td>
                <td class="col-xs-2">
                    <%= renderMin[i] %>
                </td>
                <td class="col-xs-2">
                    <%= renderAvg[i] %>
                </td>
                <td class="col-xs-2">
                    <%= renderMax[i] %>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>

</div>
<script type="text/javascript">
    var times = [];
    var frontTimes = [];
    var renderMinTime = [];
    var renderAvgTime = [];
    var renderMaxTime = [];

    <% for(int i=0;i<times.length;i++) {%>
    times.push((<%=times[i]%>));
    frontTimes.push([new Date(<%= times[i] %>), <%= Math.round(frontTimes[i].doubleValue()) %>]);
    renderMinTime.push([new Date(<%= times[i] %>), <%= renderMin[i] %>]);
    renderAvgTime.push([new Date(<%= times[i] %>), <%= renderAvg[i] %>]);
    renderMaxTime.push([new Date(<%= times[i] %>), <%= renderMax[i] %>]);
    <% } %>

    document.getElementById('date_range').innerHTML += 'From: '+new Date(times[<%=times.length%>-1])+'<br/>To:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +new Date(times[0])

    if(localStorage.getItem('frontTimes')==null){
        localStorage.setItem('frontTimes', 'false');
    }
    if(localStorage.getItem('renderMinTime')==null){
        localStorage.setItem('renderMinTime', 'false');
    }
    if(localStorage.getItem('renderAvgTime')==null){
        localStorage.setItem('renderAvgTime', 'false');
    }
    if(localStorage.getItem('renderMaxTime')==null){
        localStorage.setItem('renderMaxTime', 'true');
    }


    var frontTimesvisible = localStorage.getItem('frontTimes')==='true';
    var renderMinTimevisible = localStorage.getItem('renderMinTime')==='true';
    var renderAvgTimevisible = localStorage.getItem('renderAvgTime')==='true';
    var renderMaxTimevisible = localStorage.getItem('renderMaxTime')==='true';

    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });

    var myChart = Highcharts.chart('front-chart-container', {
        chart: {
            zoomType: 'x,y'
        },

        title: {
            text: 'Render Times'
        },

        tooltip: {
            formatter: function() {
                var index = this.point.index;
                var date =  new Date(times[index]);
                return Highcharts.dateFormat('%a %d %b %H:%M:%S', date)
                    + '<br/> <b>'+this.series.name+'</b> '+ this.y + ' '+this.series.options.unit+ '<br/>'
            }
        },

        xAxis: {
            labels:{
                formatter:function(obj){
//                        var index = this.point.index;
//                        var date =  new Date(times[index]);
                    return Highcharts.dateFormat('%a %d %b %H:%M:%S', new Date(times[this.value]));
                }
            },
            reversed: true
        },

        yAxis: {
            title: {
                text: 'front'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        plotOptions: {
            line: {
                marker: {
                    enabled: false
                },
                events: {
                    legendItemClick: function(event) {
                        var series = this.yAxis.series;
                        seriesLen = series.length;

                        if(event.target.index==0){
                            localStorage.setItem('frontTimes', !series[0].visible);
                        }
                        if(event.target.index==1){
                            localStorage.setItem('renderMinTime', !series[1].visible);
                        }
                        if(event.target.index==2){
                            localStorage.setItem('renderAvgTime', !series[2].visible);
                        }
                        if(event.target.index==3){
                            localStorage.setItem('renderMaxTime', !series[3].visible);
                        }
                    }
                }
            }
        },
        series: [{
            name: 'front Performed',
            data: frontTimes,
            visible: frontTimesvisible,
            unit: 'times',
            turboThreshold: 10000
        }, {
            name: 'Min render Time',
            data: renderMinTime,
            visible: renderMinTimevisible,
            unit: 'ms',
            turboThreshold: 10000

        }, {
            name: 'Average render Time',
            data: renderAvgTime,
            visible: renderAvgTimevisible,
            unit: 'ms',
            turboThreshold: 10000

        }, {
            name: 'Max render Time',
            data: renderMaxTime,
            visible: renderMaxTimevisible,
            unit: 'ms',
            turboThreshold: 10000
        }]
    });

</script>
</body>
</html>