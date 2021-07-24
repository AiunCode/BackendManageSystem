package com.aiun.common.constant;

/**
 * @author X21147
 * @Description 产品销售状态
 * @date 2021/7/20 17:50
 */
public enum ProductStatusEnum {
    // 在线售卖状态
    ON_SALE(1, "在线"),

    UN_SALE(2,"下架");

    private int code;
    private String value;

    ProductStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
