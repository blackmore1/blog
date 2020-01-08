package com.demon4u.blog.dao;

import com.demon4u.blog.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagDao extends JpaRepository<TagEntity, Integer> {

}
