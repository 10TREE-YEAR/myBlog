package com.wjl.blog.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;

/**
* @Description:  rabbitMQ配置类
* @Param:  
* @return:  
* @Author: wangjialu
* @Date: 2020/2/16 
*/
@Configuration
public class RabbitMQConfig {

    private static Logger log = LoggerFactory.getLogger(RabbitMQConfig.class);

    @Autowired
    private CachingConnectionFactory connectionFactory;

    /**
     *     exchange是交换机交换机的主要作用是接收相应的消息并且绑定到指定的队列.交换机有四种类型,分别为Direct,topic,headers,Fanout.
     *
     *     Direct是RabbitMQ默认的交换机模式,也是最简单的模式.即创建消息队列的时候,指定一个BindingKey.当发送者发送消息的时候,指定对应的Key.当Key和消息队列的BindingKey一致的时候,消息将会被发送到该消息队列中.
     *
     * 　　topic转发信息主要是依据通配符,队列和交换机的绑定主要是依据一种模式(通配符+字符串),而当发送消息的时候,只有指定的Key和该模式相匹配的时候,消息才会被发送到该消息队列中.
     *
     * 　　headers也是根据一个规则进行匹配,在消息队列和交换机绑定的时候会指定一组键值对规则,而发送消息的时候也会指定一组键值对规则,当两组键值对规则相匹配的时候,消息会被发送到匹配的消息队列中.
     *
     * 　　Fanout是路由广播的形式,将会把消息发给绑定它的全部队列,即便设置了key,也会被忽略.
     */

    //===============以下是验证topic Exchange的队列==========
    // Bean默认的name是方法名
    @Bean(name="message")
    public Queue queueMessage() {
        return new Queue("topic.message");
    }

    @Bean(name="messages")
    public Queue queueMessages() {
        return new Queue("topic.messages");
    }
    //===============以上是验证topic Exchange的队列==========

    @Bean
    TopicExchange exchange() {
        // 参数1为交换机的名称
        return new TopicExchange("exchange");
    }

    @Bean
    // 如果参数名和上面用到方法名称一样，可以不用写@Qualifier
    Binding bindingExchangeMessage(@Qualifier("message")Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
    }

    /**
     * 将队列topic.messages与exchange绑定，routing_key为topic.#,模糊匹配
     * @param queueMessages
     * @param exchange
     * @return
     */
    @Bean
    Binding bindingExchangeMessages(@Qualifier("messages")Queue queueMessages, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessages).to(exchange).with("topic.#");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(){
        //若使用confirm-callback或return-callback，必须要配置publisherConfirms或publisherReturns为true
        //每个rabbitTemplate只能有一个confirm-callback和return-callback，如果这里配置了，那么写生产者的时候不能再写confirm-callback和return-callback
        //使用return-callback时必须设置mandatory为true，或者在配置中设置mandatory-expression的值为true
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        /**
         * 如果消息没有到exchange,则confirm回调,ack=false
         * 如果消息到达exchange,则confirm回调,ack=true
         * exchange到queue成功,则不回调return
         * exchange到queue失败,则回调return(需设置mandatory=true,否则不回回调,消息就丢了)
         */
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if(ack){
                    log.info("消息发送成功:correlationData({}),ack({}),cause({})",correlationData,ack,cause);
                }else{
                    log.info("消息发送失败:correlationData({}),ack({}),cause({})",correlationData,ack,cause);
                }
            }
        });
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}",exchange,routingKey,replyCode,replyText,message);
            }
        });
        return rabbitTemplate;
    }

}
