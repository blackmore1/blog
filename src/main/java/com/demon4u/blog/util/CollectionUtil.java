package com.demon4u.blog.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CollectionUtil {
    public static Map<String, Long> ListRowToMap(List list) {
        Map<String ,Long> map = new LinkedHashMap<>();
        for (Object row : list) {
            Object[] cells = (Object[]) row;
            map.put((String)cells[1], (long) cells[0]);
        }
        return map;
    }
}
