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
    <jsp:include page="/WEB-INF/view/common/basecss.jsp" flush="true"/>
</head>
<body class="no-skin">
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>

    <div class="main-container-inner">
        <a class="menu-toggler" id="menu-toggler" href="#">
            <span class="menu-text"></span>
        </a>

        <div class="main-content">
            <div class="breadcrumbs" id="breadcrumbs">
                <script type="text/javascript">
                    try {
                        ace.settings.check('breadcrumbs', 'fixed')
                    } catch (e) {
                    }
                </script>

                <ul class="breadcrumb">
                    <li>
                        <i class="icon-home home-icon"></i>
                        <a href="#">首页</a>
                    </li>
                    <li class="active">控制台</li>
                </ul>
                <!-- .breadcrumb -->

                <%--<div class="nav-search" id="nav-search">
                    <form class="form-search">
								<span class="input-icon">
									<input type="text" placeholder="Search ..." class="nav-search-input"
                                           id="nav-search-input" autocomplete="off"/>
									<i class="icon-search nav-search-icon"></i>
								</span>
                    </form>
                </div><!-- #nav-search -->--%>
            </div>

            <div class="page-content">


                <%--<jsp:include page="charts/circle_chart.jsp" flush="true"></jsp:include>--%>

                <div class="row" style="text-align: center"><h1 style="color: red">${title}</h1></div>

                <div class="row" style="margin: 40px; text-align: center;"><p style="align-content: center">
                    <img src="${image.imgUrl}" width="${image.width}" height="${image.height}"></p></div>

                <div class="row" style="font-size: 18px; text-indent:2em; color: green; margin: 40px;"><p>
                    公告内容：${content}</p></div>

                <div class="row" style="font-size: 18px; text-indent:2em; color: blue; margin: 40px;"><p>
                    发布时间：${createTime}</p></div>
            </div>
            <!-- /.page-content -->


        </div>
        <!-- /.main-content -->

    </div>
    <!-- /.main-container-inner -->

    <%-- <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
         <i class="icon-double-angle-up icon-only bigger-110"></i>
     </a>--%>
</div>


</div><!-- /.main-container -->
<!-- basic scripts -->
<jsp:include page="/WEB-INF/view/common/basejs.jsp" flush="true"/>
</body>
</html>

