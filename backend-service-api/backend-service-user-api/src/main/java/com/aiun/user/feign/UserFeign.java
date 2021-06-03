package com.aiun.user.feign;

import com.aiun.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author lenovo
 */
@FeignClient("backend-user")
@RequestMapping("/user/")
public interface UserFeign {
    @PostMapping("search")
    int checkAdminRole(@RequestBody(required = false) User user);
}
