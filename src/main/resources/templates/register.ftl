<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <link rel="stylesheet" href="/static/plugins/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/static/css/register.css"/>
    <link rel="stylesheet" href="/static/css/footer.css"/>
    <script type="text/javascript" src="/static/plugins/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap.min.js"></script>
    <title>注册</title>
</head>
<body>
<div class="cms-register-outer">
    <h3>用户注册</h3>
    <div class="cms-register-inner">
        <div class="cms-register-line">
            <input type="text" class="form-control" id="username" placeholder="用户名">
        </div>
        <div class="cms-register-line">
            <input type="tel" class="form-control" id="username" placeholder="电话">
        </div>
        <div class="cms-register-line">
            <input type="email" class="form-control" id="username" placeholder="电话">
        </div>
        <div class="cms-register-line">
            <input type="password" class="form-control" id="password" placeholder="密码">
        </div>
        <div class="cms-register-line">
            <input type="password" class="form-control" id="check-password" placeholder="确认密码">
        </div>
        <div class="cms-register-line form-inline">
            <input type="text" class="form-control" id="vcode" placeholder="验证码">
            <img src="/vcode" class="cms-verify-img" id="cms-verify-img"/>
        </div>
        <div class="cms-register-line">
            <button class="btn btn-block btn-primary" id="cms-register-btn">注册</button>
        </div>
    </div>
</div>
<footer>copyright &copy; 2019 by 凉衫薄</footer>
<script type="text/javascript" src="/static/js/register.js"></script>
</body>
</html>