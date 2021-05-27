package com.aiun.user.service;

import com.aiun.common.ServerResponse;
import com.aiun.user.pojo.User;

/**
 * 用户接口
 * @author lenovo
 */
public interface IUserService {
    /**
     * 用户登录
     * @param userName
     * @param password
     * @return
     */
    ServerResponse<User> login(String userName, String password);
    /**
     * 注册
     * @param user
     * @return
     */
    ServerResponse<String> register(User user);

    /**
     * 用户名或邮箱的校验
     * @param str
     * @param type
     * @return
     */
    ServerResponse<String> checkValid(String str, String type);

    /**
     * 检查是否是管理员
     * @param user
     * @return
     */
    int checkAdminRole(User user);
}
