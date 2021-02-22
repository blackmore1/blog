package com.demon4u.blog.controller;

import com.demon4u.blog.dao.ArticleDao;
import com.demon4u.blog.dto.ResponseDto;
import com.demon4u.blog.entity.ArticleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    private ArticleDao articleDao;
    @RequestMapping("test")
    public List<ArticleEntity> test() {
        Pageable pageable = PageRequest.of(3, 2, Sort.by("id").descending());
        Page<ArticleEntity> page = articleDao.findAll(pageable);
        System.out.println(page.getTotalPages());
        System.out.println(page.getNumber());
        System.out.println(page.getSize());
        System.out.println(page.getTotalElements());
        return page.getContent();
    }

    @RequestMapping("/admin1")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String printAdmin() {
        return "如果你看见这句话，说明你有ROLE_ADMIN角色";
    }

    @RequestMapping("/user1")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_USER')")
    public String printUser() {
        return "如果你看见这句话，说明你有ROLE_USER角色";
    }

    @RequestMapping("/rfdTest")
    public ResponseDto test1(String str, @RequestParam("i") int f) {
        ResponseDto dto = new ResponseDto();
        dto.setMsg(str);
        return dto;
    }
}
