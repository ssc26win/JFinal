<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html style="min-width: 5000px;">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>水务状态图表</title>
    <meta name="description" content="overview &amp; stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <jsp:include page="/WEB-INF/view/common/basecss.jsp" flush="true"/>
    <script src="${res_url}js/charts/jquery.min.js"></script>
    <script src="https://img.hcharts.cn/highcharts/highcharts.js"></script>
    <script src="https://img.hcharts.cn/highcharts/modules/exporting.js"></script>
    <script src="https://img.hcharts.cn/highcharts/modules/series-label.js"></script>
    <script src="https://img.hcharts.cn/highcharts/modules/oldie.js"></script>
    <script src="https://img.hcharts.cn/highcharts-plugins/highcharts-zh_CN.js"></script>
</head>
<style type="text/css">
    html {
        overflow-x: auto;
        overflow-y: hidden;
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
            <div class="row" style="">
                <div class="col-sm-12">
                    <div id="companyUseDAll" style="min-width: 5000px;margin: 0 auto"></div>
                </div>
            </div>
            <div class="row" style="">
                <div class="col-sm-12">
                    <div id="companyUseD" style="min-width: 5000px; margin: 0 auto"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<script language="JavaScript">
    $(document).ready(function () {
        Highcharts.chart('companyUseDAll', {
            chart: {
                type: 'column'
            },
            title: {
                text: '${companyTitle}'
            },
            subtitle: {
                text: '数据来源：通州节水办。（点击可查看各单位具体的用水量）'
            },
            xAxis: {
                type: 'category'
            },
            yAxis: {
                title: {
                    text: '管辖范围内总的用水量.单位（立方米）'
                }
            },
            legend: {
                enabled: false
            },
            credits: {
                enabled: false // 禁用版权信息
            },
            plotOptions: {
                series: {
                    borderWidth: 0,
                    dataLabels: {
                        enabled: true,
                        format: '{point.y}'
                    }
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}(<span style="color: red;">点击查看该日</span>)</span><br/>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b><br/>'
            },
            series: [{
                name: '日期',
                colorByPoint: true,
                data: JSON.parse('${seriesJsonData}'),
                events: {
                    click: function (data) {
                        var date = data.point.name;
                        if (date != "") {
                            setOne(date);
                        }
                    }
                }
            }],
        });
        var myDate = new Date();
        var time = myDate.toLocaleDateString().split('/').join('-');
        setOne(time);
    })
    function setOne(date) {
        console.log(date);
        $.get("${context_path}/report/daily/chart/setOneDaily?date=" + date, function (data) {
            var title = {
                text: ''
            };
            var subtitle = {
                text: '用水量'
            };
            var xAxis = {
                categories: data.companies
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
                    name: '各单位用水量',
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
                            var companyName = event.point.category;
                            window.location.href = encodeURI("${context_path}/report/daily?companyName=" + companyName + "&date=" + date);
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
            $('#companyUseD').highcharts(json);
        });
    }
</script>
</body>
</html>

