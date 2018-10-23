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
      <div class="row" style="margin-top: 20px;">
        <div class="col-sm-12">
          <div id="companyUseD" style="margin: 0 auto"></div>
        </div>
        <div class="col-sm-12">
          <div id="companyUseM" style="margin: 0 auto"></div>
        </div>
      </div>
    </div>
  </div>
</div>
<script language="JavaScript">
  $(document).ready(function () {
    $.get("${context_path}/chart/companyWaterLine/getCPADaily", function (data) {
      var title = {
        text: ''
      };
      var subtitle = {
        text: data.subtitle
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
          name: data.seriesName,
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

              window.location.href = "${context_path}/statis/cpadaily?time=" + time + "&type=" + data.type;
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

    $.get("${context_path}/chart/companyWaterLine/getCPAMonth", function (data) {
      var title = {
        text: ''
      };
      var subtitle = {
        text:  data.subtitle
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
          name: data.seriesName,
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
              window.location.href = "${context_path}/statis/cpamonth?time=" + time + "&type=" + data.type;
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
      $('#companyUseM').highcharts(json);
    });
  })
</script>
</body>
</html>
