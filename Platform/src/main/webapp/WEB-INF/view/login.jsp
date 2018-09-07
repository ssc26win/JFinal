<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/taglib.jsp"%>

<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>登录</title>

		<meta name="description" content="User login page" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="/res/ace-1.3.3/assets/css/bootstrap.css" />
		<link rel="stylesheet" href="/res/ace-1.3.3/assets/css/font-awesome.css" />

		<!-- text fonts -->
		<link rel="stylesheet" href="/res/ace-1.3.3/assets/css/ace-fonts.css" />

		<!-- ace styles -->
		<link rel="stylesheet" href="/res/ace-1.3.3/assets/css/ace.css" />

		<!--[if lte IE 9]>
			<link rel="stylesheet" href="/res/ace-1.3.3/assets/css/ace-part2.css" />
		<![endif]-->
		<link rel="stylesheet" href="/res/ace-1.3.3/assets/css/ace-rtl.css" />

		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="/res/ace-1.3.3/assets/css/ace-ie.css" />
		<![endif]-->

		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

		<!--[if lt IE 9]>
		<script src="/res/ace-1.3.3/assets/js/html5shiv.js"></script>
		<script src="/res/ace-1.3.3/assets/js/respond.js"></script>
		<![endif]-->
		<style type="text/css">
			.yzm-pic {
				width: 99px;
				height: 33px;
				border: 1px solid #b2cff2;
			}
			.search {
				left: 0;
				position: relative;
			}

			#auto_div {
				display: none;
				position: absolute;
				border: 1px #74c0f9 solid;
				background: #FFF;
				top: 34px;
				left: 0;
				color: #323232;
				z-index: 9999;
				/*margin-left: 12px;*/
			}
		</style>
	</head>

	<body class="login-layout light-login">
		<div class="main-container">
			<div class="main-content">
				<div class="row">
					<div class="col-sm-10 col-sm-offset-1">
						<div class="login-container">
							<div class="center">
								<h1>
									<i class=""></i>
								</h1>
							</div>

							<div class="space-6"></div>

							<div class="position-relative">
								<div id="login-box" class="login-box visible widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header blue lighter bigger">
												<i class="ace-icon fa fa-coffee green"></i>
												通州区远传水表采集系统
											</h4>

											<div class="space-6"></div>

											<form>
												<fieldset>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" class="form-control" placeholder="请输入用户名" name="username" id="username"/>
															<i class="ace-icon fa fa-user"></i>
														</span>
													</label>

													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" class="form-control" placeholder="请输入密码" name="password" id="password"/>
															<i class="ace-icon fa fa-lock"></i>
														</span>
													</label>
													<div class="block clearfix">
															<input type="text"  style="width: 198px;vertical-align:middle;" placeholder="验证码" name="imgCode" id="imgCode" />
															<img class="yzm-pic" id="img" src="${context_path}/image/getCode" style="vertical-align:middle;"/>
														
													</div>
													<div class="space"></div>

													<div class="clearfix">
														<label class="inline">
																<input type="checkbox" class="ace" id="autoLogin">
																<span class="lbl">&nbsp;下次自动登录</span>
															</label>
														<button type="button" id="login-btn" class="width-35 pull-right btn btn-sm btn-primary">
															<i class="ace-icon fa fa-key"></i>
															<span class="bigger-110">登陆</span>
														</button>
													</div>


													<div class="space-4"></div>
												</fieldset>
											</form>

										</div><!-- /.widget-main -->

										<div class="toolbar clearfix">
											<div>
												<a href="#" data-target="#forgot-box" class="forgot-password-link">
													<i class="ace-icon fa fa-arrow-left"></i>
													忘记密码？
												</a>
											</div>

											<div>
												<a href="#" data-target="#signup-box" class="user-signup-link">
													免费注册
													<i class="ace-icon fa fa-arrow-right"></i>
												</a>
											</div>
										</div>
									</div><!-- /.widget-body -->
								</div><!-- /.login-box -->

								<div id="forgot-reset" class="forgot-box widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header red lighter bigger">
												<i class="ace-icon fa fa-key"></i>
												重置密码
											</h4>

											<div class="space-6"></div>
											<p>
												请填写新密码
											</p>
											<form>
												<input type="hidden" id="resetEmail" name="resetEmail" value="${email}"/>
												<input type="hidden" id="resettime" name="resettime" value="${time}"/>
												<fieldset>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" id="reUsernameEmail" name="reUsernameEmail" class="form-control" readonly value="${reUsernameEmail}" />
															<i class="ace-icon fa fa-user"></i>
														</span>
													</label>

													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" id="reSetpassword" name="reSetpassword" class="form-control" placeholder="请输入密码" />
															<i class="ace-icon fa fa-lock"></i>
														</span>
													</label>

													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" id="reSetpassword2" name="reSetpassword2"  class="form-control" placeholder="请输入确认密码" />
															<i class="ace-icon fa fa-retweet"></i>
														</span>
													</label>
													<div class="space-4"></div>

													<div class="clearfix">
														<button type="reset" class="width-30 pull-left btn btn-sm">
															<i class="ace-icon fa fa-refresh"></i>
															<span class="bigger-110">重置</span>
														</button>

														<button type="button" id="rePwdEmail-btn" class="width-65 pull-right btn btn-sm btn-success">
															<span class="bigger-110">确定</span>

															<i class="ace-icon fa fa-arrow-right icon-on-right"></i>
														</button>
													</div>
												</fieldset>
											</form>
										</div><!-- /.widget-main -->
									</div><!-- /.widget-body -->
								</div><!-- /.forgot-box -->

								<div id="forgot-box" class="forgot-box widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header red lighter bigger">
												<i class="ace-icon fa fa-key"></i>
												重置密码
											</h4>

											<div class="space-6"></div>
											<p>
												请填写账户名及邮箱
											</p>
											<form>
												<fieldset>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" class="form-control" placeholder="请输入用户名" name="usernameEmail" id="usernameEmail"/>
															<i class="ace-icon fa fa-user"></i>
														</span>
													</label>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="email" id="sendrePwdEmail" name="sendrePwdEmail" class="form-control" placeholder="请输入邮箱" />
															<i class="ace-icon fa fa-envelope"></i>
														</span>
													</label>

													<div class="clearfix">
														<button type="button" class="width-35 pull-right btn btn-sm btn-danger" id="sendrePwdEmail-btn">
															<i class="ace-icon fa fa-lightbulb-o"></i>
															<span class="bigger-110">发送</span>
														</button>
													</div>
												</fieldset>
											</form>
										</div><!-- /.widget-main -->

										<div class="toolbar center">
											<a href="#" data-target="#login-box" class="back-to-login-link">
												返回登录
												<i class="ace-icon fa fa-arrow-right"></i>
											</a>
										</div>
									</div><!-- /.widget-body -->
								</div><!-- /.forgot-box -->


								<div id="signup-box" class="signup-box widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header green lighter bigger">
												<i class="ace-icon fa fa-users blue"></i>
												新用户注册
											</h4>

											<div class="space-6"></div>
											<p> 请添写您的信息: </p>

											<form action="sys/">
												<fieldset>

													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" id="companyName" name="companyName" class="form-control cnwth" placeholder="请输入单位名称" />
															<div id="auto_div"></div>
															<input type="hidden" id="innerCode" name="innerCode" class="form-control" />
															<i class="ace-icon fa fa-home"></i>
														</span>
													</label>

													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="email" id="remail" name="remail" class="form-control" placeholder="请输入邮箱" />
															<i class="ace-icon fa fa-envelope"></i>
														</span>
													</label>

                                                    <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" id="rphone" name="rphone" class="form-control" placeholder="请输入手机号" />
															<i class="ace-icon fa fa-phone"></i>
														</span>
                                                    </label>

													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" id="rusername" name="rusername" class="form-control" placeholder="请输入用户名" />
															<i class="ace-icon fa fa-user"></i>
														</span>
													</label>

													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" id="rpassword" name="rpassword" class="form-control" placeholder="请输入密码" />
															<i class="ace-icon fa fa-lock"></i>
														</span>
													</label>

													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" id="r2password" name="r2password"  class="form-control" placeholder="请输入确认密码" />
															<i class="ace-icon fa fa-retweet"></i>
														</span>
													</label>

													<%--<label class="block">
														<input type="checkbox" class="ace" />
														<span class="lbl">
															同意
															<a href="#">User Agreement</a>
														</span>
													</label>--%>

													<div class="space-24"></div>

													<div class="clearfix">
														<button type="reset" class="width-30 pull-left btn btn-sm">
															<i class="ace-icon fa fa-refresh"></i>
															<span class="bigger-110">重置</span>
														</button>

														<button type="button" id="regist-btn" class="width-65 pull-right btn btn-sm btn-success">
															<span class="bigger-110">注册</span>

															<i class="ace-icon fa fa-arrow-right icon-on-right"></i>
														</button>
													</div>
												</fieldset>
											</form>
										</div>

										<div class="toolbar center">
											<a href="#" data-target="#login-box" class="back-to-login-link">
												<i class="ace-icon fa fa-arrow-left"></i>
												返回登录
											</a>
										</div>
									</div><!-- /.widget-body -->
								</div><!-- /.signup-box -->
							</div><!-- /.position-relative -->

							<div class="navbar-fixed-top align-right">
								<br />
								&nbsp;
								<a id="btn-login-dark" href="#">Dark</a>
								&nbsp;
								<span class="blue">/</span>
								&nbsp;
								<a id="btn-login-blur" href="#">Blur</a>
								&nbsp;
								<span class="blue">/</span>
								&nbsp;
								<a id="btn-login-light" href="#">Light</a>
								&nbsp; &nbsp; &nbsp;
							</div>
						</div>
					</div><!-- /.col -->
				</div><!-- /.row -->
			</div><!-- /.main-content -->
		</div><!-- /.main-container -->

		<!-- basic scripts -->

		<!--[if !IE]> -->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='/res/ace-1.3.3/assets/js/jquery.js'>"+"<"+"/script>");
		</script>

		<!-- <![endif]-->

		<!--[if IE]>
		<script type="text/javascript">
		 window.jQuery || document.write("<script src='/res/ace-1.3.3/assets/js/jquery1x.js'>"+"<"+"/script>");
		</script>
		<![endif]-->
		<script src="/res/js/layer/layer.js"></script>

		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='/res/ace-1.3.3/assets/js/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>

		<!-- inline scripts related to this page -->
		<script type="text/javascript">
			jQuery(function($) {
				document.onkeydown = function (e) { 
					var theEvent = window.event || e; 
					var code = theEvent.keyCode || theEvent.which; 
					if (code == 13) { 
					    $('#login-btn').click();
					} 
				}
				$('#img').click(function(){
					$('#img').attr("src","${context_path}/image/getCode?tm="+Math.random());
				});
				 $('#login-btn').click(function(event) {
				      event.stopPropagation();
				      var $btn = $(this);
				      if ($btn.hasClass("disabled")) {
				        return false;
				      }
				      var $loginname = $('#username');
				      var $password = $('#password');
				      var $imgCode = $('#imgCode');
				      if (!$loginname.val()) {
				        layer.alert('请输入用户名！');
				        $loginname.focus();
				        return false;
				      }
				      if (!$password.val()) {
					  	layer.alert('请输入密码！');
				        $password.focus();
				        return false;
				      }
				      if (!$imgCode.val()) {
						    layer.alert('请输入验证码！');
					        $imgCode.focus();
					        return false;
					      }
				      var submitData = {
				   		username : $loginname.val(),
				      	password : $password.val(),
				      	imageCode : $imgCode.val(),
				      	autoLogin:$("#autoLogin").is(':checked') ==true?1:0,
				      	url:"${url}"
				      };
				      $btn.addClass("disabled");
				      $.post("${context_path}/doLogin", submitData, function(data) {
								$btn.removeClass("disabled");
								if (data.code == 0) {
									window.top.location.href = "${context_path}/";
								} else {
									layer.alert(data.msg);
                                    $('#img').attr("src","${context_path}/image/getCode?tm="+Math.random());
								}
							}, "json");
				      return false;
				    });
                $('#regist-btn').click(function(event) {
                    event.stopPropagation();
                    var $btn = $(this);
                    if ($btn.hasClass("disabled")) {
                        return false;
                    }
                    var $rphone = $('#rphone');
                    var $remail = $('#remail');
                    var $rusername = $('#rusername');
                    var $rpassword = $('#rpassword');
                    var $r2password = $('#r2password');
					var $innerCode = $('#innerCode');
					if (!$innerCode.val()) {
						layer.alert('请输入所属公司编码！');
						$innerCode.focus();
						return false;
					}
                    if (!$rphone.val()) {
                        layer.alert('请输入手机号！');
                        $rphone.focus();
                        return false;
                    }
                    if (!$remail.val()) {
                        layer.alert('请输入邮箱！');
                        $remail.focus();
                        return false;
                    }
                    if (!$rusername.val()) {
                        layer.alert('请输入用户名！');
                        $rusername.focus();
                        return false;
                    }
                    if (!$rpassword.val()) {
                        layer.alert('请输入密码！');
                        $rpassword.focus();
                        return false;
                    }
                    if (!$r2password.val()) {
                        layer.alert('请输入确认密码！');
                        $r2password.focus();
                        return false;
                    }
                    if ($rpassword.val() != $r2password.val()) {
                        layer.alert('密码与确认密码不一致！');
                        $r2password.focus();
                        return false;
                    }
                    var submitData = {
                        phone:$rphone.val(),
                        email:$remail.val(),
                        username:$rusername.val(),
                        password:$rpassword.val(),
						innerCode:$innerCode.val()
                    };
                    $btn.addClass("disabled");
                    $.post("${context_path}/regist", submitData, function(data) {
                        $btn.removeClass("disabled");
                        if (data.code == 0) {
                            layer.alert("恭喜您，注册成功，请登录！", function(){
                                window.top.location.href = "${context_path}/";
                            });
                        } else {
                            layer.alert(data.msg);
                        }
                    }, "json");
                    return false;
                });
				$('#sendrePwdEmail-btn').click(function(event) {
					var index = layer.load(0, {shade: false});
					event.stopPropagation();
					var $btn = $(this);
					if ($btn.hasClass("disabled")) {
						return false;
					}
					var $remailUName = $('#usernameEmail');
					if (!$remailUName.val()) {
						layer.close(index);
						layer.alert('请输入用户名！');
						$remailUName.focus();
						return false;
					}
					var $remail = $('#sendrePwdEmail');
					if (!$remail.val()) {
						layer.close(index);
						layer.alert('请输入邮箱！');
						$remail.focus();
						return false;
					}
					var submitData = {
						email:$remail.val(), username:$remailUName.val()
					};
					$btn.addClass("disabled");
					$.post("${context_path}/resetPwdSendEmail", submitData, function(data) {
						$btn.removeClass("disabled");
						if (data.code == 0) {
							layer.alert("发送邮件成功，请登录您的邮箱！", function(){
								window.top.location.href = "${context_path}/";
							});
						} else {
							layer.alert(data.msg);
						}
						layer.close(index);
					}, "json");
					return false;
				});
				$('#rePwdEmail-btn').click(function(event) {
					event.stopPropagation();
					var $btn = $(this);
					if ($btn.hasClass("disabled")) {
						return false;
					}
					var $remail = $('#resetEmail');
					var $rpassword = $('#reSetpassword');
					var $r2password = $('#reSetpassword2');
					if (!$rpassword.val()) {
						layer.alert('请输入密码！');
						$rpassword.focus();
						return false;
					}
					if (!$r2password.val()) {
						layer.alert('请输入确认密码！');
						$r2password.focus();
						return false;
					}
					if ($rpassword.val() != $r2password.val()) {
						layer.alert('密码与确认密码不一致！');
						$r2password.focus();
						return false;
					}
					var submitData = {
						email:$remail.val(),
						password : $rpassword.val()
					};
					$btn.addClass("disabled");
					$.post("${context_path}/savePwdForget", submitData, function(data) {
						$btn.removeClass("disabled");
						if (data.code == 0) {
							layer.alert("恭喜您，修改成功，请登录！", function(){
								window.top.location.href = "${context_path}/";
							});
						} else {
							layer.alert(data.msg);
						}
					}, "json");
					return false;
				});
			});
			jQuery(function($) {
				$(document).on('click', '.toolbar a[data-target]', function(e) {
					e.preventDefault();
					var target = $(this).data('target');
					$('.widget-box.visible').removeClass('visible');//hide others
					$(target).addClass('visible');//show target
				});
			});

			//you don't need this, just used for changing background
			jQuery(function($) {
				$('#btn-login-dark').on('click', function(e) {
					$('body').attr('class', 'login-layout');
					$('#id-text2').attr('class', 'white');
					$('#id-company-text').attr('class', 'blue');
					e.preventDefault();
				});
				$('#btn-login-light').on('click', function(e) {
					$('body').attr('class', 'login-layout light-login');
					$('#id-text2').attr('class', 'grey');
					$('#id-company-text').attr('class', 'blue');
					e.preventDefault();
				});
				$('#btn-login-blur').on('click', function(e) {
					$('body').attr('class', 'login-layout blur-login');
					$('#id-text2').attr('class', 'white');
					$('#id-company-text').attr('class', 'light-blue');
					e.preventDefault();
				});

			});

			$(function() {
				if ($("#resetEmail").val() != "") {
					$("#login-box").removeClass("visible");
					$("#forgot-reset").addClass("visible");
				}
			});
		</script>
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
							$(this).css("background-color", "#FFE4B5");
						});
						newDivNode.mouseout(function () {
							if ($("#companyName").val() == "") {
								$("#innerCode").val("");
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
				$("#auto_div").css("width" , $(".form-control").css("width"));

			});

		</script>
	</body>
</html>


