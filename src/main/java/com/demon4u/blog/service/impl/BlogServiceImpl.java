package com.demon4u.blog.service.impl;

import com.demon4u.blog.constant.UriConstant;
import com.demon4u.blog.dao.ArticleDao;
import com.demon4u.blog.dao.CommentDao;
import com.demon4u.blog.dao.TagDao;
import com.demon4u.blog.dto.ArticleDto;
import com.demon4u.blog.dto.CommentDto;
import com.demon4u.blog.dto.PageDto;
import com.demon4u.blog.entity.ArticleEntity;
import com.demon4u.blog.entity.CommentEntity;
import com.demon4u.blog.security.dao.UserDao;
import com.demon4u.blog.security.entity.UserEntity;
import com.demon4u.blog.service.BlogService;
import com.demon4u.blog.task.AddHotTask;
import com.demon4u.blog.util.ClientUtil;
import com.demon4u.blog.util.CollectionUtil;
import com.demon4u.blog.util.DateUtil;
import com.demon4u.blog.util.FreemarkerUtil;
import com.google.common.collect.Lists;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private TagDao tagDao;
    @Autowired
    private AddHotTask addHotTask;
    @Autowired
    private CommentDao commentDao;

    @Override
    public void frameData(ModelAndView mv) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("clickCount").descending());
        Page<ArticleEntity> page = articleDao.findAll(pageable);
        mv.addObject("popularArticles", page.getContent());
        mv.addObject("tags", CollectionUtil.ListRowToMap(articleDao.findGroupByWord()));
        mv.addObject("archives", CollectionUtil.ListRowToMap(articleDao.findGroupByPublishDate()));
        mv.addObject("totalArticles", articleDao.count());
        mv.addObject("actions", FreemarkerUtil.useStaticPacker(UriConstant.class.getName()));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //有登陆用户就返回登录用户
        if (authentication != null) {
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                mv.addObject("user", authentication.getPrincipal());
            }
        }
    }

    @Override
    public void archives(ModelAndView mv, Integer currPage) {
        Pageable pageable = PageRequest.of(currPage - 1, 10, Sort.by("id").descending());
        Page<ArticleEntity> page = articleDao.findAll(pageable);
        PageDto pageDto = new PageDto(page, UriConstant.ARCHIVES + "/page");
        mv.addObject("pageDto", pageDto);
        Map<String, List<ArticleEntity>> map = new LinkedHashMap<>();
        for (ArticleEntity entity : page.getContent()) {
            String year = DateUtil.formatYYYY(entity.getPublishDate());
            if (map.containsKey(year)) {
                map.get(year).add(entity);
            } else {
                map.put(year, Lists.newArrayList(entity));
            }
        }
        mv.addObject("articleMap", map);
    }

    @Override
    public void archivesY(ModelAndView mv, String year, Integer currPage) {
        Pageable pageable = PageRequest.of(currPage - 1, 10, Sort.by("blog_id").descending());
        Page<ArticleEntity> page = articleDao.findByYear(year, pageable);
        PageDto pageDto = new PageDto(page, UriConstant.ARCHIVES + "/" + year + "/page");
        mv.addObject("pageDto", pageDto);
        Map<String, List<ArticleEntity>> map = new LinkedHashMap<>();
        map.put(year, page.getContent());
        mv.addObject("articleMap", map);
    }

    @Override
    public void article(ModelAndView mv, Integer id, HttpServletRequest request) {
        ArticleEntity entity = articleDao.findById(id).orElse(null);
        mv.addObject("action", UriConstant.ARTICLE);
        mv.addObject("lastArticle", articleDao.findLastArticle(id));
        mv.addObject("nextArticle", articleDao.findNextArticle(id));
        addHotTask.addHot(ClientUtil.getIpAddr(request), entity);
        mv.addObject("article", new ArticleDto(entity, false, commentDao.countByArticleId(entity.getId())));
    }

}
