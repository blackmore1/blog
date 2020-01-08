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
    <link rel="stylesheet" href="/editor.md/css/editormd.css" />
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row">
        <form class="layui-form">
            <div class="layui-form-item">
                <label for="title" class="layui-form-label">
                    <span class="x-red">*</span>标题
                </label>
                <div class="layui-input-inline">
                    <input type="text" id="title" name="title" required="" lay-verify="required"
                           autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label for="keyword" class="layui-form-label">
                    <span class="x-red">*</span>关键字
                </label>
                <div class="layui-input-inline">
                    <select name="keyword" lay-verify="required" lay-search="">
                        <option value="">直接选择或搜索选择</option>
                        <#list tags as tag>
                            <option value="${tag.word}">${tag.word}</option>
                        </#list>
                    </select>
                </div>
            </div>
            <div class="layui-form-item">
                <label for="picture" class="layui-form-label">
                    封面
                </label>
                <div class="layui-input-inline">
                    <input type="text" id="picture" name="picture" required=""
                           autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label"><span class="x-red">*</span>正文</label>
                <div class="layui-input-block">
                    <div  id="editormd">
                        <textarea style="display:none;" name="content"></textarea>
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <label for="L_repass" class="layui-form-label">
                </label>
                <button  class="layui-btn" lay-filter="add" lay-submit="">
                    增加
                </button>
            </div>
        </form>
    </div>
</div>
<script src="/js/jquery.min.js"></script>
<script src="/editor.md/editormd.js"></script>
<script>
    var editor;
    $(function() {
        editor = editormd("editormd", {
            height : 720,
            toc : true,
            tocm            :true,
            emoji : true,       // Support Github emoji, Twitter Emoji(Twemoji), fontAwesome, Editor.md logo emojis.
            taskList : true,
            path   : '/editor.md/lib/'
        });
    });
    layui.use(['form', 'layer'],
        function() {
            $ = layui.jquery;
            var form = layui.form,
                    layer = layui.layer;

            //自定义验证规则
            form.verify({
                nikename: function(value) {
                    if (value.length < 5) {
                        return '昵称至少得5个字符啊';
                    }
                },
                pass: [/(.+){6,12}$/, '密码必须6到12位'],
                repass: function(value) {
                    if ($('#L_pass').val() != $('#L_repass').val()) {
                        return '两次密码不一致';
                    }
                }
            });

            //监听提交
            form.on('submit(add)',
                    function(data) {
                        console.log(data);
                        //发异步，把数据提交
                        $.ajax({
                            type : "POST",
                            contentType: "application/json;charset=UTF-8",
                            url : "/admin/blog/insert",
                            data : JSON.stringify(data.field),
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
