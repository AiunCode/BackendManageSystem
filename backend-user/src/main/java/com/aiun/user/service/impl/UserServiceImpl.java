package com.aiun.user.service.impl;

import com.aiun.common.Const;
import com.aiun.common.ServerResponse;
import com.aiun.common.util.JsonUtil;
import com.aiun.common.util.MD5Util;
import com.aiun.user.mapper.UserMapper;
import com.aiun.user.pojo.User;
import com.aiun.user.service.IUserService;
import com.alibaba.druid.support.json.JSONUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 用户模块实现类
 * @author lenovo
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public ServerResponse<User> login(String userName, String password) {
        int resultCount = userMapper.checkUsername(userName);
        if (resultCount <= 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        //密码登录并MD5加密
        String md5password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(userName, md5password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        String key = userName;
        String token = JsonUtil.object2JsonStr(user);
        ValueOperations<String, String>valueOperations = redisTemplate.opsForValue();
        //用户登录成功后，将该用户对象作为token值保存到redis里，并且设置过期时间为1小时
        valueOperations.set(key, token, 1, TimeUnit.HOURS);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        //指定用户角色
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if(resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }

        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if(StringUtils.isNotBlank(type)) {
            // 校验用户名
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            // 校验邮箱
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccess("校验成功");
    }

    @Override
    public ServerResponse<User> getInformation(int userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null) {
            return ServerResponse.createByErrorMessage("当前用户不存在");
        }
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse<String> resetPassword(String oldPassword, String newPassword, User user) {
        //防止横向越权，要校验一下这个用户的旧密码，一定要指定是这个用户
        //因为会查询一个count(1)，若不指定id，结果就是true
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword), user.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }

        return ServerResponse.createByErrorMessage("密码更新失败");
    }
}
