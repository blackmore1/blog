package com.demon4u.blog.dao;

import com.demon4u.blog.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentDao extends JpaRepository<CommentEntity, Integer> {
    Page<CommentEntity> findByArticleIdAndParentId(Integer articleId, Integer parentId, Pageable pageable);
    List<CommentEntity> findByParentId(Integer parentId);
    List<CommentEntity> findByParentIdAndUid(Integer parentId, Integer uid);
    Long countByArticleId(Integer articleId);
}
