<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <link rel="stylesheet" href="/static/plugins/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/static/css/login.css"/>
    <script type="text/javascript" src="/static/plugins/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap.min.js"></script>
    <title>登录</title>
</head>
<body>
<div class="login-outer">
    <h3>用户登录</h3>
    <div class="login-inner">
        <form>
            <div class="login-line">
                <input type="text" class="form-control" id="username" placeholder="用户名">
            </div>
            <div class="login-line">
                <input type="password" class="form-control" id="password" placeholder="密码">
            </div>
            <div class="login-line form-inline">
                <input type="text" class="form-control" id="vcode" placeholder="验证码">
                <img src="/vcode" class="verify-img" id="verify-img"/>
            </div>
            <div class="form-group login-line">
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="remember-me">
                    <label class="form-check-label" for="remember-me">
                        记住我
                    </label>
                </div>
            </div>
            <div class="login-line">
                <button type="submit" class="btn btn-block btn-primary">登录</button>
            </div>
        </form>
        <#--<form class="layui-form" id="login-form" action="/login.do" method="post">
            <div class="layui-form-item">
                <input type="text" name="username" id="username" required placeholder="用户名" class="layui-input">
            </div>
            <div class="layui-form-item">
                <input type="password" name="password" id="password" required placeholder="密码" class="layui-input">
            </div>
            <div class="layui-form-item">
                <div class="layui-inline">
                    <input type="text" name="vcode" id="vcode" required placeholder="验证码" class="layui-input">
                </div>
                <div class="layui-inline">
                    <img src="/vcode" class="verify-img" id="verify-img"/>
                </div>
            </div>
            <div class="layui-form-item">
                <input name="remember-me" lay-skin="primary" title="记住我" type="checkbox" value="true">
            </div>
            <div class="layui-form-item">
                <input type="submit" class="layui-btn layui-btn-normal" id="login-btn" value="登陆">
            </div>
        </form>-->
        <a href="/register">注册用户</a>
    </div>
</div>
<script type="text/javascript" src="/static/js/login.js"></script>
</body>
</html>