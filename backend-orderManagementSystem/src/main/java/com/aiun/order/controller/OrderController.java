package com.aiun.order.controller;

import com.aiun.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 订单控制层
 * @author lenovo
 */
@RestController
@RequestMapping("/")
public class OrderController {
    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("create.do")
    public String create() {
        return "Hello order";
    }

}
