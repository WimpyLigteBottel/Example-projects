package com.example.rabbitmqspringboot

import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class Consumer {

    val log = LoggerFactory.getLogger(this::class.java)

    //This default method to use if not specifying in the message ListenerAdapter
    fun handleMessage(message: String) {
        println("RECEIVED MESSAGE: $message")
    }

    fun printOutMessage(message: String) {
        println("RECEIVED MESSAGE: $message")
    }

    fun logMessage(message: String) {
        log.info("RECEIVED MESSAGE: $message")
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
