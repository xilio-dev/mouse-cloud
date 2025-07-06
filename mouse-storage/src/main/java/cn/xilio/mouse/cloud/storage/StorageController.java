package cn.xilio.mouse.cloud.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/storage")
public class StorageController {
    @Autowired
    private StorageService storageService;

    @PutMapping("deduct")
    public void deduct(String commodityCode, int count) {
        storageService.deduct(commodityCode, count);
    }
}
