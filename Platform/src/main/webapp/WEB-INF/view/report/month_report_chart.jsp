<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html style="min-width: 5000px;">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>通州区节水管理平台</title>
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
            <div class="row">
                <div class="col-xs-12">
                    <!-- PAGE CONTENT BEGINS -->
                    <div class="widget-box">
                        <div class="widget-header widget-header-small">
                            <h5 class="widget-title lighter">筛选</h5>
                        </div>

                        <div class="widget-body">
                            <div class="widget-main">
                                <div class="row">
                                    <div class="col-xs-12">
                                        <form id="exportForm" action="${context_path}/report/month/chart"
                                              method="post">
                                            <input type="text" style="display:none"/>

                                            <div class="input-group">
                                                日期时间:
                                                <input type="text" id="startTime" name="startTime" value="${startTime}"
                                                       class="form_date" style="width: 100px;"/>~
                                                <input type="text" id="endTime" name="endTime" value="${endTime}"
                                                       class="form_date" style="width: 100px;"/>
                                                <select id="type" name="type"
                                                        style="margin-left: 5px;width: 159px;height: 34px;">
                                                    <option value="">请选择单位类型</option>
                                                </select>
                                                <input type="text" id="name" name="name" value="${name}" class=""
                                                       placeholder="请输入单位名称"
                                                       style="margin-left: 5px;"/>
                                                <input type="text" id="innerCode" name="innerCode" value="${innerCode}"
                                                       class=""
                                                       placeholder="请输入单位编号" style="margin-left: 5px;"/>
                                                <select id="watersType" name="watersType"
                                                        style="margin-left: 5px;width: 159px; height: 34px;">
                                                    <option value="">请选择水源类型</option>
                                                </select>
                                                <select id="meterAttr" name="meterAttr"
                                                        style="margin-left: 5px;width: 159px; height: 34px;">
                                                    <option value="">请选择水表属性</option>
                                                </select>
                                                <select id="street" name="street"
                                                        style="margin-left: 5px;width: 159px; height: 34px;">
                                                    <option value="">所属乡镇或街道</option>
                                                </select>
                        <span class="input-group-btn">
                            <button type="button" id="btn_search" class="btn btn-purple btn-sm">
                                <span class="ace-icon fa fa-search icon-on-right bigger-110"></span>
                                搜索
                            </button>
                        </span>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row" style="">
                <div class="col-sm-12">
                    <div id="companyUseDAll" style="min-width:100%;max-width: 5000px;margin: 0 auto"></div>
                </div>
            </div>
            <div class="row" style="">
                <div class="col-sm-12">
                    <div id="companyUseD" style="min-width:100%;max-width: 5000px; margin: 0 auto"></div>
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
                headerFormat: '<span style="font-size:11px">{series.name}(<span style="color: red;">点击查看该月</span>)</span><br/>',
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
                            setOneMonth(date);
                        }
                    }
                }
            }]
        });
        var myDate = new Date();
        var time = myDate.toLocaleDateString().split('/').join('-');
        time = time.substr(0, time.lastIndexOf('-'));
        setOneMonth(time);
    })
    function setOneMonth(date) {
        console.log(date);
        $.get("${context_path}/report/month/chart/setOneMonth?date=" + date, function (data) {
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
                            window.location.href = encodeURI("${context_path}/report/month?companyName=" + companyName + "&date=" + date);
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
    function getDictMapData() {
        var submitData = {};
        $.post("${context_path}/dict/getSearchStatisUseDict", submitData, function (data) {
            var watersType = data.WatersType;
            for (var i = 0; i < watersType.length; i++) {
                $("#watersType").append("<option value='" + watersType[i].value + "'>" + watersType[i].name + "</option>");
            }
            var street = data.Street;
            for (var i = 0; i < street.length; i++) {
                $("#street").append("<option value='" + street[i].value + "'>" + street[i].name + "</option>");
            }
            var meterAttr = data.MeterAttr;
            for (var i = 0; i < meterAttr.length; i++) {
                $("#meterAttr").append("<option value='" + meterAttr[i].value + "'>" + meterAttr[i].name + "</option>");
            }
            var companyType = data.CompanyType;
            for (var i = 0; i < companyType.length; i++) {
                $("#type").append("<option value='" + companyType[i].value + "'>" + companyType[i].name + "</option>");
            }
            $("#watersType").val(${watersType});
            $("#street").val(${street});
            $("#meterAttr").val(${meterAttr});
            $("#type").val(${type});
        }, "json");
    }

    $(function () {
        getDictMapData();
    })
</script>
<jsp:include page="/WEB-INF/view/common/chartjs.jsp" flush="true"/>
</body>
</html>

