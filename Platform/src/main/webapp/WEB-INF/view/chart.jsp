<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/8/26
  Time: 15:38
  To change this template use File | Settings | File Templates.
--%>
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
<body class="no-skin">
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try{ace.settings.check('main-container' , 'fixed')}catch(e){}
    </script>
    <div class="main-content" id="page-wrapper">
        <div class="page-content" id="page-content">
            <div class="row">
                <div class="col-xs-6">
                    <div id="container1" style="width: 45%; height: 25%; margin: 0 auto"></div>
                </div>
                <div class="col-xs-6">
                    <div id="container12" style="width: 45%; height: 25%; margin: 0 auto"></div>
                </div>
            </div>
            <div class="row" style="margin-top: 20px;">
                <div class="col-xs-12">
                    <div id="container" style="width: 50%; height: 35%; margin: 0 auto"></div>
                </div>
                <div class="col-xs-12">
                    <div id="containe2" style="width: 50%; height: 35%; margin: 0 auto"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<script language="JavaScript">
    $(document).ready(function () {
        var total, normalTotal, exptionTotal;
        $.get("${context_path}/chart", function (data) {
            total = data.total;
            normalTotal = data.normalTotal;
            exptionTotal = data.exptionTotal;
            var chart1 = {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
            };
            var title1 = {
                text: '远传水表数量（共：' + total + ' 按比例查看）'
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
                    {name:'正常表数量' + '(' + normalTotal + ')', y:normalTotal,url:'${context_path}/basic/meter/normal'},
                    {name:'异常表数量' + '(' + exptionTotal + ')', y:exptionTotal,url:'${context_path}/basic/meter/exption'},
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
            $('#container1').highcharts(json1);
        })

        var total2, warnTotal2, normalTotal2, otherTotal;
        $.get("${context_path}/chart/company", function (data) {
            total2 = data.total;
            warnTotal2 = data.warnTotal;
            normalTotal2 = data.normalTotal;
            otherTotal = data.otherTotal;
            var chart1 = {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            };
            var title1 = {
                text: '单位数量（共：' + total2 + ' 按比例查看）'
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
                    {name:'正常单位数量' + '(' + normalTotal2 + ')', y:normalTotal2,url:'${context_path}/basic/company/normal'},
                    {name:'预警单位数量' + '(' + warnTotal2 + ')', y:warnTotal2,url:'${context_path}/basic/company/warn'},
                    {name:'其他' + '(' + otherTotal + ')', y:otherTotal,url:'${context_path}/basic/company/other'},
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
            $('#container12').highcharts(json1);
        })

        $.get("${context_path}/chart/getDilay", function (data) {
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

                            window.location.href="${context_path}/statis/daily?time="+time;

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
            $('#container').highcharts(json);

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

                            window.location.href="${context_path}/statis/month?time="+time;


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
            $('#containe2').highcharts(json);

        });

    });
//    $(function(){
//        var obj = $(".highcharts-plot-lines-0").firstChild;
//        $(obj).attr("stroke","");
//    })
</script>
</body>
</html>
