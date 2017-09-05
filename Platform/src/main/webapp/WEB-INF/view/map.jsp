<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>单位地址导航</title>
    <meta name="description" content="overview &amp; stats" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <!-- bootstrap & fontawesome -->
    <jsp:include page="/WEB-INF/view/common/basecss.jsp" flush="true" />
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=e3ZohdqyB0RL98hFOiC29xqh"></script>
    <script src="${res_url}js/map/jquery-latest.js"></script>
    <style>
        #mapbox {
            width: 100%;
            height: 100%;
            margin: 0;
            padding: 0;
        }

        #mapbox #map {
            height:0px;
            padding-bottom:50%;
        }

        #mapbox #results {
            height:0px;
            padding-bottom:50%;
            float: right;
        }

    </style>
</head>
<body class="no-skin">
    <!-- /section:basics/navbar.layout -->
    <div class="main-container" id="main-container">
        <script type="text/javascript">
            try{ace.settings.check('main-container' , 'fixed')}catch(e){}
        </script>
        <div class="main-content" id="page-wrapper">
        <%--<h2 onmousedown="drag(this.parentNode,event)">百度地图<a href="javascript:" onclick="document.getElementById('map').style.display='none'" class="close">×</a></h2>--%>
        <div id="mapbox" class="row">
            <div id="map" style="cursor: crosshair;" class="col-sm-9"></div>
            <div class="col-sm-3">
                <div class="widget-body">
                    <div class="widget-main">
                        <div class="input-group">
                            <input type="text" id="searchtext" name="searchtext" class="form-control search-query" placeholder="请输入关键字" value="${address}"/>
                            <span class="input-group-btn">
                                <button type="button" id="searchbt" class="btn btn-purple btn-sm" onclick="searchlocal($('#searchtext').val(),'',0)">
                                    <span class="ace-icon fa fa-search icon-on-right bigger-110"></span>
                                    搜索
                                </button>
                            </span>
                        </div>
                        <%--<div id="mapx" style="margin-top: 5px;"></div>
                        <div id="mapy" style="margin-top: 5px;"></div>
                        <div id="level" style="margin-top: 5px;"></div>--%>
                        <div class="input-group">
                            <div id="results" style="margin-top: 5px; font-size: 12px;padding: 0;"></div>
                        </div>
                        <%--<div class="input-group">--%>
                            <%--<div id="mapx" style="margin-top: 5px;"></div>--%>
                            <%--<div id="mapy" style="margin-top: 5px;"></div>--%>
                            <%--<div id="level" style="margin-top: 5px;"></div>--%>
                        <%--</div>--%>
                    </div>
                </div>
            </div>
        </div>
        </div>
    </div>
