package com.xxxx.seckill.rabbitmq;

import com.xxxx.seckill.mapper.OrderMapper;
import com.xxxx.seckill.pojo.Order;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.service.ISeckillOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * 消息消费者
 */
@Service
@Slf4j
public class MQReceiver {

    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private IOrderService orderService;

    @RabbitListener(queues = "queue")
    public void receive(Object msg){
        log.info("接收消息： " + msg);
    }

    @RabbitListener(queues = "queue01")
    public void receive01(@Payload SeckillOrder msg){
        log.info("QUEUE01接收消息： " + msg);
        seckillOrderService.save(msg);
    }

    @RabbitListener(queues = "queue02")
    public void receive02(@Payload Order msg){
        log.info("QUEUE02接收消息： " + msg);
        orderService.save(msg);
    }

//    @RabbitListener(queues = "queue01")
//    public void receive03(Object msg){
//        log.info("QUEUE01接收消息： " + msg);
//    }
//
//    @RabbitListener(queues = "queue02")
//    public void receive04(Object msg){
//        log.info("QUEUE02接收消息： " + msg);
//    }
}
