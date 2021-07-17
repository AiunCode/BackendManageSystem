package com.aiun.order.controller;

import com.aiun.common.Const;
import com.aiun.common.ResponseCode;
import com.aiun.common.ServerResponse;
import com.aiun.common.util.JsonUtil;
import com.aiun.order.service.ICartService;
import com.aiun.order.vo.CartVo;
import com.aiun.user.pojo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 购物车控制层
 * @author lenovo
 */
@Api(tags = "购物车接口")
@RestController
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    private ICartService iCartService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 添加购物车功能
     * @param productId 产品id
     * @param count 产品个数
     * @return 封装好的购物车 VO
     */
    @RequestMapping("add")
    @ApiOperation(value = "购物车添加商品功能")
    public ServerResponse<CartVo> add(HttpServletRequest request, Integer productId, Integer count) {
        ServerResponse hasLogin = loginHasExpired(request);

        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            return iCartService.add(user.getId(), productId, count);
        }

        return hasLogin;
    }
    /**
     * 更新购物车商品功能
     * @param productId 产品 id
     * @param count 产品个数
     * @return 封装好的购物车 VO
     */
    @RequestMapping("update")
    @ApiOperation(value = "更新购物车商品功能")
    public ServerResponse<CartVo> update(HttpServletRequest request, Integer productId, Integer count) {
        ServerResponse hasLogin = loginHasExpired(request);

        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            return iCartService.update(user.getId(), productId, count);
        }

        return hasLogin;
    }

    /**
     * 删除购物车商品
     * @param productIds 多个产品的id
     * @return 封装好的购物车 VO
     */
    @RequestMapping("delete_product")
    @ApiOperation(value = "删除购物车商品")
    public ServerResponse<CartVo> deleteProduct(HttpServletRequest request, String productIds) {
        ServerResponse hasLogin = loginHasExpired(request);

        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            return iCartService.deleteProduct(user.getId(), productIds);
        }

        return hasLogin;
    }

    /**
     * 查询购物车商品列表
     * @return 封装好的购物车 VO
     */
    @RequestMapping("list")
    @ApiOperation(value = "查询购物车商品列表")
    public ServerResponse<CartVo> list(HttpServletRequest request) {
        ServerResponse hasLogin = loginHasExpired(request);

        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            return iCartService.list(user.getId());
        }

        return hasLogin;
    }

    /**
     * 全选
     * @return 封装好的购物车 VO
     */
    @RequestMapping("select_all")
    @ApiOperation(value = "购物车全选")
    public ServerResponse<CartVo> selectAll(HttpServletRequest request) {
        ServerResponse hasLogin = loginHasExpired(request);

        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            return iCartService.selectOrUnSelect(user.getId(), null, Const.Cart.CHECKED);
        }
        return hasLogin;
    }

    /**
     * 反选
     * @return 封装好的购物车 VO
     */
    @RequestMapping("un_select_all")
    @ApiOperation(value = "反选")
    public ServerResponse<CartVo> unSelectAll(HttpServletRequest request) {
        ServerResponse hasLogin = loginHasExpired(request);

        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            return iCartService.selectOrUnSelect(user.getId(), null, Const.Cart.UN_CHECKED);
        }
        return hasLogin;
    }

    /**
     * 单独选
     * @return 封装好的购物车 VO
     */
    @RequestMapping("select")
    @ApiOperation(value = "单独选")
    public ServerResponse<CartVo> select(HttpServletRequest request, Integer productId) {
        ServerResponse hasLogin = loginHasExpired(request);

        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            return iCartService.selectOrUnSelect(user.getId(), productId, Const.Cart.CHECKED);
        }
        return hasLogin;
    }

    /**
     * 单独反选
     * @return 封装好的购物车 VO
     */
    @RequestMapping("un_select")
    @ResponseBody
    @ApiOperation(value = "单独反选")
    public ServerResponse<CartVo> unSelect(HttpServletRequest request, Integer productId) {
        ServerResponse hasLogin = loginHasExpired(request);

        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            return iCartService.selectOrUnSelect(user.getId(), productId, Const.Cart.UN_CHECKED);
        }
        return hasLogin;
    }

    /**
     * 查询当前用户购物车里产品的数量
     * @return 返回产品的数量
     */
    @RequestMapping("get_cart_product_count")
    @ResponseBody
    @ApiOperation(value = "查询当前用户购物车里产品的数量")
    public ServerResponse<Integer> getCartProductCount(HttpServletRequest request) {
        ServerResponse hasLogin = loginHasExpired(request);

        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            return iCartService.getCartProductCount(user.getId());
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
