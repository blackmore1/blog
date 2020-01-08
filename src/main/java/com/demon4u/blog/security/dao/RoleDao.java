package com.demon4u.blog.security.dao;

import com.demon4u.blog.security.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleDao extends JpaRepository<RoleEntity, Integer> {
    List<RoleEntity> findByUserId(Integer userId);
    RoleEntity findByUserIdAndName(Integer userId, String name);
}
