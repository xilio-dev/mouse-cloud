package cn.xilio.mouse.cloud.rocketmq.producer;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class RocketMQProducerApp implements CommandLineRunner {
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    public static void main(String[] args) {
        SpringApplication.run(RocketMQProducerApp.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
       //testSendCommonMessage();
        rocketMQTemplate.asyncSend("string-request-topic", "异步发送2", new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.printf("asyncSend1 to topic %s sendResult=%s %n", "string-request-topic", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.printf("asyncSend1 to topic %s sendResult=%s %n", "mouse-topic", throwable);
            }
        });
    }

    private void testSendCommonMessage() {
        SendResult sendResult = rocketMQTemplate.syncSend("mouse-topic", "发送一条消息");
        System.out.printf("syncSend1 to topic %s sendResult=%s %n", "mouse-topic", sendResult);
    }


}
