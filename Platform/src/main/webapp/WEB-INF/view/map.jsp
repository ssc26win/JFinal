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
                <div style="height:0px;padding-bottom:48%;" id="map"></div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    var markerArr;
    markerArr = JSON.parse('${companys}');
    function map_init() {
        var map = new BMap.Map("map"); // 创建Map实例
        var point = new BMap.Point(116.657140, 39.909982); //地图中心点
        map.centerAndZoom(point, 15); // 初始化地图,设置中心点坐标和地图级别。
        map.enableScrollWheelZoom(true); //启用滚轮放大缩小
        //地图、卫星、混合模式切换
        map.addControl(new BMap.MapTypeControl({
            mapTypes : [ BMAP_NORMAL_MAP, BMAP_SATELLITE_MAP,
                BMAP_HYBRID_MAP ]
        }));
        //向地图中添加缩放控件
        var ctrlNav = new window.BMap.NavigationControl({
            anchor : BMAP_ANCHOR_TOP_LEFT,
            type : BMAP_NAVIGATION_CONTROL_LARGE
        });
        map.addControl(ctrlNav);
        //向地图中添加缩略图控件
        var ctrlOve = new window.BMap.OverviewMapControl({
            anchor : BMAP_ANCHOR_BOTTOM_RIGHT,
            isOpen : 1
        });
        map.addControl(ctrlOve);
        //向地图中添加比例尺控件
        var ctrlSca = new window.BMap.ScaleControl({
            anchor : BMAP_ANCHOR_BOTTOM_LEFT
        });
        map.addControl(ctrlSca);
        var point = new Array(); //存放标注点经纬信息的数组
        var marker = new Array(); //存放标注点对象的数组
        var info = new Array(); //存放提示信息窗口对象的数组
        var searchInfoWindow = new Array();//存放检索信息窗口对象的数组
        for (var i = 0; i < markerArr.length; i++) {
            var p0 = markerArr[i].longitude; //
            var p1 = markerArr[i].latitude; //按照原数组的point格式将地图点坐标的经纬度分别提出来
            point[i] = new window.BMap.Point(p0, p1); //循环生成新的地图点
            marker[i] = new window.BMap.Marker(point[i]); //按照地图点坐标生成标记
            map.addOverlay(marker[i]);
            marker[i].setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
            //显示marker的title，marker多的话可以注释掉
            var label = new window.BMap.Label("<span style='font-size:12px;'><b>"+markerArr[i].name+"</b></span>", {
                offset : new window.BMap.Size(20, -10)
            });
            marker[i].setLabel(label);
            // 创建信息窗口对象
            info[i] = "<p style='font-size:15px;lineheight:1.8em;padding-left: 10px;'>"
                    + "</br>用水量：" + markerArr[i].waterUseNum + "（立方米）</br>"
                    + "</br>单位地址：" + markerArr[i].address + "</p>";
            //创建百度样式检索信息窗口对象
            searchInfoWindow[i] = new BMapLib.SearchInfoWindow(map,
                    info[i], {
                        title : "<h5>"+markerArr[i].name+"</h5>", //标题
                        width : 350, //宽度
                        height : 80, //高度
                        panel : "panel", //检索结果面板
                        enableAutoPan : true, //自动平移
                        searchTypes : [
                            BMAPLIB_TAB_SEARCH, //周边检索
                            BMAPLIB_TAB_TO_HERE, //到这里去
                            BMAPLIB_TAB_FROM_HERE //从这里出发
                        ]
                    });
            //添加点击事件
            marker[i].addEventListener("click", (function(k) {
                // js 闭包
                return function() {
                    //将被点击marker置为中心
                    map.centerAndZoom(point[k], 15);
                    //在marker上打开检索信息窗口
                    searchInfoWindow[k].open(marker[k]);
                }
            })(i));
        }
    }
    //异步调用百度js
    function map_load() {
        var load = document.createElement("script");
        load.src = "http://api.map.baidu.com/api?v=2.0&ak=IDvNBsejl9oqMbPF316iKsXR&callback=map_init";
        document.body.appendChild(load);
    }
    window.onload = map_load;
</script>
</body>
</html>
