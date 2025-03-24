package com.example

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
open class LaunchApplication {
    companion object {
        var log = LoggerFactory.getLogger(LaunchApplication.javaClass)
    }
}


fun main(args: Array<String>) {
    runApplication<LaunchApplication>(*args)
    LaunchApplication.log.info("Started application")
}
