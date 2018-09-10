<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>后台管理系统</title>

    <meta name="description" content="overview &amp; stats" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <!-- bootstrap & fontawesome -->
    <jsp:include page="/WEB-INF/view/common/basecss.jsp" flush="true" />
</head>
<body class="no-skin">
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try{ace.settings.check('main-container' , 'fixed')}catch(e){}
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
                                    <div class="col-xs-12 col-sm-8">
                                        <form id="exportForm" action="${context_path}/basic/meter/export" method="post">
                                            <input type="hidden" id="flagType" name="flagType" value="${flag}"/>
                                            <div class="input-group">
                                                    <span class="input-group-addon">
                                                        <i class="ace-icon fa fa-check"></i>
                                                    </span>
                                                <input type="text" id="name" name="name" class="form-control search-query" placeholder="请输入关键字" />
                                                    <span class="input-group-btn">
                                                        <button type="button" id="btn_search" class="btn btn-purple btn-sm">
                                                            <span class="ace-icon fa fa-search icon-on-right bigger-110"></span>
                                                            搜索
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
                <div class="col-xs-12">
                    <div class="row-fluid" style="margin-bottom: 5px;">
                        <div class="span12 control-group">
                            <jc:button className="btn btn-primary" id="btn-add" permission="/basic/meter" textName="添加"/>
                            <jc:button className="btn btn-info" id="btn-edit" permission="/basic/meter" textName="编辑"/>
                            <jc:button className="btn btn-danger" id="btn-deleteData" permission="/basic/meter" textName="删除"/>
                            <jc:button className="btn btn-warning" id="btn-importData" permission="/basic/meter" textName="导入"/>
                            <jc:button className="btn btn-success" id="btn-exportData" textName="导出"/>
                        </div>
                    </div>
                    <!-- PAGE CONTENT BEGINS -->
                    <table id="grid-table_meter"></table>

                    <div id="grid-pager_meter"></div>

                    <script type="text/javascript">
                        var $path_base = "..";//in Ace demo this will be used for editurl parameter
                    </script>

                    <!-- PAGE CONTENT ENDS -->
                </div><!-- /.col -->
            </div><!-- /.row -->
        </div>
    </div>
</div><!-- /.main-container -->
<!-- basic scripts -->
<jsp:include page="/WEB-INF/view/common/basejs.jsp" flush="true" />

