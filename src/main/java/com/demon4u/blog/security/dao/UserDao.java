package com.demon4u.blog.security.dao;

import com.demon4u.blog.security.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<UserEntity, Integer> {
    UserEntity findByUsername(String username);
    UserEntity findByAppTypeAndAppId(String appType, String appId);
}
