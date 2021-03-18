package com.demon4u.blog.controller;

import com.demon4u.blog.constant.RoleType;
import com.demon4u.blog.dao.ArticleDao;
import com.demon4u.blog.dao.CommentDao;
import com.demon4u.blog.dao.TagDao;
import com.demon4u.blog.dao.VisitorDao;
import com.demon4u.blog.dto.PageDto;
import com.demon4u.blog.entity.ArticleEntity;
import com.demon4u.blog.entity.TagEntity;
import com.demon4u.blog.entity.VisitorEntity;
import com.demon4u.blog.security.dao.RoleDao;
import com.demon4u.blog.security.dao.UserDao;
import com.demon4u.blog.security.entity.RoleEntity;
import com.demon4u.blog.security.entity.UserEntity;
import com.demon4u.blog.task.CalcuVisitorTask;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller()
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Transactional
public class AdminController {
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private TagDao tagDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private VisitorDao visitorDao;
    @Autowired
    private CommentDao commentDao;

    @RequestMapping("")
    public String index() {
        return "admin/index";
    }
    @RequestMapping("/welcome")
    public String welcome(Model model, Principal principal) {
        model.addAttribute("adminName", principal.getName());
        model.addAttribute("articleNum", articleDao.count());
        model.addAttribute("userNum", userDao.count());
        model.addAttribute("commentNum", commentDao.count());
        Pageable pageable = PageRequest.of(0, 7, Sort.by("date").descending());
        List<VisitorEntity> visitors = visitorDao.findAll(pageable).getContent();
        List<String> dateList = new ArrayList<>();
        List<Integer> pvList = new ArrayList<>();
        List<Integer> uvList = new ArrayList<>();
        for (int i = visitors.size() - 1; i >= 0; i--) {
            VisitorEntity visitor = visitors.get(i);
            dateList.add("\"" + visitor.getDate().substring(5) + "\"");
            pvList.add(visitor.getPv());
            uvList.add(visitor.getUv());
        }
        model.addAttribute("dateList", dateList);
        model.addAttribute("pvList", pvList);
        model.addAttribute("uvList", uvList);
        return "admin/welcome";
    }
    @RequestMapping("/blogList/page/{currPage}")
    public String blogList(@PathVariable(value = "currPage", required = false) Integer currPage, Model model) {
        Pageable pageable = PageRequest.of(currPage - 1, 10, Sort.by("id").descending());
        Page<ArticleEntity> page = articleDao.findAll(pageable);
        PageDto pageDto = new PageDto(page, "/admin/blogList/page");
        model.addAttribute("pageDto", pageDto);
        model.addAttribute("articles", page.getContent());
        return "admin/blogList";
    }
    @RequestMapping("/blog/del/{id}")
    @ResponseBody
    public String delBlog(@PathVariable(value = "id", required = false) Integer id, Model model) {
        articleDao.deleteById(id);
        return "success";
    }
    @RequestMapping("/search/{q}/page/{currPage}")
    public String search(@PathVariable(value = "q") String q,
                         @PathVariable(value = "currPage", required = false) Integer currPage, Model model) {
        Pageable pageable = PageRequest.of(currPage - 1, 10, Sort.by("id").descending());
        Page<ArticleEntity> page = articleDao.findByTitleLike("%" + q + "%", pageable);
        PageDto pageDto = new PageDto(page, "/admin/search" + q + "/page");
        model.addAttribute("pageDto", pageDto);
        model.addAttribute("articles", page.getContent());
        model.addAttribute("q", q);
        return "admin/blogList";
    }
    @RequestMapping("/toInsertBlog")
    public String toInsert(Model model) {
        model.addAttribute("tags", tagDao.findAll());
        return "admin/insertArticle";
    }
    @RequestMapping("/blog/insert")
    @ResponseBody
    public String insert(@RequestBody ArticleEntity entity) {
        entity.setPublishDate(new Date());
        entity.setUpdateDate(new Date());
        entity.setClickCount(0);
        articleDao.save(entity);
        return "success";
    }
    @RequestMapping("/toEditBlog/{id}")
    public String toEditBlog(Model model, @PathVariable(value = "id", required = false) Integer id) {
        model.addAttribute("article", articleDao.findById(id).get());
        model.addAttribute("tags", tagDao.findAll());
        return "admin/editArticle";
    }
    @RequestMapping("/blog/edit")
    @ResponseBody
    public String edit(@RequestBody ArticleEntity entity) {
        ArticleEntity newEntity = articleDao.findById(entity.getId()).get();
        newEntity.setContent(entity.getContent());
        newEntity.setKeyword(entity.getKeyword());
        newEntity.setPicture(entity.getPicture());
        newEntity.setTitle(entity.getTitle());
        newEntity.setUpdateDate(new Date());
        articleDao.save(newEntity);
        return "success";
    }
    @RequestMapping("/tagList")
    public String tagList(Model model) {
        model.addAttribute("tags", tagDao.findAll());
        return "admin/tagList";
    }
    @RequestMapping("/tag/del/{id}")
    @ResponseBody
    public String delTag(@PathVariable(value = "id", required = false) Integer id, Model model) {
        tagDao.deleteById(id);
        return "success";
    }
    @RequestMapping("/toAddTag")
    public String toAddTag() {
        return "admin/addTag";
    }
    @RequestMapping("/tag/insert")
    @ResponseBody
    public String addTag(@RequestBody TagEntity entity) {
        tagDao.save(entity);
        return "success";
    }

