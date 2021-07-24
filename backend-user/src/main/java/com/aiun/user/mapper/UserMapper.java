package com.aiun.user.mapper;

import com.aiun.user.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    /**
     * 根据主键删除用户
     * @param id 主键
     * @return 影响的记录行数
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入用户
     * @param record 用户信息
     * @return 影响的记录行数
     */
    int insert(@Param("record") User record);

    /**
     * 有选择的插入用户
     * @param record 用户信息
     * @return 影响的记录行数
     */
    int insertSelective(@Param("record") User record);

    /**
     * 通过主键查询用户
     * @param id 主键
     * @return 用户信息
     */
    User selectByPrimaryKey(Integer id);

    /**
     * 通过主键有选择的更新用户
     * @param record 用户信息
     * @return 影响的记录行数
     */
    int updateByPrimaryKeySelective(@Param("record") User record);

    /**
     * 通过主键更新用户
     * @param record 用户信息
     * @return 影响的记录行数
     */
    int updateByPrimaryKey(@Param("record") User record);

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return 返回用户信息
     */
    User selectLogin(@Param("username") String username, @Param("password") String password);

    /**
     * 检查用户名
     * @param username 用户名
     * @return 影响的记录行数
     */
    int checkUsername(String username);

    /**
     * 检查email
     * @param email 邮箱
     * @return 影响的记录行数
     */
    int checkEmail(String email);

    /**
     * 检查密码
     * @param password
     * @param userId
     * @return
     */
    int checkPassword(@Param("password") String password, @Param("userId") Integer userId);
}