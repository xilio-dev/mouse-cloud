package cn.xilio.mouse.cloud.business;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "orderClient",value = "mouse-order")
public interface OrderClient {

    @GetMapping("mouse-order/order/create")
    public void create(@RequestParam("userId") String userId, @RequestParam("commodityCode") String commodityCode, @RequestParam("orderCount") int orderCount);
}
