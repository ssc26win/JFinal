<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>后台管理系统</title>

    <meta name="description" content="overview &amp; stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <!-- bootstrap & fontawesome -->
    <jsp:include page="/WEB-INF/view/common/basecss.jsp" flush="true"/>
</head>
<body class="no-skin">
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>
    <div class="main-content" id="page-wrapper">
        <div class="page-content" id="page-content">
            <div class="row">
                <div class="col-xs-12">
                    <!-- PAGE CONTENT BEGINS -->
                    <div class="widget-box">
                        <div class="widget-header widget-header-small">
                            <h5 class="widget-title lighter">筛选</h5>
                        </div>

                        <div class="widget-body">
                            <div class="widget-main">
                                <div class="row">
                                    <div class="col-xs-12">
                                        <form id="exportForm" action="${context_path}/report/daily/exportData"
                                              method="post">
                                            <input type="text" style="display:none"/>

                                            <div class="input-group">
                                                日期时间:
                                                <input type="text" id="startTime" name="startTime" value=""
                                                       class="form_date" style="width: 100px;"/>~
                                                <input type="text" id="endTime" name="endTime" value=""
                                                       class="form_date" style="width: 100px;"/>
                                                <select id="type" name="type"
                                                        style="margin-left: 5px;width: 159px;height: 34px;">
                                                    <option value="">请选择单位类型</option>
                                                    <option value="1">用水单位</option>
                                                    <option value="2">供水单位</option>
                                                </select>
                                                <input type="text" id="name" name="name" class="" placeholder="请输入单位名称"
                                                       style="margin-left: 5px;"/>
                                                <input type="text" id="innerCode" name="innerCode" class=""
                                                       placeholder="请输入单位编号" style="margin-left: 5px;"/>
                                                <select id="watersType" name="watersType"
                                                        style="margin-left: 5px;width: 159px; height: 34px;">
                                                    <option value="">请选择水源类型</option>
                                                </select>
                                                <select id="meterAttr" name="meterAttr"
                                                        style="margin-left: 5px;width: 159px; height: 34px;">
                                                    <option value="">请选择水表属性</option>
                                                </select>
                                                <select id="street" name="street"
                                                        style="margin-left: 5px;width: 159px; height: 34px;">
                                                    <option value="">所属乡镇或街道</option>
                                                </select>
                                                <span class="input-group-btn">
                                                    <button type="button" id="btn_search" class="btn btn-purple btn-sm">
                                                        <span class="ace-icon fa fa-search icon-on-right bigger-110"></span>
                                                        搜索
                                                    </button>
                                                    <button type="button" id="btn-exportData"
                                                            class="btn btn-success btn-sm" style="margin-left:10px;">
                                                        导出
                                                    </button>
                                                    <button type="button" id="btn-chart" class="btn btn-primary btn-sm"
                                                            style="margin-left:10px;">
                                                        查看图表
                                                    </button>
                                                </span>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-xs-12" style="margin-bottom: 5px;">
                    <!-- PAGE CONTENT BEGINS -->
                    <table id="grid-table"></table>

                    <div id="grid-pager"></div>

                    <script type="text/javascript">
                        var $path_base = "..";//in Ace demo this will be used for editurl parameter
                    </script>

                    <!-- PAGE CONTENT ENDS -->
                </div>
                <!-- /.col -->
            </div>
            <!-- /.row -->
        </div>
    </div>
</div>
<!-- /.main-container -->
<!-- basic scripts -->
<jsp:include page="/WEB-INF/view/common/basejs.jsp" flush="true"/>

