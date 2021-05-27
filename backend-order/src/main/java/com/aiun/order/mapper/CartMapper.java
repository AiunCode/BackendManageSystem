package com.aiun.order.mapper;

import com.aiun.order.pojo.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CartMapper {
    /**
     * 通过主键删除购物车
     * @param id 主键
     * @return 影响的记录行
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入购物车信息
     * @param record 购物车信息
     * @return 影响的记录行
     */
    int insert(@Param("record") Cart record);

    /**
     * 有选择的插入购物车信息
     * @param record 购物车信息
     * @return 影响的记录行
     */
    int insertSelective(@Param("record") Cart record);

    /**
     * 通过主键有选择的插入
     * @param id 主键
     * @return 购物车信息
     */
    Cart selectByPrimaryKey(Integer id);

    /**
     * 通过主键有选择的更新购物车数据
     * @param record 购物车
     * @return 影响的记录行
     */
    int updateByPrimaryKeySelective(@Param("record") Cart record);

    /**
     * 通过主键更新购物车数据
     * @param record 购物车信息
     * @return 影响的记录行
     */
    int updateByPrimaryKey(@Param("record") Cart record);

    /**
     * 根据用户Id查询商品选中状态
     * @param userId 用户 id
     * @return 所有购物车信息
     */
    List<Cart> selectCheckedCartByUserId(Integer userId);

    /**
     * 通过用户id和产品id查询购物车
     * @param userId 用户 id
     * @param productId 产品 id
     * @return 购物车信息
     */
    Cart selectCartByUserIdProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    /**
     * 通过用户id查询购物车
     * @param userId 用户 id
     * @return 购物车信息
     */
    List<Cart> selectCartByUserId(Integer userId);

    /**
     * 根据用户id和产品id删除
     * @param userId 用户 id
     * @param productIds 需要删除的产品 id
     * @return 影响的记录行
     */
    int deleteByUserIdProductIds(@Param("userId") Integer userId, @Param("productIds") List<String> productIds);

    /**
     * 更新购物车产品的选中状态
     * @param userId 用户 id
     * @param productId 需要选中的产品 id
     * @param checked 选中状态
     * @return 影响的记录行
     */
    int checkedOrUnCheckedProduct(@Param("userId") Integer userId, @Param("productId") Integer productId, @Param("checked") Integer checked);

    /**
     * 获取购物车产品数量
     * @param userId 用户 id
     * @return 影响的记录行
     */
    int selectCartProductCount(@Param("userId") Integer userId);
    /**
     * 根据userId查询产品是否有未被选中的
     * @param userId 用户 id
     * @return 影响的记录行
     */
    int selectCartProductCheckedStatusByUserId(Integer userId);
}