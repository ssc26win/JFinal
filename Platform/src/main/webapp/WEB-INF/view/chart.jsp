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
    .adbody{font:13px/180% Arial,Lucida,Verdana,"宋体",Helvetica,sans-serif;color:#333;background:#fff;}
    /* tipfloat */
    .tipfloat,.tipfloat .close{background:url('${res_url}img/tipright.png') no-repeat;}
    .tipfloat{display:none;z-index:999;position:fixed;_position:absolute;right:0px;bottom:0;width:236px;height:196px;overflow:hidden;}
    .tipfloat .tiphead{height:30px;line-height:30px;overflow:hidden;padding:0 5px;}
    .tipfloat .tiphead strong{float:left;color:#fff;font-size:14px;}
    .tipfloat .tiphead .close{display:block;float:right;margin:5px 0 0 0;width:18px;height:18px;line-height:999em;overflow:hidden;cursor:pointer;background-position:-236px 0;}
    /* ranklist */
    .ranklist{padding:10px 10px 10px 10px;}
    .ranklist li{height:16px;line-height:16px;overflow:hidden;position:relative;padding:0 70px 0 30px;margin:0 0 10px 0;vertical-align:bottom;}
    .ranklist li em{width:20px;height:16px;overflow:hidden;display:block;position:absolute;left:0;top:0;text-align:center;font-style:normal;color:#333;}
    .ranklist li em{background-position:0 -16px;}
    .ranklist li.top em{background-position:0 0;color:#fff;}
    .ranklist li .num{position:absolute;right:0;top:0;color:#999;}
</style>
<body class="no-skin">
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try{ace.settings.check('main-container' , 'fixed')}catch(e){}
    </script>
    <div class="main-content" id="page-wrapper">
        <div class="page-content" id="page-content">
            <div class="row">
                <div class="col-sm-6">
                    <div id="containerCompaniesTerm" style="width: 60%; height: 25%; margin: 0 auto"></div>
                </div>
                <div class="col-sm-6">
                    <div id="containerMetersTerm" style="width: 60%; height: 25%; margin: 0 auto"></div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-6">
                    <div id="containerCompany" style="width: 60%; height: 25%; margin: 0 auto"></div>
                </div>
                <div class="col-sm-6">
                    <div id="containerMeter" style="width: 60%; height: 25%; margin: 0 auto"></div>
                </div>
            </div>
            <div class="row" style="margin-top: 20px;">
                <div class="col-sm-6">
                    <div id="containerUseD" style="margin: 0 auto"></div>
                </div>
                <div class="col-sm-6">
                    <div id="containerUseM" style="margin: 0 auto"></div>
                </div>
            </div>
            <div class="row" style="margin-top: 20px;">
                <div class="col-sm-6">
                    <div id="containerSupplyD" style="margin: 0 auto"></div>
                </div>
                <div class="col-sm-6">
                    <div id="containerSupplyM" style="margin: 0 auto"></div>
                </div>
            </div>
            <div class="adbody" style="display: none;">
                <div class="tipfloat" style="display: block;">
                    <div class="tiphead"><strong id="adtitle"></strong><span title="关闭" class="close">关闭</span></div>
                    <div class="ranklist">
                        <p style="text-indent:2em;" id="adcontent"></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script language="JavaScript">
    $(document).ready(function () {
        $.get("${context_path}/chart/metersByTerm", function (data) {
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
                    events:{
                        click:function (e) {
                            location.href=e.point.url
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
                enabled:false // 禁用版权信息
            }
            var json1 = {};
            json1.chart = chart1;
            json1.title = title1;
            json1.tooltip = tooltip1;
            json1.series = series1;
            json1.plotOptions = plotOptions1;
            json1.credits=credits;
            $('#containerMetersTerm').highcharts(json1);
        })

        $.get("${context_path}/chart/companiesByTerm", function (data) {
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
                    events:{
                        click:function (e) {
                            location.href=e.point.url
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
                enabled:false // 禁用版权信息
            }
            var json1 = {};
            json1.chart = chart1;
            json1.title = title1;
            json1.tooltip = tooltip1;
            json1.series = series1;
            json1.plotOptions = plotOptions1;
            json1.credits=credits;
            $('#containerCompaniesTerm').highcharts(json1);
        })
        $.get("${context_path}/chart/meter", function (data) {
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
                    events:{
                        click:function (e) {
                            location.href=e.point.url
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
                    {name:'正常表' + '(' + normalTotal + ')', y:normalTotal,url:'${context_path}/basic/meter?flag=Normal'},
                    {name:'停用表' + '(' + stopTotal + ')', y:stopTotal,url:'${context_path}/basic/meter?flag=Stop'},
                    {name:'备用表' + '(' + exptionTotal + ')', y:exptionTotal,url:'${context_path}/basic/meter?flag=Exception'},
                    {name:'未启用表' + '(' + disableTotal + ')', y:disableTotal,url:'${context_path}/basic/meter?flag=Disable'},
                ]
            }];

            var credits = {
                enabled:false // 禁用版权信息
            }
            var json1 = {};
            json1.chart = chart1;
            json1.title = title1;
            json1.tooltip = tooltip1;
            json1.series = series1;
            json1.plotOptions = plotOptions1;
            json1.credits=credits;
            $('#containerMeter').highcharts(json1);
        })

        $.get("${context_path}/chart/company", function (data) {
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
                    events:{
                        click:function (e) {
                            location.href=e.point.url
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
                    //{name:'单位总数量' + '(' + total2 + ')', y:total2,url:'${context_path}/basic/company'},
                    {name:'用水单位' + '(' + normalTotal2 + ')', y:normalTotal2,url:'${context_path}/basic/company?flag=Normal'},
                    {name: warnTitle + '单位' + '(' + warnTotal2 + ')', y:warnTotal2,url:'${context_path}/basic/company?flag=Warn'},
                    {name:'供水单位' + '(' + supplyTotal + ')', y:supplyTotal,url:'${context_path}/basic/company?flag=Supply'},
                ]
            }];
            var credits = {
                enabled:false // 禁用版权信息
            }
            var json1 = {};
            json1.chart = chart1;
            json1.title = title1;
            json1.tooltip = tooltip1;
            json1.series = series1;
            json1.plotOptions = plotOptions1;
            json1.credits=credits;
            $('#containerCompany').highcharts(json1);
        })

        $.get("${context_path}/chart/getDaily", function (data) {
            var title = {
                text: ''
            };
            var subtitle = {
                text: '日用水量'
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
                    name: '日用水量',
                    data: data.sumWater
                }
            ];
            var plotOptions={
                spline: {
                    shadow: true,
                        animation: true,
                        lineWidth: 1
                },
                series: {
                    color: '#00b16a',
                        lineWidth:4,
                        cursor: 'pointer',
                        events: {
                        click: function(event) {

                         /*   alert(event.point.category); // X轴值
                            alert(this.data[event.point.x].y); // Y轴值*/
                            var time=event.point.category

                            window.location.href="${context_path}/statis/daily?time="+time + "&type=1";
                        }
                    }
                }
            };
            var credits = {
                enabled:false // 禁用版权信息
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
            json.credits=credits;
            $('#containerUseD').highcharts(json);
        });

        $.get("${context_path}/chart/getMonth", function (data) {
            var title = {
                text: ''
            };
            var subtitle = {
                text: '月用水量'
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
                    name: '月用水量',
                    data: data.sumWater
                }
            ];
            var plotOptions={
                spline: {
                    shadow: true,
                    animation: true,
                    lineWidth: 1
                },
                series: {
                    color: '#00b16a',
                    lineWidth:4,
                    cursor: 'pointer',
                    events: {
                        click: function(event) {
                            /*   alert(event.point.category); // X轴值
                             alert(this.data[event.point.x].y); // Y轴值*/
                            var time=event.point.category
                            window.location.href="${context_path}/statis/month?time="+time + "&type=1";
                        }
                    }
                }
            };
            var credits = {
                enabled:false // 禁用版权信息
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
            $('#containerUseM').highcharts(json);
        });
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
            var plotOptions={
                spline: {
                    shadow: true,
                    animation: true,
                    lineWidth: 1
                },
                series: {
                    color: '#00b16a',
                    lineWidth:4,
                    cursor: 'pointer',
                    events: {
                        click: function(event) {
                            /*   alert(event.point.category); // X轴值
                             alert(this.data[event.point.x].y); // Y轴值*/
                            var time=event.point.category

                            window.location.href="${context_path}/statis/daily?time=" + time + "&type=2";
                        }
                    }
                }
            };
            var credits = {
                enabled:false // 禁用版权信息
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
            json.credits=credits;
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
            var plotOptions={
                spline: {
                    shadow: true,
                    animation: true,
                    lineWidth: 1
                },
                series: {
                    color: '#00b16a',
                    lineWidth:4,
                    cursor: 'pointer',
                    events: {
                        click: function(event) {
                            /*   alert(event.point.category); // X轴值
                             alert(this.data[event.point.x].y); // Y轴值*/
                            var time=event.point.category
                            window.location.href="${context_path}/statis/month?time=" + time + "&type=2";
                        }
                    }
                }
            };
            var credits = {
                enabled:false // 禁用版权信息
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
    $(function(){
        var titHeight=$(".tiphead").height();
        $(".tipfloat").animate({height:"show"}, 500);
        $(".close").click(function(){
            $(".tipfloat").animate({height:titHeight-50},1000,function(){
                $(".tipfloat").hide();
            });
        });
    });
    function getMsgData(){
        var submitData = {};
        $.post("${context_path}/chart/getNewsMsg", submitData, function(data) {
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
        },"json");
    }
    $(function(){
        getMsgData();
    })
</script>
</body>
</html>
