package com.demon4u.blog.service.impl;

import com.demon4u.blog.config.AppConfig;
import com.demon4u.blog.constant.UriConstant;
import com.demon4u.blog.dao.ArticleDao;
import com.demon4u.blog.entity.ArticleEntity;
import com.demon4u.blog.service.SearchEngineService;
import com.demon4u.blog.util.DateUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 搜索引擎相关
 * */
@Service
public class SearchEngineServiceImpl implements SearchEngineService, InitializingBean {
    public static String sitemap = null;

    @Autowired
    private ArticleDao articleDao;

    @Override
    public String buildSitemap() {
        Pageable pageable = PageRequest.of(0, 50, Sort.by("id").descending());
        Page<ArticleEntity> page = articleDao.findAll(pageable);
        List<ArticleEntity> articles = page.getContent();
        Document document = DocumentHelper.createDocument();
        Element urlsetElement = document.addElement("urlset");
        for (ArticleEntity entity : articles) {
            Element urlElement = urlsetElement.addElement("url");
            urlElement.addElement("loc").setText(AppConfig.domain + UriConstant.ARTICLE + "/" + entity.getId());
            urlElement.addElement("priority").setText("0.5");
            urlElement.addElement("lastmod").setText(DateUtil.formatYYYY_MM_dd(entity.getUpdateDate()));
            urlElement.addElement("changefreq").setText("weekly");
        }
        sitemap = document.asXML();
        return sitemap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        buildSitemap();
    }
}
