package com.demon4u.blog.util;

import java.text.NumberFormat;

public class FormatUtil {
    public static String twoDecimal(double d) {
        NumberFormat df= NumberFormat.getNumberInstance() ;
        df.setMaximumFractionDigits(2);
        df.setGroupingUsed(false);
        return df.format(d);
    }

    public static void main(String[] args) {
        System.out.println(twoDecimal(2.333));
        System.out.println(twoDecimal(2.000));
        System.out.println(twoDecimal(1));
        System.out.println(twoDecimal(74837430974391.32748));
    }
}
