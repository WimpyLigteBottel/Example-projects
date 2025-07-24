package nel.marco.decorator

import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.runBlocking
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.util.concurrent.TimeUnit


@SpringBootApplication
class Launcher

fun main() {
    SpringApplication.run(Launcher::class.java)
}


@ControllerAdvice
class ControllerAdvise() {
    @ExceptionHandler(exception = [Throwable::class])
    fun reduceExceptionMessage(throwable: Throwable): ResponseEntity<String> {
        println(throwable.message)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("Something went wrong: ${throwable.message}") // no stack trace
    }
}

@Service
@EnableScheduling
class StartupRequest() {

    @Scheduled(fixedRate = 1000, timeUnit = TimeUnit.MILLISECONDS)
    fun schedule() {
//        println(executeMono())
        println(executeFlow())
    }

    fun executeMono() = runBlocking {
        WebClient.create("http://localhost:8080").get().uri {
            it.path("/decorator/mono").build()
        }.retrieve()
            .bodyToMono<String>()
            .awaitSingle()
    }

    fun executeFlow() = runBlocking {
        WebClient.create("http://localhost:8080").get().uri {
            it.path("/decorator/flow").build()
        }.retrieve()
            .bodyToMono<String>()
            .awaitSingle()
    }
}