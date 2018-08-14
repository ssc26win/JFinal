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
                                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="innerCode">单位编号</label>
                                <div class="col-xs-12 col-sm-9">
                                    <div class="clearfix">
                                        <input type="text" id="innerCode" name="innerCode" value="${item.innerCode}" class="col-xs-12 col-sm-6">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="lineNum">路别</label>
                                <div class="col-xs-12 col-sm-9">
                                    <div class="clearfix">
                                        <input type="text" id="lineNum" name="lineNum" value="${item.lineNum}" class="col-xs-12 col-sm-6">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="meterNum">水表表号</label>
                                <div class="col-xs-12 col-sm-9">
                                    <div class="clearfix">
                                        <input type="text" id="meterNum" name="meterNum" value="${item.meterNum}" class="col-xs-12 col-sm-6">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="meterAddress">表记地址</label>
                                <div class="col-xs-12 col-sm-9">
                                    <div class="clearfix">
                                        <input type="text" id="meterAddress" name="meterAddress" value="${item.meterAddress}" class="col-xs-12 col-sm-6">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="times">读数倍数</label>
                                <div class="col-xs-12 col-sm-9">
                                    <div class="clearfix">
                                        <input type="number" id="times" name="times" value="${item.times}" class="col-xs-12 col-sm-6">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="watersType">水源类型</label>
                                <div class="col-xs-12 col-sm-9">
                                    <div class="clearfix">
                                        <input type="hidden" id="watersTypeInput" name="watersTypeInput" value="${item.watersType}" />
                                        <select id="watersType" name="watersType" class="col-xs-12 col-sm-6"></select>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="meterAttr">水表属性</label>
                                <div class="col-xs-12 col-sm-9">
                                    <div class="clearfix">
                                        <input type="text" id="meterAttr"  name="meterAttr" value="${item.meterAttr}" class="col-xs-12 col-sm-6">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="chargeType">收费类型</label>
                                <div class="col-xs-12 col-sm-9">
                                    <div class="clearfix">
                                        <input type="hidden" id="chargeTypeInput" name="chargeTypeInput" value="${item.chargeType}" />
                                        <select id="chargeType" name="chargeType" value="${item.chargeType}" class="col-xs-12 col-sm-6" ></select>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="billingCycle">计费周期</label>
                                <div class="col-xs-12 col-sm-9">
                                    <div class="clearfix">
                                        <input type="text" id="billingCycle" name="billingCycle" value="${item.billingCycle}" class="col-xs-12 col-sm-6">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="registDate">注册日期</label>
                                <div class="col-xs-12 col-sm-9">
                                    <div class="clearfix">
                                        <input type="text" id="registDate" name="registDate" value="${item.registDate}" class="col-xs-12 col-sm-6 form_datetime">
                                    </div>
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
                innerCode:{
                    required: true
                },
                meterNum:{
                    required: true
                },
                meterAddress:{
                    required: true
                },
            },
            messages: {
                innerCode:{
                    required: "请输入单位编号"
                },
                meterNum:{
                    required: "请输入水表表号"
                },
                meterAddress:{
                    required: "请输入表计地址"
                },
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
                $.post("${context_path}/basic/meter/save" ,postData,
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
        $.post("${context_path}/dict/getMeterUseDict", submitData, function(data) {
            var chargeType = data.ChargeType;
            var watersType = data.WatersType;
            var waterUseType = data.WaterUseType;
            for(var i = 0;i<chargeType.length;i++) {
                if ($("#chargeTypeInput").val() == chargeType[i].value) {
                    $("#chargeType").append("<option selected value='" + chargeType[i].value + "'>"+chargeType[i].name+"</option>");
                } else {
                    $("#chargeType").append("<option value='" + chargeType[i].value + "'>"+chargeType[i].name+"</option>");
                }
            }
            for(var i = 0;i<watersType.length;i++) {
                if ($("#watersTypeInput").val() == watersType[i].value) {
                    $("#watersType").append("<option selected value='" + watersType[i].value + "'>"+watersType[i].name+"</option>");
                } else {
                    $("#watersType").append("<option value='" + watersType[i].value + "'>"+watersType[i].name+"</option>");
                }
            }
        },"json");
    }
    $(function(){
        getDictMapData();
    })
</script>
</body>

</html>
