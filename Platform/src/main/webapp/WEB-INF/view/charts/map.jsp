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
<style type="text/css">
    html {
        overflow-x: hidden;
        overflow-y: hidden;
    }
    #searchResultPanel {
        border:1px solid #C0C0C0;
        height:auto;
        display:none;
    }
</style>
</head>
<body>
<div class="main-container" id="main-container">
    <%--<div class="breadcrumbs" id="breadcrumbs">
        <script type="text/javascript">
            try {
                ace.settings.check('breadcrumbs', 'fixed')
            } catch (e) {
            }
        </script>

        <ul class="breadcrumb">
            <li>
                <i class="icon-home home-icon"></i>
                <a href="#">首页</a>
            </li>
            <li class="active">导航地图</li>
        </ul><!-- .breadcrumb -->

        <div class="nav-search" id="nav-search">
            <form class="form-search">
                    <span class="input-icon">
                        <div class="no-margin-right">
                            <label style="margin-right: 10px;">
                                <input type="radio" id="all" name="maptype" value="0" checked/> 全部
                            </label>
                            <label style="margin-right: 10px;">
                                <input type="radio" id="normal" name="maptype" value="1"/> 正常单位
                            </label>
                            <label style="margin-right: 10px;">
                                <input type="radio" id="warn" name="maptype" value="2"/> <span style="color: red">预警单位</span>
                            </label>
                            <input type="button" class="btn btn-primary btn-xs" value="搜索" onclick="findCompanyMap();">
                        </div>
                    </span>
            </form>
        </div><!-- #nav-search -->
<script type="text/javascript">
    $(function(){
        if ('${type}' != '') {
            $("input[name='maptype']").each(function(){
                if($(this).val()=='${type}') {
                    $(this).attr("checked",true);
                }
            });
        }
    })
    function findCompanyMap() {
        var type = $("input[name='maptype']:checked").val();
        window.location.href = "${context_path}/#/chart/baiduMap?type="+ type;
    }
</script>
    </div>--%>
    <script type="text/javascript">
        try{ace.settings.check('main-container' , 'fixed')}catch(e){}
    </script>
    <div class="main-content" id="page-wrapper">
        <%--<div class="row">
            <div class="col-sm-12">
                <div class="input-group" style="margin-right: 2px;">
                    <span class="input-group-addon">
                        <i class="ace-icon fa fa-check"></i>
                    </span>
                    <input type="text" id="suggestId" name="suggestId" class="form-control search-query" placeholder="请输入关键字" />
                    <div id="searchResultPanel"></div>
                </div>
            </div>
        </div>--%>
        <div class="row" style="margin-top: 1px">
            <div class="col-sm-12">
                <div style="height:0px;padding-bottom:47%;" id="map"></div>
            </div>
        </div>
    </div>
</div>
<script src="${res_url}js/charts/jquery.min.js"></script>
<script type="text/javascript">
    var map;
    var myValue;
    // 百度地图API功能
    function G(id) {
        return document.getElementById(id);
    }
    function setPlace(){
        map.clearOverlays();    //清除地图上所有覆盖物
        function myFun(){
            var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
            map.centerAndZoom(pp, 18);
            map.addOverlay(new BMap.Marker(pp));    //添加标注
        }
        //智能搜索
        var local = new BMap.LocalSearch(map, {onSearchComplete: myFun});
        local.search(myValue);
    }
    var markerArr;
    markerArr = JSON.parse('${companys}');
    var longitude = 116.66321;
    var latitude = 39.91598;
    var position = '${position}';
    if (position!='') {
        longitude = parseFloat(position.split(",")[0]);
        latitude = parseFloat(position.split(",")[1].toString());
    }
    function map_init() {
        //alert(JSON.stringify(markerArr));
        map = new BMap.Map("map"); // 创建Map实例
        var point = new BMap.Point(longitude, latitude); //地图中心点
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
            var state = markerArr[i].state;
            var p0 = markerArr[i].longitude; //
            var p1 = markerArr[i].latitude; //按照原数组的point格式将地图点坐标的经纬度分别提出来
            point[i] = new window.BMap.Point(p0, p1); //循环生成新的地图点

            //创建标注对象并添加到地图
            //var myIcon = new BMap.Icon("${context_path}/${res_url}img/d6.png", new BMap.Size(23, 50), {offset: new BMap.Size(10, 25)});
            marker[i] = new window.BMap.Marker(point[i]); //按照地图点坐标生成标记
            map.addOverlay(marker[i]);
            //marker[i].setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
            //显示marker的title，marker多的话可以注释掉
            var title = "<span style='font-size:10px;color: #0CC415;' title='立方米'>用水量: <b>"+markerArr[i].waterUseNum+" m³</b></span>";
            if (state == 1) {
                title = "<span style='font-size:10px;color: #FFD306;' title='立方米'>用水量: <b>"+markerArr[i].waterUseNum+" m³</b></span>";
            } else if (state == 2) {
                title = "<span style='font-size:10px;color: #FF0000;' title='立方米'>用水量: <b>"+markerArr[i].waterUseNum+" m³</b></span>";
            }
            if (markerArr[i].waterUseNum == 0) {
                title = "<span style='font-size:10px;color: #FF8040;' title='立方米'>用水量: <b>"+markerArr[i].waterUseNum+" m³</b></span>";
            }
            var label = new window.BMap.Label(title, {
                offset : new window.BMap.Size(20, -10)
            });
            marker[i].setLabel(label);
            // 创建信息窗口对象
            if (state == 1) {
                info[i] = "<p style='font-size:10px;padding-left: 10px;'>" + "</br>单位名称：" + markerArr[i].name + "</br>" + "</br>单位地址：" + markerArr[i].address + "</p>";
            } else if (state == 2) {
                info[i] = "<p style='font-size:10px;padding-left: 10px;'>" + "</br>单位名称：" + markerArr[i].name + "</br>" + "</br>单位地址：" + markerArr[i].address + "</p>";
            } else {
                info[i] = "<p style='font-size:10px;padding-left: 10px;'>" + "</br>单位名称：" + markerArr[i].name + "</br>" + "</br>单位地址：" + markerArr[i].address + "</p>";
            }
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
        /*//建立一个自动完成的对象
        var ac = new BMap.Autocomplete({"input" : "suggestId", "location" : map});

        ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
            var str = "";
            var _value = e.fromitem.value;
            var value = "";
            if (e.fromitem.index > -1) {
                value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
            }
            str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

            value = "";
            if (e.toitem.index > -1) {
                _value = e.toitem.value;
                value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
            }
            str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
            G("searchResultPanel").innerHTML = str;
        });

        ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
            var _value = e.item.value;
            myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
            G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
            setPlace();
        });*/
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