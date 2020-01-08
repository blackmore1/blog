package com.demon4u.blog.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String formatYYYY(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(date);
    }
    public static String formatYYYY_MM_dd(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
