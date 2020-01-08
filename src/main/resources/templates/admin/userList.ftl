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
    <script src="/X-admin/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="/X-admin/js/xadmin.js"></script>
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="x-nav">
    <a class="layui-btn layui-btn-small" style="line-height:1.6em;margin-top:3px;float:right" onclick="location.reload()" title="刷新">
        <i class="layui-icon layui-icon-refresh" style="line-height:30px"></i></a>
</div>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-body ">
                        <div class="layui-inline layui-show-xs-block">
                            <input type="text"  placeholder="请输入username" autocomplete="off" class="layui-input search-input"  value="${q!}">
                        </div>
                        <div class="layui-inline layui-show-xs-block">
                            <button class="layui-btn"  lay-submit="" lay-filter="sreach" onclick='window.open("/admin/searchUser/" + $(".search-input").val(), "_self");'><i class="layui-icon">&#xe615;</i></button>
                            <button class="layui-btn" onclick="xadmin.open('添加用户','/admin/toAddUser',600,400)"><i class="layui-icon"></i>添加</button>
                        </div>
                </div>
                <div class="layui-card-body " style="overflow-x: auto">
                    <table class="layui-table layui-form">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>用户名</th>
                            <th>昵称</th>
                            <th>第三方类型</th>
                            <th>第三方标识</th>
                            <th>创建时间</th>
                            <th>权限</th>
                            <th>操作</th>
                        </thead>
                        <tbody>
                        <#list users as user>
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.username}</td>
                            <td>${user.nickname}</td>
                            <td>${user.appType!}</td>
                            <td>${user.appId!}</td>
                            <td>${user.createTime}</td>
                            <td>
                            <#if (user.authorities?size>1)>管理员<#else>用户</#if>
                            </td>
                            <td class="td-manage">
                                <a title="编辑"  onclick="xadmin.open('编辑','/admin/toEditUser/${user.id}', 600, 400)" href="javascript:;">
                                    <i class="layui-icon">&#xe642;</i>
                                </a>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
                <div class="layui-card-body ">
                    <div class="page">
                        <div>
                            <#if pageDto??>
                            <#if (pageDto.lastUrl)??>
                                <a class="prev" href="${pageDto.lastUrl}">&lt;&lt;</a>
                            </#if>
                            <#list pageDto.pageUrls.keySet() as key>
                                <#if pageDto.number == key>
                                <span class="current">${key}</span>
                                <#else>
                                <a class="num" href="${pageDto.pageUrls.get(key)}">${key}</a>
                                </#if>
                            </#list>
                            <#if (pageDto.nextUrl)??>
                                <a class="next" href="${pageDto.nextUrl}">&gt;&gt;</a>
                            </#if>
                            </#if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    layui.use(['laydate','form'], function(){
        var laydate = layui.laydate;
        var form = layui.form;

        //执行一个laydate实例
        laydate.render({
            elem: '#start' //指定元素
        });

        //执行一个laydate实例
        laydate.render({
            elem: '#end' //指定元素
        });
    });

    /*用户-停用*/
    function member_stop(obj,id){
        layer.confirm('确认要停用吗？',function(index){

            if($(obj).attr('title')=='启用'){

                //发异步把用户状态进行更改
                $(obj).attr('title','停用')
                $(obj).find('i').html('&#xe62f;');

                $(obj).parents("tr").find(".td-status").find('span').addClass('layui-btn-disabled').html('已停用');
                layer.msg('已停用!',{icon: 5,time:1000});

            }else{
                $(obj).attr('title','启用')
                $(obj).find('i').html('&#xe601;');

                $(obj).parents("tr").find(".td-status").find('span').removeClass('layui-btn-disabled').html('已启用');
                layer.msg('已启用!',{icon: 5,time:1000});
            }

        });
    }

</script>
</html>