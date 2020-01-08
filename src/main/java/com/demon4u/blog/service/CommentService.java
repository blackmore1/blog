package com.demon4u.blog.service;

import com.demon4u.blog.dto.ResponseDto;

import javax.servlet.http.HttpServletRequest;

public interface CommentService {
    String comment(Integer articleId, Integer currPage);
    ResponseDto publishComment(String content, Integer articleId, HttpServletRequest request);
    ResponseDto publishSubComment(String content, Integer articleId, Integer parentId, Integer replyTo, HttpServletRequest request);
}