<script type="text/javascript">
    $(document).ready(function () {

        var grid_selector = "#grid-table";
        var pager_selector = "#grid-pager";
        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
        });
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                //setTimeout is for webkit only to give time for DOM changes and then redraw!!!
                setTimeout(function () {
                    $(grid_selector).jqGrid('setGridWidth', parent_column.width());
                }, 0);
            }
        });

        var url = '${context_path}/report/daily/getListData';
        var sTime = '${date}';
        var eTime = '${date}';
        var companyName = '${companyName}';
        if ((sTime != undefined && sTime != null && sTime != '') && (eTime != undefined && eTime != null && eTime != '')) {
            var sTime = '${date}' + ' 00:00:00';
            var eTime = '${date}' + ' 23:59:59';
            $("#startTime").val(sTime);
            $("#endTime").val(eTime);
            url = '${context_path}/report/daily/getListData?time=' + new Date().getTime();
            url = url + '&startTime=' + sTime + '&endTime=' + eTime;
        }
        if (companyName != undefined && companyName != null && companyName != '') {
            $("#name").val(companyName);
            url = encodeURI(url + '&byName=yes&name=' + companyName);
        }

        $("#grid-table").jqGrid({
            url: url,
            mtype: "GET",
            datatype: "json",
            colModel: JSON.parse('${columnsDay}'),
            viewrecords: true,
            height: 560,
            rowNum: 20,
            multiselect: true,//checkbox多选
            altRows: true,//隔行变色
            autoScroll: true,
            recordtext: "{0} - {1} 共 {2} 条",
            pgtext: "第 {0} 页 共 {1} 页",
            pager: pager_selector,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                }, 0);
            }
        });
        $(window).triggerHandler('resize.jqGrid');

        $(window).bind('resize', function () {
            $("#jqgrid").setGridWidth($(window).width() * 0.75);
            $("#grid-table").setGridHeight($(window).height() - 200);
        });
        $("#btn_search").click(function () {
            //此处可以添加对查询数据的合法验证
            var street = $("#street").val();
            var watersType = $("#watersType").val();
            var type = $("#type").val();

            var name = $("#name").val();
            var innerCode = $("#innerCode").val();
            var startTime = $("#startTime").val();
            var endTime = $("#endTime").val();
            var meterAttr = $("#meterAttr").val();

            $("#grid-table").jqGrid('setGridParam', {
                datatype: 'json',
                postData: {
                    'street': street, 'watersType': watersType, 'type': type, 'name': name, 'innerCode': innerCode,
                    'startTime': startTime, 'endTime': endTime, 'meterAttr': meterAttr
                }, //发送数据
                page: 1
            }).trigger("reloadGrid"); //重新载入
        });
        $("#btn-chart").click(function () {
            var street = $("#street").val();
            var watersType = $("#watersType").val();
            var type = $("#type").val();

            var name = $("#name").val();
            var innerCode = $("#innerCode").val();
            var startTime = $("#startTime").val();
            var endTime = $("#endTime").val();
            var meterAttr = $("#meterAttr").val();

            var url = "${context_path}/report/daily/chart?time=" + new Date().getTime();

            if (street != "" && street != undefined) {
                url = url + "&street=" + street;
            }
            if (watersType != "" && watersType != undefined) {
                url = url + "&watersType=" + watersType;
            }
            if (type != "" && type != undefined) {
                url = url + "&type=" + type;
            }
            if (name != "" && name != undefined) {
                url = url + "&name=" + name;
            }
            if (innerCode != "" && innerCode != undefined) {
                url = url + "&innerCode=" + innerCode;
            }
            if (startTime != "" && startTime != undefined) {
                url = url + "&startTime=" + startTime;
            }
            if (endTime != "" && endTime != undefined) {
                url = url + "&endTime=" + endTime;
            }
            if (meterAttr != "" && meterAttr != undefined) {
                url = url + "&meterAttr=" + meterAttr;
            }
            window.location.href = encodeURI(url);
        });
        $("#btn-exportData").click(function () {
            $("#exportForm").submit();
        });
    });
    //replace icons with FontAwesome icons like above
    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first': 'ace-icon fa fa-angle-double-left bigger-140',
            'ui-icon-seek-prev': 'ace-icon fa fa-angle-left bigger-140',
            'ui-icon-seek-next': 'ace-icon fa fa-angle-right bigger-140',
            'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        })
    }

    /**获取选中的列***/
    function getSelectedRows() {
        var grid = $("#grid-table");
        var rowKey = grid.getGridParam("selrow");
        if (!rowKey)
            return "-1";
        else {
            var selectedIDs = grid.getGridParam("selarrrow");
            var result = "";
            for (var i = 0; i < selectedIDs.length; i++) {
                result += selectedIDs[i] + ",";
            }
            return result;
        }
    }

    function getOneSelectedRows() {
        var grid = $("#grid-table");
        var rowKey = grid.getGridParam("selrow");
        if (!rowKey) {
            return "-1";
        } else {
            var selectedIDs = grid.getGridParam("selarrrow");
            if (selectedIDs.length == 1) {
                return selectedIDs[0];
            } else {
                return "-2";
            }
        }
    }

    function reloadGrid() {
        $("#grid-table").trigger("reloadGrid"); //重新载入
    }

    function getDictMapData() {
        var submitData = {};
        $.post("${context_path}/dict/getSearchStatisUseDict", submitData, function (data) {
            var watersType = data.WatersType;
            for (var i = 0; i < watersType.length; i++) {
                $("#watersType").append("<option value='" + watersType[i].value + "'>" + watersType[i].name + "</option>");
            }
            var street = data.Street;
            for (var i = 0; i < street.length; i++) {
                $("#street").append("<option value='" + street[i].value + "'>" + street[i].name + "</option>");
            }
            var meterAttr = data.MeterAttr;
            for (var i = 0; i < meterAttr.length; i++) {
                $("#meterAttr").append("<option value='" + meterAttr[i].value + "'>" + meterAttr[i].name + "</option>");
            }
        }, "json");
    }

    $(function () {
        getDictMapData();
    })

    jQuery(function ($) {
        document.onkeydown = function (e) {
            var theEvent = window.event || e;
            var code = theEvent.keyCode || theEvent.which;
            if (code == 13) {
                $('#btn_search').click();
            }
        }
    })
</script>

</body>
</html>
