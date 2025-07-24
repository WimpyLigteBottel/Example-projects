package nel.marco.retry

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.runBlocking
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.reactive.function.client.WebClient
import java.util.concurrent.TimeUnit


@SpringBootApplication
class Launcher

fun main() {
    SpringApplication.run(Launcher::class.java)
}


@ControllerAdvice
@EnableScheduling
class ControllerAdvise() {
    @ExceptionHandler(exception = [Throwable::class])
    fun reduceExceptionMessage(throwable: Throwable): ResponseEntity<String> {
        println(throwable.message)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("Something went wrong: ${throwable.message}") // no stack trace
    }

    @Scheduled(fixedRate = 1000, timeUnit = TimeUnit.MILLISECONDS)
    fun retryDecoratorExample2() {
        runCatching {
            runBlocking {
                WebClient.create().get().uri("http://localhost:8080/retry").retrieve().toBodilessEntity().awaitSingle()
            }
        }.onFailure {
            println("retry webclient failed: " + it.message)
        }
    }

}
