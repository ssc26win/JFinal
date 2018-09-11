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
                                <div class="col-sm-4 common-tip-append">
                                    <input type="text" id="name" name="name" value="${item.name}" class="form-control" autocomplete="off" >
                                </div>
                                <label class="col-sm-2 control-label" for="realCode">单位编号:</label>
                                <div class="col-sm-4 common-tip-append">
                                    <input type="text" id="realCode" name="realCode" value="${item.realCode}" class="form-control">
                                </div>
                            </div>
                            <div class="form-group" style="margin-top: 15px;">
                                <label class="col-sm-2 control-label" for="waterUnit">所属节水办:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="waterUnit" name="waterUnit" value="${item.waterUnit}" class="form-control">
                                </div>
                                <label class="col-sm-2 control-label" for="county">所属区县:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="county" name="county" value="${item.county}" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="address">单位地址:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="address" name="address" value="${item.address}" class="form-control">
                                </div>
                                <label class="col-sm-2 control-label" for="position"><a href="#" title="点击获取" style="text-decoration: none" onclick="getPosition();">
                                    <b title="手动输入位置必须“英文,隔开经纬度”">位置</b></a>信息:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="position" name="position" value="${position}" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="customerType">用户类型:</label>
                                <div class="col-sm-4">
                                    <input type="hidden" id="customerTypeInput" name="customerTypeInput" value="${item.customerType}">
                                    <select id="customerType" name="customerType" class="form-control"> </select>
                                </div>
                                <label class="col-sm-2 control-label" for="street">所属乡镇:</label>
                                <div class="col-sm-4">
                                    <input type="hidden" id="streetInput" name="streetInput" value="${item.street}" class="form-control">
                                    <select id="street" name="street" class="form-control"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="customerType">注册时间:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="createTime" name="createTime" value="${item.createTime}" class="form-control form_datetime">
                                </div>
                                <label class="col-sm-2 control-label" for="streetSrc">原乡镇或街道:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="streetSrc" name="streetSrc" value="${item.streetSrc}" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="gb_industry">国标行业:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="gb_industry" name="gb_industry" value="${item.gb_industry}" class="form-control">
                                </div>
                                <label class="col-sm-2 control-label" for="main_industry">主要行业:</label>
                                <div class="col-sm-4">
                                    <input type="text" id="main_industry" name="main_industry" value="${item.main_industry}" class="form-control">
                                </div>
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
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="waterUseType">取水用途:</label>
                                <div class="col-sm-4">
                                    <input type="hidden" id="waterUseTypeInput" name="waterUseTypeInput" value="${item.waterUseType}">
                                    <select  id="waterUseType" name="waterUseType" value="${item.waterUseType}" class="form-control"> </select>
                                </div>
                                <label class="col-sm-2 control-label" for="self_well_price">自备井基本水价:</label>
                                <div class="col-sm-4">
                                    <input type="number" id="self_well_price" name="self_well_price" value="${item.self_well_price}" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="surface_price">地表水基本水价:</label>
                                <div class="col-sm-4">
                                    <input type="number" id="surface_price" name="surface_price" value="${item.surface_price}" class="form-control">
                                </div>
                                <label class="col-sm-2 control-label" for="self_free_price">自来水基本水价:</label>
                                <div class="col-sm-4">
                                    <input type="number" id="self_free_price" name="self_free_price" value="${item.self_free_price}" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="company_type">单位类型:</label>
                                <div class="col-sm-4">
                                    <input  type="hidden" id="company_typeInput" name="company_typeInput" value="${item.company_type}" />
                                    <select id="company_type" name="company_type" value="${item.companyType}" class="form-control">
                                        <option value="1">用水单位</option>
                                        <option value="2">供水单位</option>
                                    </select>
                                </div>
                                <label class="col-sm-2 control-label" for="term">周期:</label>
                                <div class="col-sm-4">
                                    <input  type="hidden" id="termInput" name="termInput" value="${item.term}" />
                                    <select id="term" name="term" value="${item.term}" class="form-control"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="memo">备注信息：</label>
                                <div class="col-sm-4">
                                    <input type="text" id="memo" name="memo" value="${item.memo}" class="form-control">
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
            focusInvalid: true,
            rules: {
                name:{
                    required: true
                },
                realCode:{
                    required: true
                }
            },
            messages: {
                name:{
                    required: "请输入单位名称"
                },
                realCode:{
                    required: "请输入单位编号"
                }
            },
            highlight: function (e) {
                $(e).closest('.common-tip-append').removeClass('has-info').addClass('has-error');
            },

            success: function (e) {
                $(e).closest('.common-tip-append').removeClass('has-error');//.addClass('has-info');
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
        $.post("${context_path}/dict/getCompanyUseDict", submitData, function(data) {
            var unitType = data.UnitType;
            var customerType = data.UserType;
            var street = data.Street;
            for(var i = 0;i<unitType.length;i++) {
                $("#unitType").append("<option value='" + unitType[i].value + "'>"+unitType[i].name+"</option>");
            }
            $("#unitType").val($("#unitTypeInput").val());
            for(var i = 0;i<customerType.length;i++) {
                $("#customerType").append("<option value='" + customerType[i].value + "'>"+customerType[i].name+"</option>");
            }
            $("#customerType").val($("#customerTypeInput").val());
            for(var i = 0;i<street.length;i++) {
                $("#street").append("<option value='" + street[i].value + "'>"+street[i].name+"</option>");
            }
            $("#street").val($("#streetInput").val());
            var waterUseType = data.WaterUseType;
            for(var i = 0;i<waterUseType.length;i++) {
                $("#waterUseType").append("<option value='" + waterUseType[i].value + "'>"+waterUseType[i].name+"</option>");
            }
            $("#waterUseType").val($("#waterUseTypeInput").val());

            var term = data.Term;
            for(var i = 0;i<term.length;i++) {
                $("#term").append("<option value='" + term[i].value + "'>"+term[i].name+"</option>");
            }
            $("#term").val($("#termInput").val());

            $("#company_type").val($("#company_typeInput").val());

        },"json");
    }
    $(function(){
        getDictMapData();
    })
</script>
</body>

</html>
