<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>水务状态图表</title>
    <meta name="description" content="overview &amp; stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <jsp:include page="/WEB-INF/view/common/basecss.jsp" flush="true"/>
    <script src="${res_url}js/charts/jquery.min.js"></script>
    <script src="${res_url}js/charts/highcharts.js"></script>
</head>
<style type="text/css">
    html {
        overflow-x: hidden;
        overflow-y: auto;
    }

    .adbody {
        font: 13px/180% Arial, Lucida, Verdana, "宋体", Helvetica, sans-serif;
        color: #333;
        background: #fff;
    }

    /* tipfloat */
    .tipfloat, .tipfloat .close {
        background: url('${res_url}img/tipright.png') no-repeat;
    }

    .tipfloat {
        display: none;
        z-index: 999;
        position: fixed;
        _position: absolute;
        right: 0px;
        bottom: 0;
        width: 236px;
        height: 196px;
        overflow: hidden;
    }

    .tipfloat .tiphead {
        height: 30px;
        line-height: 30px;
        overflow: hidden;
        padding: 0 5px;
    }

    .tipfloat .tiphead strong {
        float: left;
        color: #fff;
        font-size: 14px;
    }

    .tipfloat .tiphead .close {
        display: block;
        float: right;
        margin: 5px 0 0 0;
        width: 18px;
        height: 18px;
        line-height: 999em;
        overflow: hidden;
        cursor: pointer;
        background-position: -236px 0;
    }

    /* ranklist */
    .ranklist {
        padding: 10px 10px 10px 10px;
    }

    .ranklist li {
        height: 16px;
        line-height: 16px;
        overflow: hidden;
        position: relative;
        padding: 0 70px 0 30px;
        margin: 0 0 10px 0;
        vertical-align: bottom;
    }

    .ranklist li em {
        width: 20px;
        height: 16px;
        overflow: hidden;
        display: block;
        position: absolute;
        left: 0;
        top: 0;
        text-align: center;
        font-style: normal;
        color: #333;
    }

    .ranklist li em {
        background-position: 0 -16px;
    }

    .ranklist li.top em {
        background-position: 0 0;
        color: #fff;
    }

    .ranklist li .num {
        position: absolute;
        right: 0;
        top: 0;
        color: #999;
    }
</style>
<body class="no-skin">
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>
    <div class="main-content" id="page-wrapper">
        <div class="page-content" id="page-content">
            <div class="row" style="margin-top: 20px;">
                <div class="col-sm-12">
                    <div id="containerSupplyD" style="margin: 0 auto"></div>
                </div>
                <div class="col-sm-12">
                    <div id="containerSupplyM" style="margin: 0 auto"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<script language="JavaScript">
    $(document).ready(function () {
        $.get("${context_path}/chart/getSupplyDay", function (data) {
            var title = {
                text: ''
            };
            var subtitle = {
                text: '日供水量'
            };
            var xAxis = {
                categories: data.day
            };
            var yAxis = {
                title: {
                    text: '水量单位（立方米）'
                },
                plotLines: [{
                    value: 0,
                    width: 10
                }]
            };
            var tooltip = {
                valueSuffix: ''
            }
            var legend = {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle',
                borderWidth: 0
            };
            var series = [
                {
                    name: '日供水量',
                    data: data.sumWater
                }
            ];
            var plotOptions = {
                spline: {
                    shadow: true,
                    animation: true,
                    lineWidth: 1
                },
                series: {
                    color: '#00b16a',
                    lineWidth: 4,
                    cursor: 'pointer',
                    events: {
                        click: function (event) {
                            /*   alert(event.point.category); // X轴值
                             alert(this.data[event.point.x].y); // Y轴值*/
                            var time = event.point.category

                            window.location.href = "${context_path}/statis/daily?time=" + time + "&type=2";
                        }
                    }
                }
            };
            var credits = {
                enabled: false // 禁用版权信息
            }
            var json = {};
            json.title = title;
            json.subtitle = subtitle;
            json.xAxis = xAxis;
            json.yAxis = yAxis;
            json.tooltip = tooltip;
            json.legend = legend;
            json.series = series;
            json.plotOptions = plotOptions;
            json.credits = credits;
            $('#containerSupplyD').highcharts(json);
        });

        $.get("${context_path}/chart/getSupplyMonth", function (data) {
            var title = {
                text: ''
            };
            var subtitle = {
                text: '月供水量'
            };
            var xAxis = {
                categories: data.month
            };
            var yAxis = {
                title: {
                    text: '水量单位（立方米）'
                },
                plotLines: [{
                    value: 0,
                    width: 10
                }]
            };
            var tooltip = {
                valueSuffix: ''
            }
            var legend = {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle',
                borderWidth: 0
            };
            var series = [
                {
                    name: '月供水量',
                    data: data.sumWater
                }
            ];
            var plotOptions = {
                spline: {
                    shadow: true,
                    animation: true,
                    lineWidth: 1
                },
                series: {
                    color: '#00b16a',
                    lineWidth: 4,
                    cursor: 'pointer',
                    events: {
                        click: function (event) {
                            var time = event.point.category
                            window.location.href = "${context_path}/statis/month?time=" + time + "&type=2";
                        }
                    }
                }
            };
            var credits = {
                enabled: false // 禁用版权信息
            }
            var json = {};
            json.title = title;
            json.subtitle = subtitle;
            json.xAxis = xAxis;
            json.yAxis = yAxis;
            json.tooltip = tooltip;
            json.legend = legend;
            json.series = series;
            json.plotOptions = plotOptions;
            json.credits = credits;
            $('#containerSupplyM').highcharts(json);
        });
    });
</script>
</body>
</html>
