package com.example.rabbitmqspringboot

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ConfigLog {

    companion object{
        val exchangeName = "exchangeMessageToQueueLogger"
        val queueLogInfo = "logInfo"
    }

    @Bean
    fun queueLog(): Queue {
        return Queue(queueLogInfo, false)
    }

    @Bean
    fun exchangeLog(): TopicExchange {
        return TopicExchange(exchangeName)
    }

    @Bean
    fun bindingQueueLogInfo(queueLog: Queue, exchangeLog: TopicExchange): Binding {
        return BindingBuilder
            .bind(queueLog)
            .to(exchangeLog)// name of the exchange (needs to be unique)
            .with("#") // On routing key it will send message to specific queue
    }

    @Bean
    fun simpleMessageListenerContainerLog(
        connectionFactory: ConnectionFactory,
        listenerAdapterLogger: MessageListenerAdapter
    ): SimpleMessageListenerContainer {
        val container = SimpleMessageListenerContainer(connectionFactory)
        container.setQueueNames(queueLogInfo) // Listen from these queue's
        container.setMessageListener(listenerAdapterLogger) // Tells the container to use this listener adapter
        return container
    }


    @Bean
    fun listenerAdapterLogger(consumer: Consumer) = MessageListenerAdapter(consumer, "logMessage")
}