package nel.marco

import io.github.resilience4j.bulkhead.BulkheadRegistry
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.reactor.bulkhead.operator.BulkheadOperator
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator
import io.github.resilience4j.reactor.retry.RetryOperator
import io.github.resilience4j.reactor.timelimiter.TimeLimiterOperator
import io.github.resilience4j.retry.RetryRegistry
import io.github.resilience4j.timelimiter.TimeLimiterRegistry
import kotlinx.coroutines.reactor.awaitSingle
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
import reactor.core.publisher.Mono
import java.util.concurrent.TimeUnit
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
@EnableScheduling
class StartupRequest(
    private val circuitBreakerRegistry: CircuitBreakerRegistry,
    private val retryRegistry: RetryRegistry,
    private val timeLimiterRegistry: TimeLimiterRegistry,
    private val bulkheadRegistry: BulkheadRegistry
) {


    //    @Scheduled(fixedRate = 1000, timeUnit = TimeUnit.MILLISECONDS)
    fun retryDecoratorExample() {
        runBlocking {
            kotlin.runCatching {
                println(
                    WebClient.create().get().uri("http://localhost:8080/decorator").retrieve()
                        .toEntity(String::class.java)
                        .awaitSingle()
                )
            }.onFailure {
                println("retry webclient failed: " + it.message)
            }
        }
    }

    var previousNumber = 1000L

    @Scheduled(fixedRate = 1000, timeUnit = TimeUnit.MILLISECONDS)
    fun retryDecoratorExample2() {
        runCatching {
            println(
                WebClient.create("http://localhost:8080").get().uri {
                    it.path("/test")
                        .queryParam("delayNumber", previousNumber)
                        .queryParam("failureChange", 0.1)
                        .build()
                }.retrieve()
                    .bodyToMono(String::class.java)
                    .transformDeferred(TimeLimiterOperator.of(timeLimiterRegistry.timeLimiter("backendA"))) // #1
                    .transformDeferred(BulkheadOperator.of(bulkheadRegistry.bulkhead("backendA")))         // #2
                    .transformDeferred(CircuitBreakerOperator.of(circuitBreakerRegistry.circuitBreaker("backendA"))) // #3
                    .transformDeferred(RetryOperator.of(retryRegistry.retry("backendA")))
                    .onErrorResume {
                        println("Failed")
                        Mono.error(it)
                    }
                    .toFuture().join()
            )

            previousNumber = previousNumber + 1000
        }.onFailure {
            println("decora: " + it.message)
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