package com.demon4u.blog.task;

import com.demon4u.blog.service.SearchEngineService;
import com.demon4u.blog.util.DateUtil;
import com.demon4u.blog.util.IpCheckUtil;
import com.demon4u.blog.util.LogUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
@EnableScheduling
public class ScheduledTask {
    private static final Logger LOGGER = LogUtil.getCommonLog();
    @Autowired
    private SearchEngineService searchEngineService;
    /**
     * 一分钟一次
     * */
    @Scheduled(cron = "0 * * * * ?")
    public void minuteTask() {
        AddHotTask.IP_ID_SET.clear();

        IpCheckUtil.IP_COUNT_MIN.clear();
    }
    /**
     * 一小时一次
     * */
    @Scheduled(cron = "0 0 * * * ?")
    public void houtTask() {
        IpCheckUtil.IP_COUNT_HOUR.clear();

        // 更新bitmap
        searchEngineService.buildSitemap();
    }

    @Autowired
    private CalcuVisitorTask calcuVisitorTask;
    /**
     * 每天一点执行
     * */
    @Scheduled(cron = "0 0 1 * * ?")
    public void oneDayTask() {
        long dateline = System.currentTimeMillis() - 1000 * 60 * 60 * 24;
        Date date = new Date(dateline);
        String dateStr = DateUtil.formatYYYY_MM_dd(date);
        LOGGER.info("{}:{}", "calcuVisitor", dateStr);
        calcuVisitorTask.calcuVisitor(true, dateStr);
    }

}
