package cn.xilio.mouse.cloud.zookeeper;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ZookeeperApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ZookeeperApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
