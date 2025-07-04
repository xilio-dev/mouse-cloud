package cn.xilio.mouse.cloud;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(contextId = "orderClient", value = "mouse-order")
public interface OrderClient {
    @GetMapping("mouse-order/order/get")
    public String getOrderId();
}
