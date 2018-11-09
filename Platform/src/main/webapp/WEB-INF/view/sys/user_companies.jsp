<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>通州区节水管理平台</title>

    <meta name="description" content="overview &amp; stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/font-awesome.css"/>

    <!-- page specific plugin styles -->
    <link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/jquery-ui.css"/>
    <link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/datepicker.css"/>
    <link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/ui.jqgrid.css"/>

    <!-- text fonts -->
    <link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/ace-fonts.css"/>

    <!-- ace styles -->
    <link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/ace.css" class="ace-main-stylesheet"
          id="main-ace-style"/>

    <!-- ztree styles -->
    <link rel="stylesheet" href="${res_url}js/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/ace-part2.css" class="ace-main-stylesheet"/>
    <![endif]-->

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/ace-ie.css"/>
    <![endif]-->

    <!-- inline styles related to this page -->

    <!-- ace settings handler -->
    <script src="${res_url}ace-1.3.3/assets/js/ace-extra.js"></script>

    <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

    <!--[if lte IE 8]>
    <script src="${res_url}ace-1.3.3/assets/js/html5shiv.js"></script>
    <script src="${res_url}ace-1.3.3/assets/js/respond.js"></script>
    <![endif]-->
    <style>
        html {
            overflow-x: hidden;
            overflow-y: hidden;
        }
        body {
            background-color: #ffffff;
        }
        .search {
            margin-top: 10px;
            left: 20px;
            margin-left: 20px;
            display: inline-block;
            width: 30px;
            height: 30px;
        }
        .left {
            /*position: absolute;*/
            margin-top: 10px;
            top: 30px;
            left: 20px;
            /*width: 358px;*/
            overflow-y: auto;
        }
        .tree-menu {
            float: right;
        }
        .tree-menu span {
            margin-left: 6px;
        }
        .tree-menu span i {
            cursor: pointer;
        }
        .icon-plus {
            background-position: -408px -96px;
        }
        .icon-remove {
            background-position: -312px 0;
        }
        .icon-edit {
            background-position: -96px -72px;
        }
        [class^="icon-"], [class*=" icon-"] {
            display: inline-block;
            width: 14px;
            height: 14px;
            line-height: 14px;
            vertical-align: text-top;
            background-image: url("${basePath}res/bootstrap/img/glyphicons-halflings.png");
            background-repeat: no-repeat;
            margin-top: 1px;
        }
        #menu_tree {
            margin-right: 20px;
        }
        li {
            line-height: 16px;
        }
        .om-tree-node a {
            display: inline-block;
            *display: inline;
            *zoom: 1;
            width: 115px;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        #vip_tip {
            text-align: center;
        }
        .actions {
            position: absolute;
            bottom: 20px;
            left: 10px;
            width: 368px;
            border-right: 1px solid #CCC;
            height: 60px;
        }
        .ztree li span.button.add {
            margin-left: 15px;
            margin-right: -1px;
            background-position: -144px 0;
            vertical-align: top;
            *vertical-align: middle
        }
    </style>
</head>

<body>
<div class="search" style="display: none;">
    <input type="text" id="companyName" name="companyName" value="${companyName}" class="cnwth" style="width: 400px;"
           autocomplete="off" placeholder="请输入单位...">
    <div id="auto_div" style="max-height: 300px; overflow-y: auto;"></div>
    <input type="hidden" id="innerCode" name="innerCode" value="">
</div>
<div class="left">
    <ul id="treeDemo" class="ztree"></ul>
</div>
<div class="actions" style="margin-left: 330px;margin-top: 30px;">
    <jc:button className="btn btn-big btn-primary" id="btn_saveOrder" textName="保存"/>
</div>
<!-- basic scripts -->

<!--[if !IE]> -->
<script type="text/javascript">
    window.jQuery || document.write("<script src='${res_url}ace-1.3.3/assets/js/jquery.js'>" + "<" + "/script>");
</script>

<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
    window.jQuery || document.write("<script src='${res_url}ace-1.3.3/assets/js/jquery1x.js'>" + "<" + "/script>");
</script>
<![endif]-->
<script type="text/javascript">
    if ('ontouchstart' in document.documentElement) document.write("<script src='${res_url}ace-1.3.3/assets/js/jquery.mobile.custom.js'>" + "<" + "/script>");
</script>
<script src="${res_url}ace-1.3.3/assets/js/bootstrap.js"></script>

