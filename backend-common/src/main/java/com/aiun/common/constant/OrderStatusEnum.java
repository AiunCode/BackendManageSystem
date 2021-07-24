package com.aiun.common.constant;

/**
 * @author X21147
 * @Description 订单状态
 * @date 2021/7/20 17:57
 */
public enum OrderStatusEnum {
    /**
     * 订单取消
     */
    CANCELED(0, "已取消"),
    /**
     * 订单未支付
     */
    NO_PAY(10, "未支付"),
    /**
     * 已付款状态
     */
    PAID(20, "已付款"),
    /**
     * 已发货状态
     */
    SHIPPED(40, "已发货"),
    /**
     * 订单完成状态
     */
    ORDER_SUCCESS(50, "订单完成"),
    /**
     * 订单关闭状态
     */
    ORDER_CLOSE(60, "订单关闭");

    private int code;
    private String value;

    OrderStatusEnum(int code, String value) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }

    public static OrderStatusEnum codeOf(int code) {
        for (OrderStatusEnum orderStatusEnum : values()) {
            if (orderStatusEnum.getCode() == code) {
                return orderStatusEnum;
            }
        }
        throw new RuntimeException("没有找到对应的枚举");
    }
}
