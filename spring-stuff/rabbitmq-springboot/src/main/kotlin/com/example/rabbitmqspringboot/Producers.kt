package com.example.rabbitmqspringboot

import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component


@Configuration
class Config {
    @Bean
    fun queuePrintln(): Queue {
        return QueueBuilder.nonDurable("println")
            .autoDelete()
            .build()
    }

    @Bean
    fun queueLog(): Queue {
        return QueueBuilder.nonDurable("logInfo")
            .withArgument("x-dead-letter-exchange", "logInfoExchange")
            .withArgument("x-dead-letter-routing-key", "logInfo")
            .autoDelete()
            .build()
    }


    @Bean
    fun deadletter(): Queue {
        return QueueBuilder.nonDurable("deadletter")
            .autoDelete()
            .build()
    }
}

@Component
class Publisher {
    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    @Autowired
    lateinit var queuePrintln: Queue

    @Autowired
    lateinit var queueLog: Queue

    fun publish(message: String) {
        rabbitTemplate.convertAndSend(queuePrintln.name, message)
    }


    fun publishToLogger(message: String) {
        rabbitTemplate.convertAndSend("logInfoExchange", queueLog.name, message)
    }
}
