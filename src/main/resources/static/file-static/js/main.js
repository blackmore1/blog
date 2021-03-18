$(function(){

  var downloadImg = new DownloadImg();
  //下载图片
  $("#downloadBtn").on("click",function(){
    var flag = $("#checkShowWays").hasClass("active");
    var filesEl;
    if(flag){
      filesEl = $("#fileContiner .item-selected");
    }else{
      filesEl = $("#fileContinerList .item-selected");
    }
    if(filesEl.length  === 1 && (filesEl.attr("type") === "preview" || filesEl.attr("type") === "others")){
      downloadImg.init('download', filesEl.data("url"));
      var a = document.getElementById("download").click();
    }else if(filesEl.length > 0){
      // var url = "/multiDownload?paths=";
      var url = "";
      for (var i = 0; i < filesEl.length; i++ ) {
        if(i === filesEl.length -1){
          url = url +$(filesEl[i]).data("relativeurl");
        }else{
          url = url +$(filesEl[i]).data("relativeurl") + ",";
        }
      }
      // window.location = url;
      postExcelFile({paths: url},"/multiDownload")
    }

  });

  var AllFlag = true;
  $("#selectAll").on("click", function () {
    var flag = $("#checkShowWays").hasClass("active");
    if(AllFlag){
      $(this).addClass("active");
      AllFlag = false;
      if(flag){
        $("#fileContiner").children().addClass("item-selected");
      }else{
        $("#listContent").children().addClass("item-selected");
      }
    }else {
      AllFlag = true;
      if(flag){
        $("#fileContiner").children().removeClass("item-selected");
      }else{
        $("#listContent").children().removeClass("item-selected");
      }
    }

  });

  //复制原图链接
    var clipboard = new ClipboardJS('#originalPic', {
        text: function(obj) {
            var text = "";
            var flag = $("#checkShowWays").hasClass("active");
            var filesEl;
            if(flag){
                filesEl = $("#fileContiner .item-selected");
            }else{
                filesEl = $("#fileContinerList .item-selected");
            }
            if(filesEl.length > 0) {
              text = window.location.protocol + "//" + window.location.hostname;
                var port = window.location.port;
                if (port != "") {
                    text = text + ":" + port;
                }
                text = text + $(filesEl[0]).data("url");
            } else {
                $('.file-tips').text("请选择图片");
                setTimeout(function () {
                    $('.file-tips').text("");
                },1500);
            }
            return text;
        }
    });
    clipboard.on('success', function(e) {
        $('.file-tips').text("复制成功");
        setTimeout(function () {
            $('.file-tips').text("");
        },1500);
    });

    clipboard.on('error', function(e) {
        console.log(e);
    });

  //hide selected
  $("#fileContiner,#fileContinerList,.list-content,.file-continer-icon").on("click", function(e){
    if(e.target === this && !e.metaKey && !e.ctrlKey && !e.shiftKey){
      $(".file").removeClass("item-selected");
      $(".list-body").removeClass("item-selected");
      $(".icon-sort .icon-paixu").removeClass("active");
      AllFlag = true;
      $("#selectAll").removeClass("active");
      return false;
    }
    $(".sort-menu-list").addClass("hide");
    $("#selectAll").removeClass("active");
  })


  //click selected
  $(".file").on("click",function(e){
    AllFlag = true;
    $("#selectAll").removeClass("active");
    if(e.metaKey || e.ctrlKey || e.shiftKey){
      $(this).hasClass("item-selected") ? $(this).removeClass("item-selected") : $(this).addClass("item-selected");

    }else{
      $(this).addClass("item-selected").siblings().removeClass("item-selected");
    }
  })

  //dblclick
  $(".file,.list-body").on("dblclick", function () {
    var type = $(this).attr("type");
    switch (type) {
      case "preview":
        break;
      case "folder":
        var url = $(this).data("url");
        window.location.href = encodeURI(url);
        break;
    }

  })

  //click icon selected
  $(".file .item-select,.list-body .list-item-select").on("click", function(){
    $(this).parent().addClass("item-selected");
    return false;
  })
  //picture modal
  $('[data-magnify=gallery]').magnify({
    initMaximized: true,
  });

  //排列方式
  if(localStorage.getItem("iconOrList")){
    if(localStorage.getItem("iconOrList")=== "icon"){
      $(".file-continer-list").hide();
      $(".file-continer").show();
      $(".set-list-icon").addClass("active");
      $(".icon-list").addClass("active");
      $(".icon-list-list").removeClass("active")
    }else{
      $(".file-continer-list").show();
      $(".file-continer").hide();
      $(".icon-list").removeClass("active");
      $(".icon-list-list").addClass("active")
    }
  }else{
    $(".file-continer").show();
  }

  $(".button-group button").on("click", function(){
    $(this).addClass("active").siblings().removeClass("active");
    var action = $(this).data("action");
    if( action=== "set-list-icon"){
      localStorage.setItem('iconOrList', "icon");
      $(".file-continer-list").hide();
      $(".file-continer").show();
    }else if(action === "set-list-list"){
      localStorage.setItem('iconOrList', "list");
      $(".file-continer-list").show();
      $(".file-continer").hide();
    }
  })

  //排序图标点击事件
  $(".icon-sort .icon-paixu").on("click",function(){
    $(this).addClass("active");
    $(".sort-menu-list").removeClass("hide");
  })

  //排列方式
  if(localStorage.getItem("sort")){
    var sort = localStorage.getItem("sort");
    var lis = $(".sort-menu-list li");
    var lisHeader = $(".list-header .title-item");
    for (var a = 0; a< lis.length; a++){
      console.log($(lis[a]).data("action"),sort)
      if($(lis[a]).data("action") === sort){
        $(lis[a]).addClass("active").siblings().removeClass("active");
        $(lisHeader[a]).addClass("active").siblings().removeClass("active");
      }
    }
  }
  $(".sort-menu-list li").on("click", function(){
    var thisAction = $(this).data("action");
    $(this).addClass("active").siblings().removeClass("active");
    $(".sort-menu-list").addClass("hide");
    $.cookie('sort', thisAction,{
      path: "/"
    });
    $.cookie('asc', "1",{
      path: "/"
    });
    localStorage.setItem('sort', thisAction);
    window.location.reload();
  })
  $(".list-header .title-item").on("click",function () {
    var thisAction = $(this).data("action");
    $(this).addClass("active").siblings().removeClass("active");
    $(".sort-menu-list").addClass("hide");
    $.cookie('sort', thisAction,{
      path: "/"
    });
    if( $.cookie("asc") == 1){
      $.cookie('asc', "-1",{
        path: "/"
      });
    }else{
      $.cookie('asc', "1",{
        path: "/"
      });
    }
    localStorage.setItem('sort', thisAction);
    window.location.reload();
  })

  //列表形式的文件列表点击
  //头部
  $(".list-content .list-body").on("click", function(){
    $(this).addClass("active").siblings().removeClass("active")
  })
  $(".list-content .list-body").on("click", function(e){
    AllFlag = true;
    $("#selectAll").removeClass("active");
    if(e.metaKey || e.ctrlKey || e.shiftKey){
      // $(this).addClass("item-selected");
      $(this).hasClass("item-selected") ? $(this).removeClass("item-selected") : $(this).addClass("item-selected");
    }else{
      $(this).addClass("item-selected").siblings().removeClass("item-selected")
    }
  });

    //监听粘贴服务 支持拖拽和粘贴上传
    document.addEventListener('paste', function (event) {
        var items = (event.clipboardData || window.clipboardData).items;
        var file = null;
        if (items && items.length) {
            // 搜索剪切板items
            for (var i = 0; i < items.length; i++) {
                if (items[i].type.indexOf('image') !== -1) {
                    file = items[i].getAsFile();
                    break;
                }
            }
        } else {
            // alert("当前浏览器不支持");
            return;
        }
        if (!file) {
            return;
        }
        // 此时file就是我们的剪切板中的图片对象
        upload(file);
    });
$('.allContainer').on('dragover',function(event){
    event.preventDefault();
    $('.file-tips').text("松开后开始上传");
}).on('dragleave',function(event){
    event.preventDefault();
    $('.file-tips').text("");
}).on('drop',function(event){
    event.preventDefault();
    $('.file-tips').text("");
    //数据在event的dataTransfer对象里
    var file = event.originalEvent.dataTransfer.files[0];
    //同样用fileReader实现图片上传
    var fd = new FileReader();
    var fileType = file.type;
    fd.readAsDataURL(file);
    fd.onload = function(){
        if(/^image\/[jpeg|png|gif|jpg]/.test(fileType)){
            upload(file);
        }else{
            alert('仅支持拖拽图片')
            return;
        }
    };
});

function upload(file) {
    $('.file-tips').text("正在上传...");
    // 这里是上传
    var xhr = new XMLHttpRequest();
    // 上传结束
    xhr.onload = function () {

    };
    xhr.onerror = function () {
        alert("网络异常，上传失败!");
    };
    var formData = new FormData();
    formData.append("file", file);
    formData.append("path", window.location.pathname);
    xhr.open('POST', '/upload', true);
    xhr.send(formData);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status >= 200 && xhr.status < 300 || xhr.status === 304) {
                //5.处理返回的结果
                var obj = xhr.responseText;
                obj = JSON.parse(obj);
                if (obj.state) {
                    window.location.reload();
                } else {
                    $('.file-tips').text(obj.msg);
                }
            } else {
                alert("网络异常，上传失败!");
            }
        }
    }
}


});
