package com.demon4u.blog.service.impl;

import com.demon4u.blog.dao.ArticleDao;
import com.demon4u.blog.dao.CommentDao;
import com.demon4u.blog.dto.CommentDto;
import com.demon4u.blog.dto.PageDto;
import com.demon4u.blog.dto.ResponseDto;
import com.demon4u.blog.entity.ArticleEntity;
import com.demon4u.blog.entity.CommentEntity;
import com.demon4u.blog.security.dao.UserDao;
import com.demon4u.blog.security.entity.UserEntity;
import com.demon4u.blog.service.CommentService;
import com.demon4u.blog.util.ClientUtil;
import com.demon4u.blog.util.IpCheckUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private Configuration configuration;
    @Autowired
    private ArticleDao articleDao;
    @Override
    public String comment(Integer articleId, Integer currPage) {
        Pageable pageable = PageRequest.of(currPage - 1, 5, Sort.by("id").descending());
        Page<CommentEntity> page = commentDao.findByArticleIdAndParentId(articleId, null, pageable);
        List<CommentEntity> comments = page.getContent();
        List<CommentDto> commentDtos = new ArrayList<>();
        for (CommentEntity comment : comments) {
            CommentDto commentDto = new CommentDto(comment);
            UserEntity user = userDao.findById(commentDto.getUid()).get();
            commentDto.setNickname(user.getNickname());
            commentDto.setAvatar(user.getAvatar());
            List<CommentEntity> subComments = commentDao.findByParentId(comment.getId());
            List<CommentDto> subCommentDtos = new ArrayList<>();
            subComments.forEach(subComment -> {
                CommentDto subCommentDto = new CommentDto(subComment);
                UserEntity subUser = userDao.findById(subComment.getUid()).get();
                subCommentDto.setNickname(subUser.getNickname());
                subCommentDto.setAvatar(subUser.getAvatar());
                UserEntity replyToUser = userDao.findById(subComment.getReplyTo()).get();
                subCommentDto.setReplyName(replyToUser.getNickname());
                subCommentDtos.add(subCommentDto);
            });
            commentDto.setSubComments(subCommentDtos);
            commentDtos.add(commentDto);
        }
        try {
            Template template = configuration.getTemplate("comment/comment.ftl");
            HashMap<String, Object> map = new HashMap<>();
            map.put("commentPage", new PageDto(page, ""));
            map.put("comments", commentDtos);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            //有登陆用户就返回登录用户
            if (authentication != null) {
                if (!(authentication instanceof AnonymousAuthenticationToken)) {
                    UserEntity user = (UserEntity) authentication.getPrincipal();
                    map.put("user", user);
                    map.put("isAdmin", user.isAdmin());
                }
            }
            Writer out = new StringWriter(2048);
            template.process(map, out);
            String html = out.toString();
            return html;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseDto publishComment(String content, Integer articleId, HttpServletRequest request) {
        ResponseDto responseDto = new ResponseDto();
        if (StringUtils.isEmpty(content)) {
            responseDto.setState(false);
            responseDto.setMsg( "请输入评论");
            return responseDto;
        }
        if (content.length() > 255) {
            responseDto.setState(false);
            responseDto.setMsg("评论不能超过255字符");
            return responseDto;
        }
        if (IpCheckUtil.isLimit("comment" + ClientUtil.getIpAddr(request), 2, 20)) {
            responseDto.setState(false);
            responseDto.setMsg("请稍后重试");
            return responseDto;
        }
        ArticleEntity article = articleDao.findById(articleId).orElse(null);
        if (article == null) {
            responseDto.setState(false);
            responseDto.setMsg("1");
            return responseDto;
        }
        CommentEntity comment = new CommentEntity();
        comment.setArticleId(articleId);
        comment.setContent(content);
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        comment.setUid(user.getId());
        comment.setReplayDate(new Date());
        commentDao.save(comment);
        responseDto.setState(true);
        responseDto.setMsg("success");
        return responseDto;
    }

    @Override
    public ResponseDto publishSubComment(String content, Integer articleId, Integer parentId, Integer replyTo, HttpServletRequest request) {
        ResponseDto responseDto = new ResponseDto();
        if (StringUtils.isEmpty(content)) {
            responseDto.setState(false);
            responseDto.setMsg("请输入评论");
            return responseDto;
        }
        if (content.length() > 255) {
            responseDto.setState(false);
            responseDto.setMsg("评论不能超过255字符");
            return responseDto;
        }
        if (articleId == null || parentId == null || replyTo == null) {
            responseDto.setState(false);
            responseDto.setMsg("参数错误");
            return responseDto;
        }
        ArticleEntity article = articleDao.findById(articleId).orElse(null);
        if (article == null) {
            responseDto.setState(false);
            responseDto.setMsg("文章不存在");
            return responseDto;
        }
        CommentEntity parentComment = commentDao.findById(parentId).orElse(null);
        if (parentComment == null || parentComment.getParentId() != null) {
            responseDto.setState(false);
            responseDto.setMsg("上级回复不存在");
            return responseDto;
        }
        if (!parentComment.getUid().equals(replyTo) && CollectionUtils.isEmpty(commentDao.findByParentIdAndUid(parentId, replyTo))) {
            responseDto.setState(false);
            responseDto.setMsg("回复对象不存在1");
            return responseDto;
        }
        UserEntity replyToUser = userDao.findById(replyTo).orElse(null);
        if (replyToUser == null) {
            responseDto.setState(false);
            responseDto.setMsg("回复对象不存在2");
            return responseDto;
        }
        if (IpCheckUtil.isLimit("comment" + ClientUtil.getIpAddr(request), 2, 20)) {
            responseDto.setState(false);
            responseDto.setMsg("请稍后重试");
            return responseDto;
        }
        CommentEntity comment = new CommentEntity();
        comment.setArticleId(articleId);
        comment.setContent(content);
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        comment.setUid(user.getId());
        comment.setReplayDate(new Date());
        comment.setParentId(parentId);
        comment.setReplyTo(replyTo);
        commentDao.save(comment);
        responseDto.setState(true);
        responseDto.setMsg("success");
        return responseDto;
    }
}
