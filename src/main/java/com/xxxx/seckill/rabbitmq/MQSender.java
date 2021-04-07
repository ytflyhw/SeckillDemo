package com.xxxx.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息发送者
 */
@Service
@Slf4j
public class MQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Object msg){
        log.info("发送消息： " + msg);
        rabbitTemplate.convertAndSend("queue", msg);
    }

    /**
     * fanout 模式发送
     * @param msg
     */
    public void sendFanout(Object msg){
        log.info("发送消息： " + msg);
        rabbitTemplate.convertAndSend("fanoutExchange", "", msg);
    }

    /**
     * direct 模式发送
     * @param msg
     */
    public void sendDirect01(Object msg){
        log.info("发送 red 消息： " + msg);
        rabbitTemplate.convertAndSend("directExchange", "queue.red", msg);
    }

    public void sendDirect02(Object msg){
        log.info("发送 black 消息： " + msg);
        rabbitTemplate.convertAndSend("directExchange", "queue.black", msg);
    }

    /**
     * topic 模式发送
     * @param msg
     */
    public void sendTopic01(Object msg){
        log.info("发送 QUEUE01 消息： " + msg);
        rabbitTemplate.convertAndSend("topicExchange", "queue.orange.black", msg);
    }

    public void sendTopic02(Object msg){
        log.info("发送 QUEUE02 消息： " + msg);
        rabbitTemplate.convertAndSend("topicExchange", "lazyypy.black.rabbit", msg);
    }

    public void sendTopic03(Object msg){
        log.info("发送 双队列 消息： " + msg);
        rabbitTemplate.convertAndSend("topicExchange", "lazy.orange.rabbit", msg);
    }
}
