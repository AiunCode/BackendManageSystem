package com.aiun.product.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "产品详情展示信息")
public class ProductDetailVo {
    @ApiModelProperty(value = "主键")
    private Integer id;
    @ApiModelProperty(value = "分类 id")
    private Integer categoryId;
    @ApiModelProperty(value = "商品名")
    private String name;
    @ApiModelProperty(value = "商品标题")
    private String subtitle;
    @ApiModelProperty(value = "主图")
    private String mainImage;
    @ApiModelProperty(value = "副图")
    private String SubImages;
    @ApiModelProperty(value = "产品详情")
    private String detail;
    @ApiModelProperty(value = "产品价格")
    private BigDecimal price;
    @ApiModelProperty(value = "产品库存")
    private Integer stock;
    @ApiModelProperty(value = "产品状态")
    private Integer status;
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    @ApiModelProperty(value = "更新时间")
    private String updateTime;
    @ApiModelProperty(value = "主图地址")
    private String imageHost;
    @ApiModelProperty(value = "父分类 id")
    private Integer parentCategoryId;
}
