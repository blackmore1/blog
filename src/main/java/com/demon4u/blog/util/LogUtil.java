package com.demon4u.blog.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
    public static Logger getTimeLog() {
        return LoggerFactory.getLogger("time");
    }
    public static Logger getCommonLog() {
        return LoggerFactory.getLogger("common");
    }
    public static Logger getErrorLog() {
        return LoggerFactory.getLogger("error");
    }
    public static Logger getHttpLog() {
        return LoggerFactory.getLogger("http");
    }
    public static Logger getMonitorLog() {
        return LoggerFactory.getLogger("monitor");
    }
}
