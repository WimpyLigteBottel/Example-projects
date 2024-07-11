package example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class OrderApp {
}

fun main(args: Array<String>) {
    SpringApplication.run(OrderApp::class.java, *args)
}
