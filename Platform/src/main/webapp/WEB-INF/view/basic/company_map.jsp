<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>单位地址导航</title>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=IDvNBsejl9oqMbPF316iKsXR"></script>
<%--<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.js"></script>--%>
<script type="text/javascript" src="${res_url}map/SearchInfoWindow_min.js"></script>
<jsp:include page="/WEB-INF/view/common/basecss.jsp" flush="true" />
<link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.css" />
<style>

</style>
</head>
<body>
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try{ace.settings.check('main-container' , 'fixed')}catch(e){}
    </script>
    <div class="main-content" id="page-wrapper">
        <div class="row">
            <div class="col-sm-12">
                <div style="height:0px;padding-bottom:60%;" id="map"></div>
            </div>
        </div>
    </div>
</div>
<script src="${res_url}js/charts/jquery.min.js"></script>
<script src="${res_url}js/layer/layer.js"></script>
<script type="text/javascript">
    // 百度地图API功能
    var map = new BMap.Map("map");
    map.centerAndZoom(new BMap.Point(116.657140, 39.909982), 15);
    map.enableScrollWheelZoom(true);
    map.addEventListener("rightclick", function(e){
        rightclickPoint = e.point.lng +"," + e.point.lat;
        layer.msg("标记此位置" + "(" + rightclickPoint + ")",{
            icon: 1,
            time: 2000
        },function(){
            var exp = new Date();
            exp.setTime(exp.getTime() + 1*60*60*1000);
            document.cookie = "P_position="+ escape (rightclickPoint) + ";expires=" + exp.toGMTString();
        });

    });
    var local = new BMap.LocalSearch(map, {
        renderOptions:{map: map}
    });
    local.search('${address}');
    //北京市通州区后南仓12号
    function closeView(){
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    }

</script>
</body>
</html>
