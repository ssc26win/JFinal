<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <!-- Standard Meta -->
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <jsp:include page="/WEB-INF/view/common/basecss.jsp" flush="true" />
</head>

<body class="no-skin">
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try{ace.settings.check('main-container' , 'fixed')}catch(e){}
    </script>

    <div class="main-container-inner">
        <div class="main-content" style="margin-left: 0px;">
            <div class="page-content">

                <div class="row">
                    <div class="col-xs-12">
                        <!-- PAGE CONTENT BEGINS -->
                        <!-- PAGE CONTENT BEGINS -->
                        <form class="form-horizontal" id="validation-form" method="post" style="margin-right: 10px;">
                            <input name="id" type="hidden" value="${id}"/>
                            <div class="form-group" style="margin-top: 15px;">
                                <label class="col-sm-2 control-label" for="name">单位名称:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="name" name="name" value="${item.name}" class="form-control">
                                </div>
                                <label class="col-sm-2 control-label" for="innerCode">单位编号:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="innerCode" name="innerCode" value="${item.innerCode}" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="address">单位地址:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="address" name="address" value="${item.address}" class="form-control">
                                </div>
                                <label class="col-sm-2 control-label" for="street"><a href="#" title="点击获取" style="text-decoration-line: none" onclick="getPosition();">位置</a>信息:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="position" name="position" value="${position}" readonly class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="customerType">用户类型:</label>
                                <div class="col-sm-4">
                                    <input type="hidden" id="customerTypeInput" name="customerTypeInput" value="${item.customerType}">
                                    <select  id="customerType" name="customerType" class="form-control"> </select>
                                </div>
                                <label class="col-sm-2 control-label" for="street">所属乡镇或街道:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="street" name="street" value="${item.street}" class="form-control">
                                </div>
                                <%--<label class="col-sm-2 control-label" for="waterUseType">取水用途:</label>--%>
                                <%--<div class="col-sm-4">--%>
                                    <%--<input type="hidden" id="waterUseTypeInput" name="waterUseTypeInput" value="${item.waterUseType}">--%>
                                    <%--<select  id="waterUseType" name="waterUseType" value="${item.waterUseType}" class="form-control"> </select>--%>
                                <%--</div>--%>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="contact">联系人:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="contact" name="contact" value="${item.contact}" class="form-control">
                                </div>
                                <label class="col-sm-2 control-label" for="phone">联系电话:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="phone" name="phone" value="${item.phone}" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="postalCode">邮政编码:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="postalCode" name="postalCode" value="${item.postalCode}" class="form-control">
                                </div>
                                <label class="col-sm-2 control-label" for="department">管水部门:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="department" name="department" value="${item.department}" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="wellCount">水井数量:</label>
                                <div class="col-sm-4">
                                    <input type="number" id="wellCount" name="wellCount" value="${item.wellCount}" class="form-control">
                                </div>
                                <label class="col-sm-2 control-label" for="firstWatermeterCount">一级表数量:</label>
                                <div class="col-sm-4">
                                    <input type="number" id="firstWatermeterCount" name="firstWatermeterCount" value="${item.firstWatermeterCount}" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="remotemeterCount">远传表数量:</label>
                                <div class="col-sm-4">
                                    <input type="number" id="remotemeterCount" name="remotemeterCount" value="${item.remotemeterCount}" class="form-control">
                                </div>
                                <label class="col-sm-2 control-label" for="unitType">节水型单位类型:</label>
                                <div class="col-sm-4">
                                    <input type="hidden" id="unitTypeInput" name="unitTypeInput" value="${item.unitType}">
                                    <select  id="unitType" name="unitType" value="${item.unitType}" class="form-control"> </select>
                                </div>
                            </div>
                            <%--<div class="form-group">--%>
                                <%--<label class="col-sm-2 control-label" for="longitude" title="点击获取"><a href="http://api.map.baidu.com/lbsapi/getpoint/index.html" target="_blank">地图经度</a>:</label>--%>
                                <%--<div class="col-sm-4">--%>
                                    <%--<input type="number" id="longitude" step="0.000000" name="longitude" value="${item.longitude}" class="form-control">--%>
                                <%--</div>--%>
                                <%--<label class="col-sm-2 control-label" for="latitude" title="点击获取"><a href="http://api.map.baidu.com/lbsapi/getpoint/index.html" target="_blank">地图纬度</a>:</label>--%>
                                <%--<div class="col-sm-4">--%>
                                    <%--<input type="number" id="latitude" step="0.000000" name="latitude" value="${item.latitude}" class="form-control">--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <div class="clearfix form-actions" align="center">
                                <div class="col-md-offset-3 col-md-9">
                                    <button id="submit-btn" class="btn btn-info" type="submit" data-last="Finish">
                                        <i class="ace-icon fa fa-check bigger-110"></i>
                                        提交
                                    </button>
                                    &nbsp; &nbsp; &nbsp;
                                    <button class="btn" type="reset">
                                        <i class="ace-icon fa fa-undo bigger-110"></i>
                                        重置
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div><!-- /.col -->
                </div><!-- /.row -->
            </div><!-- /.page-content -->
        </div><!-- /.main-content -->
    </div><!-- /.main-container-inner -->
