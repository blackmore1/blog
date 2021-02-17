<div class="column is-8-tablet is-8-desktop is-8-widescreen has-order-2 column-main">
    <#if indexType??>
        <div class="card" style="transition: opacity 0.3s ease-out 0s, transform 0.3s ease-out 0s; opacity: 1; transform-origin: center top;">
            <div class="card-content">
                <nav class="breadcrumb" aria-label="breadcrumbs">
                    <ul>
                        <li class="is-active"><a href="#" aria-current="page"><#if indexType=="tag">标签<#elseif indexType="search">搜索</#if></a></li>
                        <li class="is-active"><a href="#" aria-current="page">${title}</a></li>
                    </ul>
                </nav>
            </div>
        </div>
    </#if>
    <#list articles as article>
    <div class="card"
         style="transition: opacity 0.3s ease-out 0s, transform 0.3s ease-out 0s; opacity: 1; transform-origin: center top 0px;">
        <div class="card-content article ">
            <h1 class=" is-size-3 is-size-4-mobile has-text-weight-normal">
                <a class="has-link-black-ter" href="${actions.ARTICLE}/${article.id}">${article.title}</a>
            </h1>
            <div class="level article-meta is-size-7 is-mobile is-overflow-x-auto">
                <div class="level-left">
                    <i class="fas fa-bookmark"></i>[<a class="has-link-grey has-text-weight-normal" href="${actions.TAGS + "/" + article.keyword}">${article.keyword}</a>]
                </div>
                <div class="level-right">
                    <#if (article.commentCount>0)>
                        <i class="fas fa-pen"></i><a class="has-link-black-ter" href='${actions.ARTICLE}/${article.id}#comment'>${article.commentCount}条评论</a>
                    </#if>
                    &nbsp;<i class="fas fa-fire"></i>${article.clickCount!}
                </div>
            </div>
            <img src="${article.picture}" style="width:100%" class="img-responsive" id="blog-cover" />
            <div class="content">
                ${article.content}
            </div>
            <div class="level article-meta is-size-7 is-mobile is-overflow-x-auto">
                <div class="level-left">
                    <i class="fas fa-clock"></i><span class="level-item has-text-grey">${article.publishDate}</span>
                </div>
                <div class="level-right">
                    <a href="${actions.ARTICLE}/${article.id}">
                    <button class="btn-circle-red">继续阅读</button>
                    </a>
                </div>
            </div>
        </div>
    </div>
    </#list>
    <div class="card card-transparent"
         style="transition: opacity 0.3s ease-out 0s, transform 0.3s ease-out 0s; opacity: 1; transform-origin: center top 0px;">
        <nav class="pagination is-centered" role="navigation" aria-label="pagination">
            <#if (pageDto.lastUrl)??>
            <div class="pagination-previous">
                <a class="is-flex-grow has-text-black-ter" href="${pageDto.lastUrl}">上一页</a>
            </div>
            </#if>
            <#if (pageDto.nextUrl)??>
            <div class="pagination-next">
                <a class="is-flex-grow has-text-black-ter" href="${pageDto.nextUrl}">下一页</a>
            </div>
            </#if>
            <#if (pageDto.pageUrls)??>
            <ul class="pagination-list is-hidden-mobile" style="list-style: none;">
                <#list pageDto.pageUrls.keySet() as key>
                    <#if pageDto.number == key>
                        <li><a class="pagination-link is-current" href="${pageDto.pageUrls.get(key)}">${key}</a></li>
                    <#else>
                        <li><a class="pagination-link has-text-black-ter" href="${pageDto.pageUrls.get(key)}">${key}</a></li>
                    </#if>
                </#list>
            </ul>
            </#if>
        </nav>
    </div>
</div>