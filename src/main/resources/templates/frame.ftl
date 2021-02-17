<!DOCTYPE html>
<html lang="zh">
<head>
    <#if title??>
        <title>${title}</title>
    <#elseif action==actions.INDEX>
        <title>首页</title>
    <#elseif action==actions.ARTICLE>
        <title>${article.title}</title>
    <#elseif action==actions.ARCHIVES>
        <title>归档</title>
    <#elseif action==actions.TAGS>
        <title>标签</title>
    <#elseif action=="error">
        <title>错误</title>
    <#else>
        <title>博客</title>
    </#if>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="referrer" content="no-referrer">

    <link rel="stylesheet" href="/css/bulma.css">
    <link rel="stylesheet" href="/css/all.css">
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/back-to-top.css">
    <link rel="stylesheet" href="/css/progressbar.css">
    <link rel="stylesheet" href="/css/player.css">
    <link rel="stylesheet" href="/css/jquery.marquee.css">
    <#if action == actions.ARTICLE>
    <link rel="stylesheet" href="/editor.md/css/editormd.preview.css" />
    </#if>
    <style>body > .footer, body > .navbar, body > .section {
        opacity: 0
    }</style>
</head>
<body class="is-3-column  pace-done">
<div class="pace  pace-inactive">
    <div class="pace-progress" data-progress-text="100%" data-progress="99"
         style="transform: translate3d(100%, 0px, 0px);">
        <div class="pace-progress-inner"></div>
    </div>
    <div class="pace-activity"></div>
</div>
<nav class="navbar navbar-main"
     style="transition: opacity 0.3s ease-out 0s, transform 0.3s ease-out 0s; opacity: 1; transform: translateY(0px);">
    <div class="container">
        <div class="navbar-brand is-flex-center">
            <a class="navbar-item navbar-logo" href="http://vv.demon4u.com/">
                <img src="/images/logo.png" alt="Hexo" height="28">
            </a>
        </div>
        <div class="navbar-menu">
            <div class="navbar-start">
                <a class="navbar-item <#if action==actions.INDEX>is-active</#if>" href="/">Home</a>
                <a class="navbar-item <#if action==actions.ARCHIVES>is-active</#if>" href="${actions.ARCHIVES}">Archives</a>
                <a class="navbar-item <#if action==actions.TAGS>is-active</#if>" href="${actions.TAGS}">Tags</a>
                <a class="navbar-item" href="http://vv.demon4u.com/about">About</a>
            </div>
            <div class="navbar-end">
                <div class="navbar-item searchDiv" style="display: none">
                    <input type="text" class="input search-input" placeholder="想要查找什么..." value="${q!}" onkeyup=" if(event.keyCode==13) { doSearch() }" style="width: 120px">
                </div>
                <a class="navbar-item search" title="搜索" href="javascript:;" rel="noopener"  onclick="showInput()">
                    <i class="fas fa-search"></i>
                </a>
                <div class="navbar-item">
                <#if user??>
                    <span class="btn-list-group">
                        <img src="<#if (user.avatar)??>${user.avatar}<#else>/images/person16.png</#if>" class="avatar">
                      <dl class="btn-list-area">
                          <dd>${user.nickname}</dd>
                          <dd><a class="has-link-black-ter" href="/logout"><img src="/images/logout.png" width="18px" height="18px" style="vertical-align:middle;margin-right: 20px"><span>退出</span></a></dd>
                      </dl>
                    </span>
                <#else>
                    <a href="/login" class="has-link-black-ter">登录</a>
                </#if>
                </div>
            </div>
        </div>
    </div>
