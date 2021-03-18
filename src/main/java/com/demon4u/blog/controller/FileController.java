package com.demon4u.blog.controller;

import com.demon4u.blog.config.AppConfig;
import com.demon4u.blog.config.FileSubffixConfig;
import com.demon4u.blog.dto.FileDto;
import com.demon4u.blog.dto.ResponseDto;
import com.demon4u.blog.util.FileUtil;
import com.demon4u.blog.util.LogUtil;
import com.demon4u.blog.util.PsdReader;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class FileController {

    /**
     * 图片列表
     * */
    @RequestMapping(AppConfig.FILE_LIST + "/**")
    public String files(HttpServletRequest request, Model model) throws IOException {
        List<FileDto> list = new ArrayList<>();
        //todo 防止里面有../
        String uri = URLDecoder.decode(request.getRequestURI(), "UTF-8");
        File file = new File(AppConfig.globalPath + uri.replaceFirst(AppConfig.FILE_LIST, ""));
        if (file.exists() && file.isDirectory()) {
            for (File file1 : Objects.requireNonNull(file.listFiles())) {
                if (file1.getName().startsWith("."))
                    continue;
                FileDto dto = new FileDto(file1);
                list.add(dto);
            }
        }
        String sort = cookie(request, "sort");
        String ascStr = cookie(request, "asc");
        int asc = 1;
        if (ascStr != null) {
            try {
                asc = Integer.parseInt(ascStr);
            } catch (NumberFormatException e) {
            }
        }
        sort(list, sort, asc);
        model.addAttribute("files", list);
        model.addAttribute("indexPath", AppConfig.FILE_LIST);
        return "/fileView";
    }
    private String cookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(key))
                    return cookie.getValue();
            }
        return null;
    }
    private void sort(List<FileDto> list, String sort, int asc) {
        if (sort == null || sort.equalsIgnoreCase(AppConfig.SORT_NAME)) {
            list.sort((o1, o2) -> {
                if (o1.isDic() && o2.isDic())
                    return asc * (o1.getName().compareTo(o2.getName()));
                if (o1.isDic() ^ o2.isDic())
                    return o1.isDic() ? -1 : 1;
                return asc * o1.getName().compareTo(o2.getName());
            });
        } else if (sort.equalsIgnoreCase(AppConfig.SORT_SIZE)) {
            list.sort((o1, o2) -> {
                if (o1.isDic() && o2.isDic())
                    return o1.getName().compareTo(o2.getName());
                if (o1.isDic() ^ o2.isDic())
                    return o1.isDic() ? -1 : 1;
                return (int) (asc * (o1.getSize() - o2.getSize()));
            });
        } else if (sort.equalsIgnoreCase(AppConfig.SORT_TYPE)) {
            list.sort((o1, o2) -> {
                if (o1.isDic() && o2.isDic())
                    return o1.getName().compareTo(o2.getName());
                if (o1.isDic() ^ o2.isDic())
                    return o1.isDic() ? -1 : 1;
                return asc * o1.getType().compareTo(o2.getType());
            });
        } else if (sort.equalsIgnoreCase(AppConfig.SORT_DATE)) {
            list.sort((o1, o2) -> {
                if (o1.isDic() && o2.isDic())
                    return asc * o1.getModDate().compareTo(o2.getModDate());
                if (o1.isDic() ^ o2.isDic())
                    return o1.isDic() ? -1 : 1;
                return asc * o1.getModDate().compareTo(o2.getModDate());
            });
        }
    }
    /**
     * 多选下载
     * */
    @RequestMapping("/multiDownload")
    @ResponseBody
    public String multiDownload(@RequestParam("paths") List<String> paths, HttpServletResponse response) throws IOException {
        List<File> list = new ArrayList<>();
        for (String path : paths) {
            File file = new File(AppConfig.globalPath + path);
            if (file.exists())
                list.add(file);
        }
        if (!CollectionUtils.isEmpty(list)) {
            File zipFile = new File(AppConfig.tempPath + System.currentTimeMillis() + ".zip");
            try {
                long t1 = System.currentTimeMillis();
                FileUtil.zip(list, zipFile);
                LogUtil.getCommonLog().info("{}打包耗时{}ms", zipFile.getName(), System.currentTimeMillis() - t1);
                if (zipFile.exists()) {
                    try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zipFile))) {
                        response.setHeader("content-type", "application/force-download");
                        response.setContentType("application/force-download");// 设置强制下载不打开
                        response.addHeader("Content-Disposition", "attachment;filename="+zipFile.getName());// 设置文件名
                        response.addHeader("Content-Length", "" + zipFile.length());
                        byte[] buffer = new byte[1024 * 1024];
                        int i = bis.read(buffer);
                        while (i != -1) {
                            response.getOutputStream().write(buffer, 0, i);
                            i = bis.read(buffer);
                        }
                        response.getOutputStream().flush();
                    }
                    zipFile.delete();
                    return "success";
                }
            } catch (Exception e) {
                LogUtil.getErrorLog().error("multiDownload", e);
                if (zipFile.exists())
                    zipFile.delete();
            }
        }
        return "fail";
    }

    /**
     * 文件预览图
     * 支持font文件、psd文件、图片本身压缩
     * */
    @RequestMapping(value = AppConfig.PREVIEW_URL + "/**")
    public void preview(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String uri = URLDecoder.decode(request.getRequestURI(), "UTF-8");
        File file = new File(AppConfig.globalPath + uri.replaceFirst(AppConfig.PREVIEW_URL, ""));
        if (!file.exists())
            return;
        String subffix = FilenameUtils.getExtension(file.getName());
        try {
            response.setContentType("image/png");
            if (FileSubffixConfig.isPicture(subffix)) { //是图片则压缩
                InputStream in = new FileInputStream(file);
                Thumbnails.of(in).scale(0.2f).toOutputStream(response.getOutputStream());
                in.close();// 必须关闭
            } else {
                BufferedImage bi = null;
                if (FileSubffixConfig.isFont(subffix)) {
                    Font dynamicFont = Font.createFont(Font.TRUETYPE_FONT, file);
                    Font dynamicFontPt = dynamicFont.deriveFont(30f);
                    int width = 180;
                    int height = 180;
                    bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    //得到它的绘制环境(这张图片的笔)
                    Graphics2D g2 = (Graphics2D) bi.getGraphics();
                    g2.fillRect(0, 0, width, height);
                    String str = "abcdefghijklm";
                    //设置背景颜色
                    g2.setColor(Color.BLACK);
                    //向图片上写字符串
                    g2.setFont(dynamicFontPt);
                    g2.drawString(str, 10, 50);
                    g2.drawString("0123456789", 10, 100);
                    g2.drawString("曲项向天歌", 10, 150);
                } else if ("psd".equalsIgnoreCase(subffix)) {
                    bi = new PsdReader(file).getImg();
                }
                if (bi != null) {
                    BufferedImage bi1 = new BufferedImage(1080, bi.getHeight() * 1080 / bi.getWidth(), BufferedImage.TYPE_INT_RGB);
                    Graphics graphics = bi1.getGraphics();
                    graphics.drawImage(bi.getScaledInstance(bi1.getWidth(), bi1.getHeight(), Image.SCALE_FAST)
                            , 0, 0, null);
                    graphics.dispose();
                    ImageIO.write(bi1, "png", response.getOutputStream());
                }
            }
        } catch (Exception e) {
            LogUtil.getErrorLog().error("preview", e);
        }
    }

    @PostMapping("/upload")
    @ResponseBody
    public ResponseDto<String> upload(@RequestParam("file") MultipartFile file, String path) throws UnsupportedEncodingException {
        ResponseDto<String> dto = new ResponseDto<>();
        if (file.isEmpty()) {
            dto.setState(false);
            dto.setMsg("上传失败，请选择文件");
            return dto;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String fileName = sdf.format(new Date()) + "." + suffix;
        String filePath = AppConfig.globalPath + "/";
        if (path != null) {
            path = URLDecoder.decode(path, "UTF-8").replaceFirst(AppConfig.FILE_LIST, "");
            if (path.startsWith("/")) {
                path = path.replaceFirst("/", "");
            }
            filePath += path;
        }
        if (!filePath.endsWith("/")) {
            filePath += "/";
        }
        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
            dto.setState(true);
            dto.setMsg("上传成功");
            return dto;
        } catch (IOException e) {
            dto.setState(false);
            dto.setMsg(e.getMessage());
            return dto;
        }
    }
}
