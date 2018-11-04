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
    <script src="https://img.hcharts.cn/highcharts/highcharts.js"></script>
    <script src="https://img.hcharts.cn/highcharts/highcharts-3d.js"></script>
    <script src="https://img.hcharts.cn/highcharts/modules/exporting.js"></script>
    <script src="https://img.hcharts.cn/highcharts/modules/drilldown.js"></script>
    <script src="https://img.hcharts.cn/highcharts-plugins/highcharts-zh_CN.js"></script>

</head>
<style type="text/css">
    html {
        overflow-x: auto;
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
            <div class="row">
                <div class="col-sm-12">
                    <div id="streetUse" style="min-width: 8000px; height: 400px; margin: 0 auto"></div>
                </div>
                <div class="col-sm-12">
                    <div id="meterAttrUse" style="min-width: 8000px; height: 400px; margin: 0 auto"></div>
                </div>
            </div>
            <div class="row" style="text-align: left;">
                <div id="watersTypeUse" style="min-width: 8000px; margin-right: 7600px; text-align: left;height: 400px"></div>
            </div>
        </div>
    </div>
</div>
<script language="JavaScript">
    $(document).ready(function () {
        Highcharts.chart('streetUse', {
            chart: {
                type: 'column'
            },
            title: {
                text: '${streetTitle}'
            },
            subtitle: {
                text: '数据来源：通州节水办。（点击可查看各单位具体的用水量）'
            },
            xAxis: {
                type: 'category'
            },
            yAxis: {
                title: {
                    text: '各乡镇总的用水量.单位（立方米）'
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
                headerFormat: '<span style="font-size:11px">{series.name}</span><br/>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b><br/>'
            },
            series: [{
                name: '所属乡镇',
                colorByPoint: true,
                data: JSON.parse('${seriesJsonData}')
            }],
            drilldown: {
                series: JSON.parse('${drilldownJsonData}')
            }
        });
        Highcharts.chart('meterAttrUse', {
            credits: {
                enabled: false // 禁用版权信息
            },
            title: {
                text: '${meterAttrTitle}'
            },
            subtitle: {
                text: '数据来源：通州节水办.'
            },
            xAxis: {
                categories: JSON.parse('${meterAttrName}')
            },
            yAxis: {
                title: {
                    text: '水量单位（立方米）'
                }
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle'
            },
            plotOptions: {
                spline: {
                    shadow: true,
                    animation: true,
                    lineWidth: 1
                },
                series: {
                    label: {
                        connectorAllowed: false
                    }
                }
            },
            series: [{
                name: '<span title="根据水表属性统计">用水量</span>',
                data: JSON.parse('${meterAttrSeris}'),
                events: {
                    click: function (event) {
                        window.location.href = "${context_path}/report/company";
                    }
                }
            }],
            responsive: {
                rules: [{
                    condition: {
                        maxWidth: 500
                    },
                    chartOptions: {
                        legend: {
                            layout: 'horizontal',
                            align: 'center',
                            verticalAlign: 'bottom'
                        }
                    }
                }]
            }
        });

        Highcharts.chart('watersTypeUse', {
            chart: {
                type: 'pie',
                options3d: {
                    enabled: true,
                    alpha: 45,
                    beta: 0
                }
            },
            title: {
                text: '${watersTypeTitle}'
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage}</b>'
            },
            credits: {
                enabled: false // 禁用版权信息
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    depth: 35,
                    dataLabels: {
                        enabled: true,
                        format: '{point.name}'
                    },
                    events: {
                        click: function (e) {
                            parent.location.href = e.point.url;
                            parent.location.reload();
                        }
                    }
                }
            },
            series: [{
                type: 'pie',
                name: '根据水源类型统计用水量占比',
                data: JSON.parse('${watersTypeSeris}')
            }]
        });
    })
</script>
</body>
</html>