<script src="${res_url}ace-1.3.3/assets/js/date-time/bootstrap-datepicker.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/jqGrid/jquery.jqGrid.src.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/jqGrid/i18n/grid.locale-en.js"></script>

<!-- ace scripts -->

<!-- ace scripts -->
<script src="${res_url}ace-1.3.3/assets/js/ace/elements.scroller.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/ace/elements.colorpicker.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/ace/elements.fileinput.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/ace/elements.typeahead.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/ace/elements.wysiwyg.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/ace/elements.spinner.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/ace/elements.treeview.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/ace/elements.wizard.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/ace/elements.aside.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/ace/ace.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/ace/ace.ajax-content.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/ace/ace.touch-drag.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/ace/ace.sidebar.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/ace/ace.sidebar-scroll-1.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/ace/ace.submenu-hover.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/ace/ace.widget-box.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/ace/ace.settings.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/ace/ace.settings-rtl.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/ace/ace.settings-skin.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/ace/ace.widget-on-reload.js"></script>
<script src="${res_url}ace-1.3.3/assets/js/ace/ace.searchbox-autocomplete.js"></script>
<script src="${res_url}js/layer/layer.js"></script>
<script type="text/javascript" src="${res_url}js/ztree/js/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript" src="${res_url}js/ztree/js/jquery.ztree.excheck-3.5.min.js"></script>
<script type="text/javascript" src="${res_url}js/ztree/js/jquery.ztree.exedit-3.5.min.js"></script>
<script type="text/javascript">
    $(document).ready(function () {

    });
</script>
<script>

    $(function () {

        $("#btn_saveOrder").click(function () {
            //alert("sadf");
            $("#btn_saveOrder").attr("disabled", "disabled");
            var nodes = zTree_Menu.getCheckedNodes(true);
            var selectIds = "";
            for (var index in nodes) {
                var item = nodes[index];
                if (item.id == '0' || parseInt(item.id) > 100000000)continue;
                selectIds += item.id + ",";
            }

            var submitData = {
                "companiesIds": selectIds,
                "uId": "${uId}"
            }
            $.post("${context_path}/sys/user/saveCompanies", submitData,
                    function (data) {
                        $("#btn_saveOrder").removeAttr("disabled");
                        if (data.code == 0) {
                            layer.msg('操作成功', {
                                icon: 1,
                                time: 1000 //2秒关闭（如果不配置，默认是3秒）
                            }, function () {
                                parent.reloadGrid();
                                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                parent.layer.close(index); //再执行关闭
                            });
                        } else {
                            layer.msg(data.msg, {
                                icon: 2,
                                time: 1000 //2秒关闭（如果不配置，默认是3秒）
                            }, function () {
                            });
                        }
                    }, "json");
        });
        resize();
        $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        zTree_Menu = $.fn.zTree.getZTreeObj("treeDemo");

        $(window).resize(function () {
            resize();
        });
        function resize() {
            h = $(window).height(),
                th = $("#top").outerHeight(true),
                    mh = $(".main-title h3").outerHeight(true);
            $(".left").height(h - th - mh - 55);
        }
    });
    var setting = {
        check: {
            enable: true
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            beforeCheck: beforeCheck,
            onCheck: onCheck
        },
        view: {
            filter: true,  //是否启动过滤
            expandLevel: 0,  //展开层级
            showFilterChildren: true, //是否显示过滤数据孩子节点
            showFilterParent: true, //是否显示过滤数据父节点
            showLine: false
        }

    };
    function beforeCheck(treeId, treeNode) {
        return (treeNode.doCheck !== false);
    }
    function onCheck(e, treeId, treeNode) {

    }

    var log, className = "dark";
    var zNodes = ${jsonTree.data};
    setting.check.chkboxType = {"Y": "ps", "N": "ps"};

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
                        $.fn.zTree.init($("#treeDemo"), setting, zNodes);
                    }
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
                    filter();
                    $("#companyName").focus();
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
        $("#auto_div").css("width", $(".cnwth").css("width"));

    });

    //过滤ztree显示数据
    function filter() {
        if ($("#innerCode").val() != "") {
            var submitData = {
                "innerCode": $("#innerCode").val()
            };
            $.post("${context_path}/sys/user/setCompanies", submitData,
                    function (data) {
                        var filterzNodes = data.data;
                        if (filterzNodes != '') {
                            $.fn.zTree.init($("#treeDemo"), setting, JSON.parse(filterzNodes));
                        }
                    }, "json")
        }
    }
</script>
</body>
</html>