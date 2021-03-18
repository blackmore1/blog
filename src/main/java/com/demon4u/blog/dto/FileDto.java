package com.demon4u.blog.dto;

import com.demon4u.blog.config.AppConfig;
import com.demon4u.blog.config.FilePictureConfig;
import com.demon4u.blog.config.FileSubffixConfig;
import com.demon4u.blog.util.FormatUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class FileDto {
    private String name;
    private boolean dic;
    //文件后缀类型
    private String type;
    private long size = 0;
    private String displaySize;
    private Date modDate;
    //预览图片
    private String prePicture;
    //相对路径 用于多个打包下载
    private String relativePath;
    //下载地址或者文件夹链接
    private String url;
    //用于前端双击响应
    private String doubleClickType;

    public FileDto(File file) throws IOException {
        this.name = file.getName();
        this.dic = file.isDirectory();
        if (!this.dic) {
            this.type = FilenameUtils.getExtension(file.getName());
            this.size = file.length();
        }
        if (this.type == null)
            this.type = "";
        if (size < FileUtils.ONE_KB) {
            this.displaySize = size + "B";
        } else if (size < FileUtils.ONE_MB) {
            this.displaySize = FormatUtil.twoDecimal((double)size/FileUtils.ONE_KB) + "KB";
        } else if (size < FileUtils.ONE_GB) {
            this.displaySize = FormatUtil.twoDecimal((double)size/FileUtils.ONE_MB) + "MB";
        } else {
            this.displaySize = FormatUtil.twoDecimal((double)size/FileUtils.ONE_GB) + "GB";
        }
        if (this.dic)
            this.displaySize = "N/A";
        this.modDate = new Date(file.lastModified());
        String temp = file.getCanonicalPath().replaceAll("\\\\", "/").replace(AppConfig.globalPath, "");
        if (!temp.startsWith("/"))
            temp = "/" + temp;
        this.relativePath = temp;
        if (this.dic)
            this.url = AppConfig.FILE_LIST + temp;
        else
            this.url = AppConfig.DOWNLOAD_PRE + temp;
        if (FileSubffixConfig.isPicture(this.type)) {
            // 图片的预览可以直接是url，但是我们需要压缩
//            this.prePicture = this.url;
            this.prePicture = AppConfig.PREVIEW_URL + temp;
            this.doubleClickType = "preview";
        } else if (FileSubffixConfig.isFont(this.type) || "psd".equalsIgnoreCase(this.type)) {
            this.prePicture = AppConfig.PREVIEW_URL + temp;
            this.doubleClickType = "preview";
        } else {
            this.prePicture = FilePictureConfig.get(dic, type);
            if (this.isDic())
                this.doubleClickType = "folder";
            else
                this.doubleClickType = "others";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDic() {
        return dic;
    }

    public void setDic(boolean dic) {
        this.dic = dic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(String displaySize) {
        this.displaySize = displaySize;
    }

    public Date getModDate() {
        return modDate;
    }

    public void setModDate(Date modDate) {
        this.modDate = modDate;
    }

    public String getPrePicture() {
        return prePicture;
    }

    public void setPrePicture(String prePicture) {
        this.prePicture = prePicture;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public String getDoubleClickType() {
        return doubleClickType;
    }

    public void setDoubleClickType(String doubleClickType) {
        this.doubleClickType = doubleClickType;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
