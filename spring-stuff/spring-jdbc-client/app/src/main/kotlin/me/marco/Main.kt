package me.marco

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
open class Launcher

fun main(args: Array<String>) {
    runApplication<Launcher>(*args)
}
