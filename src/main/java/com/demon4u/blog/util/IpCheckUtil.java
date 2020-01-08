package com.demon4u.blog.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IpCheckUtil {
    public static final Map<String, Integer> IP_COUNT_MIN = new ConcurrentHashMap<>();
    public static final Map<String, Integer> IP_COUNT_HOUR = new ConcurrentHashMap<>();
    public static boolean isLimit(String key, Integer limit1, Integer limit2) {
        Integer count1 = IP_COUNT_MIN.get(key);
        Integer count2 = IP_COUNT_HOUR.get(key);
        if (count1 == null)
            count1 = 0;
        if (count2 == null)
            count2 = 0;
        count1++;
        count2++;
        IP_COUNT_MIN.put(key, count1);
        IP_COUNT_HOUR.put(key, count2);
        return count1 > limit1 || count2 > limit2;
    }
}