    @Autowired
    private CalcuVisitorTask calcuVisitorTask;
    @RequestMapping("/visitor")
    @ResponseBody
    public String visitor(String date) {
        calcuVisitorTask.calcuVisitor(true, date);
        return "success";
    }

    @Autowired
    private RoleDao roleDao;
    @RequestMapping("/userList/page/{currPage}")
    public String userList(@PathVariable(value = "currPage", required = false) Integer currPage, Model model) {
        Pageable pageable = PageRequest.of(currPage - 1, 10);
        Page<UserEntity> page = userDao.findAll(pageable);
        PageDto pageDto = new PageDto(page, "/admin/userList/page");
        List<UserEntity> users = page.getContent();
        users.forEach(user -> {
            List<RoleEntity> roles = roleDao.findByUserId(user.getId());
            user.setAuthorities(roles);
        });
        model.addAttribute("pageDto", pageDto);
        model.addAttribute("users", users);
        return "admin/userList";
    }
    @RequestMapping("/searchUser/{q}")
    public String searchUser(@PathVariable(value = "q") String q, Model model) {
        UserEntity user = userDao.findByUsername(q);
        List<UserEntity> users = new ArrayList<>();
        if (user != null) {
            user.setAuthorities(roleDao.findByUserId(user.getId()));
            users.add(user);
        }
        model.addAttribute("users", users);
        model.addAttribute("q", q);
        return "admin/userList";
    }

    @RequestMapping("/toEditUser/{id}")
    public String toEditUser(Model model, @PathVariable(value = "id", required = false) Integer id) {
        UserEntity user = userDao.findById(id).get();
        user.setAuthorities(roleDao.findByUserId(user.getId()));
        model.addAttribute("user", user);
        model.addAttribute("isAdmin", user.isAdmin());
        return "admin/editUser";
    }
    @RequestMapping("/user/edit")
    @ResponseBody
    public String editUser(Integer id, String role) {
        UserEntity user = userDao.findById(id).get();
        if (!RoleType.ROLE_USER.equals(role) && !RoleType.ROLE_ADMIN.equals(role)) {
            return "fail";
        }
        RoleEntity roleEntity = roleDao.findByUserIdAndName(user.getId(), RoleType.ROLE_ADMIN);
        if (RoleType.ROLE_USER.equals(role) && roleEntity != null) {
            roleDao.delete(roleEntity);
        } else if (RoleType.ROLE_ADMIN.equals(role) && roleEntity == null) {
            roleEntity = new RoleEntity(user.getId(), RoleType.ROLE_ADMIN);
            roleDao.save(roleEntity);
        }
        return "success";
    }
    @RequestMapping("/toAddUser")
    public String toAddUser() {
        return "admin/addUser";
    }
    @RequestMapping("/user/add")
    @ResponseBody
    public String addUser(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return "不能为空";
        }
        UserEntity user = userDao.findByUsername(username);
        if (user != null) {
            return "用户名已存在";
        }
        user = new UserEntity();
        user.setUsername(username);
        user.setNickname(username);
        user.setPassword(password);
        user.setCreateTime(new Date());
        userDao.save(user);
        return "success";
    }
}
