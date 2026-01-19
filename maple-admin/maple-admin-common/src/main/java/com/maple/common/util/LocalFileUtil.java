package com.maple.common.util;

import com.maple.common.config.LocalFileProperties;
import com.maple.common.config.exception.ErrorCode;
import com.maple.common.config.exception.MapleCheckException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class LocalFileUtil {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LocalFileUtil.class);
    
    private final LocalFileProperties fileProperties;

    public LocalFileUtil(LocalFileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }

    private static final List<String> FILE_TYPE_LIST_IMAGE = Arrays.asList(
            "image/png",
            "image/jpg",
            "image/jpeg",
            "image/bmp");

    public String uploadImage(MultipartFile file) {
        
        String contentType = file.getContentType();
        if (!FILE_TYPE_LIST_IMAGE.contains(contentType)) {
            throw new RuntimeException("上传失败，不允许的文件类型");
        }
        int size = (int) file.getSize();
        if (size > fileProperties.getMaxFileSize()) {
            throw new RuntimeException("文件过大");
        }
        String fileName = file.getOriginalFilename();
        
        String afterName = StringUtils.substringAfterLast(fileName, ".");
        
        String prefName = StringUtils.substringBeforeLast(fileName, ".");
        
        fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + prefName + "." + afterName;
        File filePath = new File(fileProperties.getImageFilePath(), fileName);

        if (filePath.exists()) {
            throw new RuntimeException("文件已经存在");
        }
        
        if (!filePath.getParentFile().exists()) {
            filePath.getParentFile().mkdirs();
        }
        try {
            file.transferTo(filePath);
        } catch (IOException e) {
            log.error("图片上传失败", e);
            throw new RuntimeException("图片上传失败");
        }
        return "/fileApi/local/images/" + fileName;
    }

    public List<Map<String, Object>> uploadFiles(MultipartFile[] files) {
        int size = 0;
        for (MultipartFile file : files) {
            size = (int) file.getSize() + size;
        }
        if (size > fileProperties.getMaxFileSize()) {
            throw new RuntimeException("文件过大");
        }
        List<Map<String, Object>> fileInfoList = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            Map<String, Object> map = new HashMap<>();
            String fileName = files[i].getOriginalFilename();
            
            String afterName = StringUtils.substringAfterLast(fileName, ".");
            
            String prefName = StringUtils.substringBeforeLast(fileName, ".");

            String fileServiceName = new SimpleDateFormat("yyyyMMddHHmmss")
                    .format(new Date()) + i + "_" + prefName + "." + afterName;
            File filePath = new File(fileProperties.getDocFilePath(), fileServiceName);
            
            if (!filePath.getParentFile().exists()) {
                filePath.getParentFile().mkdirs();
            }
            try {
                files[i].transferTo(filePath);
            } catch (IOException e) {
                log.error("文件上传失败", e);
                throw new RuntimeException("文件上传失败");
            }
            map.put("fileName", fileName);
            map.put("filePath", filePath);
            map.put("fileServiceName", "/fileApi/local/doc/" + fileServiceName);
            fileInfoList.add(map);
        }
        return fileInfoList;
    }

    public void deleteFile(String[] fileNameArr) {
        for (String fileName : fileNameArr) {
            String filePath = fileProperties.getDocFilePath() + fileName;
            File file = new File(filePath);
            if (file.exists()) {
                try {
                    Files.delete(file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                    log.warn("文件删除失败", e);
                }
            } else {
                log.warn("文件: {} 删除失败，该文件不存在", fileName);
            }
        }
    }

    public void downLoadFile(HttpServletResponse response, String fileName) {
        String encodeFileName = null;
        try {
            encodeFileName = URLDecoder.decode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "下载文件失败");
        }
        File file = new File(fileProperties.getDocFilePath() + encodeFileName);
        
        if (!file.exists()) {
            throw new MapleCheckException(ErrorCode.COMMON_ERROR, "文件不存在！");
        }
        try (FileInputStream inputStream = new FileInputStream(file);
             ServletOutputStream outputStream = response.getOutputStream()) {
            response.reset();
            
            response.setContentType("application/octet-stream;charset=utf-8");
            
            String afterName = StringUtils.substringAfterLast(fileName, "_");
            
            afterName = response.encodeURL(new String(afterName.getBytes(), StandardCharsets.ISO_8859_1.displayName()));
            response.setHeader("Content-type", "application-download");
            
            response.addHeader("Content-Disposition", "attachment;filename=" + afterName);
            response.addHeader("filename", afterName);
            
            int length = 1024;
            byte[] buf = new byte[1024];
            int readLength = inputStream.read(buf, 0, length);
            while (readLength != -1) {
                outputStream.write(buf, 0, readLength);
                readLength = inputStream.read(buf, 0, length);
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
