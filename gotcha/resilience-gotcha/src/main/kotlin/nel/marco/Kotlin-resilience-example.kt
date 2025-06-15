package nel.marco

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.reactive.function.client.WebClient
import kotlin.system.measureTimeMillis


@SpringBootApplication
class Launcher

fun main() {
    SpringApplication.run(Launcher::class.java)
}


@ControllerAdvice
class ControllerAdvise() {

    @ExceptionHandler(exception = [Throwable::class])
    fun reduceExceptionMessage(throwable: Throwable): ResponseEntity<String> {
        repeat(3) { println("") }
        println(throwable.message)
        repeat(3) { println("") }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("Something went wrong: ${throwable.message}") // no stack trace
    }


}

@Service
class StartupRequest : CommandLineRunner {

    override fun run(vararg args: String?) {
        runBlocking {
            retryExample()
//            timeoutExample()
        }
    }


    suspend fun retryExample() {
        kotlin.runCatching {
            WebClient.create().get().uri("http://localhost:8080/retry").retrieve().toBodilessEntity().awaitSingle()
        }.onFailure {
            println("retry webclient failed: " + it.message)
        }
    }

    private suspend fun timeoutExample() {
        kotlin.runCatching {
            val time = measureTimeMillis {
                WebClient.create().get().uri("http://localhost:8080/timeout").retrieve()
                    .toEntity(String::class.java)
                    .awaitSingle()
            }

            println("$time" + "ms")
        }.onFailure {
            println("timeout webclient failed: " + it.message)
        }
    }
}