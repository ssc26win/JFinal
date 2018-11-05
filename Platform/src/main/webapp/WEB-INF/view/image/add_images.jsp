<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>上传图片</title>

    <link rel="stylesheet" href="${res_url}/bootstrap-fileinput/css/fileinput.min.css">
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <script src="${res_url}/bootstrap-fileinput/js/fileinput.min.js"></script>


    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css" rel="stylesheet">
    <link href="${res_url}/bootstrap-fileinput/css/fileinput.css" media="all" rel="stylesheet" type="text/css"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" media="all"
          rel="stylesheet" type="text/css"/>
    <link href="${res_url}/bootstrap-fileinput/themes/explorer-fa/theme.css" media="all" rel="stylesheet"
          type="text/css"/>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <script src="${res_url}/bootstrap-fileinput/js/plugins/sortable.js" type="text/javascript"></script>
    <script src="${res_url}/bootstrap-fileinput/js/fileinput.js" type="text/javascript"></script>
    <script src="${res_url}/bootstrap-fileinput/js/locales/zh.js" type="text/javascript"></script>
    <script src="${res_url}/bootstrap-fileinput/themes/explorer-fa/theme.js" type="text/javascript"></script>
    <script src="${res_url}/bootstrap-fileinput/themes/fa/theme.js" type="text/javascript"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"
            type="text/javascript"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"
            type="text/javascript"></script>
    <script src="${res_url}js/layer/layer.js"></script>
</head>
<style type="text/css">
    html {
        overflow-x: hidden;
        overflow-y: auto;
    }
</style>
<body class="no-skin">
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>

    <div class="main-container-inner">
        <div class="main-content" style="margin-left: 0px;">
            <div class="page-content">
                <div class="row">
                    <div class="col-sm-1"></div>
                    <div class="col-sm-10" style="height: 60%;">
                        <form enctype="multipart/form-data">
                            <div class="form-group">
                                <input id="input-700" name="kartik-input-700[]" type="file" multiple
                                       class="file-loading">
                                <!-- 必须的 -->
                            </div>
                            <div class="form-group">
                                <div id="kv-error-1" style="margin-top:10px;display:none"></div>
                                <div id="kv-success-1" class="alert alert-success fade in"
                                     style="margin-top:10px;display:none"></div>
                            </div>
                            <div class="form-group">
                                <div class="file-loading" id="input702Loading">
                                    <input id="input-702" name="kartik-input-702[]" type="file" multiple>
                                </div>
                            </div>
                            <div class="form-group">
                                <input type="hidden" id="relaId" class="form-control" name="relaId"
                                       value="${relaId}">
                                <input type="hidden" id="relaTable" class="form-control" name="relaTable"
                                       value="${relaTable}">
                                <input type="hidden" id="responseImgIds" class="form-control" name="responseImgIds"
                                       value="${responseImgIds}">
                                <input type="hidden" id="responseImgNames" class="form-control" name="responseImgNames"
                                       value="${responseImgNames}">
                            </div>
                        </form>
                    </div>
                    <div class="col-sm-1"></div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

