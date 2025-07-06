package cn.xilio.mouse.cloud;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "accountClient",value = "mouse-account")
public interface AccountClient {

    @PutMapping("mouse-account/account/debit")
    public void debit(@RequestParam("userId") String userId, @RequestParam("money") int money);
}
