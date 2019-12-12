<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <link rel="stylesheet" href="/static/plugins/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/static/css/login.css"/>
    <link rel="stylesheet" href="/static/css/footer.css"/>
    <script type="text/javascript" src="/static/plugins/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap.min.js"></script>
    <title>登录</title>
</head>
<body>
<div class="cms-login-outer">
    <h3>用户登录</h3>
    <div class="cms-login-inner">
        <form id="cms-login-form" action="/login.do" method="post">
            <div class="cms-login-line">
                <input type="text" class="form-control" id="username" name="username" placeholder="用户名">
            </div>
            <div class="cms-login-line">
                <input type="password" class="form-control" id="password" name="password" placeholder="密码">
            </div>
            <div class="cms-login-line form-inline">
                <input type="text" class="form-control" id="vcode" name="vcode" placeholder="验证码">
                <img src="/vcode" class="cms-verify-img" id="verify-img"/>
            </div>
            <div class="form-group cms-login-line">
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="remember-me" name="remember-me">
                    <label class="form-check-label" for="remember-me">
                        记住我
                    </label>
                </div>
            </div>
            <div class="cms-login-line">
                <button type="submit" class="btn btn-block btn-primary">登录</button>
            </div>
        </form>
        <a href="/register">注册用户</a>
    </div>
</div>
<footer>copyright &copy; 2019 by 凉衫薄</footer>
<script type="text/javascript" src="/static/js/login.js"></script>
</body>
</html>