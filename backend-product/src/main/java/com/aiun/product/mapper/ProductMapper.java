package com.aiun.product.mapper;

import com.aiun.product.pojo.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lenovo
 */
@Mapper
@Component
public interface ProductMapper {
    /**
     * 通过主键删除产品
     * @param id 主键
     * @return 影响行数
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入产品数据
     * @param record 产品类
     * @return 影响行数
     */
    int insert(@Param("record") Product record);

    /**
     * 有选择的插入产品数据
     * @param record 产品类
     * @return 影响行数
     */
    int insertSelective(Product record);

    /**
     * 通过主键有选择的插入产品数据
     * @param id 主键
     * @return 产品类
     */
    Product selectByPrimaryKey(Integer id);

    /**
     * 通过主键有选择的更新产品数据
     * @param product 产品类
     * @return 影响行数
     */
    int updateByPrimaryKeySelective(@Param("record") Product product);

    /**
     * 通过主键更新产品信息
     * @param product 产品类
     * @return 影响行数
     */
    int updateByPrimaryKey(@Param("record") Product product);

    /**
     * 获取产品列表
     * @return 产品列表
     */
    List<Product> selectList();

    /**
     * 通过产品名或id获取产品
     * @param productName 产品名
     * @param productId 产品 id
     * @return 产品列表
     */
    List<Product> selectByNameAndProductId(@Param("productName") String productName, @Param("productId") Integer productId);

    /**
     * 通过产品名或品种id获取产品
     * @return 产品列表
     */
    List<Product> selectByNameAndCategoryIds(@Param("productName") String productName, @Param("categoryIdList") List<Integer> categoryIdList);
}