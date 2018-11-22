<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>通州区节水管理平台</title>
    <meta name="description" content="overview &amp; stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <jsp:include page="/WEB-INF/view/common/basecss.jsp" flush="true"/>
    <script src="${res_url}js/charts/jquery.min.js"></script>
    <script src="${res_url}js/charts/highcharts.js"></script>
</head>
<style type="text/css">
    html {
        overflow-x: auto;
        overflow-y: auto;
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
        $.get("${context_path}/chart/supplyWaterLine/getSupplyDay", function (data) {
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
                    text: '水量单位（m³）'
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

        $.get("${context_path}/chart/supplyWaterLine/getSupplyMonth", function (data) {
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
                    text: '水量单位（m³）'
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
