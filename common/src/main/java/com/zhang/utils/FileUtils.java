package com.zhang.utils;

import com.zhang.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author zhang
 * &#064;date  2024/2/10
 * &#064;Description  上传文件工具类
 */
@Component
@Slf4j
public class FileUtils {

    @Value("${image.basePath}")
    private String UPLOAD_PATH;

    @Value("${image.urlPath}")
    private String urlPrefix;


    public String uploads(MultipartFile file) throws IOException {
        String fileName = Objects.requireNonNull(file.getOriginalFilename()).substring(0,file.getOriginalFilename().lastIndexOf("."));
        final String fileSuffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".")+1);
        //文件名
        String filename = System.currentTimeMillis() + "." + fileName + "." + fileSuffix;

        //文件写入
        File descFile = new File(UPLOAD_PATH, filename);
        file.transferTo(descFile);

        return urlPrefix + "/image/" + filename;

    }

    public String checkPhotoAndUploads(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            log.error("用户未上传文件");
            throw new UserException("请选中文件");
        }
        // 原始文件名称
        String fileName = file.getOriginalFilename();

        // 解析到文件后缀
        int index = fileName.lastIndexOf(".");
        String suffix;
        if (index == -1 || (suffix = fileName.substring(index + 1)).isEmpty()) {
            log.error("文件格式类型错误");
            String msg= "文件后缀不能为空";
            throw new UserException(msg);
        }
        // 允许上传的文件后缀列表
        Set<String> allowSuffix = new HashSet<>(Arrays.asList("jpg", "jpeg", "png", "gif"));
        if (!allowSuffix.contains(suffix.toLowerCase())) {
            log.error("文件格式类型错误");
            String msg="非法的文件，不允许的文件类型：" + suffix;
            throw new UserException(msg);
        }
        return this.uploads(file);
    }
}
