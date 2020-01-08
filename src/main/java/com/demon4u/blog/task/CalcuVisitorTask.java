package com.demon4u.blog.task;

import com.demon4u.blog.dao.VisitorDao;
import com.demon4u.blog.entity.VisitorEntity;
import com.demon4u.blog.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashSet;
import java.util.Set;


@Component
public class CalcuVisitorTask {
    @Autowired
    private VisitorDao visitorDao;
    public void  calcuVisitor(boolean force, String date) {
        File file = new File("./logs/time/time." + date);
        if (!file.exists()) {
            file = new File("./logs/time/time");
            if (!file.exists())
                return;
        }
        VisitorEntity entity = visitorDao.findById(date).orElse(null);
        if (!force && entity != null) {
            return;
        }
        Set<String> ipSet = new HashSet<>();
        int allCount = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (StringUtils.isNotBlank(line) && !line.startsWith(date))
                    return;
                String[] arr = line.split("#");
                if (arr.length < 5) {
                    continue;
                }
                allCount++;
                ipSet.add(arr[2]);
            }
        } catch (Exception e) {
            LogUtil.getErrorLog().error("calcuVisitor", e);
        }
        if (entity == null) {
            entity = new VisitorEntity();
        }
        entity.setDate(date);
        entity.setPv(allCount);
        entity.setUv(ipSet.size());
        visitorDao.save(entity);
    }
}
