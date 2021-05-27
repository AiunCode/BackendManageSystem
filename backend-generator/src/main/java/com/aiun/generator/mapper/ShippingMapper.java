package com.aiun.generator.mapper;

import com.aiun.generator.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(@Param("record") Shipping record);

    int insertSelective(@Param("record") Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(@Param("record") Shipping record);

    int updateByPrimaryKey(@Param("record") Shipping record);

    int deleteByShippingIdUserId(@Param("userId") Integer userId, @Param("shippingId") Integer shippingId);

    int updateByShipping(@Param("record") Shipping record);

    Shipping selectByShippingIdUserId(@Param("userId") Integer userId, @Param("shippingId") Integer shippingId);

    List<Shipping> selectByUserId(@Param("userId") Integer userId);
}