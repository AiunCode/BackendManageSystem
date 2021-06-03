package com.aiun.shipping.feign;

import com.aiun.shipping.pojo.Shipping;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 绑定 shippingController
 * @author lenovo
 */
@FeignClient(value = "backend-shipping")
@RequestMapping("/shipping/")
public interface ShippingFeign {
    @GetMapping("{id}")
    Shipping findById(@PathVariable(name="id") Integer id);
}
