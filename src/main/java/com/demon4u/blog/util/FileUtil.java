package com.demon4u.blog.util;

import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;

public class FileUtil {
    /**
     * 打包文件
     * @param files 文件或文件夹的集合
     * @param out 输出的zip文件
     */
    public static void zip(List<File> files, File out) throws IOException {
        if (files != null) {
            Map<String, File> map = new HashMap<String, File>();
            for (File f : files) {
                list(f, null, map);
            }
            if (!map.isEmpty()) {
                ZipArchiveOutputStream zaos = new ZipArchiveOutputStream(out);
                zaos.setUseZip64(Zip64Mode.AsNeeded);
                zaos.setMethod(ZipEntry.STORED);//关键代码,设置压缩模式为存储
                for (Map.Entry<String, File> entry : map.entrySet()) {
                    File file = entry.getValue();
                    ZipArchiveEntry zae = new ZipArchiveEntry(file, entry.getKey());
                    zaos.putArchiveEntry(zae);
                    InputStream is = new FileInputStream(file);
                    //1MB
                    byte[] b = new byte[1024 * 1024];
                    int i = -1;
                    while ((i = is.read(b)) != -1) {
                        zaos.write(b, 0, i);
                    }
                    zaos.flush();
                    is.close();
                    zaos.closeArchiveEntry();
                }
                zaos.finish();
                zaos.close();
            }
        }
    }

    private static void list(File f, String parent, Map<String, File> map) {
        String name = f.getName();
        if (parent != null) {
            name = parent + "/" + name;//设置在zip包里的相对路径
        }
        if (f.isFile()) {
            map.put(name, f);
        } else if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                list(file, name, map);
            }
        }
    }
}
