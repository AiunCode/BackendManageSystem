package com.aiun.order.service.impl;

import com.aiun.common.constant.UserConst;
import com.aiun.common.ResponseCode;
import com.aiun.common.ServerResponse;
import com.aiun.common.util.BigDecimalUtils;
import com.aiun.order.mapper.CartMapper;
import com.aiun.order.pojo.Cart;
import com.aiun.order.service.ICartService;
import com.aiun.order.vo.CartProductVO;
import com.aiun.order.vo.CartVO;
import com.aiun.product.feign.ProductFeign;
import com.aiun.product.pojo.Product;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lenovo
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductFeign productFeign;
    @Value("mageHost")
    private String mageHost;

    @Override
    public ServerResponse<CartVO> add(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart == null) {
            //产品不在购物车里，需要新增一个这个产品的记录
            Cart cartItem = new Cart();
            cartItem.setQuantity(count)
                    .setChecked(UserConst.Cart.CHECKED)
                    .setProductId(productId)
                    .setUserId(userId);
            int resultCount = cartMapper.insert(cartItem);
            if (resultCount == 0){
                return ServerResponse.createByErrorMessage("添加购物车失败");
            }
        } else {
            //这个产品已经在购物车里了
            //需要将数量相加
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVO> update(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
        }
        int resoultCount = cartMapper.updateByPrimaryKeySelective(cart);
        if (resoultCount == 0) {
            return ServerResponse.createByErrorMessage("更新购物车失败");
        }
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVO> deleteProduct(Integer userId, String productIds) {
        List<String> productList = new ArrayList<>();
        String[] productIdStrings = productIds.split(",");
        for(String id : productIdStrings) {
            productList.add(id);
        }
        if (productList.isEmpty()) {
            return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdProductIds(userId, productList);
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVO> list(Integer userId) {
        CartVO cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    @Override
    public ServerResponse<CartVO> selectOrUnSelect(Integer userId, Integer productId, Integer checked) {
        cartMapper.checkedOrUnCheckedProduct(userId, productId, checked);
        return this.list(userId);
    }

    @Override
    public ServerResponse<Integer> getCartProductCount(Integer userId) {
        if (userId == null) {
            return ServerResponse.createBySuccess(0);
        }

        return ServerResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
    }

    /**
     * 封装前端用到的购物车数据
     * @param userId 用户 id
     * @return 封装的购物车 VO
     */
    private CartVO getCartVoLimit(Integer userId) {
        CartVO cartVo = new CartVO();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVO> cartProductVOList = Lists.newArrayList();

        BigDecimal cartTotalPrice = new BigDecimal("0");
        if (!cartList.isEmpty()) {
            for (Cart cartItem : cartList) {
                CartProductVO cartProductVo = new CartProductVO();
                cartProductVo.setId(cartItem.getId())
                             .setProductId(cartItem.getProductId())
                             .setUserId(cartItem.getUserId());

                Product product = productFeign.findById(cartItem.getProductId());
                if (product != null) {
                    cartProductVo.setProductMainImage(product.getMainImage())
                                 .setProductName(product.getName())
                                 .setProductPrice(product.getPrice())
                                 .setProductStatus(product.getStatus())
                                 .setProductSubtitle(product.getSubtitle())
                                 .setProductStock(product.getStock());
                    //判断库存
                    int buyLimitCount = 0;
                    if (product.getStock() >= cartItem.getQuantity()) {
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(UserConst.Cart.LIMIT_NUM_SUCCESS);
                    } else {
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(UserConst.Cart.LIMIT_NUM_FAIL);
                        //购物车中更新有效库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId())
                                       .setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    //计算总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()))
                                 .setProductChecked(cartItem.getChecked());
                }
                if (cartItem.getChecked() == UserConst.Cart.CHECKED) {
                    //如果已经勾选，增加到整个购物车的总价中
                    cartTotalPrice = BigDecimalUtils.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());

                }
                cartProductVOList.add(cartProductVo);
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice)
              .setCartProductVOList(cartProductVOList)
              .setAllChecked(this.getAllCheckedStatus(userId))
              .setImageHost(mageHost);

        return cartVo;
    }

    /**
     * 查询是否是全选中状态
     * @param userId 用户 id
     * @return 产品是否是选中状态
     */
    private boolean getAllCheckedStatus(Integer userId) {
        if (userId == null) {
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
    }
}
