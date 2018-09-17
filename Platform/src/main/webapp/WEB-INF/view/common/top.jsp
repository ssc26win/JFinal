<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="navbar-header pull-left">
	<a href="javascript:;" class="navbar-brand">
		<small>
			<i class="fa fa-cloud"></i>
			通州区节水管理平台
		</small>
	</a>
</div>
<div class="navbar-buttons navbar-header pull-right" role="navigation">
	<ul class="nav ace-nav">
		<!-- #section:basics/navbar.user_menu -->
		<li class="light-blue">
			<a data-toggle="dropdown" href="#" class="dropdown-toggle" style="white-space: nowrap;" >
				<%--<!--  img class="nav-user-photo" src="${res_url}ace-1.3.3/assets/avatars/user.jpg" alt="Jason's Photo" /-->--%>
			<i class="fa fa-user"></i>
				<span style="white-space: nowrap; ">
					<small>欢迎，</small>
					<b>${sessionScope.sysUser.name}</b>
				</span>
			</a>

			<ul class="dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close"
                    style="min-width: 110px;margin-right: 4px;padding-left: 5px;">
				<li>
					<a href="javascript:;" id="pwd-update">
						<i class="ace-icon fa fa-cog"></i>
						密码设置
					</a>
				</li>
				<li class="divider"></li>
				<li>
					<a href="${context_path}/loginOut">
						<i class="ace-icon fa fa-power-off"></i>
						退出
					</a>
				</li>
			</ul>
		</li>
		<li class="light-blue">
			<a href="#" onclick="getMsgList();">
				<i class="fa fa-envelope-o" aria-hidden="true" style="margin-left: 10px;margin-right: 10px;">
					<span class="count" id="msgCount" style="display: none"></span>
				</i>
			</a>
		</li>
	</ul>
</div>
<script type="text/javascript">
	window.jQuery || document.write("<script src='${res_url}ace-1.3.3/assets/js/jquery.js'>"+"<"+"/script>");
</script>
<script type="application/javascript">
	$(function () {
		var submitData = {};
		$.post("${context_path}/basic/msgreceiver/msgCount", submitData, function(data) {
			var count = data.msgCount;
			if (count != '' && parseInt(count) > 0) {
				$("#msgCount").text(count);
				$("#msgCount").show();
			} else {
				$("#msgCount").remove();
			}
		});
	});
    function getMsgList() {
		parent.layer.open({
			title:'查看消息',
			type: 2,
			area: ['980px', '600px'],
			fix: false, //不固定
			maxmin: true,
			content: '${context_path}/basic/msgreceiver/myMsgList'
		});
	}
</script>