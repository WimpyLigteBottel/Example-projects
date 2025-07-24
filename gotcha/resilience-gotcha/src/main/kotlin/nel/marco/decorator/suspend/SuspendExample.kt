package nel.marco.decorator.suspend

import io.github.resilience4j.bulkhead.BulkheadRegistry
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.kotlin.bulkhead.executeSuspendFunction
import io.github.resilience4j.kotlin.circuitbreaker.executeSuspendFunction
import io.github.resilience4j.kotlin.retry.executeSuspendFunction
import io.github.resilience4j.kotlin.timelimiter.decorateSuspendFunction
import io.github.resilience4j.retry.RetryRegistry
import io.github.resilience4j.timelimiter.TimeLimiterRegistry
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random

@RestController
class ExampleSuspendDecoratorRestController(
    private val config: Resilience4jConfigSuspend
) {

    @GetMapping("/decorator/suspend")
    suspend fun retry(): String {
        println("before: ${Thread.currentThread().name}")
        return config.decorateSuspend(
            name = "backendA",
            fallbackFunction = { "Fallback executed!" },
            block = {        // Simulate some processing time
                delay(100)

                if (Random.nextDouble() < 0.5) { // Increased failure rate for testing
                    throw RuntimeException("Service failed!")
                }
                "Service executed successfully"
            }
        )
    }
}

@Component
class Resilience4jConfigSuspend(
    private val circuitBreakerRegistry: CircuitBreakerRegistry,
    private val retryRegistry: RetryRegistry,
    private val timeLimiterRegistry: TimeLimiterRegistry,
    private val bulkheadRegistry: BulkheadRegistry
) {

    suspend fun <T : Any> decorateSuspend(
        name: String,
        fallbackFunction: suspend () -> T,
        block: suspend () -> T
    ): T {
        val circuitBreaker = circuitBreakerRegistry.circuitBreaker(name)
        val retry = retryRegistry.retry(name)
        val bulkhead = bulkheadRegistry.bulkhead(name)
        val timeLimiter = timeLimiterRegistry.timeLimiter(name)

        val decorated = timeLimiter.decorateSuspendFunction {
            bulkhead.executeSuspendFunction {
                circuitBreaker.executeSuspendFunction {
                    retry.executeSuspendFunction {
                        block()
                    }
                }
            }
        }

        return try {
            runBlocking {
                println("inside: ${Thread.currentThread().name}")
                decorated.invoke()
            }
        } catch (e: Exception) {
            println("Exception in suspend decorator: ${e.message}")
            fallbackFunction()
        }
    }


}