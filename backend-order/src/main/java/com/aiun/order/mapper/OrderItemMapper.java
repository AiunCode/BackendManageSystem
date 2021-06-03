package com.aiun.order.mapper;


import com.aiun.order.pojo.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单详情映射接口
 * @author lenovo
 */
@Mapper
@Component
public interface OrderItemMapper {
    /**
     * 通过主键删除订单详情
     * @param id 主键
     * @return 影响行数
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入订单详情信息
     * @param record 订单详情信息
     * @return 影响行数
     */
    int insert(@Param("record") OrderItem record);

    /**
     *
     * @param record 订单详情信息
     * @return 影响行数
     */
    int insertSelective(@Param("record") OrderItem record);

    /**
     * 通过主键查询订单详情
     * @param id 主键
     * @return 订单详情信息
     */
    OrderItem selectByPrimaryKey(Integer id);

    /**
     * 通过主键有选择的更新订单详情信息
     * @param record 订单详情信息
     * @return 影响行数
     */
    int updateByPrimaryKeySelective(@Param("record") OrderItem record);

    /**
     * 通过主键更新订单详情
     * @param record 订单详情信息
     * @return 影响行数
     */
    int updateByPrimaryKey(@Param("record") OrderItem record);

    /**
     * 通过订单号获取订单详情
     * @param orderNo 订单号
     * @return 订单详情信息列表
     */
    List<OrderItem> getByOrderNo(@Param("orderNo") Long orderNo);

    /**
     * 通过订单号和用户 id 获取订单详情
     * @param orderNo 订单号
     * @param userId 用户 id
     * @return 订单详情信息列表
     */
    List<OrderItem> getByOrderNoAndUserId(@Param("orderNo") Long orderNo, @Param("userId") Integer userId);

    /**
     * 订单详情批量插入
     * @param orderItemList 订单信息列表
     */
    void batchInsert(@Param("orderItemList") List<OrderItem> orderItemList);
}