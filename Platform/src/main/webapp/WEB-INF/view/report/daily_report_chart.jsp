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
      <div class="row" style="margin-top: 20px;">
        <div class="col-sm-12">
          <div id="companyUseD" style="margin: 0 auto"></div>
        </div>
      </div>
    </div>
  </div>
</div>
<script language="JavaScript">
  var chart = Highcharts.chart('companyUseD', {
    title: {
      text: '${companyTitle}'
    },
    subtitle: {
      text: '数据来源：通州节水办'
    },
    xAxis: {
      categories: JSON.parse('${days}')
    },
    yAxis: {
      title: {
        text: '用水量'
      }
    },
    legend: {
      layout: 'vertical',
      align: 'right',
      verticalAlign: 'middle'
    },
    plotOptions: {
      series: {
        label: {
          connectorAllowed: false
        },
        pointStart: 2010
      }
    },
    series: [{
      name: '安装，实施人员',
      data: [43934, 52503, 57177, 69658, 97031, 119931, 137133, 154175]
    }, {
      name: '工人',
      data: [24916, 24064, 29742, 29851, 32490, 30282, 38121, 40434]
    }, {
      name: '销售',
      data: [11744, 17722, 16005, 19771, 20185, 24377, 32147, 39387]
    }, {
      name: '项目开发',
      data: [null, null, 7988, 12169, 15112, 22452, 34400, 34227]
    }, {
      name: '其他',
      data: [12908, 5948, 8105, 11248, 8989, 11816, 18274, 18111]
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
</script>
</body>
</html>

