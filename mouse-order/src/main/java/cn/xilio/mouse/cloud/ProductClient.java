package cn.xilio.mouse.cloud;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(contextId = "productClient",value = "mouse-product")
public interface ProductClient {
    @GetMapping("mouse-product/product/get")
    public String getProductName();
}
