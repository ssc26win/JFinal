<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>通州区节水管理平台</title>

    <meta name="description" content="overview &amp; stats" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <!-- bootstrap & fontawesome -->
    <jsp:include page="/WEB-INF/view/common/basecss.jsp" flush="true" />
</head>
<style type="text/css">
    html {
        overflow-x: hidden;
        overflow-y: hidden;
    }
</style>
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
                                    <div class="col-sm-12">
                                        <form id="exportForm" action="${context_path}/basic/company/export" method="post">
                                            <input type="text" style="display:none"/>
                                            <input type="hidden" id="flagType" name="flagType" value="${flag}"/>
                                            <div class="input-group">
                                                    <input type="text" id="name" name="name" class="search-query" style="height: 34px;width: 400px;" placeholder="请输入关键字" />
                                                    <select id="company_type" name="company_type" style="height: 34px;width: 159px;margin-left: 5px;"><option value="">请选择单位类型</option>
                                                    </select>
                                                    <span class="" style="margin-left: 10px;margin-bottom: 5px;">
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
                            <jc:button className="btn btn-primary" id="btn-add" permission="/basic/company" textName="添加"/>
                            <jc:button className="btn btn-info" id="btn-edit" permission="/basic/company" textName="编辑"/>
                            <jc:button className="btn btn-danger" id="btn-deleteData" permission="/basic/company" textName="删除"/>
                            <jc:button className="btn btn-warning" id="btn-importData" permission="/basic/company" textName="导入"/>
                            <jc:button className="btn btn-success" id="btn-exportData" textName="导出"/>
                        </div>
                    </div>
                    <!-- PAGE CONTENT BEGINS -->

                    <table id="grid-table"></table>
                </div>
                <div class="col-xs-12">
                    <div id="grid-pager"></div>

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
        var grid_selector = "#grid-table";
        var pager_selector = "#grid-pager";
        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            $(grid_selector).jqGrid( 'setGridWidth', $(".page-content").width() );
        });
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid' , function(ev, event_name, collapsed) {
            if( event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed' ) {
                //setTimeout is for webkit only to give time for DOM changes and then redraw!!!
                setTimeout(function() {
                    $(grid_selector).jqGrid( 'setGridWidth', parent_column.width() );
                }, 0);
            }
        });
        var flag = '${flag}';
        var url = '${context_path}/basic/company/getListData';
        if (flag != null && flag != undefined && flag != '') {
            url = '${context_path}/basic/company/get'+flag+'ListData';
        }

        var companyType = '${companyType}';
        if (companyType != null && companyType != undefined && companyType != '') {
            url = '${context_path}/basic/company/getListData?companyType=' +companyType;
            $("#company_type").val(companyType);
        }

        var term = '${term}';
        if (term != null && term != undefined && term != '') {
            url = '${context_path}/basic/company/getListData?term=' +term;
        }
        $("#grid-table").jqGrid({
            url:url,
            mtype: "GET",
            datatype: "json",
            colModel: [
                { label: '所属节水办', name: 'water_unit', width: 100, sortable:false},
                { label: '单位名称', name: 'name', width: 280, sortable:false},
                { label: '单位编号', name: 'real_code', width: 80, sortable:false},
                { label: '所属区县', name: 'county', width: 80, sortable:false},
                { label: '所属乡镇', name: 'streetName', width: 100, sortable:false},
                { label: '原乡镇或街道', name: 'street_src', width: 120, sortable:false},
                { label: '单位地址', name: 'address', width: 180,sortable:false},
                { label: '单位类型', name: 'companyTypeName', width: 100, sortable:false},
                { label: '用户类型', name: 'customerTypeName', width: 100, sortable:false},
                { label: '国标行业', name: 'gb_industry', width: 120, sortable:false},
                { label: '主要行业', name: 'main_industry', width: 120, sortable:false},
                { label: '取水用途', name: 'waterUseTypeName', width: 90, sortable:false},
                { label: '联系人', name: 'contact', width: 70, sortable:false},
                { label: '联系电话', name: 'phone', width: 120, sortable:false},
                { label: '邮政编码', name: 'postal_code', width: 80, sortable:false},
                { label: '管水部门', name: 'department', width: 80, sortable:false},
                { label: '水井数量', name: 'well_count', width: 80, sortable:true},
                { label: '一级表数量', name: 'first_watermeter_count', width: 80, sortable:true},
                { label: '远传表数量', name: 'remotemeter_count', width: 80, sortable:true},
                { label: '节约用水型单位类型', name: 'unitTypeName', width: 130, sortable:false},
                { label: '自备井水价', name: 'self_well_price', width: 80, sortable:true},
                { label: '地表水水价', name: 'surface_price', width: 80, sortable:true},
                { label: '自来水水价', name: 'self_free_price', width: 80, sortable:true},
                { label: '注册时间', name: 'create_time', width: 140, sortable:true},
                { label: '周期', name: 'termName', width: 80, sortable:false},
                { label: '备注信息', name: 'memo', width: 150, sortable:false}
            ],
            viewrecords: true,
            height:560,
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
        setTimeout(function () {
            $("#jqgrid").setGridWidth($(window).width()*0.75);
            $("#grid-table").setGridHeight($(window).height()-240);
        }, 500);
        $(window).bind('resize', function() {
            $("#jqgrid").setGridWidth($(window).width()*0.75);
            $("#grid-table").setGridHeight($(window).height()-200);
        });
        $("#btn_search").click(function(){
            //此处可以添加对查询数据的合法验证
            var name = $("#name").val();
            var company_type = $("#company_type").val();
            $("#grid-table").jqGrid('setGridParam',{
                datatype:'json',
                postData:{'name':name,'companyType':company_type},//发送数据
                page:1
            }).trigger("reloadGrid"); //重新载入
        });
        $("#btn-add").click(function(){//添加页面
            parent.layer.open({
                title:'添加新单位',
                type: 2,
                area: ['850px', '600px'],
                fix: false, //不固定
                maxmin: true,
                content: '${context_path}/basic/company/add'
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
                    title:'修改单位信息',
                    type: 2,
                    area: ['850px', '600px'],
                    fix: false, //不固定
                    maxmin: true,
                    content: '${context_path}/basic/company/add?id='+rid
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
            $.post("${context_path}/basic/company/delete", submitData, function(data) {
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
        $("#grid-table").trigger("reloadGrid"); //重新载入
    }

    $("#btn-importData").click(function(){//添加页面
        parent.layer.open({
            title:'导入水表信息',
            type: 2,
            area: ['770px', '400px'],
            fix: false, //不固定
            maxmin: true,
            content: '${context_path}/basic/company/importPage'
        });
    });

    jQuery(function($) {
        document.onkeydown = function (e) {
            var theEvent = window.event || e;
            var code = theEvent.keyCode || theEvent.which;
            if (code == 13) {
                $('#btn_search').click();
            }
        }
    })
    function getDictMapData() {
        var submitData = {};
        $.post("${context_path}/dict/getCompanyUseDict", submitData, function (data) {
            var companyType = data.CompanyType;
            var companyTypeTarget = '${companyType}';
            if (companyTypeTarget != null && companyTypeTarget != undefined && companyTypeTarget != '') {
                parseInt(companyTypeTarget);
            }
            for(var i = 0;i<companyType.length;i++) {
                $("#company_type").append("<option value='" + companyType[i].value + "'>"+companyType[i].name+"</option>");
            }
            $("#company_type").val(companyTypeTarget);
        }, "json");
    }

    $(function () {
        getDictMapData();
    })
</script>

</body>
</html>
