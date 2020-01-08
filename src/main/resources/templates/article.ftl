<link rel="stylesheet" href="/editor.md/css/editormd.preview.css" />
<div class="column is-8-tablet is-8-desktop is-6-widescreen has-order-2 column-main">
    <#if article??>
    <div class="card"
         style="transition: opacity 0.3s ease-out 0s, transform 0.3s ease-out 0s; opacity: 1; transform-origin: center top 0px;">
        <div class="card-content article ">
            <h2 class="title is-size-3 is-size-4-mobile has-text-weight-normal">
                ${article.title}
            </h2>
            <div class="level article-meta is-size-7 is-mobile is-overflow-x-auto">
                <div class="level-left">
                    <i class="fas fa-bookmark"></i>[<a class="has-link-grey has-text-weight-normal" href="${actions.TAGS + "/" + article.keyword}">${article.keyword}</a>]
                    &nbsp;<i class="fas fa-fire"></i>${article.clickCount!}
                </div>
                <div class="level-right">
                    <i class="fas fa-clock"></i><span class="level-item has-text-grey">${article.publishDate}</span>
                </div>
            </div>
            <img src="${article.picture}" style="width:100%" class="img-responsive" id="blog-cover" />
            <div class="" id="art_content">
                <textarea id="content_area" style="display:none;"></textarea>
            </div>
            <br>
            <div class="level article-meta is-size-7 is-mobile is-overflow-x-auto">
                <div class="level-left">
                    <i class="fas fa-clock"></i><span class="level-item has-text-grey">最后修改于${article.updateDate}</span>
                </div>
                <div class="level-right">
                </div>
            </div>
        </div>
    </div>
    </#if>
    <div class="card card-transparent" style="transition: opacity 0.3s ease-out, transform 0.3s ease-out; opacity: 1; transform-origin: center top 0px;">
        <div class="level post-navigation is-flex-wrap is-mobile">
            <#if lastArticle??>
            <div class="level-start">
                <a class="level level-item has-link-grey  article-nav-prev" href="${actions.ARTICLE + "/" + lastArticle.id}">
                    <i class="level-item fas fa-chevron-left"></i>
                    <span class="level-item">${lastArticle.title}</span>
                </a>
            </div>
            </#if>
            <#if nextArticle??>
            <div class="level-end">
                <a class="level level-item has-link-grey  article-nav-next" href="${actions.ARTICLE + "/" + nextArticle.id}">
                    <span class="level-item">${nextArticle.title}</span>
                    <i class="level-item fas fa-chevron-right"></i>
                </a>
            </div>
            </#if>
        </div>
    </div>
    <div class="card" id="comment"
         style="transition: opacity 0.3s ease-out 0s, transform 0.3s ease-out 0s; opacity: 1; transform-origin: center top 0px;">
        <div class="card-content">
            <#if user??>
            <textarea class="textarea" id="comment-textarea" rows="3" placeholder="说点什么..." style="background: url(/images/person5.png) no-repeat center;
                background-size: contain;background-position: 90%"></textarea>
            <#else>
            <div style="text-align: center"><a href="/login">登录</a>后评论 </div>
            </#if>
            <div class="level-right comment-btn-list" style="margin-left: auto;margin-top: 10px;width: 100%;display: none">
                <button class="btn-circle-gray comment-cancel">取消</button>
                <button class="btn-circle-red comment-publish" style="margin-left: 10px">发表</button>
            </div>
            <div class="comment-list-box" style="font-size: 14px">

            </div>
        </div>
    </div>
</div>