<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8"/>
    <title>用户登录</title>

    <link rel="stylesheet" href="./css/bulma.css">
</head>

<body>
<div class="box" style="margin: 10% auto 0 auto;width: 300px">
    <h2 style="text-align: center;">Sign In</h2>
    <form method="post" action="/login">
        <input type="text" class="input" name="username" placeholder="用户名"
               style="margin-top: 10px;padding-right:30px; background: url(/images/user.png) no-repeat center;background-size: 20px;background-position: 230px">
        <input type="password" class="input" name="password" placeholder="密码"
               style="margin-top: 10px;padding-right:30px; background: url(/images/pwd.png) no-repeat center;background-size: 20px;background-position: 230px">
        <label class="checkbox" style="margin-top: 10px;">
            <input type="checkbox" name="remember-me" checked="checked">记住我
        </label>
        <button class="button" type="submit" style="margin-top: 10px;background-color: #EC407A;color: #fff;float: right;height: 30px;line-height: 1">登录</button>
    </form>
    <a href="/github/callback" class="button" style="margin-top: 10px;background-color: #000;color: #fff;height: 30px;line-height: 1;padding-left: 5px">
        <img src="/images/github.png" height="20px" width="20px" style="margin-right: 5px">Github</a>
    <div style="color: rgba(194,12,60,0.8)">
    <#if RequestParameters['error']??>
        ${SPRING_SECURITY_LAST_EXCEPTION.message}
    </#if></div>
</div>
</body>
</html>