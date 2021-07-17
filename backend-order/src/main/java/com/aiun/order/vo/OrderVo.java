package com.aiun.order.vo;

import com.aiun.shipping.vo.ShippingVo;
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
 * 展示给前端的数据
 * @author lenovo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "订单展示信息")
public class OrderVo {
    @ApiModelProperty(value = "订单号")
    private Long orderNo;
    @ApiModelProperty(value = "支付金额")
    private BigDecimal payment;
    @ApiModelProperty(value = "支付类型")
    private Integer paymentType;
    @ApiModelProperty(value = "支付类型描述")
    private String paymentTypeDesc;
    @ApiModelProperty(value = "商品库存")
    private Integer postage;
    @ApiModelProperty(value = "商品状态")
    private Integer status;
    @ApiModelProperty(value = "商品状态描述")
    private String statusDesc;
    @ApiModelProperty(value = "支付时间")
    private String paymentTime;
    @ApiModelProperty(value = "发货时间")
    private String sendTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "交易关闭时间")
    private String closeTime;
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    //订单明细
    @ApiModelProperty(value = "订单详情列表")
    private List<OrderItemVo> orderItemVoList;
    @ApiModelProperty(value = "主图地址")
    private String imageHost;
    @ApiModelProperty(value = "地址 id")
    private Integer shippingId;
    @ApiModelProperty(value = "收货人姓名")
    private String receiverName;
    @ApiModelProperty(value = "地址展示信息")
    private ShippingVo shippingVo;

}
