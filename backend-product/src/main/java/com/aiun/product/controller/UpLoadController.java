package com.aiun.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author lenovo
 */
@Controller
public class UpLoadController {
    /**
     * 获取file.html页面
     * @return 跳转到 file 页面
     */
    @RequestMapping("/file")
    public String file(){
        return "file";
    }
}
