package com.aiun.order.mapper;

import com.aiun.order.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * order模块映射接口
 * @author lenovo
 */
@Mapper
@Component
public interface OrderMapper {
    /**
     * 通过主键删除订单
     * @param id 主键
     * @return 影响行数
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 创建订单
     * @param record 订单信息
     * @return 影响行数
     */
    int insert(@Param("record") Order record);

    /**
     * 有选择的创建订单
     * @param record 订单信息
     * @return 影响行数
     */
    int insertSelective(@Param("record") Order record);

    /**
     * 有选择的通过主键创建订单
     * @param id 主键
     * @return 订单
     */
    Order selectByPrimaryKey(Integer id);

    /**
     * 有选择的通过主键更新订单
     * @param record 订单信息
     * @return 影响行数
     */
    int updateByPrimaryKeySelective(@Param("record") Order record);

    /**
     * 修改订单
     * @param record 订单信息
     * @return 影响行数
     */
    int updateByPrimaryKey(@Param("record") Order record);

    /**
     * 通过用户id和订单和查询订单
     * @param userId 用户id
     * @param orderNo 订单号
     * @return 订单
     */
    Order selectByUserIdAndOrderNo(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);

    /**
     * 通过订单号查询订单
     * @param orderNo 订单号
     * @return 订单
     */
    Order selectByOrderNo(Long orderNo);

    /**
     * 通过用户 id 查询订单
     * @param userId 用户 id
     * @return 返回用户所有的订单
     */
    List<Order> selectByUserId(Integer userId);

    /**
     * 查询所有的订单
     * @return 返回订单列表
     */
    List<Order> selectAllOrder();
}