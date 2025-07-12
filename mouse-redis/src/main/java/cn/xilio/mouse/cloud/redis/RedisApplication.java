package cn.xilio.mouse.cloud.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
public class RedisApplication implements CommandLineRunner {
    @Autowired
    private StringRedisTemplate redisTemplate;
    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        redisTemplate.opsForValue().set("name", "mouse");
        redisTemplate.opsForValue().set("fa", "away");
        redisTemplate.opsForValue().set("sily", "xilio");
        String name = redisTemplate.opsForValue().get("name");
        String fa = redisTemplate.opsForValue().get("fa");
        String sily = redisTemplate.opsForValue().get("sily");
        System.out.println(name);
        System.out.println(fa);
        System.out.println(sily);
    }
}
