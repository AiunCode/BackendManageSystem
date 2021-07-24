package com.aiun.shipping.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author lenovo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "收货地址展示信息")
public class ShippingVO {
    @ApiModelProperty(value = "收货人")
    private String receiverName;
    @ApiModelProperty(value = "收货人固定电话")
    private String receiverPhone;
    @ApiModelProperty(value = "收货人移动电话")
    private String receiverMobile;
    @ApiModelProperty(value = "收货人省份")
    private String receiverProvince;
    @ApiModelProperty(value = "收货人城市")
    private String receiverCity;
    @ApiModelProperty(value = "收货人详细地址")
    private String receiverDistrict;
    @ApiModelProperty(value = "收货人地址")
    private String receiverAddress;
    @ApiModelProperty(value = "收货人邮箱")
    private String receiverZip;
}
