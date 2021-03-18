package com.demon4u.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.File;
import java.io.IOException;

@Configuration
@PropertySource(value = {"classpath:conf/appConfig.properties"} ,encoding = "utf-8")
public class AppConfig {
    public static String domain;
    @Value("${domain}")
    public void setDomain(String domain) {
        AppConfig.domain = domain;
    }

    //下面为文件浏览相关配置
    //全局文件前缀 不以/结尾
    public static String globalPath;

    //临时存放目录 /结尾 例如存放zip
    public static String tempPath;

    //下载文件url前缀
    public static final String DOWNLOAD_PRE = "/fbDownload";

    //文件列表url前缀
    public static final String FILE_LIST = "/file";

    //图片预览路径
    public static final String PREVIEW_URL = "/preview";
    //静态资源路径
    public static final String FILE_STATIC_PATH = "/file-static";

    //按名称排序
    public static final String SORT_NAME = "name";
    public static final String SORT_TYPE = "type";
    public static final String SORT_SIZE = "size";
    public static final String SORT_DATE = "date";

    @Value(value = "${app.global.path}")
    public void setGlobalPath(String globalPath) throws IOException {
        File file = new File(globalPath);
        AppConfig.globalPath = file.getCanonicalPath().replaceAll("\\\\", "/");
    }

    @Value(value = "${app.temp.path}")
    public void setTempPath(String tempPath) {
        if (!tempPath.endsWith("/"))
            tempPath = tempPath + "/";
        AppConfig.tempPath = tempPath;
    }
}
