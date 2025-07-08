package cn.xilio.mouse.cloud.elk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xilio
 */
@RequestMapping("logstash")
@RestController
public class LogstashController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogstashController.class);

    @GetMapping("info")
    public String info() {
        LOGGER.info("记录Info日志");
        return "logstash：info";
    }

    @GetMapping("debug")
    public String debug() {
        LOGGER.info("记录Debug日志");
        return "logstash：debug";
    }
}
