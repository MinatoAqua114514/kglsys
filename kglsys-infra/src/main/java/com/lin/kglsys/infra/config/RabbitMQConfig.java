package com.lin.kglsys.infra.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置类
 * 负责程序化地声明队列、交换机和绑定
 */
@Configuration
public class RabbitMQConfig {

    // 从 application.yml 中注入队列名称
    @Value("${app.rabbitmq.tasks-queue}")
    private String tasksQueueName;

    @Value("${app.rabbitmq.results-queue}")
    private String resultsQueueName;

    @Value("${app.rabbitmq.email-queue}")
    private String emailQueueName;

    // --- 全局公共配置 ---

    /**
     * 配置全局的消息转换器，使用Jackson2进行JSON序列化和反序列化。
     * 这使得我们可以在生产者和消费者之间直接传递DTO对象。
     * @return MessageConverter
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // --- 队列声明 (Queue Declarations) ---

    /**
     * 声明用于发送【判题任务】的队列 (code_tasks_queue)
     * @return Queue
     */
    @Bean
    public Queue tasksQueue() {
        // 使用 QueueBuilder 创建一个持久化的队列
        // 持久化(durable)意味着即使RabbitMQ重启，队列本身也不会丢失
        return QueueBuilder.durable(tasksQueueName).build();
    }

    /**
     * 声明用于接收【判题结果】的队列 (code_results_queue)
     * @return Queue
     */
    @Bean
    public Queue resultsQueue() {
        return QueueBuilder.durable(resultsQueueName).build();
    }

    /**
     * 声明用于发送【邮箱验证码】的队列 (email-verify-code)
     * @return Queue
     */
    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(emailQueueName).build();
    }

    // 如果未来需要使用交换机(Exchange)和绑定(Binding)，也将在这里声明
    // 例如:
    // @Bean
    // public TopicExchange codeExchange() { ... }
    //
    // @Bean
    // public Binding tasksBinding(Queue tasksQueue, TopicExchange codeExchange) { ... }
}