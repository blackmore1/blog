<!DOCTYPE html>
<html class="x-admin-sm">

<head>
    <meta charset="UTF-8">
    <title>欢迎页面-X-admin2.2</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />
    <link rel="stylesheet" href="/X-admin/css/font.css">
    <link rel="stylesheet" href="/X-admin/css/xadmin.css">
    <script type="text/javascript" src="/X-admin/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="/X-admin/js/xadmin.js"></script>
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row">
        <form class="layui-form">
            <input type="text" name="id" style="display: none" value="${user.id}" />
            <div class="layui-form-item">
                <label for="title" class="layui-form-label">
                    <span class="x-red">*</span>用户名
                </label>
                <div class="layui-input-inline">
                    ${user.username}
                </div>
            </div>
            <div class="layui-form-item">
                <label for="keyword" class="layui-form-label">
                    <span class="x-red">*</span>权限
                </label>
                <div class="layui-input-inline">
                    <select name="role" lay-verify="required" lay-search="">
                        <option value="ROLE_USER">用户</option>
                        <option value="ROLE_ADMIN" <#if isAdmin>selected="selected"</#if>>管理员</option>
                    </select>
                </div>
            </div>
            <div class="layui-form-item">
                <label for="L_repass" class="layui-form-label">
                </label>
                <button  class="layui-btn" lay-filter="add" lay-submit="">
                    更新
                </button>
            </div>
        </form>
    </div>
</div>
<script src="/js/jquery.min.js"></script>
<script src="/editor.md/editormd.js"></script>
<script>
    layui.use(['form', 'layer'],
            function() {
                $ = layui.jquery;
                var form = layui.form,
                        layer = layui.layer;

                //监听提交
                form.on('submit(add)',
                        function(data) {
                            console.log(data);
                            //发异步，把数据提交
                            $.ajax({
                                type : "POST",
                                contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                                url : "/admin/user/edit",
                                data : data.field,
                                success : function(result) {
                                    console.log(result);
                                    layer.alert(result, {
                                                icon: 6
                                            },
                                            function() {
                                                //关闭当前frame
                                                xadmin.close();

                                                // 可以对父窗口进行刷新
                                                xadmin.father_reload();
                                            });
                                },
                                error : function(e){
                                    layer.msg(e.status,{icon:2,time:1000});
                                }
                            });
                            return false;
                        });

            });</script>
</body>

</html>
