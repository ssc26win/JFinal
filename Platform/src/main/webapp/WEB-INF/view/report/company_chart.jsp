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
                                        <form id="exportForm" action="${context_path}/report/company/chart"
                                              method="post">
                                            <input type="text" style="display:none"/>

                                            <div class="input-group">
                                                日期:
                                                <input type="text" id="startTime" name="startTime" value="${startTime}"
                                                       class="form_date" style="width: 90px;"/>~<input type="text" id="endTime" name="endTime" value="${endTime}"
                                                       class="form_date" style="width: 90px;"/>
                                                <select id="type" name="type"
                                                        style="margin-left: 5px;width: 110px;height: 34px;">
                                                    <option value="">选择单位类型</option>
                                                </select>
                                                <input type="text" id="name" name="name" value="${name}" class="" placeholder="输入单位名称"
                                                       style="margin-left: 5px;width: 120px;"/>
                                                <input type="text" id="innerCode" name="innerCode" value="${innerCode}" class=""
                                                       placeholder="输入单位编号" style="margin-left: 5px;width: 120px;"/>
                                                <select id="watersType" name="watersType"
                                                        style="margin-left: 5px;width: 110px; height: 34px;">
                                                    <option value="">选择水源类型</option>
                                                </select>
                                                <select id="meterAttr" name="meterAttr"
                                                        style="margin-left: 5px;width: 110px; height: 34px;">
                                                    <option value="">选择水表属性</option>
                                                </select>
                                                <select id="street" name="street"
                                                        style="margin-left: 5px;width: 140px; height: 34px;">
                                                    <option value="">所属乡镇或街道</option>
                                                </select>
                                                <span class="input-group-btn">
                                                    <button type="submit" id="btn_search" class="btn btn-purple btn-xs">
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
            <div class="row">
                <div class="col-sm-12">
                    <div id="streetUse" style="min-width: ${widthSum}px; height: 400px; margin: 0 auto"></div>
                </div>
                <div class="col-sm-12">
                    <div id="meterAttrUse" style="min-width: ${widthSum}px; height: 400px; margin: 0 auto"></div>
                </div>
            </div>
            <div class="row" style="text-align: center; margin-top: 100px;">
                <div class="col-sm-6">
                    <div id="watersTypeUse" style="text-align: center;height: 400px;margin: 0 auto"></div>
                </div>
                <div class="col-sm-6"></div>
            </div>
        </div>
    </div>
</div>
<script language="JavaScript">
    $(document).ready(function () {
        getDictMapData();
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
                    text: '各单位总的用水量.单位（m³）'
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
                name: '所属单位',
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
                    text: '水量单位（m³）'
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
                        var url = "${context_path}/report/company?meterAttrName=" + event.point.category;
                        var street = $("#street").val();
                        var watersType = $("#watersType").val();
                        var type = $("#type").val();

                        var name = $("#name").val();
                        var innerCode = $("#innerCode").val();
                        var startTime = $("#startTime").val();
                        var endTime = $("#endTime").val();
                        var meterAttr = $("#meterAttr").val();

                        if (street != "" && street != undefined) {
                            url = url + "&street=" + street;
                        }
                        if (watersType != "" && watersType != undefined) {
                            url = url + "&watersType=" + watersType;
                        }
                        if (type != "" && type != undefined) {
                            url = url + "&type=" + type;
                        }
                        if (name != "" && name != undefined) {
                            url = url + "&name=" + name;
                        }
                        if (innerCode != "" && innerCode != undefined) {
                            url = url + "&innerCode=" + innerCode;
                        }
                        if (startTime != "" && startTime != undefined) {
                            url = url + "&startTime=" + startTime;
                        }
                        if (endTime != "" && endTime != undefined) {
                            url = url + "&endTime=" + endTime;
                        }
                        if (meterAttr != "" && meterAttr != undefined) {
                            url = url + "&meterAttr=" + meterAttr;
                        }
                        window.location.href = encodeURI(url);
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
                            //window.location.href = encodeURI(e.point.url);
                            //parent.location.href = encodeURI(e.point.url);
                            //parent.location.reload();
                            var url = e.point.url;
                            var street = $("#street").val();
                            var watersType = $("#watersType").val();
                            var type = $("#type").val();

                            var name = $("#name").val();
                            var innerCode = $("#innerCode").val();
                            var startTime = $("#startTime").val();
                            var endTime = $("#endTime").val();
                            var meterAttr = $("#meterAttr").val();

                            if (street != "" && street != undefined) {
                                url = url + "&street=" + street;
                            }
                            //if (watersType != "" && watersType != undefined) {
                            //    url = url + "&watersType=" + watersType;
                            //}
                            if (type != "" && type != undefined) {
                                url = url + "&type=" + type;
                            }
                            if (name != "" && name != undefined) {
                                url = url + "&name=" + name;
                            }
                            if (innerCode != "" && innerCode != undefined) {
                                url = url + "&innerCode=" + innerCode;
                            }
                            if (startTime != "" && startTime != undefined) {
                                url = url + "&startTime=" + startTime;
                            }
                            if (endTime != "" && endTime != undefined) {
                                url = url + "&endTime=" + endTime;
                            }
                            if (meterAttr != "" && meterAttr != undefined) {
                                url = url + "&meterAttr=" + meterAttr;
                            }
                            window.location.href = encodeURI(url);
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
            for(var i = 0;i<companyType.length;i++) {
                $("#type").append("<option value='" + companyType[i].value + "'>"+companyType[i].name+"</option>");
            }
            if ('${watersType}' != '') {
                $("#watersType").val(${watersType});
            }
            if ('${street}' != '') {
                $("#street").val(${street});
            }
            if ('${meterAttr}' != '') {
                $("#meterAttr").val(${meterAttr});
            }
            if ('${type}' != '') {
                $("#type").val(${type});
            }
        }, "json");
    }

</script>
<jsp:include page="/WEB-INF/view/common/chartjs.jsp" flush="true"/>
</body>
</html>
