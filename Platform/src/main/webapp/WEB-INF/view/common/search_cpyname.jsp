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
                    $(this).css("background-color", "#ebebeb");
                });
                newDivNode.mouseout(function () {
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
        old_value = $("#name").val();
        $("#name").focus(function () {
            if ($("#name").val() == "") {
                AutoComplete("auto_div", "name", name_list);
            }
        });
        $("#name").keyup(function () {
            AutoComplete("auto_div", "name", name_list);
        });
    });

</script>