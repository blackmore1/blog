
function DownloadImg() {
    this.frame = null; //
    this.isIE = !!window.ActiveXObject || ("ActiveXObject" in window);
}
/**
 *@param clickId a标签的id值
 *@param imgUrl 要下载的图片的路径
 */
DownloadImg.prototype.init = function(clickId, imgUrl) {
    var oA = document.getElementById(clickId),
        that = this;
    /*检测是否为同源图片*/
    var imgHost, localHost = location.host,
        doubleIndex = imgUrl.indexOf('//');
    if (doubleIndex != -1) {
        imgHost = imgUrl.substring(doubleIndex + 2, imgUrl.indexOf(doubleIndex + 2));
        if (imgHost != localHost) { //如果图片非同源
            oA.href = imgUrl;
            return;
        }
    }
    /*检测是否为同源图片end*/
    if (that.isIE) { //如果是IE浏览器，使用方法二
        oA.onclick = function() {
            that.createIframe(imgUrl);
        }
    } else { //如果是非IE浏览器，使用方法一
        oA.download = imgUrl.substring(imgUrl.lastIndexOf('/') + 1);
        oA.href = imgUrl;
    }
}
DownloadImg.prototype.createIframe = function(imgUrl) {
    var that = this;
    //如果隐藏的iframe不存在则创建
    if (!this.frame) {
        var oBody = document.getElementsByTagName('body')[0],
            frame = document.createElement('iframe');
        frame.style.display = 'none';
        frame.name = 'downloadIframe';//在IE7下设置好的name属性会变成submitName，用setAttribute设置也是同样效果，待解决
        frame.width = 0;
        frame.height = 0;
        this.frame = frame;
        // this.frame.onload = this.downloadImg();//这种方式绑定会有问题，待解决

        this.addEvent(this.frame, 'load', function() { //给iframe绑定一个load方法，load完成便会触发下载方法
            that.downloadImg();
        })
        oBody.appendChild(this.frame); //将创建的iframe添加到body中
    }
    if (this.frame.src != imgUrl) { //如果本次要下载的图片路径不等于上一次下载的图片路径，那么对iframe进行重新赋值，那么又将会触发load事件，从而间接的触发下载事件
        this.frame.src = imgUrl;
    } else { //如果本次要下载的图片路径等于上一次下载的图片路径，直接调用下载图片方法
        this.downloadImg();
    }
}
DownloadImg.prototype.downloadImg = function() {
    if (this.frame.src != '' && this.frame.src != 'about:blank') { //如果iframe的src路径存在那么调用下载浏览器的保存方法
        window.frames["downloadIframe"].document.execCommand("SaveAs");
    }
}
DownloadImg.prototype.addEvent = function(el, eventType, handler) { //事件兼容
    if (el.attachEvent) {
        el.attachEvent('on' + eventType, handler);
    } else if (el.addEventListener) {
        el.addEventListener(eventType, handler, false);
    } else {
        el['on' + eventType] = handler;
    }
}



function getFile(url){
  $.ajax({
        url: 'http://10.8.4.21:8084/multiDownload?paths=/Documents/bbs/%E6%B4%BB%E5%8A%A8%E6%96%B9%E6%A1%88/%E7%AB%9E%E7%8C%9C%E6%B4%BB%E5%8A%A8/Desktop/%E5%B9%B8%E8%BF%90%E5%A4%A7%E6%8A%BD%E5%A5%96.psd',
        type: 'post',
        data: {},
        success: function (result) {
        }
    });
}



 function  postExcelFile(params, url) {
    //params是post请求需要的参数，url是请求url地址
    var form = document.createElement("form");
    form.style.display = "none";
    form.action = url;
    form.method = "post";
    document.body.appendChild(form);
    // 动态创建input并给value赋值
    for (var key in params) {
        var input = document.createElement("input");
        input.type = "hidden";
        input.name = key;
        input.value = params[key];
        form.appendChild(input);
    }

    form.submit();
    form.remove();
}