<script type="text/javascript">
    var company = '${company}';
    var address = '${address}';
    var waterUseNum = '${waterUseNum}';
    $(function(){
        searchlocal(company,address,waterUseNum);
    });

    //显示一个对象的所有属性
    function showAtrributes(event) {
        var out = '';
        for (var p in event) {
            if (typeof(p) != "function") {
                out += p + "=" + event[p] + "  ";
            }
        }
        alert(out);
    }
    var key = 'F4bfb7ec82f386cf8541158ad5801138';
    var map = new BMap.Map("map"); // 创建地图实例
    var point = new BMap.Point(116.404, 39.915); // 创建点坐标
    map.centerAndZoom(point, 15); // 初始化地图，设置中心点坐标和地图级别

    /*
     1.
     NavigationControl：地图平移缩放控件，默认位于地图左上方，它包含控制地图的平移和缩放的功能。
     OverviewMapControl：缩略地图控件，默认位于地图右下方，是一个可折叠的缩略地图。
     ScaleControl：比例尺控件，默认位于地图左下方，显示地图的比例关系。
     MapTypeControl：地图类型控件，默认位于地图右上方。
     CopyrightControl：版权控件，默认位于地图左下方。
     */
    //配置控件参数
    //var opts = {type: BMAP_NAVIGATION_CONTROL_SMALL}
    //map.addControl(new BMap.NavigationControl(opts));
    map.addControl(new BMap.NavigationControl());
    map.addControl(new BMap.ScaleControl());
    //map.addControl(new BMap.OverviewMapControl());
    //map.addControl(new BMap.MapTypeControl());
    //map.setCurrentCity("台州");
    map.setDefaultCursor("crosshair");

    /*
     var marker = new BMap.Marker(point);        // 创建标注
     map.addOverlay(marker);

     var opts = {
     //width : 250,     // 信息窗口宽度
     //height: 100,     // 信息窗口高度
     title : "Hello"  // 信息窗口标题
     }
     var infoWindow = new BMap.InfoWindow("World", opts);  // 创建信息窗口对象
     map.openInfoWindow(infoWindow, map.getCenter());      // 打开信息窗口
     */
    map.enableScrollWheelZoom();//滚轮缩放事件
    //map.enableKeyboard(); //键盘方向键缩放事件
    map.enableContinuousZoom(); // 开启连续缩放效果
    //map.enableInertialDragging();　// 开启惯性拖拽效果

    preMarker = '';

    //点击地图选址
    map.addEventListener("click", function (e) {   //点击事件
        if (!e.overlay) {
            document.getElementById("mapx").innerHTML = "鼠标当前x位置:" + e.point.lng;
            document.getElementById("mapy").innerHTML = "鼠标当前y位置:" + e.point.lat;
            document.getElementById("level").innerHTML = "缩放等级:" + this.getZoom();
            var targetUrl = 'http://api.map.baidu.com/geocoder/v2/?ak=' + key + '&location=' + e.point.lat + ',' + e.point.lng + '&output=json&pois=0';
            $.ajax({
                url: targetUrl,
                type: "get",
                async: false,
                dataType: 'jsonp',
                beforeSend: function () {
                    //alert(targetUrl);
                },
                success: function (data, status) {
                    if (status == 'success' && data.status == 0) {
                        var myIcon = new BMap.Icon("http://api.map.baidu.com/img/markers.png", new BMap.Size(23, 25), {
                            offset: new BMap.Size(10, 25), // 指定定位位置
                            imageOffset: new BMap.Size(0, 0 - 10 * 25) // 设置图片偏移
                        });
                        var marker = new BMap.Marker(e.point, {icon: myIcon});
                        map.removeOverlay(preMarker);
                        map.addOverlay(marker);
                        content = "<div>单位地址：" + data.result.formatted_address + "</div>";
                        var info = new BMap.InfoWindow(content);
                        marker.openInfoWindow(info);
                        preMarker = marker;
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert(XMLHttpRequest.status);
                    alert(XMLHttpRequest.readyState);
                    alert(textStatus);
                    alert(errorThrown);
                }
            });

        }
    });

    map.addEventListener("dragend", function () {   //拖拽事件
        var center = map.getCenter();
        document.getElementById("mapx").innerHTML = "拖拽后中心x位置:" + center.lng;
        document.getElementById("mapy").innerHTML = "拖拽后中心y位置:" + center.lat;
        document.getElementById("level").innerHTML = "缩放等级:" + this.getZoom();
    });

    map.addEventListener("zoomend", function () {   //缩放事件
    });


    /*自定义搜索2*/
    function searchlocal(company,address,waterUseNum) {
        var markerArray = new Array();
        var typeArray = new Array(company, address, '', '');
        var local = new BMap.LocalSearch(map, {
            renderOptions: {
                map: map,
                //panel: "results",//结果容器id
                autoViewport: true,   //自动结果标注
                selectFirstResult: false   //不指定到第一个目标
            },
            pageCapacity: 8,
            //自定义marker事件
            onMarkersSet: function (pois) {
                for (var i = 0; i < pois.length; i++) {
                    (function () {
                        var index = i;
                        var curPoi = pois[i];
                        var curMarker = pois[i].marker;
                        markerArray[i] = curMarker;

                        content = "<h4>" + curPoi.title + " " + typeArray[curPoi.type] + "</h4>";
                        content += "<div>单位地址：" + curPoi.address + "</div>";
                        
                        content += "<div>用水量信息：" + waterUseNum + "（立方米）</div>";
                        
                        //content += '<form action="around.php" method="post"><input type="hidden" name="lng" value="' + curPoi.point.lng + '"><input type="hidden" name="lat" value="' + curPoi.point.lat + '"><input type="submit" value="查看附近幼儿园"></form>';

                        curMarker.addEventListener('click', function (event) {
                            //showAtrributes(event);
                            var info = new BMap.InfoWindow(content);
                            curMarker.openInfoWindow(info);
                            var position = curMarker.getPosition();
                            //document.getElementById("mapx").innerHTML = "拖拽后中心x位置:" + position.lng;
                            //document.getElementById("mapy").innerHTML = "拖拽后中心y位置:" + position.lat;
                            //document.getElementById("level").innerHTML="缩放等级:"+this.getZoom();

                        });
                    })();
                }

            },
            //自定义搜索回调数据
            onSearchComplete: function (results) {
                if (local.getStatus() == BMAP_STATUS_SUCCESS) {

                    var html = '<ol style="list-style: none outside none; padding: 0px; margin: 0px;">';
                    for (var i = 0; i < results.getCurrentNumPois(); i++) {
                        var poi = results.getPoi(i);
                        var bYposition = 2 - i * 20;
                        html += '<li id="poi' + i + '" index="' + i + '" style="margin: 2px 0px; padding: 0px 5px 0px 3px; cursor: pointer; overflow: hidden; line-height: 17px;">';
                        html += '<span style="background:url(http://api.map.baidu.com/bmap/red_labels.gif) 0 ' + bYposition + 'px no-repeat;padding-left:10px;margin-right:3px"></span>'
                        html += '<span style="color:#00c;text-decoration:underline"><b>' + poi.title + '</b> ' + typeArray[poi.type] + '</span>';
                        html += '<br/><span style="color:#666;">' + poi.address + '</span>'
                        html += '</li>';
                    }
                    html += "</ol>";
                    $("#results").html(html);

                    for (var i = 0; i < results.getCurrentNumPois(); i++) {
                        (function () {
                            var index = i + 1;
                            var poi = results.getPoi(i);
                            content = "<h4>" + poi.title + typeArray[poi.type] + "</h4>";
                            content += "<div>单位地址：" + poi.address + "</div>";
                            var info = new BMap.InfoWindow(content)
                            $("#poi" + i).click(function () {
                                markerArray[$(this).attr('index')].openInfoWindow(info);
                            });
                        })();
                    }
                }
            },
        });

        local.search(document.getElementById("searchtext").value);
    }
</script>

<script type="text/javascript">
    function drag(obj, e) {
        var e = e ? e : event;
        var mouse_left = e.clientX - obj.offsetLeft;
        var mouse_top = e.clientY - obj.offsetTop;
        var docmousemove = document.onmousemove;
        var docmouseup = document.onmouseup;
        document.onselectstart = function (e) {
            return false
        }
        document.onmouseup = function () {
            document.onmousemove = docmousemove;
            document.onmouseup = docmouseup;
            document.onselectstart = function (e) {
                return true
            }
        }
        document.onmousemove = function (ev) {
            var ev = ev ? ev : event;
            if (ev.clientX - mouse_left > 0 && document.documentElement.clientWidth - (ev.clientX - mouse_left) - obj.offsetWidth + document.documentElement.scrollLeft > 0) {
                obj.style.left = (ev.clientX - mouse_left) + "px";
            }
            if (ev.clientY - mouse_top > 0 && document.documentElement.clientHeight - (ev.clientY - mouse_top) - obj.offsetHeight + document.documentElement.scrollTop > 0) {
                obj.style.top = (ev.clientY - mouse_top) + "px";
            }
        }
    }
</script>
</body>
</html>