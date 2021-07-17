package com.aiun.product.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "Category对象", description = "收货地址表")
public class Category {
    @ApiModelProperty(value = "主键")
    private Integer id;
    @ApiModelProperty(value = "商品父 id")
    private Integer parentId;
    @ApiModelProperty(value = "品类名")
    private String name;
    @ApiModelProperty(value = "品类状态")
    private Boolean status;
    @ApiModelProperty(value = "排序规则")
    private Integer sortOrder;
    @JsonFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @JsonFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}