</nav>
<section class="section" style="transition: opacity 0.3s ease-out 0s, transform 0.3s ease-out 0s; opacity: 1;">
    <div class="container">
        <div class="columns">
            <#if action == actions.INDEX>
                <#include "/index.ftl">
            <#elseif action == actions.ARTICLE>
                <#include "/article.ftl">
            <#elseif action == actions.ARCHIVES>
                <#include "/archives.ftl">
            <#elseif action == actions.TAGS>
                <#include "/tags.ftl">
            <#elseif action=="error">
                <#include "/error.ftl">
            </#if>


            <div class="column is-4-tablet is-4-desktop is-3-widescreen  has-order-1 column-left ">
            <#--关注我-->
                <div class="card widget"
                     style="transition: opacity 0.3s ease-out 0s, transform 0.3s ease-out 0s; opacity: 1; transform-origin: center top 0px;">
                    <div class="card-content">
                        <nav class="level">
                            <div class="level-item has-text-centered" style="flex-shrink: 1">
                                <div>
                                    <figure class="image is-128x128 has-mb-6">
                                        <img class="" src="/images/me.jpg" alt="Demon" style="border-radius:50%;">
                                    </figure>
                                    <p class="is-size-4 is-block">
                                        枫叶
                                    </p>
                                    <p class="is-size-6 is-flex is-flex-center has-text-grey">
                                        <i class="fas fa-map-marker-alt has-mr-7"></i>
                                        <span>ShangHai</span>
                                    </p>
                                </div>
                            </div>
                        </nav>
                        <nav class="level is-mobile">
                            <div class="level-item has-text-centered is-marginless">
                                <div>
                                    <p class="heading">
                                        文章
                                    </p>
                                    <a href="${actions.ARCHIVES}">
                                        <p class="title has-text-weight-normal">
                                        ${totalArticles}
                                        </p>
                                    </a>
                                </div>
                            </div>
                            <div class="level-item has-text-centered is-marginless">
                                <div>
                                    <p class="heading">
                                        标签
                                    </p>
                                    <a href="${actions.TAGS}">
                                        <p class="title has-text-weight-normal">
                                            ${tags?size}
                                        </p>
                                    </a>
                                </div>
                            </div>
                        </nav>

                        <div class="level">
                            <a class="level-item button is-link is-rounded" href="https://github.com/blackmore1"
                               target="_blank">
                                关注我</a>
                        </div>
                        <div class="level is-mobile">
                            <a class="level-item button is-white is-marginless" target="_blank" title="Github"
                               href="https://github.com/blackmore1">
                                <i class="fab fa-github"></i>
                            </a>
                            <a class="level-item button is-white is-marginless" target="_blank" title="Facebook"
                               href="https://facebook.com/">
                                <i class="fab fa-facebook"></i>
                            </a>
                            <a class="level-item button is-white is-marginless" target="_blank" title="Twitter"
                               href="https://twitter.com/">
                                <i class="fab fa-twitter"></i>
                            </a>
                            <a class="level-item button is-white is-marginless" target="_blank" title="Dribbble"
                               href="https://dribbble.com/">
                                <i class="fab fa-dribbble"></i>
                            </a>
                            <a class="level-item button is-white is-marginless" target="_blank" title="RSS"
                               href="http://vv.demon4u.com/">
                                <i class="fas fa-rss"></i>
                            </a>
                        </div>
                    </div>
                </div>

                <div class="card widget"
                     style="transition: opacity 0.3s ease-out 0s, transform 0.3s ease-out 0s; opacity: 1; transform-origin: center top 0px;">
                    <div class="card-content">
                        <div id="QPlayer">
                            <div id="pContent">
                                <div id="player">
                                    <span class="cover"></span>
                                    <div class="ctrl">
                                        <div class="musicTag marquee">
                                            <strong>Title</strong>
                                            <span> - </span>
                                            <span class="artist">Artist</span>
                                        </div>
                                        <div class="player_progress">
                                            <div class="timer left">0:00</div>
                                            <div class="contr">
                                                <div class="rewind icon"></div>
                                                <div class="playback icon"></div>
                                                <div class="fastforward icon"></div>
                                            </div>
                                            <div class="right">
                                                <div class="liebiao icon"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <ol id="playlist"></ol>
                        </div>
                    </div>
                </div>

                <div class="card widget"
                     style="transition: opacity 0.3s ease-out 0s, transform 0.3s ease-out 0s; opacity: 1; transform-origin: center top 0px;">
                    <div class="card-content">
                        <div class="menu">
                            <h3 class="menu-label">
                                归档
                            </h3>
                            <ul class="menu-list">
                                <#list archives.keySet() as key>
                                    <li>
                                        <a class="level is-marginless" href="${actions.ARCHIVES + "/" + key}">
                                        <span class="level-start">
                                            <span class="level-item">${key}年</span>
                                        </span>
                                            <span class="level-end">
                                            <span class="level-item tag">${archives[key]}</span>
                                        </span>
                                        </a>
                                    </li>
                                </#list>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

            <div class="column is-4-tablet is-4-desktop is-3-widescreen is-hidden-desktop-only has-order-3 column-right ">
                <div class="card widget"
                     style="transition: opacity 0.3s ease-out 0s, transform 0.3s ease-out 0s; opacity: 1; transform-origin: center top 0px;">
                    <div class="card-content">
                        <h3 class="menu-label">
                            最热文章
                        </h3>
                        <#list popularArticles as article>
                            <article class="media">
                                <a href="${actions.ARTICLE + "/" + article.id}" class="media-left">
                                    <p class="image is-64x64">
                                        <img class="thumbnail" src="/images/thumbnail.svg" alt="test9">
                                    </p>
                                </a>
                                <div class="media-content">
                                    <div class="content">
                                        <div>
                                            <time class="has-text-grey is-size-7 is-uppercase">${article.publishDate}
                                            </time>
                                        </div>
                                        <a href="${actions.ARTICLE + "/" + article.id}"
                                           class="title has-link-black-ter is-size-6 has-text-weight-normal">${article.title}</a>
                                        <p class="is-size-7 is-uppercase">
                                        </p>
                                    </div>
                                </div>
                            </article>
                        </#list>
                    </div>
                </div>

                <div class="card widget"
                     style="transition: opacity 0.3s ease-out 0s, transform 0.3s ease-out 0s; opacity: 1; transform-origin: center top 0px;">
                    <div class="card-content">
                        <div class="menu">
                            <h3 class="menu-label">
                                标签
                            </h3>
                            <div class="field is-grouped is-grouped-multiline">
                                <#list tags.keySet() as tag>
                                <div class="control">
                                    <a class="tags has-addons" href="${actions.TAGS + "/" + tag}">
                                        <span class="tag">${tag}</span>
                                        <span class="tag is-grey">${tags[tag]}</span>
                                    </a>
                                </div>
                                </#list>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<footer class="footer" style="transition: opacity 0.3s ease-out 0s, transform 0.3s ease-out 0s; opacity: 1;">
    <div class="container">
        <div class="level">
            <div class="level-start has-text-centered-mobile">
                <a class="footer-logo is-block has-mb-6" href="http://vv.demon4u.com/">
                    <img src="/images/logo.png" alt="Hexo" height="28">
                </a>
                <p class="is-size-7">
                    © 2019&nbsp;
                    Powered by <a href="https://github.com/blackmore1" target="_blank">blackmore1</a>
                </p>
            </div>
            <div class="level-end">
                <div class="field has-addons is-flex-center-mobile has-mt-5-mobile is-flex-wrap is-flex-middle">
                    <p class="control">
                        <a class="button is-white is-large" target="_blank" title="Creative Commons"
                           href="https://creativecommons.org/">
                            <i class="fab fa-creative-commons"></i>
                        </a>
                    </p>
                    <p class="control">
                        <a class="button is-white is-large" target="_blank" title="Attribution 4.0 International"
                           href="https://creativecommons.org/licenses/by/4.0/">
                            <i class="fab fa-creative-commons-by"></i>
                        </a>
                    </p>
                    <p class="control">
                        <a class="button is-white is-large" target="_blank" title="Download on GitHub"
                           href="https://github.com/blackmore1">
                            <i class="fab fa-github"></i>
                        </a>
                    </p>
                </div>
            </div>
        </div>
    </div>
