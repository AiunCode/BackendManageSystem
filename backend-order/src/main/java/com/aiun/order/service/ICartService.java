package com.aiun.order.service;

import com.aiun.common.ServerResponse;
import com.aiun.order.vo.CartVo;

/**
 * 购物车接口
 * @author lenovo
 */
public interface ICartService {
    /**
     * 添加购物车功能
     * @param userId 用户 id
     * @param productId 产品 id
     * @param count
     * @return
     */
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    /**
     * 更新购物车商品功能
     * @param userId 用户 id
     * @param productId 产品 id
     * @param count
     * @return
     */
    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    /**
     * 删除购物车商品
     * @param userId 用户 id
     * @param productIds 产品所有 id
     * @return
     */
    ServerResponse<CartVo> deleteProduct(Integer userId, String productIds);

    /**
     * 查询购物车商品列表
     * @param userId 用户 id
     * @return
     */
    ServerResponse<CartVo> list(Integer userId);

    /**
     *
     * @param userId 用户 id
     * @param productId 产品 id
     * @param checked
     * @return
     */
    ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer productId, Integer checked);

    /**
     * 得到购物车中产品数量
     * @param userId 用户 id
     * @return
     */
    ServerResponse<Integer> getCartProductCount(Integer userId);
}
