package cn.xilio.mouse.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("order")
@RestController
public class OrderController {
    @Autowired
    private ProductClient productClient;
    @Autowired
    private OrderService orderService;
    @GetMapping("info")
    public String info(){
        String name = productClient.getProductName();
        int a=1/0;
        return "orderInfo:productName="+name;
    }
    @GetMapping("get")
    public String getOrderId(){
        return "order1001";
    }
    @GetMapping("create")
    public void create(String userId, String commodityCode, int orderCount){
        orderService.create(userId, commodityCode, orderCount);
    }
}
