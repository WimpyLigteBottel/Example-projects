package nel.marco

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@SpringBootApplication
class Launcher

fun main(args: Array<String>) {
    runApplication<Launcher>()
}

@Component
class ExecuteCode : CommandLineRunner {

    val client = RestClient.create("http://localhost:8080")

    override fun run(vararg args: String) {
        client.get()
            .uri("/orders/{id}/lesson1", 1)
            .retrieve()
            .toBodilessEntity()


        client.get()
            .uri("/orders/{id}/lesson2", 1)
            .retrieve()
            .toBodilessEntity()
//
//
//        client.get()
//            .uri("/orders/{id}/lesson3", 1)
//            .retrieve()
//            .toBodilessEntity()

    }
}