<script type="text/javascript">
    $(document).ready(function () {
        var grid_selector = "#grid-table_meter";
        var pager_selector = "#grid-pager_meter";
        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            $(grid_selector).jqGrid( 'setGridWidth', $(".page-content").width() );
        });
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid' , function(ev, event_name, collapsed) {
            if( event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed' ) {
                //setTimeout is for webkit only to give time for DOM changes and then redraw!!!
                setTimeout(function() {
                    $(grid_selector).jqGrid('setGridWidth', parent_column.width() );
                }, 0);
            }
        });
        var flag = '${flag}';
        var url = '${context_path}/basic/meter/getListData';
        if (flag != null && flag != undefined && flag != '') {
            url = '${context_path}/basic/meter/get'+flag+'ListData/';
        }
        var term = '${term}';
        if (term != null && term != undefined && term != '') {
            url = '${context_path}/basic/meter/getListData?term=' +term;
        }
        $("#grid-table_meter").jqGrid({
            url:url,
            mtype: "GET",
            datatype: "json",
            colModel: [
                { label: '单位名称', name: 'companyName', width: 250, sortable:false},
                { label: '单位编号', name: 'real_code', width: 70, sortable:true},
                { label: '所属节水办', name: 'water_unit', width: 100, sortable:false},
                { label: '所属区县', name: 'county', width: 70, sortable:false},
                { label: '路别', name: 'line_num', width: 70, sortable:false},
                { label: '水表表号', name: 'meter_num', width: 70,sortable:false},
                { label: '表计地址', name: 'meter_address', width: 90,sortable:false},
                { label: '最小单位', name: 'times', width: 70, sortable:false},
                { label: '水源类型', name: 'watersTypeName', width: 70, sortable:false},
                { label: '国标行业', name: 'gb_industry', width: 120, sortable:false},
                { label: '主要行业', name: 'main_industry', width: 100, sortable:false},
                { label: '取水用途', name: 'waterUseTypeName', width: 100, sortable:false},
                { label: '水表属性', name: 'meterAttrName', width: 150, sortable:false},
                { label: '收费类型', name: 'chargeTypeName', width: 80, sortable:false},
                { label: '计费周期', name: 'billing_cycle', width: 80, sortable:false},
                { label: '注册日期', name: 'regist_date', width: 130, sortable:true},
                { label: '备注信息', name: 'memo', width: 120, sortable:false},
                { label: '生产厂家', name: 'vender', width: 120, sortable:false},
                { label: '周期', name: 'termName', width: 70, sortable:false}
            ],
            viewrecords: true,
            height: 560,
            rowNum: 20,
            multiselect: true,//checkbox多选
            altRows: true,//隔行变色
            shrinkToFit:false,
            autoScroll: true,
            recordtext:"{0} - {1} 共 {2} 条",
            pgtext:"第 {0} 页 共 {1} 页",
            pager: pager_selector,
            loadComplete : function() {
                var table = this;
                setTimeout(function(){
                    updatePagerIcons(table);
                }, 0);
            }
        });
        $(window).triggerHandler('resize.jqGrid');

        $(window).bind('resize', function() {
            $("#jqgrid").setGridWidth($(window).width()*0.75);
            $("#grid-table_meter").setGridHeight($(window).height()-200);
        });
        $("#btn_search").click(function(){
            //此处可以添加对查询数据的合法验证
            var name = $("#name").val();
            $("#grid-table_meter").jqGrid('setGridParam',{
                datatype:'json',
                postData:{'name':name}, //发送数据
                page:1
            }).trigger("reloadGrid"); //重新载入
        });
        $("#btn-add").click(function(){//添加页面
            parent.layer.open({
                title:'添加新水表',
                type: 2,
                area: ['770px', '580px'],
                fix: false, //不固定
                maxmin: true,
                content: '${context_path}/basic/meter/add'
            });
        });
        $("#btn-deleteData").click(function(){
            deleteData();
        });
        $("#btn-edit").click(function(){//添加页面
            var rid = getOneSelectedRows();
            if(rid == -1){
                layer.msg("请选择一个记录", {
                    icon: 2,
                    time: 1000 //2秒关闭（如果不配置，默认是3秒）
                });
            }else if(rid == -2 ){
                layer.msg("只能选择一个记录", {
                    icon: 2,
                    time: 1000 //2秒关闭（如果不配置，默认是3秒）
                });
            }else {
                parent.layer.open({
                    title:'修改水表信息',
                    type: 2,
                    area: ['770px', '580px'],
                    fix: false, //不固定
                    maxmin: true,
                    content: '${context_path}/basic/meter/add?id='+rid
                });
            }
        });
        $("#btn-exportData").click(function(){
            $("#exportForm").submit();
        });
    });
    //replace icons with FontAwesome icons like above
    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
            'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
            'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
            'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
            if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
        })
    }

    /**获取选中的列***/
    function getSelectedRows() {
        var grid = $("#grid-table_meter");
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
        var grid = $("#grid-table_meter");
        var rowKey = grid.getGridParam("selrow");
        if (!rowKey){
            return "-1";
        }else {
            var selectedIDs = grid.getGridParam("selarrrow");
            if(selectedIDs.length==1){
                return selectedIDs[0];
            }else{
                return "-2";
            }
        }
    }

    function deleteData(){
        var rid = getOneSelectedRows();
        if(rid == -1) {
            layer.msg("请选择一个记录", {
                icon: 2,
                time: 1000 //2秒关闭（如果不配置，默认是3秒）
            });
            return;
        }
        var submitData = {
            "ids" : getSelectedRows()
        };
        layer.confirm("确认删除记录？",function(){
            $.post("${context_path}/basic/meter/delete", submitData,function(data) {
                if (data.code == 0) {
                    layer.msg("操作成功", {
                        icon: 1,
                        time: 1000 //1秒关闭（如果不配置，默认是3秒）
                    },function(){
                        reloadGrid();
                    });
                }  else{
                    layer.alert("操作失败");
                }
            },"json");
        });
    }

    function reloadGrid(){
        $("#grid-table_meter").trigger("reloadGrid"); //重新载入
    }

    $("#btn-importData").click(function(){//添加页面
        parent.layer.open({
            title:'导入水表信息',
            type: 2,
            area: ['770px', '400px'],
            fix: false, //不固定
            maxmin: true,
            content: '${context_path}/basic/meter/importPage'
        });
    });
</script>

</body>
</html>
