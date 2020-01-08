package com.demon4u.blog.dto;

import com.demon4u.blog.constant.BlogConstant;
import com.demon4u.blog.entity.CommentEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentDto {
    private Integer id;
    private String content;
    private Integer uid;
    private String nickname;
    private String avatar;
    private Integer parentId;
    private Integer replyTo;
    private String replyName;
    private Date date;
    private List<CommentDto> subComments = new ArrayList<>();
    public CommentDto(CommentEntity entity) {
        this.id = entity.getId();
        this.content = entity.getContent();
        this.uid = entity.getUid();
        this.parentId = entity.getParentId();
        this.replyTo = entity.getReplyTo();
        this.date = entity.getReplayDate();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = StringUtils.isEmpty(avatar) ? BlogConstant.DEFAULT_AVATAR : avatar;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Integer replyTo) {
        this.replyTo = replyTo;
    }

    public String getReplyName() {
        return replyName;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<CommentDto> getSubComments() {
        return subComments;
    }

    public void setSubComments(List<CommentDto> subComments) {
        this.subComments = subComments;
    }
}
