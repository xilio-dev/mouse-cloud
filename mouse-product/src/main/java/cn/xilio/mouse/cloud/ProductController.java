package cn.xilio.mouse.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product")
public class ProductController {
    @Autowired
    private OrderClient orderClient;
    @GetMapping("info")
    public String info(){
        String orderId = orderClient.getOrderId();
        return "productInfo:orderId="+orderId;
    }
    @GetMapping("get")
    public String getProductName(){
        return "mouse";
    }
}
