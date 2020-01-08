<div class="column is-8-tablet is-8-desktop is-6-widescreen has-order-2 column-main">
    <div class="card widget" style="transition: opacity 0.3s ease-out, transform 0.3s ease-out; opacity: 1; transform-origin: center top 0px;">
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
                                <span class="tag is-grey">${tags.get(tag)}</span>
                            </a>
                        </div>
                    </#list>
                </div>
            </div>
        </div>
    </div>
</div>