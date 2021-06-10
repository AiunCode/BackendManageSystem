package com.aiun.order.controller;

import com.aiun.common.Const;
import com.aiun.common.ResponseCode;
import com.aiun.common.ServerResponse;
import com.aiun.common.util.JsonUtil;
import com.aiun.order.service.IOrderService;
import com.aiun.user.pojo.User;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

/**
 * 订单控制层
 * @author lenovo
 */
@RestController
@FeignClient
@RequestMapping("/order/")
public class OrderController {
    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 创建订单
     * @param request 请求
     * @param shippingId 收货地址id
     * @return 结果
     */
    @RequestMapping("create")
    public ServerResponse create(HttpServletRequest request, Integer shippingId) {
        ServerResponse hasLogin = loginHasExpired(request);
        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            return iOrderService.createOrder(user.getId(), shippingId);
        }
        return hasLogin;
    }

    /**
     * 取消订单
     * @param request 请求
     * @param orderNo 订单号
     * @return 返回结果
     */
    @RequestMapping("cancel")
    public ServerResponse cancel(HttpServletRequest request, Long orderNo) {
        ServerResponse hasLogin = loginHasExpired(request);
        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            return iOrderService.cancel(user.getId(), orderNo);
        }
        return hasLogin;
    }
    /**
     * 获取购物车中已经选中的商品信息
     * @param request 请求
     * @return 返回结果
     */
    @RequestMapping("get_order_cart_product")
    public ServerResponse getOrderCartProduct(HttpServletRequest request) {
        ServerResponse hasLogin = loginHasExpired(request);
        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            return iOrderService.getOrderCartProduct(user.getId());
        }
        return hasLogin;
    }

    /**
     * 获取订单详细信息
     * @param request 请求
     * @param orderNo 订单号
     * @return 订单详情
     */
    @RequestMapping("detail")
    public ServerResponse detail(HttpServletRequest request, Long orderNo) {
        ServerResponse hasLogin = loginHasExpired(request);
        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                return iOrderService.getOrderDetail(null, orderNo);
            } else {
                return iOrderService.getOrderDetail(user.getId(), orderNo);
            }
        }
        return hasLogin;
    }

    /**
     * 获取订单列表信息
     * @param request 请求
     * @param pageNum 分页的当前页
     * @param pageSize 页的大小
     * @return 返回订单列表信息
     */
    @RequestMapping("list")
    public ServerResponse list(HttpServletRequest request, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ServerResponse hasLogin = loginHasExpired(request);
        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                return iOrderService.getOrderList(null, pageNum, pageSize);
            } else {
                return iOrderService.getOrderList(user.getId(), pageNum, pageSize);
            }
        }
        return hasLogin;
    }
    /**
     * 订单查询
     * @param request 请求
     * @param orderNo 订单号
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return 返回结果
     */
    @RequestMapping("manage/search")
    public ServerResponse<PageInfo> orderSearch(HttpServletRequest request, Long orderNo, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ServerResponse hasLogin = loginHasExpired(request);
        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                return iOrderService.manageSearch(orderNo, pageNum, pageSize);
            } else {
                return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
            }
        }
        return hasLogin;
    }

    /**
     * 发货
     * @param request 请求
     * @param orderNo 订单号
     * @return 发货信息
     */
    @RequestMapping("send_goods")
    public ServerResponse<String> orderSendGoods(HttpServletRequest request, Long orderNo) {
        ServerResponse hasLogin = loginHasExpired(request);
        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                return iOrderService.manageSendGoods(orderNo);
            } else {
                return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
            }
        }
        return hasLogin;
    }
    /**
     * 判断用户登录是否过期
     */
    private ServerResponse<User> loginHasExpired(HttpServletRequest request) {
        String key = request.getHeader(Const.AUTHORITY);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String value = valueOperations.get(key);
        if (StringUtils.isEmpty(value)) {
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        User user = JsonUtil.jsonStr2Object(value, User.class);
        if (!key.equals(user.getUsername())) {
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        valueOperations.set(key, value, 1, TimeUnit.HOURS);
        return ServerResponse.createBySuccess(user);
    }
}
