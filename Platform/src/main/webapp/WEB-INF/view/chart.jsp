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
    <meta charset="UTF-8"/>
    <title>Highcharts 教程 | 菜鸟教程(runoob.com)</title>
    <script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
    <script src="https://code.highcharts.com/highcharts.js"></script>
</head>
<body>
<div id="container1" style="width: 550px; height: 400px; margin: 0 auto"></div>
<div id="container" style="width: 550px; height: 400px; margin: 0 auto"></div>
<script language="JavaScript">

    $(document).ready(function () {
        var total, warnTotal, exptionTotal;
        $.get("${context_path}/chart", function (data) {
            total = data.total;
            warnTotal = data.warnTotal;
            exptionTotal = data.exptionTotal;

            var chart1 = {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            };
            var title1 = {
                text: '水表数量'
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
                name: '水表数量',
                data: [
                    {name:'远传水表数量', y:total,url:''},
                    {name:'异常表数量', y:warnTotal,url:''},

                {name:'预警单位数量', y:exptionTotal,url:''},
                ]
            }];

            var json1 = {};
            json1.chart = chart1;
            json1.title = title1;
            json1.tooltip = tooltip1;
            json1.series = series1;
            json1.plotOptions = plotOptions1;
            $('#container1').highcharts(json1);
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
                    text: '水量单位'
                },
                plotLines: [{
                    value: 0,
                    width: 10,
                    color: '#808080'
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

            var json = {};

            json.title = title;
            json.subtitle = subtitle;
            json.xAxis = xAxis;
            json.yAxis = yAxis;
            json.tooltip = tooltip;
            json.legend = legend;
            json.series = series;
            json.plotOptions = plotOptions;
            $('#container').highcharts(json);

        });



    });
</script>
</body>
</html>
