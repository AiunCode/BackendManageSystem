package com.aiun.product.service.impl;

import com.aiun.product.service.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传
 * @author lenovo
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID() + "." + fileExtensionName;

        logger.info("开始上传文件，上传的文件名：{}，上传的路径：{}，新文件名：{}", fileName, path, uploadFileName);
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            //赋予写权限
            fileDir.setWritable(true);
            //递归创建目录
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }
        return targetFile.getName();
    }
}
