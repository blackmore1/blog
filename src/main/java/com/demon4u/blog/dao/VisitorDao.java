package com.demon4u.blog.dao;

import com.demon4u.blog.entity.VisitorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorDao extends JpaRepository<VisitorEntity, String> {

}
