package com.aiun.common.constant;

/**
 * @author X21147
 * @Description 支付状态
 * @date 2021/7/20 17:59
 */
public enum PaymentTypeEnum {
    /**
     * 在线支付状态
     */
    ONLINE_PAY(1,"在线支付");

    private String value;
    private int code;

    PaymentTypeEnum(int code, String value) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }

    public static PaymentTypeEnum codeOf(int code) {
        for (PaymentTypeEnum paymentTypeEnum : values()) {
            if (paymentTypeEnum.getCode() == code) {
                return paymentTypeEnum;
            }
        }
        throw new RuntimeException("没有找到对应的枚举");
    }
}
