package com.example.rabbitmqspringboot

import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
@RabbitListener(
    bindings = [
        QueueBinding(
            value = Queue(value = "logInfo"),
            exchange = Exchange(value = "logInfoExchange"),
            key = ["logInfo"]
        )
    ]
)
class Consumer {

    val log = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    @RabbitHandler(isDefault = true)
    fun logMessage(message: Message) {
        throw RuntimeException("OH NOW I FAIELD")

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

@RabbitListener(queues = ["deadletter"])
@Component
class ConsumerDeadletter {
    @RabbitHandler(isDefault = true)
    fun handleMessage(message: String) {
        println("HANDLED DEAD LETTER MESSAGE: $message")
    }
}
