<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>通州区节水管理平台</title>
<script type="text/javascript" src="https://api.map.baidu.com/api?v=2.0&ak=IDvNBsejl9oqMbPF316iKsXR"></script>
<%--<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.js"></script>--%>
<script type="text/javascript" src="${res_url}map/SearchInfoWindow_min.js"></script>
<jsp:include page="/WEB-INF/view/common/basecss.jsp" flush="true" />
<link rel="stylesheet" href="https://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.css" />
<style type="text/css">
#searchResultPanel {
    border:1px solid #C0C0C0;
    height:auto;
    display:none;
}
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
                <div class="input-group" style="margin-right: 2px;">
                    <span class="input-group-addon">
                        <i class="ace-icon fa fa-check"></i>
                    </span>
                    <input type="text" id="suggestId" name="suggestId" class="form-control search-query" placeholder="请输入关键字，右键点击标记地址" />
                    <div id="searchResultPanel"></div>
                </div>
            </div>
            <div class="col-sm-12" style="margin-top: 1px">
                <div style="height:0px;padding-bottom:60%;" id="map"></div>
            </div>
        </div>
    </div>
</div>
<script src="${res_url}js/charts/jquery.min.js"></script>
<script src="${res_url}js/layer/layer.js"></script>
<script type="text/javascript">
    // 百度地图API功能
    function G(id) {
        return document.getElementById(id);
    }
    // 百度地图API功能
    var map = new BMap.Map("map");
    var longitude = 116.657140;
    var latitude = 39.909982;
    var position = '${position}';
    if (position!='') {
        longitude = parseFloat(position.split(",")[0]);
        latitude = parseFloat(position.split(",")[1].toString());
    }

    var point = new BMap.Point(longitude, latitude);
    map.centerAndZoom(point, 15);
    //创建标注对象并添加到地图
    var marker = new BMap.Marker(point); //按照地图点坐标生成标记
    map.addOverlay(marker);

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

    //建立一个自动完成的对象
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

    var myValue;
    ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
        var _value = e.item.value;
        myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
        setPlace();
    });

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


    if (position=='') {
        var local = new BMap.LocalSearch(map, {
            renderOptions:{map: map}
        });
        local.search('${address}');
    }
    //北京市通州区后南仓12号
    function closeView(){
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    }

</script>
</body>
</html>
