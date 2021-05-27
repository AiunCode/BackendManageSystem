package com.aiun.product.feign;

import com.aiun.product.pojo.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value="backend-product")
@RequestMapping("/product/")
public interface ProductFeign {
    @GetMapping("{id}")
    Product findById(@PathVariable(name="id") Integer id);

    @PostMapping("update")
    int updateBySelective(@RequestBody Product product);
}
