package com.aiun.shipping.mapper;

import com.aiun.shipping.pojo.Shipping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 地址映射接口
 * @author lenovo
 */
@Mapper
@Component
public interface ShippingMapper {
    /**
     * 通过主键删除地址
     * @param id 主键
     * @return 影响行数
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *  插入地址
     * @param record 地址信息
     * @return 影响行数
     */
    int insert(@Param("record") Shipping record);

    /**
     * 有选择的插入地址
     * @param record 地址信息
     * @return 影响行数
     */
    int insertSelective(@Param("record") Shipping record);

    /**
     * 通过主键查询地址
     * @param id 主键
     * @return 返回地址信息
     */
    Shipping selectByPrimaryKey(Integer id);

    /**
     * 通过主键有选择的更新地址信息
     * @param record 地址信息
     * @return 影响行数
     */
    int updateByPrimaryKeySelective(@Param("record") Shipping record);

    /**
     * 通过主键更新地址信息
     * @param record 地址信息
     * @return 影响行数
     */
    int updateByPrimaryKey(@Param("record") Shipping record);

    /**
     * 通过地址 id 和用户 id 删除地址
     * @param userId 用户 id
     * @param shippingId 地址 id
     * @return 影响行数
     */
    int deleteByShippingIdUserId(@Param("userId") Integer userId, @Param("shippingId") Integer shippingId);

    /**
     * 更新地址信息
     * @param record 地址信息
     * @return 影响行数
     */
    int updateByShipping(@Param("record") Shipping record);

    /**
     *  通过地址 id 和用户 id 查询地址
     * @param userId 用户 id
     * @param shippingId 地址 id
     * @return 返回地址信息
     */
    Shipping selectByShippingIdUserId(@Param("userId") Integer userId, @Param("shippingId") Integer shippingId);

    /**
     * 通过用户 id 查询地址列表
     * @param userId 用户 id
     * @return 地址列表
     */
    List<Shipping> selectByUserId(@Param("userId") Integer userId);
}