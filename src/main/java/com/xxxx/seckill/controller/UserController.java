package com.xxxx.seckill.controller;


import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.rabbitmq.MQSender;
import com.xxxx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ytflyhw
 * @since 2021-03-09
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MQSender mqSender;
    /**
     * 用户信息（测试）
     */
    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user){
        return RespBean.success(user);
    }

    /**
     * 测试发送 RabbitMQ 消息
     */
    @RequestMapping("/MQ")
    @ResponseBody
    public void mq(){
        mqSender.send("hello");
    }

    /**
     * fanout（广播模式）
     */
    @RequestMapping("/MQ/fanout")
    @ResponseBody
    public void mqfanout(){
        mqSender.sendFanout("hello");
    }

    /**
     * direct（直连模式）
     */
    @RequestMapping("/MQ/direct")
    @ResponseBody
    public void mqdirect(){
        mqSender.sendDirect01("hello");
        mqSender.sendDirect02("bye");
    }

    /**
     * topic（模式）
     */
    @RequestMapping("/MQ/topic")
    @ResponseBody
    public void mqtopic(){
        mqSender.sendTopic01("hello queue01");
        mqSender.sendTopic02("hello queue02");
        mqSender.sendTopic01("hello queue01");
        mqSender.sendTopic02("hello queue02");
        mqSender.sendTopic01("hello queue01");
        mqSender.sendTopic02("hello queue02");
        mqSender.sendTopic01("hello queue01");
        mqSender.sendTopic02("hello queue02");
        mqSender.sendTopic01("hello queue01");
        mqSender.sendTopic02("hello queue02");
        mqSender.sendTopic03("bye");
    }
}
