package com.aiun.product.feign;

import com.aiun.product.pojo.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value="backend-product")
@RequestMapping("/product/")
public interface ProductFeign {
    @GetMapping("{id}")
    Product findById(@PathVariable(name="id") Integer id);

    @RequestMapping(value = "update",method = RequestMethod.GET)
    void updateStockById(@RequestParam("id") Integer id, @RequestParam("stock") int stock);
}
