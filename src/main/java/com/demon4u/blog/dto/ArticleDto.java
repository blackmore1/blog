package com.demon4u.blog.dto;

import com.demon4u.blog.entity.ArticleEntity;
import com.demon4u.blog.util.HtmlUtil;
import org.springframework.web.util.JavaScriptUtils;

import java.util.Date;

public class ArticleDto {
    private Integer id;
    private String title;
    private String content;
    private String keyword;
    private String picture;
    private Integer clickCount;
    private Date publishDate;
    private Date updateDate;
    private Long commentCount;

    public ArticleDto(ArticleEntity entity, boolean subContent, Long commentCount) {
        if (entity == null) {
            return;
        }
        this.id = entity.getId();
        this.title = entity.getTitle();
        if (subContent) {
            String tmpContent = HtmlUtil.filterHtml(HtmlUtil.mdToHtml(entity.getContent()));
            this.content = tmpContent.length() > 100 ? tmpContent.substring(0, 100) + "……" : tmpContent;
        } else
            this.content = JavaScriptUtils.javaScriptEscape(entity.getContent());
        this.keyword = entity.getKeyword();
        this.picture = entity.getPicture();
        this.clickCount = entity.getClickCount();
        this.publishDate = entity.getPublishDate();
        this.updateDate = entity.getUpdateDate();
        this.commentCount = commentCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
