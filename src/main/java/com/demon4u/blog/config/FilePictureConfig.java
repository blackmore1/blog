package com.demon4u.blog.config;

import java.util.HashMap;
import java.util.Map;

public class FilePictureConfig {
    private static final Map<String, String> PICTURE_MAP = new HashMap<>();
    static {
        PICTURE_MAP.put("zip", "/images/zip.png");
        PICTURE_MAP.put("tar", "/images/zip.png");
        PICTURE_MAP.put("7z", "/images/zip.png");
        PICTURE_MAP.put("rar", "/images/zip.png");
        PICTURE_MAP.put("txt", "/images/txt.png");
        PICTURE_MAP.put("doc", "/images/doc.png");
        PICTURE_MAP.put("docx", "/images/doc.png");
        PICTURE_MAP.put("ppt", "/images/ppt.png");
        PICTURE_MAP.put("pptx", "/images/ppt.png");
        PICTURE_MAP.put("mp3", "/images/mp3.png");
        PICTURE_MAP.put("mp4", "/images/video.png");
        PICTURE_MAP.put("mkv", "/images/video.png");
        PICTURE_MAP.put("avi", "/images/video.png");
        PICTURE_MAP.put("psd", "/images/psd.png");
        PICTURE_MAP.put("sql", "/images/text.png");
        PICTURE_MAP.put("json", "/images/text.png");
        PICTURE_MAP.put("config", "/images/text.png");
        PICTURE_MAP.put("conf", "/images/text.png");
        PICTURE_MAP.put("xml", "/images/text.png");
        PICTURE_MAP.put("js", "/images/text.png");
        PICTURE_MAP.put("css", "/images/text.png");
        PICTURE_MAP.put("log", "/images/text.png");
        PICTURE_MAP.put("html", "/images/chrome.png");
        PICTURE_MAP.put("pdf", "/images/pdf.png");
        PICTURE_MAP.put("xlsx", "/images/xlsx.png");
        PICTURE_MAP.put("xls", "/images/xlsx.png");
    }
    public static String get(boolean dic, String suf) {
        if (dic)
            return AppConfig.FILE_STATIC_PATH + "/images/folder.png";
        String pic = PICTURE_MAP.get(suf);
        if (pic == null) {
            return AppConfig.FILE_STATIC_PATH + "/images/unknown.png";
        }
        return AppConfig.FILE_STATIC_PATH + pic;
    }
}
