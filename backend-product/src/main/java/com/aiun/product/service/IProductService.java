package com.aiun.product.service;

import com.aiun.common.ServerResponse;
import com.aiun.product.pojo.Product;
import com.aiun.product.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

/**
 * @author lenovo
 */
public interface IProductService {
    /**
     * 保存或更新产品
     * @param product
     * @return
     */
    ServerResponse saveOrUpdateProduct(Product product);

    /**
     * 产品列表（分页）
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    /**
     * 查询产品
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    /**
     * 前端获取产品详细信息
     * @param productId
     * @return
     */
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    /**
     * 通过商品种类的关键字获取产品
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getProductBykeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);

    /**
     * 根据id 查询产品
     * @param id
     * @return
     */
    Product findById(Integer id);

    int updateBySelective(Product product);
}
