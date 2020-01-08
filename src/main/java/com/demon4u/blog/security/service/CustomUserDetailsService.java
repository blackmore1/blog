package com.demon4u.blog.security.service;

import com.demon4u.blog.constant.RoleType;
import com.demon4u.blog.security.dao.RoleDao;
import com.demon4u.blog.security.dao.UserDao;
import com.demon4u.blog.security.entity.RoleEntity;
import com.demon4u.blog.security.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中取出用户信息
        UserEntity user = userDao.findByUsername(username);

        // 判断用户是否存在
        if(user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        // 添加权限
        List<RoleEntity> roles = roleDao.findByUserId(user.getId());
//        if (CollectionUtils.isEmpty(roles)) {
//            roles.add(new RoleEntity(user.getId(), RoleType.ROLE_USER));
//        }
        user.setAuthorities(roles);

        // 返回UserDetails实现类
        return user;
    }
}

