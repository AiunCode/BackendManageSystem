package com.aiun.user.controller;

import com.aiun.common.Const;
import com.aiun.common.ServerResponse;
import com.aiun.user.pojo.User;
import com.aiun.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 用户模块控制层
 * @author lenovo
 */
@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ServerResponse<User> login(String username, String password) {
        ServerResponse<User> response = iUserService.login(username, password);
        return response;
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping(value = "register", method =  RequestMethod.POST)
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    @PostMapping("query")
    public int checkAdminRole(@RequestBody User user){
        return iUserService.checkAdminRole(user);
    }
}
