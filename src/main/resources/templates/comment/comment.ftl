<#list comments as comment>
    <ul class="comment-list">
        <li class="comment-line-box">
            <img class="comment-avatar" src="${comment.avatar}">
            <div class="comment-right-box">
                <span class="comment-name-box"><span class="comment-name">${comment.nickname}</span></span>
                <span style="font-size: 12px;color: #999;vertical-align: top;display: inline-block;">${comment.date}</span>
                <#if user??>
                <span class="comment-opt">
                    <#if isAdmin><a class="comment-del" cId="${comment.id}">删除</a></#if>
                    <a class="comment-reply" uid="${comment.uid}" pId="${comment.id}">回复</a>
                    </#if>
                </span>
                <#if (comment.subComments?size>0)><a class="comment-hide" cId="${comment.id}" count="${comment.subComments?size}">查看回复(${comment.subComments?size})</a></#if>
                <span style="display: block;margin-top: 5px">${comment.content}</span>
            </div>
        </li>
        <div class="${"sub-comment-div"+comment.id}" style="display: none">
        <#list comment.subComments as subComment>
            <li class="comment-line-box" style="margin-left: 32px">
                <div style="float:left;width: 5px;height: auto; background: #c5c5c5;margin-right: 5px"></div>
                <img class="comment-avatar" src="${subComment.avatar}">
                <div class="comment-right-box">
                    <span class="comment-name-box"> <span class="comment-name">${subComment.nickname}</span><span style="color: #999">回复</span><span class="comment-name">${subComment.replyName}</span></span>
                    <span style="font-size: 12px;color: #999;vertical-align: top;display: inline-block;">${subComment.date}</span>
                    <#if user??>
                    <span class="comment-opt">
                        <#if isAdmin><a class="comment-del" cId="${subComment.id}">删除</a></#if>
                        <a class="comment-reply" uid="${subComment.uid}"  pId="${comment.id}">回复</a></span>
                    </#if>
                    <span style="display: block;margin-top: 5px">${subComment.content}</span>
                </div>
            </li>
        </#list>
        </div>
    </ul>
</#list>
<#if (commentPage.pageUrls)??>
    <nav class="pagination is-centered" role="navigation" aria-label="pagination">
        <ul class="pagination-list is-hidden-mobile" style="list-style: none;">
            <#list commentPage.pageUrls.keySet() as key>
                <#if commentPage.number == key>
                    <li><a class="pagination-link is-current">${key}</a></li>
                <#else>
                    <li><a class="pagination-link has-text-black-ter a-page" onclick="getComment(${key})">${key}</a></li>
                </#if>
            </#list>
        </ul>
    </nav>
</#if>