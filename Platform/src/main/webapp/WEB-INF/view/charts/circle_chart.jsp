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
        overflow-x: hidden;
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
                <div class="col-sm-6">
                    <div id="containerCompaniesTerm" style="margin: 0 auto"></div>
                </div>
                <div class="col-sm-6">
                    <div id="containerMetersTerm" style="margin: 0 auto"></div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-6">
                    <div id="containerCompany" style="margin: 0 auto"></div>
                </div>
                <div class="col-sm-6">
                    <div id="containerMeter" style="margin: 0 auto"></div>
                </div>
            </div>
            <%--<div class="adbody" style="display: none;">
                <div class="tipfloat" style="display: block;">
                    <div class="tiphead"><strong id="adtitle"></strong><span title="关闭" class="close">关闭</span></div>
                    <div class="ranklist">
                        <p style="text-indent:2em;" id="adcontent"></p>
                    </div>
                </div>
            </div>--%>
        </div>
    </div>
</div>
<script language="JavaScript">
    $(document).ready(function () {
        $.get("${context_path}/chart/circleStatus/metersByTerm", function (data) {
            var chart1 = {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            };
            var title1 = {
                text: '水表总数量（' + data.total + '）'
            };
            var tooltip1 = {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}</b>'
            };
            var plotOptions1 = {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    events: {
                        click: function (e) {
                            location.href = e.point.url
                        }
                    },
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                }
            };
            var series1 = [{
                type: 'pie',
                name: '水表数量（%）',
                data: data.MeterTermSerArray
            }];

            var credits = {
                enabled: false // 禁用版权信息
            }
            var json1 = {};
            json1.chart = chart1;
            json1.title = title1;
            json1.tooltip = tooltip1;
            json1.series = series1;
            json1.plotOptions = plotOptions1;
            json1.credits = credits;
            $('#containerMetersTerm').highcharts(json1);
        })

        $.get("${context_path}/chart/circleStatus/companiesByTerm", function (data) {
            var chart1 = {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            };
            var title1 = {
                text: '单位总数量（' + data.total + '）'
            };
            var tooltip1 = {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}</b>'
            };
            var plotOptions1 = {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    events: {
                        click: function (e) {
                            location.href = e.point.url
                        }
                    },
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                }
            };
            var series1 = [{
                type: 'pie',
                name: '单位数量（%）',
                data: data.CompanyTermSerArray
            }];
            var credits = {
                enabled: false // 禁用版权信息
            }
            var json1 = {};
            json1.chart = chart1;
            json1.title = title1;
            json1.tooltip = tooltip1;
            json1.series = series1;
            json1.plotOptions = plotOptions1;
            json1.credits = credits;
            $('#containerCompaniesTerm').highcharts(json1);
        })
        $.get("${context_path}/chart/circleStatus/meter", function (data) {
            var total = data.total;
            var normalTotal = data.normalTotal;
            var exptionTotal = data.exptionTotal;
            var stopTotal = data.stopTotal;
            var disableTotal = data.disableTotal;
            var chart1 = {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            };
            var title1 = {
                text: '水表总数量（' + total + '）'
            };
            var tooltip1 = {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}</b>'
            };
            var plotOptions1 = {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    events: {
                        click: function (e) {
                            location.href = e.point.url
                        }
                    },
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                }
            };
            var series1 = [{
                type: 'pie',
                name: '水表数量（%）',
                data: [
                    //{name:'远传水表总数量' + '(' + total + ')', y:total,url:'${context_path}/basic/meter'},
                    {
                        name: '正常表' + '(' + normalTotal + ')',
                        y: normalTotal,
                        url: '${context_path}/basic/meter?flag=Normal'
                    },
                    {name: '停用表' + '(' + stopTotal + ')', y: stopTotal, url: '${context_path}/basic/meter?flag=Stop'},
                    {
                        name: '异常表' + '(' + exptionTotal + ')',
                        y: exptionTotal,
                        url: '${context_path}/basic/meter?flag=Exception'
                    },
                    {
                        name: '未启用表' + '(' + disableTotal + ')',
                        y: disableTotal,
                        url: '${context_path}/basic/meter?flag=Disable'
                    },
                ]
            }];

            var credits = {
                enabled: false // 禁用版权信息
            }
            var json1 = {};
            json1.chart = chart1;
            json1.title = title1;
            json1.tooltip = tooltip1;
            json1.series = series1;
            json1.plotOptions = plotOptions1;
            json1.credits = credits;
            $('#containerMeter').highcharts(json1);
        });

        $.get("${context_path}/chart/circleStatus/company", function (data) {
            var chart1 = {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            };
            var title1 = {
                text: data.totalTitle
            };
            var tooltip1 = {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}</b>'
            };
            var plotOptions1 = {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    events: {
                        click: function (e) {
                            location.href = e.point.url
                        }
                    },
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                }
            };
            var series1 = [{
                type: 'pie',
                name: '单位数量（%）',
                data:  data.seriesDataArray
            }];
            var credits = {
                enabled: false // 禁用版权信息
            }
            var json1 = {};
            json1.chart = chart1;
            json1.title = title1;
            json1.tooltip = tooltip1;
            json1.series = series1;
            json1.plotOptions = plotOptions1;
            json1.credits = credits;
            $('#containerCompany').highcharts(json1);
        })
    })
        //暂时废弃
       /* $.get("/chart/circleStatus/company", function (data) {
            var total2 = data.total;
            var warnTotal2 = data.warnTotal;
            var normalTotal2 = data.normalTotal;
            var supplyTotal = data.supplyTotal;
            var warnTitle = data.warnTitle;

            var chart1 = {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            };
            var title1 = {
                text: '单位总数量（' + total2 + '）'
            };
            var tooltip1 = {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}</b>'
            };
            var plotOptions1 = {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    events: {
                        click: function (e) {
                            location.href = e.point.url
                        }
                    },
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                }
            };
            var series1 = [{
                type: 'pie',
                name: '单位数量（%）',
                data: [
                    //{name:'单位总数量' + '(' + total2 + ')', y:total2,url:'/basic/company'},
                    {
                        name: '用水单位' + '(' + normalTotal2 + ')',
                        y: normalTotal2,
                        url: '/basic/company?flag=Normal'
                    },
                    {
                        name: warnTitle + '单位' + '(' + warnTotal2 + ')',
                        y: warnTotal2,
                        url: '/basic/company?flag=Warn'
                    },
                    {
                        name: '供水单位' + '(' + supplyTotal + ')',
                        y: supplyTotal,
                        url: '/basic/company?flag=Supply'
                    },
                ]
            }];
            var credits = {
                enabled: false // 禁用版权信息
            }
            var json1 = {};
            json1.chart = chart1;
            json1.title = title1;
            json1.tooltip = tooltip1;
            json1.series = series1;
            json1.plotOptions = plotOptions1;
            json1.credits = credits;
            $('#containerCompany').highcharts(json1);
        })
    })*/
    /*$(function () {
        var titHeight = $(".tiphead").height();
        $(".tipfloat").animate({height: "show"}, 500);
        $(".close").click(function () {
            $(".tipfloat").animate({height: titHeight - 50}, 1000, function () {
                $(".tipfloat").hide();
            });
        });
    });
   function getMsgData() {
        var submitData = {};
        $.post("/chart/getNewsMsg", submitData, function (data) {
            var title = data.title;
            var content = data.content;
            if (title == "" || title == undefined || content == "" || content == undefined) {
                $(".tipfloat").hide();
            } else {
                $("#adtitle").text(title);
                $("#adtitle").attr("title", title);
                $("#adcontent").text(content);
                $("#adcontent").attr("title", content);
                $(".adbody").show();
            }
        }, "json");
    }
    $(function () {
        getMsgData();
    })*/
</script>
</body>
</html>
