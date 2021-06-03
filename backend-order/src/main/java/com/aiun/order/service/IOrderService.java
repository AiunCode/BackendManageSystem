package com.aiun.order.service;

import com.aiun.common.ServerResponse;
import com.aiun.order.vo.OrderVo;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * 订单接口
 * @author lenovo
 */
public interface IOrderService {
    /**
     * 创建订单
     * @param userId 用户id
     * @param shippingId 收货地址id
     * @return 返回订单信息
     */
    ServerResponse<OrderVo> createOrder(Integer userId, Integer shippingId);

    /**
     * 取消订单
     * @param userId 用户 id
     * @param orderNo 订单号
     * @return 返回结果
     */
    ServerResponse<String> cancel(Integer userId, Long orderNo);

    /**
     * 获取购物车中已经选中的商品信息
     * @param userId 用户 id
     * @return 返回结果
     */
    ServerResponse getOrderCartProduct(Integer userId);
    /**
     * 获取订单详细信息
     * @param userId 用户 id
     * @param orderNo 订单号
     * @return 返回结果
     */
    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);

    /**
     * 获取订单列表
     * @param userId 用户 id
     * @return 返回分页订单信息
     */
    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);

    /**
     * 发货信息
     * @param orderNo 订单号
     * @return 返回发货信息
     */
    ServerResponse<String> manageSendGoods(Long orderNo);
    /**
     * 订单查询
     * @param orderNo
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);
}
