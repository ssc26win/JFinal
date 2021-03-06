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
              <div class="form-group">
                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="innerCode">单位编号</label>
                <div class="col-xs-12 col-sm-9">
                  <div class="clearfix">
                    <input type="text" id="innerCode" name="innerCode" ${id ne null?'readonly':'' } value="${item.innerCode}" class="col-xs-12 col-sm-6">
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="meterAddress">表计地址</label>
                <div class="col-xs-12 col-sm-9">
                  <div class="clearfix">
                    <input type="text" id="meterAddress" name="meterAddress" value="${item.meter_address}" class="col-xs-12 col-sm-6">
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="watersType">水源类型</label>
                <div class="col-xs-12 col-sm-9">
                  <div class="clearfix">
                    <input type="hidden" id="watersTypeInput" name="watersTypeInput" value="${item.watersType}" />
                    <select id="watersType" name="watersType" class="col-xs-12 col-sm-6"></select>
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="netWater">净用水量</label>
                <div class="col-xs-12 col-sm-9">
                  <div class="clearfix">
                    <input type="number" id="netWater" name="netWater" value="${item.netWater}" class="col-xs-12 col-sm-6">
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="sumWater">总用水量</label>
                <div class="col-xs-12 col-sm-9">
                  <div class="clearfix">
                    <input type="number" id="sumWater" name="sumWater" value="${item.sumWater}" class="col-xs-12 col-sm-6">
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="voltage">电池电压（伏特v）</label>
                <div class="col-xs-12 col-sm-9">
                  <div class="clearfix">
                    <input type="number" id="voltage" name="voltage" value="${item.voltage}" class="col-xs-12 col-sm-6">
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="writeTime">抄表时间</label>
                <div class="col-xs-12 col-sm-9">
                  <div class="clearfix">
                    <input type="text" id="writeTime" name="writeTime" value="${item.writeTime}" class="col-xs-12 col-sm-6 form_datetime">
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
          </div><!-- /.col -->
        </div><!-- /.row -->
      </div><!-- /.page-content -->
    </div><!-- /.main-content -->
  </div><!-- /.main-container-inner -->
</div><!-- /.main-container -->
<jsp:include page="/WEB-INF/view/common/basejs.jsp" flush="true" />
<script type="text/javascript">
  jQuery(function($) {
    var $validation = true;
    $('#validation-form').validate({
      errorElement: 'div',
      errorClass: 'help-block',
      focusInvalid: false,
      rules: {
        meterAddress:{
          required: true
        },
      },
      messages: {
        meterAddress:{
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
        $.post("${context_path}/statis/actual/save" ,postData,
                function(data){
                  if(data.code==0){
                    parent.reloadGrid(); //重新载入
                    layer.msg('操作成功', {
                      icon: 1,
                      time: 1000 //2秒关闭（如果不配置，默认是3秒）
                    },function(){
                      var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                      parent.layer.close(index); //再执行关闭
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
    $.post("${context_path}/dict/getByType", submitData, function(data) {
      var watersType = data.WatersType;
      for(var i = 0;i<watersType.length;i++) {
        if ($("#watersTypeInput").val() == watersType[i].value) {
          $("#watersType").append("<option selected value='" + watersType[i].value + "'>"+watersType[i].name+"</option>");
        } else {
          $("#watersType").append("<option value='" + watersType[i].value + "'>"+watersType[i].name+"</option>");
        }
      }
    },"json");
  }
  $(function(){
    getDictMapData();
  })
</script>
</body>

</html>
