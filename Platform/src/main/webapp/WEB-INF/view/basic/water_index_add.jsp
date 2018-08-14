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
              <div class="form-group" style="margin-top:15px;">
                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="innerCode">单位编号:</label>
                <div class="col-xs-12 col-sm-9">
                  <div class="clearfix">
                    <input type="text" id="innerCode" name="innerCode" value="${item.innerCode}" class="col-xs-12 col-sm-6">
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="watersType">水源类型:</label>
                <div class="col-xs-12 col-sm-9">
                  <div class="clearfix">
                    <select id="watersType" name="watersType" class="col-xs-12 col-sm-6"></select>
                    <input type="hidden" id="watersTypeInput" name="watersTypeInput" value="${item.watersType}">
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="waterIndex">年用水指标(立方米):</label>
                <div class="col-xs-12 col-sm-9">
                  <div class="clearfix">
                    <input type="number" id="waterIndex" name="waterIndex" value="${item.waterIndex}" class="col-xs-12 col-sm-6">
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-3 control-label" for="january">一月:</label>
                <div class="col-sm-2">
                  <input type="number" id="january" name="january" value="${item.january}" class="form-control">
                </div>
                <label class="col-sm-2 control-label" for="february">二月:</label>
                <div class="col-sm-2">
                  <input type="number" id="february" name="february" value="${item.february}" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-3 control-label" for="march">三月:</label>
                <div class="col-sm-2">
                  <input type="number" id="march" name="march" value="${item.march}" class="form-control">
                </div>
                <label class="col-sm-2 control-label" for="april">四月:</label>
                <div class="col-sm-2">
                  <input type="number" id="april" name="april" value="${item.april}" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-3 control-label" for="may">五月:</label>
                <div class="col-sm-2">
                  <input type="number" id="may" name="may" value="${item.may}" class="form-control">
                </div>
                <label class="col-sm-2 control-label" for="june">六月:</label>
                <div class="col-sm-2">
                  <input type="number" id="june" name="june" value="${item.june}" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-3 control-label" for="july">七月:</label>
                <div class="col-sm-2">
                  <input type="number" id="july" name="july" value="${item.july}" class="form-control">
                </div>
                <label class="col-sm-2 control-label" for="august">八月:</label>
                <div class="col-sm-2">
                  <input type="number" id="august" name="august" value="${item.august}" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-3 control-label" for="september">九月:</label>
                <div class="col-sm-2">
                  <input type="number" id="september" name="september" value="${item.september}" class="form-control">
                </div>
                <label class="col-sm-2 control-label" for="october">十月:</label>
                <div class="col-sm-2">
                  <input type="number" id="october" name="october" value="${item.october}" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-3 control-label" for="november">十一月:</label>
                <div class="col-sm-2">
                  <input type="number" id="november" name="november" value="${item.november}" class="form-control">
                </div>
                <label class="col-sm-2 control-label" for="december">十二月:</label>
                <div class="col-sm-2">
                  <input type="number" id="december" name="december" value="${item.december}" class="form-control">
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
          watersType:{
            required: true
          },
          waterIndex:{
            required: true
          },
      },
      messages: {
          innerCode:{
            required: "请输入单位编号"
          },
          watersType:{
            required: "请选择水源类型"
          },
          waterIndex:{
            required: "请输入年用水指标"
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
        $.post("${context_path}/basic/waterindex/save" ,postData,
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
    $.post("${context_path}/dict/getIndexUseDict", submitData, function(data) {
      var watersType = data.WatersType;
      for(var i = 0;i<watersType.length;i++) {
        $("#watersType").append("<option value='" + watersType[i].value + "'>"+watersType[i].name+"</option>");
      }
      $("#watersType").val($("#watersTypeInput").val());
    },"json");
  }
  $(function(){
    getDictMapData();
  })
</script>
</body>

</html>
