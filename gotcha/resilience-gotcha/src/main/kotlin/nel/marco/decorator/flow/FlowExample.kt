package nel.marco.decorator.flow

import io.github.resilience4j.bulkhead.BulkheadRegistry
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.kotlin.bulkhead.bulkhead
import io.github.resilience4j.kotlin.circuitbreaker.circuitBreaker
import io.github.resilience4j.kotlin.retry.retry
import io.github.resilience4j.kotlin.timelimiter.timeLimiter
import io.github.resilience4j.retry.RetryRegistry
import io.github.resilience4j.timelimiter.TimeLimiterRegistry
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.coroutines.coroutineContext
import kotlin.random.Random

@RestController
class ExampleFlowDecoratorRestController(
    private val config: Resilience4jConfigFlow

) {
    @GetMapping("/decorator/flow")
    suspend fun retry(): String {
        println("before: ${Thread.currentThread().name}")

        return runBlocking {
            config.decorateWithFlow(
                name = "backendA",
                fallbackFunction = { "Fallback executed!" },
                block = {
                    println("during: ${Thread.currentThread().name}")

                    if (Random.nextDouble() < 0.5) { // Increased failure rate for testing
                        throw RuntimeException("Service failed!")
                    }

                    "Service executed successfully"
                }
            )
        }
    }
}

@Component
class Resilience4jConfigFlow(
    private val circuitBreakerRegistry: CircuitBreakerRegistry,
    private val retryRegistry: RetryRegistry,
    private val timeLimiterRegistry: TimeLimiterRegistry,
    private val bulkheadRegistry: BulkheadRegistry
) {

    suspend fun <T : Any> decorateWithFlow(
        name: String,
        fallbackFunction: suspend () -> T,
        block: suspend () -> T
    ) =
        flow { emit(block.invoke()) }
            .bulkhead(bulkheadRegistry.bulkhead(name))
            .retry(retryRegistry.retry(name))
            .circuitBreaker(circuitBreakerRegistry.circuitBreaker(name))
            .timeLimiter(timeLimiterRegistry.timeLimiter(name))
            .catch { throwable ->
                println("Exception caught in flow decorator: ${throwable.message}")
                emit(fallbackFunction())
            }
            .first()
}