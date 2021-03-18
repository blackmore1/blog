
<!DOCTYPE html>
<html lang="Zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, maximum-scale=1.0, minimum-scale=1.0, initial-scale=1.0, user-scalable=no, viewport-fit=cover">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>文件管理</title>
    <link rel="stylesheet" href="/file-static/css/reset.css">
    <link rel="stylesheet" href="/file-static/js/jquery.magnify.min.css">
    <link rel="stylesheet" href="/file-static/css/index.css">
    <link rel="stylesheet" href="/file-static/css/font/iconfont.css">
</head>

<body>
<div class="allContainer">
<div class="header-content">
    <a href="${indexPath}">
    <button type="button" class="large-button home-button">
        <i class="font-icon icon-home"></i>
        <span>首页</span>
    </button>
    </a>
    <button type="button" class="large-button" id="downloadBtn">
        <i class="font-icon icon-download"></i>
        <span>下载</span>
    </button>

    <button type="button" class="large-button" id="selectAll">
        <i class="iconfont icon-quanxuan" style="vertical-align: middle;"></i>
        <span>全选</span>
    </button>
    <button type="button" class="large-button" id="originalPic">
        <i style="vertical-align: middle;"></i>
        <span>原图</span>
    </button>

    <div class="file-tips-div">
        <span class="file-tips"></span>
    </div>

    <div class="button-group">
        <button type="button" class="btn icon-list active" data-action="set-list-icon" id="checkShowWays"><i></i></button>
        <button type="button" class="btn icon-list-list" data-action="set-list-list"><i></i></button>
    </div>
    <div class="icon-sort">
        <i class="iconfont icon-paixu"></i>
        <ul class="sort-menu-list hide">
            <li data-action="name" class="active"><i class="iconfont icon-check_mark"></i><span>名称</span></li>
            <li data-action="type"><i class="iconfont icon-check_mark"></i><span>类型</span></li>
            <li data-action="size"><i class="iconfont icon-check_mark"></i><span>大小</span></li>
            <li data-action="date"><i class="iconfont icon-check_mark"></i><span>修改时间</span></li>
        </ul>
    </div>
</div>

<div class="file-continer-icon">
    <div class="file-continer" style="display: none" id="fileContiner">
        <#list files as file>
            <div class="file" type="${file.doubleClickType}" data-url="${file.url}" data-relativeUrl="${file.relativePath}">
                <div class="item-select">
                    <div class="iconfont icon-check_mark"></div>
                </div>
                <div class="picture">
                    <img <#if file.doubleClickType='preview'> data-magnify="gallery" data-src="${file.prePicture}"
                    </#if> src="${file.prePicture}"  alt="${file.name}">
                </div>
                <div class="title-type-name">
                    <span>${file.name}</span>
                </div>
            </div>
        </#list>

    </div>
</div>

<div class="file-continer-list hide" id="fileContinerList">
    <div class="list-header">
        <div class="title-item flex2 active" data-action="name">
            <span>名称</span>
            <span class="iconfont icon-chevronup"></span>
        </div>
        <div class="title-item flex1" data-action="type">
            <span>类型</span>
            <span class="iconfont icon-chevronup"></span>
        </div>
        <div class="title-item flex1" data-action="size">
            <span>大小</span>
            <span class="iconfont icon-chevronup"></span>
        </div>
        <div class="title-item flex2" data-action="date">
            <span>修改时间</span>
            <span class="iconfont icon-chevronup"></span>
        </div>
    </div>

    <div class="list-content" id="listContent">
        <#list files as file>
            <div class="list-body"<#if file.doubleClickType='preview'> data-magnify="gallery" data-src="${file.prePicture}"
                    </#if>  type="${file.doubleClickType}" data-url="${file.url}" data-relativeUrl="${file.relativePath}">
                <div class="list-item flex2">
                    <span><img src="${file.prePicture}" alt="${file.name}"></span>
                    <span>${file.name}</span>
                </div>
                <div class="list-item flex1">
                    <span>${file.type}</span>
                </div>
                <div class="list-item flex1">
                    <span>${file.displaySize}</span>
                </div>
                <div class="list-item flex2">
                    <span>${file.modDate?string('yyyy/MM/dd HH:mm:ss')}</span>
                </div>
                <div class="list-item-select">
                    <div class="iconfont icon-check_mark"></div>
                </div>
            </div>
        </#list>
    </div>
</div>
<a href="#" id="download"></a>
</div>
</body>
<script src="/file-static/js/jquery-2.1.4.js"></script>
<script src="/file-static/js/jquery.magnify.min.js"></script>
<script src="/file-static/js/jquery.cookie.js"></script>
<script src="/js/clipboard.min.js"></script>
<script src="/file-static/js/download.js"></script>
<script src="/file-static/js/main.js"></script>
</html>
