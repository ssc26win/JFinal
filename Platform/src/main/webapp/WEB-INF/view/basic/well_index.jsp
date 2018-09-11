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
                                        <form id="exportForm" action="${context_path}/basic/well/export" method="post">
                                            <input type="text" style="display:none"/>
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
                            <jc:button className="btn btn-primary" id="btn-add" permission="/basic/well" textName="添加"/>
                            <jc:button className="btn btn-info" id="btn-edit" permission="/basic/well" textName="编辑"/>
                            <jc:button className="btn btn-danger" id="btn-deleteData" permission="/basic/well" textName="删除"/>
                            <jc:button className="btn btn-warning" id="btn-importData" permission="/basic/well" textName="导入"/>
                            <jc:button className="btn btn-success" id="btn-exportData" textName="导出"/>
                        </div>
                    </div>
                    <!-- PAGE CONTENT BEGINS -->
                    <table id="grid-table"></table>

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

        $("#grid-table").jqGrid({
            url:'${context_path}/basic/well/getListData',
            mtype: "GET",
            datatype: "json",
            colModel: [
                { label: '单位名称', name: 'companyName', width: 250, sortable:false},
                { label: '单位编号', name: 'real_code', width: 70, sortable:true},
                { label: '水井编号', name: 'well_num', width: 100, sortable:false},
                { label: '水井名称', name: 'name', width: 250,sortable:false},
                { label: '所属节水办', name: 'water_unit', width: 100, sortable:false},
                { label: '所属区县', name: 'county', width: 70, sortable:false},
                { label: '乡', name: 'streetName', width: 150, sortable:false},
                { label: '村', name: 'village', width: 150, sortable:false},
                { label: '水井地址', name: 'address', width: 120, sortable:false},
                { label: '成井时间（年）', name: 'year', width: 90, sortable:true},
                { label: '井深（米）', name: 'well_depth', width: 90, sortable:true},
                { label: '地下水埋深（米）', name: 'ground_depth', width: 120, sortable:true},
                { label: '是否为单位自备井', name: 'oneselfWellName', width: 150, sortable:false},
                { label: '井口井管内径（毫米）', name: 'inner_diameter', width: 160, sortable:true},
                { label: '井壁管材料', name: 'material', width: 90, sortable:false},
                { label: '应用状况', name: 'application', width: 60, sortable:false},
                { label: '水源类型', name: 'waters_type', width: 60, sortable:false},
                { label: '是否已配套机电设备', name: 'electromechanicsName', width: 170, sortable:false},
                { label: '是否已安装水量计量设施', name: 'calculateWaterName', width: 180, sortable:false},
                { label: '水泵型号', name: 'pump_model', width: 80, sortable:false},
                { label: '水量计量设施类型', name: 'calculateTypeName', width: 120, sortable:false},
                { label: '是否为规模以上地下水水源地的水井', name: 'aboveScaleName', width:200, sortable:false},
                { label: '所在地貌类型区', name: 'geomorphicTypeName', width: 100, sortable:false},
                { label: '所取用地下水的类型', name: 'groundTypeName', width: 140, sortable:false},
                { label: '所在水资源三级区名称及编码', name: 'name_code', width: 200, sortable:false},
                { label: '主要取水用途及效益', name: 'use_efficiency', width: 130, sortable:false},
                { label: '取水量确定方法', name: 'method', width: 100, sortable:false},
                { label: '是否已办理取水许可证', name: 'licenceName', width: 140, sortable:false},
                { label: '取水许可证编号', name: 'licence_code', width: 100, sortable:false},
                { label: '年许可取水量（万立方米）', name: 'water_withdrawals', width: 180, sortable:true}
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
            //$("#jqgrid").setGridWidth($(window).width()*0.75);
            //	$("#grid-table").setGridHeight($(window).height()-200);
        });
        $("#btn_search").click(function(){
            //此处可以添加对查询数据的合法验证
            var name = $("#name").val();
            $("#grid-table").jqGrid('setGridParam',{
                datatype:'json',
                postData:{'name':name}, //发送数据
                page:1
            }).trigger("reloadGrid"); //重新载入
        });
        $("#btn-add").click(function(){//添加页面
            parent.layer.open({
                title:'添加水井',
                type: 2,
                area: ['900px', '600px'],
                fix: false, //不固定
                maxmin: true,
                content: '${context_path}/basic/well/add'
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
                    area: ['900px', '600px'],
                    fix: false, //不固定
                    maxmin: true,
                    content: '${context_path}/basic/well/add?id='+rid
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
            $.post("${context_path}/basic/well/delete", submitData,function(data) {
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
            title:'导入水井信息',
            type: 2,
            area: ['770px', '400px'],
            fix: false, //不固定
            maxmin: true,
            content: '${context_path}/basic/well/importPage'
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
</script>

</body>
</html>
