<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <!-- Standard Meta -->
  <meta charset="utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
  <title>Login Example - Semantic</title>
  <jsp:include page="/WEB-INF/view/common/basecss.jsp" flush="true" />
  <%--<link rel="stylesheet" href="${res_url}js/omui/development-bundle/themes/default/om-fileupload.css" />--%>
  <script src="${res_url}ace-1.3.3/assets/js/jquery.js" ></script>
  <script src="${res_url}ace-1.3.3/assets/js/ace-extra.js"></script>
  <%--<script src="${res_url}uploadify/jquery.min.js"></script>--%>
  <link rel="stylesheet" type="text/css" href="${res_url}uploadify/uploadify.css">
  <script src="${res_url}uploadify/jquery.uploadify.min.js" type="text/javascript"></script>
  <style type="text/css">
    .uploadify-button{
      background-color: white;
    }
    .uploadify:hover .uploadify-button{
      background-color: white;
    }
  </style>
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
            <div class="widget-box">

              <div class="widget-body">
                <div class="widget-main">
                  <!-- #section:plugins/fuelux.wizard.container -->
                  <div class="step-content pos-rel" id="step-container">
                    <div class="step-pane active" id="step1">
                      <form class="form-horizontal" id="validation-form" method="post">
                        <div class="form-group">
                          <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="file_upload">excel
                              (<a href="#" onclick="downloadFile();" title="下载示例" style="color: #008800" >demo</a>):</label>
                          <div class="col-xs-12 col-sm-9">
                            <div class="clearfix">
                              <div class="cover-area" style="border: 1px solid #e0e0e0;width: 80%;border-radius:5px;padding: 5px 0 0 5px;">
                                <div class="cover-hd">
                                  <input id="file_upload" name="file_upload" type="file" />
                                  <input id="importUrl" class="cover-input" value="${item.importUrl}" name="importUrl" type="hidden" />
                                </div>
                                <p id="upload-tip" class="upload-tip"></p>
                                <p id="apkArea" class="cover-bd" style="display: ${action eq 'add'?'none':''}">
                                  <a class="vb cover-del" href="#" style="width: 600px;">${item.importUrl}</a>
                                </p>
                              </div>
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
                            <button class="btn" type="button" onclick="cancelBtn();">
                              <i class="ace-icon fa fa-undo bigger-110"></i>
                              取消
                            </button>
                          </div>
                        </div>
                      </form>
                    </div>
                  </div>
                </div><!-- /.widget-main -->
              </div><!-- /.widget-body -->
            </div>
          </div><!-- /.col -->
        </div><!-- /.row -->
      </div><!-- /.page-content -->
    </div><!-- /.main-content -->
  </div><!-- /.main-container-inner -->
</div><!-- /.main-container -->
<jsp:include page="/WEB-INF/view/common/basejs.jsp" flush="true" />
<script type="text/javascript">
    var uploadUrl = "${uploadUrl}";
    var demoPath = "${context_path}/basic/" + uploadUrl + "/downloadDemo";
    var uploadPath = "${context_path}/basic/" + uploadUrl + "/uploadImportData";
    var importDataPath = "${context_path}/basic/" + uploadUrl + "/importData";
    function downloadFile() {
        try{
          var elemIF = document.createElement("iframe");
          elemIF.src = demoPath;
          elemIF.style.display = "none";
          document.body.appendChild(elemIF);
        }catch(e){
        }
    }
    jQuery(function($) {
    $('#file_upload').uploadify({
      //校验数据
      'swf' : '${res_url}uploadify/uploadify.swf', //指定上传控件的主体文件，默认‘uploader.swf’
      'uploader' : uploadPath, //指定服务器端上传处理文件，默认‘upload.php’
      'auto' : true, //手动上传
      'buttonImage' : '${res_url}uploadify/uploadify-upload.png', //浏览按钮背景图片
      'width' :110,
      'height' :30,
      'cancelImg': 'uploadify/uploadify-cancel.png',
      //'buttonText': '选 择应用',
      'multi' : false, //单文件上传
      'fileTypeExts' : '*.xls;*.xlsx;', //允许上传的文件后缀
      'fileSizeLimit' : '50MB', //上传文件的大小限制，单位为B, KB, MB, 或 GB
      'successTimeout' : 30, //成功等待时间
      'onUploadSuccess' : function(file, data, response) {//每成功完成一次文件上传时触发一次
        data=eval("["+data+"]")[0];
        $("#apkArea").show().find(".cover-del").html(data.fileName);
        $("#importUrl").val(data.fileUrl);
      },
      'onUploadError' : function(file, data, response) {//当上传返回错误时触发
        $('#f_pics').append("<div class=\"pics_con\">" + data + "</div>");
      }
    });

    var $validation = true;

    $('#validation-form').validate({
      errorElement: 'div',
      errorClass: 'help-block',
      focusInvalid: false,
      //title versionNo url natureNo  contents
      rules: {
      },
      messages: {
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
        if ($("#importUrl").val()=="") {
            layer.msg("请选择一个excel文件", {
              icon: 2,
              time: 1000 //2秒关闭（如果不配置，默认是3秒）
            });
            return;
        }
        var submitData = {
          importUrl:$("#importUrl").val()
        };
        $btn.addClass("disabled");
        $.post(importDataPath, submitData,function(data) {
          $btn.removeClass("disabled");
          if(data.code==0){
            window.parent.reloadGrid(); //重新载入
            layer.msg("保存成功", {
              icon: 1,
              time: 1000 //2秒关闭（如果不配置，默认是3秒）
            },function(){
              closeView();
            });
          }else{
            layer.msg(data.msg, {
              icon: 2,
              time: 1000 //2秒关闭（如果不配置，默认是3秒）
            },function(){
            });
          }
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

  function cancelBtn(){
    closeView();
  }
</script>
</body>

</html>

