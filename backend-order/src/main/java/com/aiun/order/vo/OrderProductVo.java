package com.aiun.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车里选中的商品信息
 * @author lenovo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "购物车里选中的商品信息")
public class OrderProductVo {
    @ApiModelProperty(value = "订单详情列表信息")
    private List<OrderItemVo> orderItemVoList;
    @ApiModelProperty(value = "产品总价")
    private BigDecimal productTotalPrice;
    @ApiModelProperty(value = "主图地址")
    private String imageHost;
}
