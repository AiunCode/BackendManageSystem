package com.aiun.shipping.controller;

import com.aiun.common.constant.UserConst;
import com.aiun.common.ResponseCode;
import com.aiun.common.ServerResponse;
import com.aiun.common.util.JsonUtils;
import com.aiun.shipping.pojo.Shipping;
import com.aiun.shipping.service.IShippingService;
import com.aiun.user.pojo.User;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 收货地址
 * @author lenovo
 */
@Api(tags = "收获地址相关接口")
@RestController
@RequestMapping("/shipping/")
public class ShippingController {
    @Autowired
    private IShippingService iShippingService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 增加收货地址
     * @param request 请求
     * @param shipping 地址信息
     * @return 返回结果
     */
    @PostMapping("add")
    @ApiOperation(value = "增加收货地址")
    public ServerResponse add(HttpServletRequest request, Shipping shipping) {
        ServerResponse hasLogin = loginHasExpired(request);
        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            return iShippingService.add(user.getId(), shipping);
        }
        return hasLogin;
    }

    /**
     * 删除收货地址
     * @param request 请求
     * @param shippingId 地址 id
     * @return 返回结果
     */
    @DeleteMapping("del")
    @ApiOperation(value = "删除收货地址")
    public ServerResponse del(HttpServletRequest request, Integer shippingId) {
        ServerResponse hasLogin = loginHasExpired(request);
        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            return iShippingService.del(user.getId(), shippingId);
        }
        return hasLogin;
    }

    /**
     * 更新收货地址
     * @param request 请求
     * @param shipping 地址信息
     * @return 返回结果
     */
    @PostMapping("update")
    @ApiOperation(value = "更新收货地址")
    public ServerResponse update(HttpServletRequest request, Shipping shipping) {
        ServerResponse hasLogin = loginHasExpired(request);
        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            return iShippingService.update(user.getId(), shipping);
        }
        return hasLogin;
    }

    /**
     * 查询收货地址
     * @param request 请求
     * @param shippingId 地址 id
     * @return 地址信息
     */
    @PostMapping("select")
    @ApiOperation(value = "查询收货地址")
    public ServerResponse<Shipping> select(HttpServletRequest request, Integer shippingId) {
        ServerResponse hasLogin = loginHasExpired(request);
        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            return iShippingService.select(user.getId(), shippingId);
        }
        return hasLogin;
    }

    /**
     * 查询某用户的所有收货地址，并分页
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @param request 请求
     * @return
     */
    @PostMapping("list")
    @ApiOperation(value = "查询某用户的所有收货地址，并分页")
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         HttpServletRequest request) {
        ServerResponse hasLogin = loginHasExpired(request);
        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            return iShippingService.list(user.getId(), pageNum, pageSize);
        }
        return hasLogin;
    }

    /**
     * 对外提供的服务
     * 根据ID查询Shipping数据
     * @param id 产品 id
     * @return 产品信息类
     */
    @GetMapping("{id}")
    public Shipping findById(@PathVariable(name="id") Integer id) {
        return iShippingService.findById(id);
    }

    /**
     * 判断用户登录是否过期
     * @param request 请求
     * @return 结果
     */
    private ServerResponse<User> loginHasExpired(HttpServletRequest request) {
        String key = request.getHeader(UserConst.AUTHORITY);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String value = valueOperations.get(key);
        if (StringUtils.isEmpty(value)) {
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        User user = JsonUtils.jsonStr2Object(value, User.class);
        if (!key.equals(user.getUsername())) {
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        valueOperations.set(key, value, 1, TimeUnit.HOURS);
        return ServerResponse.createBySuccess(user);
    }
}
