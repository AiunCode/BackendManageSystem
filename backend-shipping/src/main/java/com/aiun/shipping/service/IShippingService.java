package com.aiun.shipping.service;

import com.aiun.common.ServerResponse;
import com.aiun.shipping.pojo.Shipping;
import com.github.pagehelper.PageInfo;

/**
 * 收货地址接口
 * @author lenovo
 */
public interface IShippingService {
    /**
     * 增加收货地址
     * @param userId
     * @param shipping
     * @return
     */
    ServerResponse add(Integer userId, Shipping shipping);

    /**
     * 删除收货地址
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse<String> del(Integer userId, Integer shippingId);

    /**
     * 更新收货地址
     * @param userId
     * @param shipping
     * @return
     */
    ServerResponse update(Integer userId, Shipping shipping);

    /**
     * 查询收货地址
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse<Shipping> select(Integer userId, Integer shippingId);

    /**
     * 查询某用户的所有收货地址，并分页
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);

    /**
     * 通过id 获取地址信息
     * @param id 订单的地址 id
     * @return 地址信息
     */
    Shipping findById(Integer id);
}
