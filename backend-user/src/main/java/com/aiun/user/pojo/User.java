package com.aiun.user.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "user对象", description = "用户表")
public class User implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade_user.id
     */
    @ApiModelProperty(value = "用户表 主键")
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade_user.username
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade_user.password
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade_user.email
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade_user.phone
     */
    @ApiModelProperty(value = "电话")
    private String phone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade_user.question
     */
    @ApiModelProperty(value = "问题")
    private String question;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade_user.answer
     */
    @ApiModelProperty(value = "答案")
    private String answer;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade_user.role
     */
    @ApiModelProperty(value = "用户角色")
    private Integer role;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade_user.create_time
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade_user.update_time
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table trade_user
     */
    private static final long serialVersionUID = 1L;
}