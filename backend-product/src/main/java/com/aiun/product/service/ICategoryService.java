package com.aiun.product.service;

import com.aiun.common.ServerResponse;
import com.aiun.product.pojo.Category;

import java.util.List;

/**
 * 种类接口
 * @author lenovo
 */
public interface ICategoryService {
    /**
     * 增加商品种类
     * @param categoryName
     * @param parentId
     * @return
     */
    ServerResponse<String> addCategory(String categoryName, Integer parentId);

    /**
     * 更新商品种类名
     * @param categoryId
     * @param categoryName
     * @return
     */
    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    /**
     * 获取子节点以及平级节点
     * @param categoryId
     * @return
     */
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    /**
     * 通过id获取当前节点以及子节点
     * @param categoryId
     * @return
     */
    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);

}
