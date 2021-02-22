package com.demon4u.blog.controller;

import com.demon4u.blog.service.SearchEngineService;
import com.demon4u.blog.service.impl.SearchEngineServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class SearchEngineController {
    @Autowired
    private SearchEngineService searchEngineService;

    @RequestMapping(value = "sitemap.xml",produces = { "application/xml;charset=UTF-8" })
    public String sitemap(HttpServletResponse response) {
        return SearchEngineServiceImpl.sitemap;
    }
}
