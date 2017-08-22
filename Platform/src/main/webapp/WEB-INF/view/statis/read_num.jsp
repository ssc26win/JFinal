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
                                    <div class="col-xs-12 col-sm-5">
                                        <div class="input-group">
                                            日期时间:
                                            <input type="text" id="startTime" name="startTime" class="form_datetime"/>~<input type="text" id="endTime" name="endTime" class="form_datetime"/>
                                            <input type="text" id="name" name="name" class="" placeholder="请输入单位名称" style="margin-left: 5px;"/>
                                            <input type="text" id="innerCode" name="innerCode" class="" placeholder="请输入单位编号" style="margin-left: 5px;"/>
                                                <span class="input-group-btn">
                                                    <button type="button" id="btn_search" class="btn btn-purple btn-sm">
                                                        <span class="ace-icon fa fa-search icon-on-right bigger-110"></span>
                                                        搜索
                                                    </button>
                                                    <button type="button" id="exportData" class="btn btn-success btn-sm" style="margin-left:10px;">
                                                        导出
                                                    </button>
                                                </span>
                                        </div>
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
            url:'${context_path}/statis/reasdnum/getListData',
            mtype: "GET",
            datatype: "json",
            colModel: [
                { label: '单位名称', name: 'name', width: 120, sortable:false},
                { label: '单位编号', name: 'inner_code', width: 80, sortable:false},
                { label: '路别', name: 'line_num', width: 100, sortable:false},
                { label: '水表表号', name: 'meter_num', width: 100,sortable:false},
                { label: '水源类型', name: 'waters_type', width: 45, sortable:false},
                { label: '水表属性', name: 'alarm', width: 45, sortable:false},
                { label: '查询时间', name: 'write_time', width: 100, sortable:true},
                { label: '水表读数', name: 'read_num', width: 80, sortable:false},
                { label: '单位地址', name: 'address', width: 100,sortable:false}
            ],
            viewrecords: true,
            height: 560,
            rowNum: 10,
            multiselect: true,//checkbox多选
            altRows: true,//隔行变色
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
            $("#grid-table").setGridHeight($(window).height()-200);
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

    function reloadGrid(){
        $("#grid-table").trigger("reloadGrid"); //重新载入
    }
</script>
<script type="text/javascript">
    $(function () {
        $(".form_datetime").datetimepicker({
            format: 'YYYY-MM-DD hh:mm:ss',
            locale: moment.locale('zh-cn', {
                months: '一月_二月_三月_四月_五月_六月_七月_八月_九月_十月_十一月_十二月'.split('_'),
                monthsShort: '1月_2月_3月_4月_5月_6月_7月_8月_9月_10月_11月_12月'.split('_'),
                weekdays: '星期日_星期一_星期二_星期三_星期四_星期五_星期六'.split('_'),
                weekdaysShort: '周日_周一_周二_周三_周四_周五_周六'.split('_'),
                weekdaysMin: '日_一_二_三_四_五_六'.split('_'),
                longDateFormat: {
                    LT: 'Ah点mm分',
                    LTS: 'Ah点m分s秒',
                    L: 'YYYY-MM-DD',
                    LL: 'YYYY年MMMD日',
                    LLL: 'YYYY年MMMD日Ah点mm分',
                    LLLL: 'YYYY年MMMD日ddddAh点mm分',
                    l: 'YYYY-MM-DD',
                    ll: 'YYYY年MMMD日',
                    lll: 'YYYY年MMMD日Ah点mm分',
                    llll: 'YYYY年MMMD日ddddAh点mm分'
                },
                meridiemParse: /凌晨|早上|上午|中午|下午|晚上/,
                meridiemHour: function (h, meridiem) {
                    let hour = h;
                    if (hour === 12) {
                        hour = 0;
                    }
                    if (meridiem === '凌晨' || meridiem === '早上' ||
                            meridiem === '上午') {
                        return hour;
                    } else if (meridiem === '下午' || meridiem === '晚上') {
                        return hour + 12;
                    } else {
                        // '中午'
                        return hour >= 11 ? hour : hour + 12;
                    }
                },
                meridiem: function (hour, minute, isLower) {
                    const hm = hour * 100 + minute;
                    if (hm < 600) {
                        return '凌晨';
                    } else if (hm < 900) {
                        return '早上';
                    } else if (hm < 1130) {
                        return '上午';
                    } else if (hm < 1230) {
                        return '中午';
                    } else if (hm < 1800) {
                        return '下午';
                    } else {
                        return '晚上';
                    }
                },
                calendar: {
                    sameDay: function () {
                        return this.minutes() === 0 ? '[今天]Ah[点整]' : '[今天]LT';
                    },
                    nextDay: function () {
                        return this.minutes() === 0 ? '[明天]Ah[点整]' : '[明天]LT';
                    },
                    lastDay: function () {
                        return this.minutes() === 0 ? '[昨天]Ah[点整]' : '[昨天]LT';
                    },
                    nextWeek: function () {
                        let startOfWeek, prefix;
                        startOfWeek = moment().startOf('week');
                        prefix = this.diff(startOfWeek, 'days') >= 7 ? '[下]' : '[本]';
                        return this.minutes() === 0 ? prefix + 'dddAh点整' : prefix + 'dddAh点mm';
                    },
                    lastWeek: function () {
                        let startOfWeek, prefix;
                        startOfWeek = moment().startOf('week');
                        prefix = this.unix() < startOfWeek.unix() ? '[上]' : '[本]';
                        return this.minutes() === 0 ? prefix + 'dddAh点整' : prefix + 'dddAh点mm';
                    },
                    sameElse: 'LL'
                },
                ordinalParse: /\d{1,2}(日|月|周)/,
                ordinal: function (number, period) {
                    switch (period) {
                        case 'd':
                        case 'D':
                        case 'DDD':
                            return number + '日';
                        case 'M':
                            return number + '月';
                        case 'w':
                        case 'W':
                            return number + '周';
                        default:
                            return number;
                    }
                },
                relativeTime: {
                    future: '%s内',
                    past: '%s前',
                    s: '几秒',
                    m: '1 分钟',
                    mm: '%d 分钟',
                    h: '1 小时',
                    hh: '%d 小时',
                    d: '1 天',
                    dd: '%d 天',
                    M: '1 个月',
                    MM: '%d 个月',
                    y: '1 年',
                    yy: '%d 年'
                },
                week: {
                    // GB/T 7408-1994《数据元和交换格式·信息交换·日期和时间表示法》与ISO 8601:1988等效
                    dow: 1, // Monday is the first day of the week.
                    doy: 4  // The week that contains Jan 4th is the first week of the year.
                }
            })
        });
    });
</script>
</body>
</html>
