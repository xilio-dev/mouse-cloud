package cn.xilio.mouse.cloud.business;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "storageClient",value = "mouse-storage")
public interface StorageClient {

    @PutMapping("mouse-storage/storage/deduct")
    public void deduct(@RequestParam("commodityCode") String commodityCode, @RequestParam("count") int count);
}
