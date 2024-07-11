package example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
class ShipmentApp {}

fun main(args: Array<String>) {
    SpringApplication.run(ShipmentApp::class.java, *args)
}
