package cn.xilio.mouse.cloud.rocketmq.producer;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;

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
        //testSendAsyncMessage();
        testRocketMQTemplateTransaction();
    }

    private void testSendCommonMessage() {
        SendResult sendResult = rocketMQTemplate.syncSend("mouse-topic", "发送一条消息");
        System.out.printf("syncSend1 to topic %s sendResult=%s %n", "mouse-topic", sendResult);
    }

    private void testSendAsyncMessage() {
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
    private void testRocketMQTemplateTransaction() throws MessagingException {
        String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 10; i++) {
            try {

                Message msg = MessageBuilder.withPayload("rocketMQTemplate transactional message " + i).
                        setHeader(RocketMQHeaders.TRANSACTION_ID, "KEY_" + i).build();
                SendResult sendResult = rocketMQTemplate.sendMessageInTransaction(
                        "springTransTopic" + ":" + tags[i % tags.length], msg, null);
                System.out.printf("------rocketMQTemplate send Transactional msg body = %s , sendResult=%s %n",
                        msg.getPayload(), sendResult.getSendStatus());

                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
