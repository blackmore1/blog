package com.demon4u.blog.util;

import javax.servlet.http.HttpServletRequest;

public class ClientUtil {
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    public static String getDeviceType(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        if (userAgent == null) {
            return "unknown";
        }
        userAgent = userAgent.toLowerCase();
        if (userAgent.contains("windows nt")) { // 判断当前客户端是否为PC
            return "pc";
        } else if (userAgent.contains("android")) { // 判断当前客户端是否为android
            return "android";
        } else if (userAgent.contains("iphone")) { // 判断当前客户端是否为iPhone
            return "iPhone";
        } else if (userAgent.contains("wap")) { // 判断当前客户端是否为wap
            return "wap";
        } else if (userAgent.contains("micromessenger")) { // 判断当前客户端是否为微信
            return "weixin";
        }
        return "unknown";
    }
}
