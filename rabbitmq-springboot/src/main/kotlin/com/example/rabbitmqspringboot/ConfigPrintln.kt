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
class ConfigPrintln {

    companion object {
        val exchangeName = "exchangeMessageToQueuePrintln"
        val queuePrintln = "println"

    }

    @Bean
    fun queuePrintln(): Queue {
        return Queue(queuePrintln, false)
    }

    @Bean
    fun exchangePrintln(): TopicExchange {
        return TopicExchange(exchangeName)
    }

    @Bean
    fun bindinQueuePrintln(queuePrintln: Queue, exchangePrintln: TopicExchange): Binding {
        return BindingBuilder
            .bind(queuePrintln)
            .to(exchangePrintln)// name of the exchange
            .with("#") // On routing key it will send message to specific queue
    }

    @Bean
    fun simpleMessageListenerContainer(
        connectionFactory: ConnectionFactory,
        listenerAdapter: MessageListenerAdapter
    ): SimpleMessageListenerContainer {
        val container = SimpleMessageListenerContainer(connectionFactory)
        container.setQueueNames(queuePrintln) // Listen from these queue's
        container.setMessageListener(listenerAdapter) // Tells the container to use this listener adapter
        return container
    }


    /*
        This setups a listener apdapter that will send messages constantly to this consumer.

        Take note you can specify specific method names for the consumer otherwise it will choose
        "handleMessage" as default method name to handle the messages
     */
    @Bean
    fun listenerAdapter(consumer: Consumer) = MessageListenerAdapter(consumer, "printOutMessage")

}