</footer>

<a id="back-to-top" title="回到顶端" href="javascript:;" rel="noopener"
   class="card has-text-centered rise-up fade-in"
   style="left: 1016.25px; width: 64px; bottom: 20px; border-radius: 4px;">
    <i class="fas fa-chevron-up"></i>
</a>
<script src="/js/jquery.min.js"></script>
<script src="/js/back-to-top.js" defer=""></script>
<script src="/js/animation.js"></script>
<script src="/js/pace.min.js"></script>
<script src="/js/main.js"></script>
<script src="/js/clipboard.min.js"></script>
<script src="/js/jquery.marquee.min.js"></script>
<script src="/js/player.js"></script>
<script type="text/javascript">
    function doSearch() {
        window.open("${actions.SEARCH + "/"}" + $(".search-input").val(), "_self");
    };
    function showInput() {
        $(".searchDiv").show();
        $(".search").css('display','none');;
    }
</script>
<#if action == actions.ARTICLE>
<script src="/editor.md/lib/marked.min.js"></script>
<script src="/editor.md/lib/prettify.min.js"></script>
<script src="/editor.md/lib/raphael.min.js"></script>
<script src="/editor.md/lib/underscore.min.js"></script>
<script src="/editor.md/lib/sequence-diagram.min.js"></script>
<script src="/editor.md/lib/flowchart.min.js"></script>
<script src="/editor.md/lib/jquery.flowchart.min.js"></script>
<script src="/editor.md/editormd.min.js"></script>
<script type="text/javascript">
    //正文
    $("#content_area").val('${article.content}')
    testEditormdView2 = editormd.markdownToHTML("art_content", {
        htmlDecode      : "style,script,iframe",  // you can filter tags decode
        emoji           : true,
        taskList        : true,
        tex             : true,  // 默认不解析
        flowChart       : true,  // 默认不解析
        sequenceDiagram : true,  // 默认不解析
        tocm            :true,
    });
    $(function(){
        setPreCss();
        setComment()
    });
</script>
</#if>

</body>