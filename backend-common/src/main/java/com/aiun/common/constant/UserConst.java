package com.aiun.common.constant;


import java.util.Set;

/**
 * 用户常量定义类
 * @author lenovo
 */
public class UserConst {
    /**
     * 用户免登录令牌
     */
    public static final String AUTHORITY = "Auth";
    /**
     * 用户状态
     */
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    /**
     * 用户角色
     */
    public interface Role {
        /**
         * 普通用户
         */
        int ROLE_CUSTOMER = 0;
        /**
         * 管理员
         */
        int ROLE_ADMIN = 1;
    }
}
