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
                        <form class="form-horizontal" id="validation-form" method="post">
                            <input name="id" type="hidden" value="${id}"/>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="companyName">单位名称:</label>
                                <div class="col-sm-3 common-tip-append search">
                                    <input type="text" id="companyName" name="companyName" value="${companyName}" class="form-control cnwth" autocomplete="off">
                                    <div id="auto_div" style="max-height: 200px; overflow-y: auto;"></div>
                                    <input type="hidden" id="innerCode" name="innerCode" value="${item.innerCode}" class="form-control">
                                </div>
                                <label class="col-sm-3 control-label" for="wellNum">水井编号:</label>
                                <div class="col-sm-3 common-tip-append">
                                    <input type="text" id="wellNum" name="wellNum" value="${item.wellNum}" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="name">水井名称:</label>
                                <div class="col-sm-3">
                                    <input type="text" id="name" name="name" value="${item.name}" class="form-control">
                                </div>
                                <label class="col-sm-3 control-label" for="address">水井地址:</label>
                                <div class="col-sm-3">
                                    <input type="text" id="address" name="address" value="${item.address}" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="village">村:</label>
                                <div class="col-sm-3">
                                    <input type="text" id="village" name="village" value="${item.village}" class="form-control">
                                </div>
                                <label class="col-sm-3 control-label" for="waterWithdrawals">年许可取水量（万立方米）:</label>
                                <div class="col-sm-3">
                                    <input type="number" id="waterWithdrawals" name="waterWithdrawals" value="${item.waterWithdrawals}" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="year">成井时间（年）:</label>
                                <div class="col-sm-3">
                                    <input type="number" id="year" name="year" value="${item.year}" class="form-control">
                                </div>
                                <label class="col-sm-3 control-label" for="wellDepth">井深（米）:</label>
                                <div class="col-sm-3">
                                    <input type="number" id="wellDepth" name="wellDepth" value="${item.wellDepth}" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="groundDepth">地下水埋深（米）:</label>
                                <div class="col-sm-3">
                                    <input type="number" id="groundDepth" name="groundDepth" value="${item.groundDepth}" class="form-control">
                                </div>
                                <label class="col-sm-3 control-label" for="oneselfWell_0">是否为单位自备井:</label>
                                <div class="col-sm-3">
                                    <input type="radio" name="oneselfWell" id="oneselfWell_0" value="0" ${item.oneselfWell eq 0?'checked':'' }/>否
                                    <input type="radio" name="oneselfWell" id="oneselfWell_1" value="1" ${item.oneselfWell eq 1?'checked':'' }/>是
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="innerDiameter">井口井管内径（毫米）:</label>
                                <div class="col-sm-3">
                                    <input type="number" id="innerDiameter" name="innerDiameter" value="${item.innerDiameter}" class="form-control">
                                </div>
                                <label class="col-sm-3 control-label" for="material">井壁管材料:</label>
                                <div class="col-sm-3">
                                    <input type="text" id="material" name="material" value="${item.material}" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="application">应用状况:</label>
                                <div class="col-sm-3">
                                    <input type="text" id="application" name="application" value="${item.application}" class="form-control">
                                </div>
                                <label class="col-sm-3 control-label" for="electromechanics_0">是否已配套机电设备:</label>
                                <div class="col-sm-3">
                                    <input type="radio" name="electromechanics" id="electromechanics_0" value="0" ${item.electromechanics eq 0?'checked':'' }/>否
                                    <input type="radio" name="electromechanics" id="electromechanics_1" value="1" ${item.electromechanics eq 1?'checked':'' }/>是
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="calculateWater_0">是否已安装水量计量设施:</label>
                                <div class="col-sm-3">
                                    <input type="radio" name="calculateWater" id="calculateWater_0" value="0" ${item.calculateWater eq 0?'checked':'' }/>否
                                    <input type="radio" name="calculateWater" id="calculateWater_1" value="1" ${item.calculateWater eq 1?'checked':'' }/>是
                                </div>
                                <label class="col-sm-3 control-label" for="pumpModel">水泵型号:</label>
                                <div class="col-sm-3">
                                    <%--<input type="hidden" id="pumpModelInput" name="pumpModelInput" value="${item.pumpModel}">
                                    <select id="pumpModel" name="pumpModel" value="${item.pumpModel}" class="form-control"></select>--%>
                                    <input type="text" id="pumpModel" name="pumpModel" value="${item.pumpModel}" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="calculateType">水量计量设施类型:</label>
                                <div class="col-sm-3">
                                    <input type="hidden" id="calculateTypeInput" name="calculateTypeInput" value="${item.calculateType}">
                                    <select id="calculateType" name="calculateType" class="form-control"></select>
                                </div>
                                <label class="col-sm-3 control-label" for="aboveScale_0">规模以上地下水源地水井:</label>
                                <div class="col-sm-3">
                                    <input type="radio" name="aboveScale" id="aboveScale_0" value="0" ${item.aboveScale eq 0?'checked':'' }/>否
                                    <input type="radio" name="aboveScale" id="aboveScale_1" value="1" ${item.aboveScale eq 1?'checked':'' }/>是
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="geomorphicType">所在地貌类型区:</label>
                                <div class="col-sm-3">
                                    <input type="hidden" id="geomorphicTypeInput" name="geomorphicTypeInput" value="${item.geomorphicType}">
                                    <select id="geomorphicType" name="geomorphicType" value="${item.geomorphicType}" class="form-control"></select>
                                </div>
                                <label class="col-sm-3 control-label" for="groundType">所取地下水类型:</label>
                                <div class="col-sm-3">
                                    <input type="hidden" id="groundTypeInput" name="groundTypeInput" value="${item.groundType}">
                                    <select id="groundType" name="groundType" value="${item.groundType}" class="form-control"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="nameCode">所在水资源三级区名称及编码:</label>
                                <div class="col-sm-3">
                                    <input type="text" id="nameCode" name="nameCode" value="${item.nameCode}" class="form-control">
                                </div>
                                <label class="col-sm-3 control-label" for="watersType">水源类型:</label>
                                <div class="col-sm-3">
                                    <%--<input type="hidden" id="watersTypeInput" name="watersTypeInput" value="${item.watersTypeInput}" >
                                    <select id="watersType" name="watersType" value="${item.watersType}" class="form-control"></select>--%>
                                    <input type="text" id="watersType" name="watersType" value="${item.watersType}" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="useEfficiency">主要取水用途及效益:</label>
                                <div class="col-sm-3">
                                    <input type="text" id="useEfficiency" name="useEfficiency" value="${item.useEfficiency}" class="form-control">
                                </div>
                                <label class="col-sm-3 control-label" for="method">取水量确定方法:</label>
                                <div class="col-sm-3">
                                    <input type="text" id="method" name="method" value="${item.method}" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="licence_0">是否已办理取水许可证:</label>
                                <div class="col-sm-3">
                                        <input type="radio" name="licence" id="licence_0" value="0" ${item.licence eq 0?'checked':'' }/>否
                                        <input type="radio" name="licence" id="licence_1" value="1" ${item.licence eq 1?'checked':'' }/>是
                                </div>
                                <label class="col-sm-3 control-label" for="licenceCode">取水许可证编号:</label>
                                <div class="col-sm-3">
                                    <input type="text" id="licenceCode" name="licenceCode" value="${item.licenceCode}" class="form-control">
                                </div>
                            </div>
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
<jsp:include page="/WEB-INF/view/common/search_cpyname.jsp" flush="true" />
<script type="text/javascript">
    jQuery(function($) {
        var $validation = true;
        $('#validation-form').validate({
            errorElement: 'div',
            errorClass: 'help-block',
            focusInvalid: true,
            rules: {
                companyName:{
                    required: true
                },
                innerCode:{
                    required: true
                },
                wellNum:{
                    required: true
                }
            },
            messages: {
                companyName:{
                    required: "请输入单位名称"
                },
                innerCode:{
                    required: "请输入单位编号"
                },
                wellNum:{
                    required: "请输入水井编号"
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
                else error.insertAfter(element);
            },

            submitHandler: function (form) {
                var $form = $("#validation-form");
                var $btn = $("#submit-btn");
                if($btn.hasClass("disabled")) return;
                var postData=$("#validation-form").serializeJson();
                $.post("${context_path}/basic/well/save" ,postData,
                        function(data){
                            if(data.code==0){
                                parent.reloadGrid(); //重新载入
                                layer.msg('操作成功', {
                                    icon: 1,
                                    time: 1000 //2秒关闭（如果不配置，默认是3秒）
                                },function(){
                                    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                    parent.layer.close(index); //再执行关闭
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
        $.post("${context_path}/dict/getWellUseDict", submitData, function(data) {
//            var watersType = data.WatersType;
//            var pumpModel = data.PumpModel;
            var calculateType = data.CalculateType;
            var geomorphicType = data.GeomorphicType;
            var groundType = data.GroundType;
//            for(var i = 0;i<watersType.length;i++) {
//                if ($("#watersTypeInput").val() == watersType[i].value) {
//                    $("#watersType").append("<option selected value='" + watersType[i].value + "'>"+watersType[i].name+"</option>");
//                } else {
//                    $("#watersType").append("<option value='" + watersType[i].value + "'>"+watersType[i].name+"</option>");
//                }
//            }
//            for (var i = 0;i<pumpModel.length;i++) {
//                $("#pumpModel").append("<option value='" + pumpModel[i].value + "'>"+pumpModel[i].name+"</option>");
//            }
//            $("#pumpModel").val($("#pumpModelInput").val());

            for (var i = 0;i<calculateType.length;i++) {
                $("#calculateType").append("<option value='" + calculateType[i].value + "'>"+calculateType[i].name+"</option>");
            }
            $("#calculateType").val($("#calculateTypeInput").val());

            for (var i = 0;i<geomorphicType.length;i++) {
                $("#geomorphicType").append("<option value='" + geomorphicType[i].value + "'>"+geomorphicType[i].name+"</option>");
            }
            $("#geomorphicType").val($("#geomorphicTypeInput").val());

            for (var i = 0;i<groundType.length;i++) {
                $("#groundType").append("<option value='" + groundType[i].value + "'>"+groundType[i].name+"</option>");
            }
            $("#groundType").val($("#groundTypeInput").val());
        },"json");
    }
    $(function(){
        getDictMapData();
    })
</script>
</body>

</html>
