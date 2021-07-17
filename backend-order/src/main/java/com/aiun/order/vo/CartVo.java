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
 * @author aiun
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode
@ApiModel(value = "购物车前端展示信息类")
public class CartVo {
    @ApiModelProperty(value = "购物车产品信息列表")
    private List<CartProductVo> cartProductVoList;
    @ApiModelProperty(value = "购物车产品总价")
    private BigDecimal cartTotalPrice;
    /**
     * 是否全部勾选
     */
    @ApiModelProperty(value = "全部勾选")
    private boolean allChecked;
    @ApiModelProperty(value = "产品主图")
    private String imageHost;
}
