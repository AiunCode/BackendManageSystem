package com.aiun.product.service.impl;

import com.aiun.common.constant.ProductStatusEnum;
import com.aiun.common.constant.UserConst;
import com.aiun.common.ResponseCode;
import com.aiun.common.ServerResponse;
import com.aiun.common.util.DateUtils;
import com.aiun.product.mapper.CategoryMapper;
import com.aiun.product.mapper.ProductMapper;
import com.aiun.product.pojo.Category;
import com.aiun.product.pojo.Product;
import com.aiun.product.service.ICategoryService;
import com.aiun.product.service.IProductService;
import com.aiun.product.vo.ProductDetailVO;
import com.aiun.product.vo.ProductListVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lenovo
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Value("mageHost")
    private String mageHost;
    @Autowired
    private ICategoryService iCategoryService;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if(product != null) {
            // 更新产品
            if (product.getId() != null) {
                int resultCount = productMapper.updateByPrimaryKey(product);
                if (resultCount > 0) {
                    return ServerResponse.createBySuccessMessage("更新产品成功!");
                }
                return ServerResponse.createByErrorMessage("更新产品失败!");
            }else{
                int count = productMapper.insert(product);
                if (count > 0) {
                    return ServerResponse.createBySuccessMessage("保存产品成功");
                }
                return ServerResponse.createByErrorMessage("更新产品失败!");
            }
        }

        return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "保持或更新产品参数不正确");
    }

    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectList();

        return getPageInfoServerResponse(productList);
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        //根据产品名进行模糊查询
        List<Product> productList = productMapper.selectByNameAndProductId(productName, productId);

        return getPageInfoServerResponse(productList);
    }

    @Override
    public ServerResponse<ProductDetailVO> getProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数不正确");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if ((product == null) || (product.getStatus() != ProductStatusEnum.ON_SALE.getCode())) {
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }

        //VO对象 value object
        ProductDetailVO productDetailVo = assembleProductDetailVo(product);

        return ServerResponse.createBySuccess(productDetailVo);
    }

    @Override
    public ServerResponse<PageInfo> getProductBykeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = new ArrayList<>();
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                //没有该分类，并且还没有关键字，返回一个空的结果集
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVO> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList = iCategoryService.selectCategoryAndChildrenById(categoryId).getData();
        }

        if (StringUtils.isNotBlank(keyword)) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum, pageSize);
        //排序处理
        if (StringUtils.isNotBlank(orderBy)) {
            PageHelper.orderBy(orderBy);
        }
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword) ? null : keyword, categoryIdList.size() == 0 ? null : categoryIdList);

        return getPageInfoServerResponse(productList);
    }

    @Override
    public Product findById(Integer id) {
        return productMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateStockById(Integer id, int stock) {
        Product product = productMapper.selectByPrimaryKey(id);
        product.setStock(stock);
        productMapper.updateByPrimaryKeySelective(product);
    }

    /**
     * 将产品列表进行分页操作
     * @param productList
     * @return
     */
    private ServerResponse<PageInfo> getPageInfoServerResponse(List<Product> productList) {
        List<ProductListVO> productListVoList = Lists.newArrayList();

        productList.forEach(e->{
            ProductListVO productListVo = assembleProductListVo(e);
            productListVoList.add(productListVo);
        });

        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);

        return ServerResponse.createBySuccess(pageResult);
    }

    /**
     * 根据产品信息给前端封装产品列表数据
     * @param product
     * @return
     */
    private ProductListVO assembleProductListVo(Product product) {
        ProductListVO productListVo = new ProductListVO();
        productListVo.setId(product.getId())
            .setCategoryId(product.getCategoryId())
            .setName(product.getName())
            .setMainImage(product.getMainImage())
            .setPrice(product.getPrice())
            .setSubtitle(product.getSubtitle())
            .setImageHost(mageHost)
            .setStatus(product.getStatus());

        return productListVo;
    }

    /**
     * productDetailVo对象的数据封装
     * @param product
     * @return
     */
    private ProductDetailVO assembleProductDetailVo(Product product) {
        ProductDetailVO productDetailVo = new ProductDetailVO();
        productDetailVo.setId(product.getId())
            .setPrice(product.getPrice())
            .setSubtitle(product.getSubtitle())
            .setMainImage(product.getMainImage())
            .setSubImages(product.getSubImages())
            .setCategoryId(product.getCategoryId())
            .setDetail(product.getDetail())
            .setName(product.getName())
            .setStatus(product.getStatus())
            .setStock(product.getStock());

        productDetailVo.setImageHost(mageHost);

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            //默认是根节点
            productDetailVo.setParentCategoryId(0);
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateUtils.dateToString(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateUtils.dateToString(product.getUpdateTime()));

        return productDetailVo;
    }
}
