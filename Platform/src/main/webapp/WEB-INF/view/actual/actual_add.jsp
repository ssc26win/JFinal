<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <!-- Standard Meta -->
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <jsp:include page="/WEB-INF/view/common/basecss.jsp" flush="true"/>
</head>
<body class="no-skin">
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>
    <div class="main-container-inner">
        <div class="main-content" style="margin-left: 0px;">
            <div class="page-content">
                <div class="row">
                    <div class="col-xs-12">
                        <!-- PAGE CONTENT BEGINS -->
                        <!-- PAGE CONTENT BEGINS -->
                        <form class="form-horizontal" id="validation-form" method="post">
                            <input id="id" name="id" type="hidden" value="${item.id}"/>

                            <div class="form-group">
                                <%--<label class="control-label col-xs-12 col-sm-3 no-padding-right" for="innerCode">单位编号</label>
                                <div class="col-xs-12 col-sm-9">
                                  <div class="clearfix">
                                    <input type="text" id="innerCode" name="innerCode" ${id ne null?'readonly':'' } value="${item.innerCode}" class="col-xs-12 col-sm-6">
                                  </div>
                                </div>--%>
                                <label class="control-label col-xs-12 col-sm-3 no-padding-right"
                                       for="companyName">单位名称</label>

                                <div class="col-xs-12 col-sm-9 search">
                                    <div class="clearfix">
                                        <input type="text" id="companyName" name="companyName" value="${companyName}" ${id ne null?'readonly':'' }
                                               class="col-xs-12 col-sm-8 cnwth" autocomplete="off">
                                    </div>
                                    <div id="auto_div" style="max-height: 200px; overflow-y: auto;"></div>
                                    <input type="hidden" id="innerCode" name="innerCode"
                                           value="${item.innerCode}" ${id ne null?'readonly':'' } >
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-12 col-sm-3 no-padding-right"
                                       for="meterAddress">表计地址</label>

                                <div class="col-xs-12 col-sm-9">
                                    <div class="clearfix">
                                        <input type="hidden" id="meterAddressInput" name="meterAddressInput"
                                               value="${item.meterAddress}"/>
                                        <select id="meterAddress" name="meterAddress"
                                                class="col-xs-12 col-sm-8" ${id ne null?'readonly':'' }></select>
                                    </div>
                                </div>
                            </div>
                            <%--<div class="form-group">
                              <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="watersType">水源类型</label>
                              <div class="col-xs-12 col-sm-9">
                                <div class="clearfix">
                                  <input type="hidden" id="watersTypeInput" name="watersTypeInput" value="${item.watersType}" />
                                  <select id="watersType" name="watersType" class="col-xs-12 col-sm-8"></select>
                                </div>
                              </div>
                            </div>--%>
                            <div class="form-group">
                                <label class="control-label col-xs-12 col-sm-3 no-padding-right"
                                       for="netWater">净用水量</label>

                                <div class="col-xs-12 col-sm-9">
                                    <div class="clearfix">
                                        <input type="number" id="netWater" name="netWater" value="${item.netWater}"
                                               class="col-xs-12 col-sm-8">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-12 col-sm-3 no-padding-right"
                                       for="sumWater">总用水量</label>

                                <div class="col-xs-12 col-sm-9">
                                    <div class="clearfix">
                                        <input type="number" id="sumWater" name="sumWater" value="${item.sumWater}"
                                               class="col-xs-12 col-sm-8">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-12 col-sm-3 no-padding-right"
                                       for="voltage">电池电压（伏特v）</label>

                                <div class="col-xs-12 col-sm-9">
                                    <div class="clearfix">
                                        <input type="number" id="voltage" name="voltage" value="${item.voltage}"
                                               class="col-xs-12 col-sm-8">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-12 col-sm-3 no-padding-right"
                                       for="writeTime">抄表时间</label>

                                <div class="col-xs-12 col-sm-9">
                                    <div class="clearfix">
                                        <input type="text" id="writeTime" name="writeTime" value="${item.writeTime}"
                                               class="col-xs-12 col-sm-8 form_datetime">
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
                    </div>
                    <!-- /.col -->
                </div>
                <!-- /.row -->
            </div>
            <!-- /.page-content -->
        </div>
        <!-- /.main-content -->
    </div>
    <!-- /.main-container-inner -->
