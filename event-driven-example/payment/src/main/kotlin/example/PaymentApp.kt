package example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class PaymentApp {
}

fun main(args: Array<String>){
    SpringApplication.run(PaymentApp::class.java, *args)
}
