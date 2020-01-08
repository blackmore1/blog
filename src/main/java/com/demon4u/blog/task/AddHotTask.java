package com.demon4u.blog.task;

import com.demon4u.blog.dao.ArticleDao;
import com.demon4u.blog.entity.ArticleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class AddHotTask {
    @Autowired
    private ArticleDao articleDao;
    public static final Set<String> IP_ID_SET = new HashSet<>();
    public void addHot(String ip, ArticleEntity entity) {
        String ipId = ip + "_" + entity.getId();
        if (!IP_ID_SET.contains(ipId)) {
            IP_ID_SET.add(ipId);
            Integer count = entity.getClickCount() == null ? 1 : (entity.getClickCount() + 1);
            entity.setClickCount(count);
            articleDao.save(entity);
        }
    }
}
