package me.marco.spelexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching


@SpringBootApplication
@EnableCaching
class SpelExampleApplication

fun main(args: Array<String>) {
    runApplication<SpelExampleApplication>(*args)
}



