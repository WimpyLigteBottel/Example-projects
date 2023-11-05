package com.example.rabbitmqspringboot

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@SpringBootApplication
class RabbitmqSpringbootApplication

fun main(args: Array<String>) {
    runApplication<RabbitmqSpringbootApplication>(*args)
}


@RestController
class InteractionRestController {


    @Autowired
    lateinit var publisher: Publisher

    @GetMapping
    fun sendMessages() {
        (0..10).forEach {
            publisher.publish("Hello $it")
        }
    }

    @GetMapping("log")
    fun sendMessages2() {
        (0..10).forEach {
            publisher.publishToLogger("Hello $it")
        }
    }
}
