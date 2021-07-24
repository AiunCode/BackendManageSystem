package com.aiun.product.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author lenovo
 */
@Api(tags = "上传文件相关接口")
@Controller
public class UpLoadController {
    /**
     * 获取file.html页面
     * @return 跳转到 file 页面
     */
    @GetMapping("/file")
    @ApiOperation(value = "获取file.html页面, 进行上传文件操作")
    public String file(){
        return "file";
    }
}
