<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <link rel="stylesheet" href="/static/plugins/layui/css/layui.css"/>
    <link rel="stylesheet" href="/static/css/index.css"/>
    <script src="/static/plugins/layui/layui.js"></script>
    <title>内容管理系统</title>
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <!-- 头部区域（可配合layui已有的水平导航） -->
    <div class="layui-header">
        <div class="layui-logo">内容管理系统</div>
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="javascript:"><i class="layui-icon layui-icon-notice"></i>&nbsp;&nbsp;消息</a>
            </li>
            <li class="layui-nav-item">
                <a href="javascript:">
                    <img src="/static/image/background.png" class="layui-nav-img layui-circle">
                    用户名
                </a>
                <dl class="layui-nav-child">
                    <dd><a href="javascript:" data-url="/user/info" data-title="个人信息"
                           data-id="info">个人信息</a></dd>
                    <dd><a href="/logout">注销</a></dd>
                </dl>
            </li>
        </ul>
    </div>

    <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
    <div class="layui-side layui-bg-black">
        <div class="layui-side-scroll">
            <ul class="layui-nav layui-nav-tree">
                <li class="layui-nav-item">
                    <a href="javascript:"><i class="layui-icon layui-icon-user"></i>&nbsp;&nbsp;用户</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:" data-url="/user/info" data-title="个人中心"
                               data-id="info">个人信息</a>
                        </dd>
                    </dl>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:"><i class="layui-icon layui-icon-set-fill"></i>&nbsp;&nbsp;系统</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:" data-url="/admin/system/setting_management" data-title="常规设置"
                               data-id="setting_management">常规设置</a></dd>
                        <dd><a href="javascript:" data-url="/admin/system/menu_management" data-title="菜单管理"
                               data-id="menu_management">菜单管理</a>
                        </dd>
                        <dd><a href="javascript:" data-url="/admin/system/backup_management" data-title="数据备份"
                               data-id="backup_management">数据备份</a></dd>
                    </dl>
                </li>
            </ul>
        </div>
    </div>

    <!-- 内容主体区域 -->
    <div class="layui-body">
        <div class="layui-tab layui-tab-brief" lay-filter="tabs" lay-allowClose="true">
            <ul class="layui-tab-title">
                <li class="layui-this"><span class="layui-icon layui-icon-home"></span>&nbsp;&nbsp;首页</li>
            </ul>
            <div class="layui-tab-content">
                <div class="layui-tab-item layui-show">
                    <iframe src="/dashboard" width="100%" height="100%" name="iframe" scrolling="auto"
                            class="iframe" framborder="0"></iframe>
                </div>
            </div>
        </div>
    </div>

    <!-- 底部固定区域 -->
    <div class="layui-footer">
        <center>底部信息</center>
    </div>
</div>
<script type="text/javascript" src="/static/js/index.js"></script>
</body>
</html>