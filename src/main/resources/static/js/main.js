function setPreCss() {
    $("pre").css({"border": "0px", "position": "relative", "padding": "0px"});
    $("pre").append("<div class='paste' style='font-size: 14px'>复制</div>");
    $("pre").mouseover(function () {
        $(this).children(".paste").css("display", "block");
    });
    $("pre").mouseout(function () {
        $(this).children(".paste").css("display", "none");
    });
    $(".paste").on("click", function () {

    });
    var clipboard = new ClipboardJS('.paste', {
        text: function(obj) {
            var childs = $(obj).prev().children();
            var text = "";
            for(i=0;i<childs.length;i++){
                if (i !== 0) {
                    text += "\n";
                }
                text += $(childs[i]).text();
            }
            return text;
        }
    });
    clipboard.on('success', function(e) {
        $(e.trigger).text("复制成功");
        setTimeout(function () {
            $(e.trigger).text("复制");
        },1500);
    });

    clipboard.on('error', function(e) {
        console.log(e);
    });
    var self = $(this);

}

var parentId = null;
var replyTo = null;

function setComment() {
    $(document).on("mouseover", ".comment-line-box", function() {
        $(this).find(".comment-opt").css('display','block');
    });
    $(document).on("mouseout", ".comment-line-box", function() {
        $(this).find(".comment-opt").css('display','none');
    });
    $(document).on("click", ".comment-reply", function() {
        $("#comment-textarea").attr('placeholder','回复@'+$(this).parent().siblings(".comment-name-box").first().text());
        $("#comment-textarea").focus();
        parentId = $(this).attr("pId");
        replyTo = $(this).attr("uid");
    });
    $(document).on("focus", "#comment-textarea", function() {
        $(".comment-btn-list").css("display","");
    });
    //取消
    $(document).on("click", ".comment-cancel", function() {
        $("#comment-textarea").attr('placeholder','说点什么...');
        $(".comment-btn-list").css("display","none");
        parentId = null;
        replyTo = null;
    });
    //查看回复
    $(document).on("click", ".comment-hide", function() {
        var divClazz = ".sub-comment-div" + $(this).attr('cId');
        var show = $(divClazz).css('display');
        $(divClazz).css("display",show =='block'?'none':'block');
        var v1 = '查看回复(' + $(this).attr("count")+")";
        $(this).text(show =='block'?v1:'隐藏回复');
    });
    //删除
    $(document).on("click", ".comment-del", function() {
        var cId = $(this).attr('cId');
        if(window.confirm('你确定要删除吗？')){
            $.ajax({
                type : "POST",
                contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                url : "/delComment",
                data : {commentId: cId},
                success : function(result) {
                    alert(result);
                    location.reload();
                },
                error : function(e){
                }
            });
        }else{
        }
    });
    $(document).on("click", ".comment-publish", function() {
        var content = $("#comment-textarea").val();
        if (!content) {
            alert("请输入评论");
            return;
        }
        var id = location.href.split('/').pop();
        if (!parentId) {
            $.ajax({
                type : "POST",
                contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                url : "/publishComment",
                data : {articleId: id,content: content},
                success : function(result) {
                    alert(result.msg);
                    if (result.state)
                        location.reload();
                },
                error : function(e){
                }
            });
        } else {
            $.ajax({
                type : "POST",
                contentType: "application/x-www-form-urlencoded;charset=UTF-8",
                url : "/publishSubComment",
                data : {articleId: id,content: content, parentId: parentId, replyTo: replyTo},
                success : function(result) {
                    alert(result.msg);
                    if (result.state)
                        location.reload();
                },
                error : function(e){
                }
            });
        }
    });
    // $("#comment-textarea").blur(function(){
    //     $("#comment-textarea").attr('placeholder','说点什么...');
    //     $(".comment-btn-list").css("display","none");
    //     return false;
    // });
    getComment(1);
}

function getComment(page) {
    var id = location.href.split('/').pop().split('#')[0];
    $.ajax({
        type : "POST",
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        url : "/comment",
        data : {articleId: id,currPage: page},
        success : function(result) {
            $(".comment-list-box").html(result);
        },
        error : function(e){
        }
    });
}
