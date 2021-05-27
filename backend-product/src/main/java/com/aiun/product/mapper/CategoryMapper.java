package com.aiun.product.mapper;

import com.aiun.product.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * @author lenovo
 */
@Mapper
@Component
public interface CategoryMapper {
    /**
     * 通过主键删除商品种类
     * @param id 主键
     * @return 操作影响行数
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入商品种类
     * @param record 商品种类类
     * @return 操作影响行数
     */
    int insert(@Param("record") Category record);

    /**
     * 有选择的插入
     * @param record 商品种类类
     * @return 操作影响行数
     */
    int insertSelective(@Param("record") Category record);

    /**
     * 通过主键查询
     * @param id 主键
     * @return 分类实体类
     */
    Category selectByPrimaryKey(Integer id);

    /**
     * 有选择的通过主键更新
     * @param record 商品种类类
     * @return 操作影响行数
     */
    int updateByPrimaryKeySelective(@Param("record") Category record);

    /**
     * 通过主键更新
     * @param record 商品种类类
     * @return 操作影响行数
     */
    int updateByPrimaryKey(@Param("record") Category record);

    /**
     * 通过父id查询商品子类
     * @param parentId 父id
     * @return 分类列表
     */
    List<Category> selectCategoryChildrenByParentId(Integer parentId);
}