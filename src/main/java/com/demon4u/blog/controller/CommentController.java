package com.demon4u.blog.controller;

import com.demon4u.blog.dao.ArticleDao;
import com.demon4u.blog.dao.CommentDao;
import com.demon4u.blog.dto.ResponseDto;
import com.demon4u.blog.security.dao.UserDao;
import com.demon4u.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private UserDao userDao;
    @RequestMapping("/comment")
    @ResponseBody
    public String commentHtml(Integer articleId, Integer currPage, HttpServletRequest request) {
        return commentService.comment(articleId, currPage);
    }

    @RequestMapping("/publishComment")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto publishComment(String content, Integer articleId, HttpServletRequest request) {
        return commentService.publishComment(content, articleId, request);
    }
    @RequestMapping("/publishSubComment")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto publishSubComment(String content, Integer articleId, Integer parentId, Integer replyTo, HttpServletRequest request) {
        return commentService.publishSubComment(content, articleId, parentId, replyTo, request);
    }

    @RequestMapping("/delComment")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delComment(Integer commentId) {
        commentDao.deleteById(commentId);
        return "success";
    }
}
