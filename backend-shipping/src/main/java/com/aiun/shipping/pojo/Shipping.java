package com.aiun.shipping.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 收货地址实体类
 * @author lenovo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "Shipping对象", description = "收货地址表")
public class Shipping {
    @ApiModelProperty(value = "收货地址主键")
    private Integer id;
    @ApiModelProperty(value = "用户 id")
    private Integer userId;
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
    @JsonFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @JsonFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}