</div>
<!-- /.main-container -->
<jsp:include page="/WEB-INF/view/common/basejs.jsp" flush="true"/>
<script type="text/javascript">
    //测试用的数据，这里可以用AJAX获取服务器数据
    var name_list = JSON.parse('${names}');
    var name_code = JSON.parse('${nameCodeMap}');
    //var value = name_code[key];
    var old_value = "";
    var highlightindex = -1;   //高亮
    //自动完成

    function AutoComplete(auto, search, mylist) {
        if ($("#" + search).val() != old_value || old_value == "") {
            var autoNode = $("#" + auto);   //缓存对象（弹出框）
            var searchList = new Array();
            var n = 0;
            old_value = $("#" + search).val();
            for (i in mylist) {
                if (mylist[i].indexOf(old_value) >= 0) {
                    searchList[n++] = mylist[i];
                }
            }
            if (searchList.length == 0) {
                autoNode.hide();
                return;
            }
            autoNode.empty();  //清空上次的记录
            for (i in searchList) {
                var wordNode = searchList[i];   //弹出框里的每一条内容
                var newDivNode = $("<div>").attr("id", i);    //设置每个节点的id值
                newDivNode.attr("style", "font:14px/25px arial;padding:0 8px;cursor: pointer;");
                newDivNode.html(wordNode).appendTo(autoNode);  //追加到弹出框
                //鼠标移入高亮，移开不高亮
                newDivNode.mouseover(function () {
                    if (highlightindex != -1) {        //原来高亮的节点要取消高亮（是-1就不需要了）
                        autoNode.children("div").eq(highlightindex).css("background-color", "white");
                    }
                    //记录新的高亮节点索引
                    highlightindex = $(this).attr("id");
                    $(this).css("background-color", "#FFE4B5");
                });
                newDivNode.mouseout(function () {
                    if ($("#companyName").val() == "") {
                        $("#innerCode").val("");
                    }
                    getAddressData('');
                    $(this).css("background-color", "white");
                });
                //鼠标点击文字上屏
                newDivNode.click(function () {
                    //取出高亮节点的文本内容
                    var comText = autoNode.hide().children("div").eq(highlightindex).text();
                    highlightindex = -1;
                    //文本框中的内容变成高亮节点的内容
                    $("#" + search).val(comText);
                    $("#innerCode").val(name_code[comText]);
                    $("#companyName").focus();
                    getAddressData(name_code[comText]);
                })
                if (searchList.length > 0) {    //如果返回值有内容就显示出来
                    autoNode.show();
                } else {               //服务器端无内容返回 那么隐藏弹出框
                    autoNode.hide();
                    //弹出框隐藏的同时，高亮节点索引值也变成-1
                    highlightindex = -1;
                }
            }
        }
        //点击页面隐藏自动补全提示框
        document.onclick = function (e) {
            if ($("#companyName").val() == "") {
                $("#innerCode").val("");
            }
            var e = e ? e : window.event;
            var tar = e.srcElement || e.target;
            if (tar.id != search) {
                if ($("#" + auto).is(":visible")) {
                    $("#" + auto).css("display", "none")
                }
            }
        }
    }
    $(function () {
        old_value = $("#companyName").val();
        $("#companyName").focus(function () {
            if ($("#companyName").val() == "") {
                AutoComplete("auto_div", "companyName", name_list);
            }
        });
        $("#companyName").keyup(function () {
            AutoComplete("auto_div", "companyName", name_list);
        });
        $("#auto_div").css("width" , $("#companyName").css("width"));

    });

</script>
<script type="text/javascript">
    jQuery(function ($) {
        var $validation = true;
        $('#validation-form').validate({
            errorElement: 'div',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                companyName: {
                    required: true
                },
                meterAddress: {
                    required: true
                }
            },
            messages: {
                companyName: {
                    required: "请输入单位名称"
                },
                meterAddress: {
                    required: "请输入表计地址"
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
                if (element.is(':checkbox') || element.is(':radio')) {
                    var controls = element.closest('div[class*="col-"]');
                    if (controls.find(':checkbox,:radio').length > 1) controls.append(error);
                    else error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
                }
                else if (element.is('.select2')) {
                    error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
                }
                else if (element.is('.chosen-select')) {
                    error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
                }
                else error.insertAfter(element.parent());
            },

            submitHandler: function (form) {
                var $form = $("#validation-form");
                var $btn = $("#submit-btn");
                if ($btn.hasClass("disabled")) return;
                var postData = $("#validation-form").serializeJson();
                $.post("${context_path}/statis/actual/save", postData,
                        function (data) {
                            if (data.code == 0) {
                                parent.reloadGrid(); //重新载入
                                layer.msg('操作成功', {
                                    icon: 1,
                                    time: 1000 //2秒关闭（如果不配置，默认是3秒）
                                }, function () {
                                    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                    parent.layer.close(index); //再执行关闭
                                });
                            } else {
                                layer.msg(data.msg, {
                                    icon: 2,
                                    time: 1000 //2秒关闭（如果不配置，默认是3秒）
                                });
                            }
                            $("#btn-submit").removeClass("disabled");
                        }, "json");
                return false;
            },
            invalidHandler: function (form) {
            }
        });

    });

    function closeView() {
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    }

    function getAddressData(innerCode) {
        $("#meterAddress").empty();
        var submitData = {"innerCode" : innerCode};
        $.post("${context_path}/basic/meter/findAddress", submitData, function (data) {
            var addressList = data.addressList;
            for (var i = 0; i < addressList.length; i++) {
                if ($("#meterAddressInput").val() == addressList[i]) {
                    $("#meterAddress").append("<option selected value='" + addressList[i] + "'>" + addressList[i] + "</option>");
                } else {
                    if ($("#id").val() == '') {
                        $("#meterAddress").append("<option value='" + addressList[i] + "'>" + addressList[i] + "</option>");
                    }
                }
            }
        }, "json");
    }
    $(function () {
        getAddressData();
    })
</script>
</body>

</html>
