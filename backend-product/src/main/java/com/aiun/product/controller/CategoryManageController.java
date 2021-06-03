package com.aiun.product.controller;

import com.aiun.common.Const;
import com.aiun.common.ResponseCode;
import com.aiun.common.ServerResponse;
import com.aiun.common.util.JsonUtil;
import com.aiun.product.service.ICategoryService;
import com.aiun.user.feign.UserFeign;
import com.aiun.user.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 产品分类
 * @author lenovo
 */
@RestController
@RequestMapping("/category/manage/")
public class CategoryManageController {
    @Autowired
    private ICategoryService iCategoryService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 增加品类
     * @param request 请求
     * @param categoryName 品类名称
     * @param parentId 父 id
     * @return 返回结果
     */
    @RequestMapping("add_category")
    public ServerResponse addCategory(HttpServletRequest request, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        ServerResponse hasLogin = loginHasExpired(request);
        System.out.println("request : " + request);
        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            System.out.println("user : " + user);
            //检查是否是管理员
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                System.out.println("categoryName : " + categoryName);
                //是管理员，处理分类的逻辑
                return iCategoryService.addCategory(categoryName, parentId);
            } else {
                return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
            }
        }
        return hasLogin;
    }

    /**
     * 设置品类名称
     * @param request 请求
     * @param categoryId 品类 id
     * @param categoryName 品类名
     * @return 返回结果
     */
    @RequestMapping("set_category_name")
    public ServerResponse setCategoryName(HttpServletRequest request, Integer categoryId, String categoryName) {
        ServerResponse hasLogin = loginHasExpired(request);
        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            //检查是否是管理员
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                //是管理员，处理分类的逻辑
                return iCategoryService.updateCategoryName(categoryId, categoryName);
            } else {
                return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
            }
        }
        return hasLogin;
    }

    /**
     * 获取节点的并且是平级的品类
     * @param request 请求
     * @param categoryId  品类 id
     * @return 返回结果
     */
    @RequestMapping("get_category")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpServletRequest request, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        ServerResponse hasLogin = loginHasExpired(request);
        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            //检查是否是管理员
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                //查询子节点的品种信息，并且不递归，保持平级
                return iCategoryService.getChildrenParallelCategory(categoryId);
            } else {
                return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
            }
        }
        return hasLogin;
    }
    /**
     * 获取节点并且递归获取子节点的品类
     * @param request 请求
     * @param categoryId 品类 id
     * @return 返回结果
     */
    @RequestMapping("get_deep_category")
    @ResponseBody
    public ServerResponse getCategoryAddDeepChildrenCategory(HttpServletRequest request, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        ServerResponse hasLogin = loginHasExpired(request);
        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            //检查是否是管理员
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                //查询当前节点并且递归查询子节点
                return iCategoryService.selectCategoryAndChildrenById(categoryId);
            } else {
                return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
            }
        }
        return hasLogin;
    }

    /**
     * 判断用户登录是否过期
     * @param request 请求
     * @return 结果
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
