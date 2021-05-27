package com.aiun.common;

/**
 * 响应状态码
 * @author lenovo
 */
public enum ResponseCode {
    /**
     * 登录成功
     */
    SUCCESS(0, "SUCCESS"),
    /**
     * 登录失败
     */
    ERROR(1, "ERROR"),
    /**
     * 需要登录
     */
    NEED_LOGIN(10, "NEED_LOGIN"),
    /**
     * 非法参数
     */
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
