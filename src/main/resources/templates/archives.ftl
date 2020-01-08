<div class="column is-8-tablet is-8-desktop is-6-widescreen has-order-2 column-main">
    <#list articleMap.keySet() as key>
        <div class="card widget" style="transition: opacity 0.3s ease-out, transform 0.3s ease-out; opacity: 1; transform-origin: center top 0px;">
            <div class="card-content">
                <h3 class="tag is-link">
                    ${key}
                </h3>
                <div class="timeline">
                    <#list articleMap.get(key) as article>
                        <article class="media">
                            <div class="media-content">
                                <div class="content">
                                    <time class="has-text-grey is-size-7 is-block is-uppercase">${article.publishDate?string('yyyy-MM-dd')}</time>
                                    <a href="${actions.ARTICLE}/${article.id}" class="title has-link-black-ter is-size-6 has-text-weight-normal">${article.title}</a>
                                    <div class="level article-meta is-mobile">
                                        <div class="level-left">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </article>
                    </#list>
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