package com.demon4u.blog.config;

import java.util.HashSet;
import java.util.Set;

public class FileSubffixConfig {
    private static final Set<String> PICTURE_SET = new HashSet<>();
    static {
        PICTURE_SET.add("png");
        PICTURE_SET.add("jpg");
        PICTURE_SET.add("bmp");
        PICTURE_SET.add("jpeg");
        PICTURE_SET.add("gif");
    }
    public static boolean isPicture(String subffix) {
        for (String str : PICTURE_SET) {
            if (str.equalsIgnoreCase(subffix))
                return true;
        }
        return false;
    }

    private static final Set<String> FONT_SET = new HashSet<>();
    static {
        FONT_SET.add("ttf");
        FONT_SET.add("otf");
        FONT_SET.add("ttc");
    }

    public static boolean isFont(String subffix) {
        for (String str : FONT_SET) {
            if (str.equalsIgnoreCase(subffix))
                return true;
        }
        return false;
    }
}