</div><!-- /.main-container -->
<jsp:include page="/WEB-INF/view/common/basejs.jsp" flush="true" />
<script type="text/javascript">
    function setPosition(obj){
        alert(obj);
    }
    function getPosition() {
        var address = $("#address").val();
        if (address=="") {
            layer.alert("请填写单位地址！");
            return;
        }
        var position = $("#position").val();
        parent.layer.open({
            title:'设置单位位置',
            type: 2,
            area: ['900px', '600px'],
            fix: false, //不固定
            maxmin: true,
            content: '${context_path}/basic/company/position?address='+address+'&position='+position,
            yes:function(layero,index) {
                $(layero).find("input").each(function(i, v) {

                });

            },
            cancel: function(){
                var name = "P_position";
                var arr,reg = new RegExp("(^| )"+name+"=([^;]*)(;|$)");
                if(arr=document.cookie.match(reg)) {
                    $("#position").val(unescape(arr[2]));
                    document.cookie = name + "=" + unescape(arr[2]) + "; " + -1;
                }
            }
        });
    }
    jQuery(function($) {

        var $validation = true;
        $('#validation-form').validate({
            errorElement: 'div',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                name:{
                    required: true
                }
            },
            messages: {
                name:{
                    required: "请输入单位名称"
                }
            },
            rules: {
                innerCode:{
                    required: true
                }
            },
            messages: {
                innerCode:{
                    required: "请输入单位编码"
                }
            },
            highlight: function (e) {
                $(e).closest('.form-group').removeClass('has-info').addClass('has-error');
            },

            success: function (e) {
                $(e).closest('.form-group').removeClass('has-error');//.addClass('has-info');
                $(e).remove();
            },

            errorPlacement: function (error, element) {
                if(element.is(':checkbox') || element.is(':radio')) {
                    var controls = element.closest('div[class*="col-"]');
                    if(controls.find(':checkbox,:radio').length > 1) controls.append(error);
                    else error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
                }
                else if(element.is('.select2')) {
                    error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
                }
                else if(element.is('.chosen-select')) {
                    error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
                }
                else error.insertAfter(element.parent());
            },

            submitHandler: function (form) {
                var $form = $("#validation-form");
                var $btn = $("#submit-btn");
                if($btn.hasClass("disabled")) return;
                var postData=$("#validation-form").serializeJson();
                $.post("${context_path}/basic/company/save" ,postData,
                        function(data){
                            if(data.code==0){
                                parent.reloadGrid(); //重新载入
                                layer.msg('操作成功', {
                                    icon: 1,
                                    time: 1000 //2秒关闭（如果不配置，默认是3秒）
                                },function(){
                                    closeView();
                                });
                            }else {
                                layer.msg(data.msg, {
                                    icon: 2,
                                    time: 1000 //2秒关闭（如果不配置，默认是3秒）
                                });
                            }
                            $("#btn-submit").removeClass("disabled");
                        },"json");
                return false;
            },
            invalidHandler: function (form) {
            }
        });

    });

    function closeView(){
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    }

    function getDictMapData(){
        var submitData = {};
        $.post("${context_path}/dict/getByType", submitData, function(data) {
            var unitType = data.UnitType;
            var customerType = data.UserType;
            var waterUseType = data.WaterUseType;
            for(var i = 0;i<unitType.length;i++) {
                $("#unitType").append("<option value='" + unitType[i].value + "'>"+unitType[i].name+"</option>");
            }
            for(var i = 0;i<customerType.length;i++) {
                $("#customerType").append("<option value='" + customerType[i].value + "'>"+customerType[i].name+"</option>");
            }
            for(var i = 0;i<waterUseType.length;i++) {
                $("#waterUseType").append("<option value='" + waterUseType[i].value + "'>"+waterUseType[i].name+"</option>");
            }
            $("#unitType").val($("#unitTypeInput").val());
            $("#customerType").val($("#customerTypeInput").val());
            $("#waterUseType").val($("#waterUseTypeInput").val());

        },"json");
    }
    $(function(){
        getDictMapData();
    })
</script>
</body>

</html>
