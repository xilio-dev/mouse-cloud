package cn.xilio.mouse.cloud.activemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class ActiveMQApplication implements CommandLineRunner {
    @Autowired
    private JmsTemplate jmsTemplate;
    public static void main(String[] args) {
        SpringApplication.run(ActiveMQApplication.class, args);
    }
    @Override
    public void run(String... args) {
        //发送一条消息
        jmsTemplate.convertAndSend("test-queue", "Hello ActiveMQ");
    }
    //消费者
    @Component
    class MessageConsumer {
        @JmsListener(destination = "test-queue")
        public void receive(String message) {
            System.out.println("接收到的消息: " + message);
        }
    }
}
