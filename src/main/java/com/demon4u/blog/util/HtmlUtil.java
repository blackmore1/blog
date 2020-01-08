package com.demon4u.blog.util;

import org.pegdown.PegDownProcessor;

public class HtmlUtil {
    private static PegDownProcessor pdp = new PegDownProcessor(Integer.MAX_VALUE);

    public static String mdToHtml(String md){
        return pdp.markdownToHtml(md);
    }
    public static String filterHtml(String html) {
        return html.replaceAll("<[^>]+>","");
    }

    public static void main(String[] args) {
        System.out.println(mdToHtml("# 一级\n" +
                "## 二级\n" +
                "``` java\n" +
                "    String str = \"test\";\n" +
                "```\n" +
                "```\n" +
                "    function fun(){\n" +
                "         echo \"这是一句非常牛逼的代码\";\n" +
                "    }\n" +
                "    fun();\n" +
                "```\n" +
                "`create database hero;`\n" +
                "> 宋泽锋说："));
    }
}
