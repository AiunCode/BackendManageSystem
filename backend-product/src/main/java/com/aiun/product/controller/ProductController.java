package com.aiun.product.controller;

import com.aiun.common.Const;
import com.aiun.common.ResponseCode;
import com.aiun.common.ServerResponse;
import com.aiun.common.util.JsonUtil;
import com.aiun.product.pojo.Product;
import com.aiun.product.service.IFileService;
import com.aiun.product.service.IProductService;
import com.aiun.product.vo.ProductDetailVo;
import com.aiun.user.feign.UserFeign;
import com.aiun.user.pojo.User;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 产品控制层
 * @author lenovo
 */
@RestController
@RequestMapping("/product/")
public class ProductController {
    @Value("${image.localhost}")
    private String mageHost;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 保存或更新产品
     * @param product 产品信息
     * @return 操作结果
     */
    @RequestMapping("manage/save")
    public ServerResponse productSave(HttpServletRequest request, Product product) {
        ServerResponse hasLogin = loginHasExpired(request);
        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                return iProductService.saveOrUpdateProduct(product);
            } else {
                return ServerResponse.createByErrorMessage("无权限操作");
            }
        }
        return hasLogin;
    }
    /**
     * 文件上传
     * @param file 文件
     * @param request 请求
     * @return 返回给前端的信息
     */
    @RequestMapping("manage/upload")
    public ServerResponse upload(HttpServletRequest request, @RequestParam(value = "upload_file", required = false) MultipartFile file) {
        ServerResponse hasLogin = loginHasExpired(request);
        if (hasLogin.isSuccess()) {
            User user = (User) hasLogin.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                String targetFileName = iFileService.upload(file, mageHost);
                String url =  mageHost + targetFileName;

                Map fileMap = Maps.newHashMap();
                fileMap.put("uri", targetFileName);
                fileMap.put("url", url);

                return ServerResponse.createBySuccess(fileMap);
            } else {
                return ServerResponse.createByErrorMessage("无权限操作");
            }
        }
        return hasLogin;
    }

    /**
     * 获取产品详情
     * @param productId 产品 id
     * @return 返回的产品详情信息
     */
    @RequestMapping("detail")
    public ServerResponse<ProductDetailVo> detail(Integer productId) {
        return iProductService.getProductDetail(productId);
    }

    /**
     * 获取产品列表
     * @param keyword 关键字
     * @param categoryId 分类 id
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @param orderBy 排序规则
     * @return 返回具体信息
     */
    @RequestMapping("list")
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
        return iProductService.getProductBykeywordCategory(keyword, categoryId, pageNum, pageSize, orderBy);
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

    /**
     * 对外提供的服务
     * 根据ID查询Sku数据
     * @param id 产品 id
     * @return 产品信息类
     */
    @GetMapping("{id}")
    public Product findById(@PathVariable(name="id") Integer id){
        return iProductService.findById(id);
    }

    /**
     * 对外提供的服务
     * 更新产品库存数量信息
     * @param id 产品 id
     * @param stock 产品库存
     * @return 操作影响行数
     */
    @GetMapping("update")
    public void updateStockById(@RequestParam("id") Integer id, @RequestParam("stock") int stock){
        iProductService.updateStockById(id, stock);
    }
}
