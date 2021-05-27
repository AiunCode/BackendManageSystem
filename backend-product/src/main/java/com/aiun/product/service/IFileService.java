package com.aiun.product.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author lenovo
 */
public interface IFileService {
    /**
     * 文件上传
     * @param file
     * @param path
     * @return
     */
    String upload(MultipartFile file, String path);
}
