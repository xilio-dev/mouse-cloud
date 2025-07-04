package cn.xilio.mouse.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("order")
@RestController
public class OrderController {
    @Autowired
    private ProductClient productClient;
    @GetMapping("info")
    public String info(){
        String name = productClient.getProductName();
        return "orderInfo:productName="+name;
    }
    @GetMapping("get")
    public String getOrderId(){
        return "order1001";
    }
}
