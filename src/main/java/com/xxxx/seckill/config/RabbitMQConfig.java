package com.xxxx.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;


/**
 * RabbitMQ 配置类
 */
@Configuration
public class RabbitMQConfig implements RabbitListenerConfigurer{

    @Autowired
    public ConnectionFactory connectionFactory;

    private static final String QUEUE01 = "queue01";     // 队列1
    private static final String QUEUE02 = "queue02";     // 队列2
    private static final String EXCHANGE01 = "fanoutExchange";    // fanout 交换机
    private static final String EXCHANGE02 = "directExchange";    // direct 交换机
    private static final String EXCHANGE03 = "topicExchange";    // direct 交换机
    private static final String ROUTINGKEY01 = "queue.red";     // 路由键1
    private static final String ROUTINGKEY02 = "queue.black";   // 路由键2

    private static final String ROUTINGKEY03 = "*.orange.*";   // 路由键3
    private static final String ROUTINGKEY04 = "*.*.rabbit";   // 路由键4
    private static final String ROUTINGKEY05 = "lazy.#";   // 路由键5

    @Bean
    public Queue queue(){
        return new Queue("queue",true);
    }

    /**
     * 队列
     * @return
     */
    @Bean
    public Queue queue01(){
        return new Queue(QUEUE01);
    }

    @Bean
    public Queue queue02(){
        return new Queue(QUEUE02);
    }

    /**
     * 交换机
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(EXCHANGE01);
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(EXCHANGE02);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE03);
    }
    /**
     * 绑定 fanout 交换机
     */
//    @Bean
//    public Binding binding01(){
//        return BindingBuilder.bind(queue01()).to(fanoutExchange());
//    }
//
//    @Bean
//    public Binding binding02(){
//        return BindingBuilder.bind(queue02()).to(fanoutExchange());
//    }
    /**
     * 绑定 direct 交换机
     */
    @Bean
    public Binding binding01(){
        return BindingBuilder.bind(queue01()).to(directExchange()).with(ROUTINGKEY01);
    }
    @Bean
    public Binding binding02(){
        return BindingBuilder.bind(queue02()).to(directExchange()).with(ROUTINGKEY02);
    }
    /**
     * 绑定 topic 交换机
     */
//    @Bean
//    public Binding binding01(){
//        return BindingBuilder.bind(queue01()).to(topicExchange()).with(ROUTINGKEY03);
//    }
//
//    @Bean
//    public Binding binding02(){
//        return BindingBuilder.bind(queue02()).to(topicExchange()).with(ROUTINGKEY04);
//    }
//
//    @Bean
//    public Binding binding03(){
//        return BindingBuilder.bind(queue02()).to(topicExchange()).with(ROUTINGKEY05);
//    }
    @Bean
    public DefaultMessageHandlerMethodFactory myHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        // 这里的转换器设置实现了 通过 @Payload 注解 自动反序列化message body
        factory.setMessageConverter(new MappingJackson2MessageConverter());
        return factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(myHandlerMethodFactory());
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        return factory;
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        // 这里的转换器设置实现了发送消息时自动序列化消息对象为message body
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

}
