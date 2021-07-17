package com.aiun.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 结合了产品和购物车的抽象对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode
@ApiModel(value = "产品和购物车的抽象对象")
public class CartProductVo {
    @ApiModelProperty(value = "主键")
    private Integer id;
    @ApiModelProperty(value = "用户 id")
    private Integer userId;
    @ApiModelProperty(value = "产品 id")
    private Integer productId;
    /**
     * 购物车中产品的数量
     */
    @ApiModelProperty(value = "购物车中产品的数量")
    private Integer quantity;
    @ApiModelProperty(value = "产品名")
    private String productName;
    @ApiModelProperty(value = "产品标题")
    private String productSubtitle;
    @ApiModelProperty(value = "产品主图")
    private String productMainImage;
    @ApiModelProperty(value = "产品价格")
    private BigDecimal productPrice;
    @ApiModelProperty(value = "产品状态")
    private Integer productStatus;
    @ApiModelProperty(value = "产品总价")
    private BigDecimal productTotalPrice;
    /**
     * 产品库存
     */
    @ApiModelProperty(value = "产品库存")
    private Integer productStock;
    /**
     * 此商品是否勾选
     */
    @ApiModelProperty(value = "商品是否勾选")
    private Integer productChecked;
    /**
     * 限制数量的一个返回结果
     */
    @ApiModelProperty(value = "限制数量的一个返回结果")
    private String limitQuantity;
}
