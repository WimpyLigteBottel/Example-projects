package nel.marco

import io.micrometer.core.annotation.Counted
import io.micrometer.core.annotation.Timed
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import java.util.concurrent.TimeUnit


@SpringBootApplication
@EnableScheduling
class Launcher

fun main(args: Array<String>) {

    SpringApplication.run(Launcher::class.java, *args)
}


@Component
class ExecuteTasks() {

    val client = WebClient.create("http://localhost:8080/hello")


    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    @Counted
    fun printHelloWorld() {
        val r = client.get().retrieve().bodyToMono(String::class.java).block()

        println(r)
    }
}

@RestController
class HelloWorldController() {

    @GetMapping("/hello")
    @Timed
    fun hello(): String {

        return "Hello World"
    }

}