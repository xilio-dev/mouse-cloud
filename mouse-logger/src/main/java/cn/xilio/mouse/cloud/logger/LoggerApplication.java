package cn.xilio.mouse.cloud.logger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoggerApplication {
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(LoggerApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(LoggerApplication.class, args);
        logger.debug("已经启动服务器{}",1);
    }
}
