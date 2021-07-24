package com.aiun.common.constant;

/**
 * @author X21147
 * @Description
 * @date 2021/7/20 17:54
 */
public class CartConst {
    public static final String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";

    public static final String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";

    /**
     * 购物车
     */
    public interface Cart{
        // 购物车选中状态
        int CHECKED = 1;
        // 购物车未选中状态
        int UN_CHECKED = 0;
    }
}
