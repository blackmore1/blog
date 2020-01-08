package com.demon4u.blog.controller;

import com.demon4u.blog.constant.BlogConstant;
import com.demon4u.blog.constant.UriConstant;
import com.demon4u.blog.dao.ArticleDao;
import com.demon4u.blog.dao.CommentDao;
import com.demon4u.blog.dao.TagDao;
import com.demon4u.blog.dto.ArticleDto;
import com.demon4u.blog.dto.PageDto;
import com.demon4u.blog.entity.ArticleEntity;
import com.demon4u.blog.service.BlogService;
import com.demon4u.blog.task.AddHotTask;
import com.demon4u.blog.util.ClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private BlogService blogService;

    @Autowired
    private TagDao tagDao;
    @Autowired
    private CommentDao commentDao;

    @RequestMapping({UriConstant.INDEX + "/{currPage}", UriConstant.INDEX, ""})
    public ModelAndView index(@PathVariable(value = "currPage", required = false) Integer currPage) {
        if (currPage == null)
            currPage = 1;
        ModelAndView mv = new ModelAndView("frame");
        blogService.frameData(mv);
        Pageable pageable = PageRequest.of(currPage - 1, BlogConstant.PAGE_SIZE, Sort.by("id").descending());
        Page<ArticleEntity> page = articleDao.findAll(pageable);
        PageDto pageDto = new PageDto(page, UriConstant.INDEX);
        mv.addObject("pageDto", pageDto);
        List<ArticleDto> articleDtos = new ArrayList<>();
        page.getContent().forEach(entity -> articleDtos.add(new ArticleDto(entity, true, commentDao.countByArticleId(entity.getId()))));
        mv.addObject("articles", articleDtos);
        mv.addObject("action", UriConstant.INDEX);
        return mv;
    }

    @RequestMapping(UriConstant.ARTICLE + "/{id}")
    public ModelAndView article(@PathVariable(value = "id", required = false) Integer id, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("frame");
        blogService.frameData(mv);
        blogService.article(mv, id, request);
        return mv;
    }

    @RequestMapping({UriConstant.ARCHIVES + "/page/{currPage}", UriConstant.ARCHIVES})
    public ModelAndView archives(@PathVariable(value = "currPage", required = false) Integer currPage) {
        if (currPage == null)
            currPage = 1;
        ModelAndView mv = new ModelAndView("frame");
        blogService.frameData(mv);
        blogService.archives(mv, currPage);
        mv.addObject("action", UriConstant.ARCHIVES);
        return mv;
    }

    @RequestMapping({UriConstant.ARCHIVES + "/{year}", UriConstant.ARCHIVES + "/{year}/page/{currPage}"})
    public ModelAndView archivesY(@PathVariable(value = "year", required = false) String year,
                                  @PathVariable(value = "currPage", required = false) Integer currPage) {
        if (currPage == null)
            currPage = 1;
        ModelAndView mv = new ModelAndView("frame");
        blogService.frameData(mv);
        blogService.archivesY(mv, year, currPage);
        mv.addObject("action", UriConstant.ARCHIVES);
        return mv;
    }
    @RequestMapping(UriConstant.TAGS)
    public ModelAndView tags() {
        ModelAndView mv = new ModelAndView("frame");
        blogService.frameData(mv);
        mv.addObject("action", UriConstant.TAGS);
        return mv;
    }

    @RequestMapping({UriConstant.TAGS + "/{tag}", UriConstant.TAGS + "/{tag}/page/{currPage}"})
    public ModelAndView tags(@PathVariable(value = "tag", required = false) String tag,
                             @PathVariable(value = "currPage", required = false) Integer currPage) {
        if (currPage == null)
            currPage = 1;
        ModelAndView mv = new ModelAndView("frame");
        blogService.frameData(mv);

        Pageable pageable = PageRequest.of(currPage - 1, BlogConstant.PAGE_SIZE, Sort.by("id").descending());
        Page<ArticleEntity> page = articleDao.findByKeyword(tag, pageable);
        PageDto pageDto = new PageDto(page, UriConstant.TAGS + "/" + tag + "/page");
        mv.addObject("pageDto", pageDto);
        List<ArticleDto> articleDtos = new ArrayList<>();
        page.getContent().forEach(entity -> articleDtos.add(new ArticleDto(entity, true, commentDao.countByArticleId(entity.getId()))));
        mv.addObject("articles", articleDtos);

        mv.addObject("title", tag);
        mv.addObject("action", UriConstant.INDEX);
        mv.addObject("indexType", "tag");
        return mv;
    }

    @RequestMapping({UriConstant.SEARCH + "/{q}", UriConstant.SEARCH + "/{q}/page/{currPage}"})
    public ModelAndView search(@PathVariable(value = "q") String q,
                             @PathVariable(value = "currPage", required = false) Integer currPage) {
        if (currPage == null)
            currPage = 1;
        ModelAndView mv = new ModelAndView("frame");
        blogService.frameData(mv);

        Pageable pageable = PageRequest.of(currPage - 1, BlogConstant.PAGE_SIZE, Sort.by("id").descending());
        Page<ArticleEntity> page = articleDao.findByTitleLike("%" + q + "%", pageable);
        PageDto pageDto = new PageDto(page, UriConstant.SEARCH + "/" + q + "/page");
        mv.addObject("pageDto", pageDto);
        List<ArticleDto> articleDtos = new ArrayList<>();
        page.getContent().forEach(entity -> articleDtos.add(new ArticleDto(entity, true, commentDao.countByArticleId(entity.getId()))));
        mv.addObject("articles", articleDtos);

        mv.addObject("title", q);
        mv.addObject("q", q);
        mv.addObject("action", UriConstant.INDEX);
        mv.addObject("indexType", "search");
        return mv;
    }

}
