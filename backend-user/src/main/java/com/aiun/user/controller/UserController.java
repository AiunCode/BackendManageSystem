package com.aiun.user.controller;

import com.aiun.common.constant.UserConst;
import com.aiun.common.ResponseCode;
import com.aiun.common.ServerResponse;
import com.aiun.common.util.JsonUtils;
import com.aiun.user.pojo.User;
import com.aiun.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 用户模块控制层
 * @author lenovo
 */
@Api(tags = "用户系统相关接口")
@RestController
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 返回登录信息
     */
    @PostMapping("login")
    @ApiOperation(value = "用户登录", notes = "用户登录")
    public ServerResponse<User> login(String username, String password) {
        ServerResponse<User> response = iUserService.login(username, password);
        return response;
    }

    /**
     * 用户注册
     * @param user 用户信息
     * @return 返回注册信息
     */
    @PostMapping("register")
    @ApiOperation(value = "用户注册")
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    /**
     * 退出登录
     * @param request 请求
     * @return 返回退出状态
     */
    @PostMapping("logout")
    @ApiOperation(value = "用户登录退出")
    public ServerResponse<String> logout(HttpServletRequest request) {
        String key = request.getHeader(UserConst.AUTHORITY);
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String value = operations.get(key);
        User user = JsonUtils.jsonStr2Object(value, User.class);
        if (key.equals(user.getUsername())) {
            redisTemplate.delete(key);
            return ServerResponse.createBySuccessMessage("退出成功");
        }
        return ServerResponse.createBySuccessMessage("当前用户未登录");
    }

    /**
     * 验证信息
     * @param str 校验的数据
     * @param type 类型（userName, email）
     * @return 返回校验结果
     */
    @PostMapping("check_valid")
    @ApiOperation(value = "验证信息")
    public ServerResponse<String> checkValid(String str, String type){
        return iUserService.checkValid(str, type);
    }

    /**
     * 获取用户信息
     * @param request 请求
     * @return 返回用户信息
     */
    @PostMapping("get_information")
    @ApiOperation(value = "获取用户信息")
    public ServerResponse<User> getInformation(HttpServletRequest request) {
        ServerResponse hasLogin = loginHasExpired(request);
        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            return iUserService.getInformation(user.getId());
        }

        return hasLogin;
    }

    /**
     * 登录状态的重设密码
     * @param request 请求
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 返回更改结果
     */
    @PostMapping("reset_password")
    @ApiOperation(value = "登录状态的重设密码")
    public ServerResponse<String> resetPassword(HttpServletRequest request, String oldPassword, String newPassword){
        ServerResponse hasLogin = loginHasExpired(request);
        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            return iUserService.resetPassword(oldPassword, newPassword, user);
        }

        return ServerResponse.createByErrorMessage("用户未登录");
    }

    /**
     * 判断用户登录是否过期
     */
    private ServerResponse<User> loginHasExpired(HttpServletRequest request) {
        String key = request.getHeader(UserConst.AUTHORITY);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String value = valueOperations.get(key);
        if (StringUtils.isEmpty(value)) {
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        User user = JsonUtils.jsonStr2Object(value, User.class);
        if (!key.equals(user.getUsername())) {
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        valueOperations.set(key, value, 1, TimeUnit.HOURS);
        return ServerResponse.createBySuccess(user);
    }
}
