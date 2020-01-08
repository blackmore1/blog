package com.demon4u.blog.dao;

import com.demon4u.blog.entity.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleDao extends JpaRepository<ArticleEntity, Integer> {
    @Query("select count(*) as count, keyword from ArticleEntity group by keyword")
    List findGroupByWord();

    //框架archives
    @Query("select count(*) as count, DATE_FORMAT(publishDate,'%Y') as date from ArticleEntity group by DATE_FORMAT(publishDate,'%Y') order by date desc")
    List findGroupByPublishDate();

    //详情页下一篇
    @Query(value = "select * from blog_article where blog_id < ?1 order by blog_id desc limit 1", nativeQuery = true)
    ArticleEntity findNextArticle(Integer id);

    //详情页上一篇
    @Query(value = "select * from blog_article where blog_id > ?1 limit 1", nativeQuery = true)
    ArticleEntity findLastArticle(Integer id);

    //archives按年
    @Query(value = "select * from blog_article where DATE_FORMAT(publish_date,'%Y')=?1", nativeQuery = true)
    Page<ArticleEntity> findByYear(String year, Pageable pageable);

    Page<ArticleEntity> findByKeyword(String keyword, Pageable pageable);

    Page<ArticleEntity> findByTitleLike(String title, Pageable pageable);
}
