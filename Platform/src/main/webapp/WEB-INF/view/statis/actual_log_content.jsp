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
                <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="content">采集内容</label>
                <div class="col-xs-12 col-sm-9">
                  <div class="clearfix">
                    <textarea id="content" name="content" class="col-xs-12 col-sm-9" rows="8" cols="18" autofocus readonly>${item.content}</textarea>
                  </div>
                </div>
              </div>
              <div class="clearfix form-actions" align="center">
                <div class="col-md-offset-3 col-md-9">
                  <button id="copy-btn" class="btn btn-info" type="submit" data-last="Finish">
                    <i class="ace-icon fa fa-check bigger-110"></i>
                    复制
                  </button>
                  &nbsp; &nbsp; &nbsp;
                  <button class="btn" type="button" id="close-btn">
                    <i class="ace-icon fa fa-undo bigger-110"></i>
                    关闭
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
    $("#close-btn").click(function(){
       closeView();
    });
    $("#copy-btn").click(function(){
       copyContent();
    });
  });
  function closeView(){
    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    parent.layer.close(index); //再执行关闭
  }
  function copyContent(){
    var content = $('#content');
    content.select();
    try{
      if(document.execCommand('copy', false, null)){
        layer.msg("复制成功", {
          icon: 1,
          time: 2000 //2秒关闭（如果不配置，默认是3秒）
        });
      }else{
        layer.msg("复制失败", {
          icon: 2,
          time: 2000 //2秒关闭（如果不配置，默认是3秒）
        });
      }
    } catch(err){
      //fail info 失败了放些提示
    }
  }
</script>
</body>

</html>
