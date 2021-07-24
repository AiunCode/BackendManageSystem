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
 * @author lenovo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "订单详情前端展示信息类")
public class OrderItemVO {
    @ApiModelProperty(value = "订单号")
    private Long orderNo;
    @ApiModelProperty(value = "产品 id")
    private Integer productId;
    @ApiModelProperty(value = "产品名")
    private String productName;
    @ApiModelProperty(value = "产品图")
    private String productImage;
    @ApiModelProperty(value = "当前产品的单价")
    private BigDecimal currentUnitPrice;
    @ApiModelProperty(value = "数量")
    private Integer quantity;
    @ApiModelProperty(value = "产品总价")
    private BigDecimal totalPrice;
    @ApiModelProperty(value = "创建时间")
    private String createTime;
}
