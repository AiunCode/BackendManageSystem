package com.aiun.product.vo;

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
@ApiModel(value = "产品列表展示信息")
public class ProductListVO {
    @ApiModelProperty(value = "主键")
    private Integer id;
    @ApiModelProperty(value = "产品分类 id")
    private Integer categoryId;
    @ApiModelProperty(value = "产品名")
    private String name;
    @ApiModelProperty(value = "产品标题")
    private String subtitle;
    @ApiModelProperty(value = "产品主图")
    private String mainImage;
    @ApiModelProperty(value = "产品价格")
    private BigDecimal price;
    @ApiModelProperty(value = "产品状态")
    private Integer status;
    @ApiModelProperty(value = "产品主图地址")
    private String imageHost;
}
