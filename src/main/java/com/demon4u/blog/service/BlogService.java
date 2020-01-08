package com.demon4u.blog.service;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

public interface BlogService {
    void frameData(ModelAndView mv);
    void archives(ModelAndView mv, Integer currPage);
    void archivesY(ModelAndView mv, String year, Integer currPage);
    void article(ModelAndView mv, Integer id, HttpServletRequest request);
}
