package cn.xilio.mouse.cloud.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class KafkaApplication implements CommandLineRunner {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    public static void main(String[] args) {
        SpringApplication.run(KafkaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 10; i++) {
            kafkaTemplate.send("test", "hello world"+i);
        }

    }
    @Component
    class KafkaConsumer{
        @KafkaListener(topics = "test",groupId = "my-group")
        public void receive(String message, Acknowledgment ack){
            System.out.println("receive message:"+message);
           // ack.acknowledge();
        }
    }
}
