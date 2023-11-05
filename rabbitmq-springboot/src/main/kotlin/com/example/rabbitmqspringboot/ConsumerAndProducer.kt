package com.example.rabbitmqspringboot

import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component


@Configuration
class Config {
    @Bean
    fun queuePrintln(): Queue {
        return Queue("println", false)
    }

    @Bean
    fun queueLog(): Queue {
        return Queue("logInfo", false)
    }
}
@Component
@RabbitListener(queues = ["logInfo"])
class Consumer {

    val log = LoggerFactory.getLogger(this::class.java)
    @RabbitHandler
    fun logMessage(message: String) {
        log.info("RECEIVED MESSAGE: $message")
    }
}

@RabbitListener(queues = ["println"])
@Component
class ConsumerPrintln {
    @RabbitHandler(isDefault = true)
    fun handleMessage(message: String) {
        println("RECEIVED MESSAGE: $message")
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
        rabbitTemplate.convertAndSend(queueLog.name, message)
    }
}