<script type="text/javascript">
    window.imgTotal = parseInt(${maxFileCount});
    $(function () {
                var uploadUrl = '${context_path}/image/uploadData?relaTable=' + $("#relaTable").val() + '&maxFileCount=' + '${maxFileCount}';
                if ($("#relaId").val() != "") {
                    uploadUrl = uploadUrl + "&relaId=" + $("#relaId").val();
                }
                if ($("#relaId").val() != "" && '${initialPreview}' != 'notConfig' && '${initialPreviewConfig}' != 'notConfig') {
                    $("#input-700").remove();
                    $("#input-702").fileinput({
                        language: 'zh',
                        theme: 'fa',
                        uploadUrl: uploadUrl,
                        uploadAsync: false,
                        minFileCount: 0,
                        maxFileCount: parseInt(${maxFileCount}),
                        enctype: 'multipart/form-data',
                        maxFileSize: 5120,//限制上传大小KB
                        allowedPreviewTypes: ['image'],
                        overwriteInitial: false,
                        showRemove: false,
                        msgFilesTooMany: "选择上传的文件数量 超过允许的最大数值！",
                        browseClass: 'btn btn-primary btn-lg',
                        uploadClass: 'btn btn-info btn-lg',
                        initialPreview: JSON.parse('${initialPreview}'),
                        initialPreviewAsData: true, // identify if you are sending preview data only and not the raw markup
                        initialPreviewFileType: 'image', // image is the default and can be overridden in config below
                        initialPreviewConfig: JSON.parse('${initialPreviewConfig}'),
                        uploadExtraData: {
                            img_key: "1000",
                            img_keywords: "happy, places"
                        }
                    }).on("filepredelete", function (jqXHR) {
                        var abort = true;
                        if (confirm("确定删除此图片?")) {
                            abort = false;
                        }
                        return abort; // 您还可以发送任何数据/对象，你可以接收` filecustomerror
                    }).on('filebatchpreupload', function (event, data) {
                        var n = data.files.length, files = n > 1 ? n + ' files' : 'one file';
                        if (!window.confirm("确定上传选择的文件吗 ?")) {
                            return {
                                message: "上传失败!", // upload error message
                                data: {} // any other data to send that can be referred in `filecustomerror`
                            };
                        }

                    }).on('fileuploaded', function (event, data, id, index) {//上传成功之后的处理
                        window.imgTotal = window.imgTotal - 1;
                        console.log(window.imgTotal);
                        var targetId = $("#responseImgIds").val();
                        if (targetId != "") {
                            $("#responseImgIds").val(targetId + "," + data.response.uploadImgId);
                        } else {
                            $("#responseImgIds").val(data.response.uploadImgId);
                        }
                        var targetName = $("#responseImgNames").val();
                        if (targetName != "") {
                            $("#responseImgNames").val(targetName + "," + data.files[index].name);
                        } else {
                            $("#responseImgNames").val(data.files[index].name);
                        }
                        var fname = data.files[index].name;
                        var out = '<li>' + '文件 # ' + (index + 1) + ' - ' + fname + ' 上传成功！' + '</li>';
                        $('#kv-success-1 ul').append(out);
                        $('#kv-success-1').fadeIn('slow');
                    }).on('filebatchpreupload', function (event, data, id, index) {
                        $('#kv-success-1').html('<h4>上传状态</h4><ul></ul>').hide();
                    }).on('filedeleted', function (event, key) {
                        window.imgTotal = window.imgTotal + 1;
                    }).on("filebatchselected", function (event, files) {
                        window.imgTotal = window.imgTotal - 1;
                    });

                } else {
                    $("#input-702").remove();
                    $("#input702Loading").remove();
                    $("#input-700").fileinput({
                        uploadUrl: '${context_path}/image/uploadData?relaTable=' + $("#relaTable").val() + '&relaId=' + $("#relaId").val(), // server upload action
                        language: 'zh',
                        theme: 'fa',
                        minFileCount: 0,
                        uploadAsync: false,
                        maxFileCount: parseInt(${maxFileCount}),
                        enctype: 'multipart/form-data',
                        maxFileSize: 5120,//限制上传大小KB
                        // overwriteInitial: false,//不覆盖已上传的图片
                        allowedPreviewTypes: ['image'],
                        // allowedFileExtensions: ['jpg', 'png', 'gif'],//可以可选择的违建格式
                        // elErrorContainer: '#kv-error-1',//错误显示的文本continner
                        showBrowse: true,
                        browseClass: 'btn btn-primary btn-lg',
                        uploadClass: 'btn btn-info btn-lg',
                        showRemove: false,
                        msgFilesTooMany: "选择上传的文件数量 超过允许的最大数值！",
                        previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
                        ajaxSettings: {
                            contentType: false
                        }
                    }).on("filepredelete", function (jqXHR) {
                        var abort = true;
                        if (confirm("确定删除此图片?")) {
                            abort = false;
                        }
                        return abort; // 您还可以发送任何数据/对象，你可以接收` filecustomerror
                    }).on('filebatchpreupload', function (event, data) {
                        var n = data.files.length, files = n > 1 ? n + ' files' : 'one file';
                        if (!window.confirm("确定上传选择的文件吗 ?")) {
                            return {
                                message: "上传失败!", // upload error message
                                data: {} // any other data to send that can be referred in `filecustomerror`
                            };
                        }
                    }).on('fileuploaded', function (event, data, id, index) {//上传成功之后的处理
                        window.imgTotal = window.imgTotal - 1;
                        var targetId = $("#responseImgIds").val();
                        if (targetId != "") {
                            $("#responseImgIds").val(targetId + "," + data.response.uploadImgId);
                        } else {
                            $("#responseImgIds").val(data.response.uploadImgId);
                        }
                        var targetName = $("#responseImgNames").val();
                        if (targetName != "") {
                            $("#responseImgNames").val(targetName + "," + data.files[index].name);
                        } else {
                            $("#responseImgNames").val(data.files[index].name);
                        }
                        var fname = data.files[index].name;
                        var out = '<li>' + '文件 # ' + (index + 1) + ' - ' + fname + ' 上传成功！' + '</li>';
                        $('#kv-success-1 ul').append(out);
                        $('#kv-success-1').fadeIn('slow');
                    }).on('filebatchpreupload', function (event, data, id, index) {
                        $('#kv-success-1').html('<h4>上传状态</h4><ul></ul>').hide();
                    }).on('filedeleted', function (event, key) {
                        window.imgTotal = window.imgTotal + 1;
                    }).on("filebatchselected", function (event, files) {
                        window.imgTotal = window.imgTotal - 1;
                    });
                }
            }
    );

    $(function () {
        $("#input-702").click(function () {
            if (window.imgTotal <= 0) {
                layer.msg("不能超出图片限制数量范围", {
                    icon: 2,
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                });
                return false;
            }

        });
        $("#input-700").click(function () {
            if (window.imgTotal <= 0) {
                layer.msg("不能超出图片限制数量范围", {
                    icon: 2,
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                });
                return false;
            }
        });
    })
</script>
</html>
