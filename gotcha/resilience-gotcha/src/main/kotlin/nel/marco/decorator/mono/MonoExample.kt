package nel.marco.decorator.mono

import io.github.resilience4j.bulkhead.BulkheadRegistry
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.reactor.bulkhead.operator.BulkheadOperator
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator
import io.github.resilience4j.reactor.retry.RetryOperator
import io.github.resilience4j.reactor.timelimiter.TimeLimiterOperator
import io.github.resilience4j.retry.RetryRegistry
import io.github.resilience4j.timelimiter.TimeLimiterRegistry
import kotlinx.coroutines.delay
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random

@RestController
class ExamplMonoDecoratorRestController(
    private val config: Resilience4jConfigMono,
) {
    @GetMapping("/decorator/mono")
    suspend fun retry(): String? {
        println("before: ${Thread.currentThread().name}")
        return config.decorateMono(
            name = "backendA",
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


@Component
class Resilience4jConfigMono(
    private val circuitBreakerRegistry: CircuitBreakerRegistry,
    private val retryRegistry: RetryRegistry,
    private val timeLimiterRegistry: TimeLimiterRegistry,
    private val bulkheadRegistry: BulkheadRegistry
) {

     suspend fun <T : Any> decorateMono(
        name: String,
        block: suspend () -> T
    ): T? {
        val circuitBreaker = circuitBreakerRegistry.circuitBreaker(name)
        val retry = retryRegistry.retry(name)
        val bulkhead = bulkheadRegistry.bulkhead(name)
        val timeLimiter = timeLimiterRegistry.timeLimiter(name)

        return mono { block.invoke() }
            .transformDeferred(RetryOperator.of(retry))
            .transformDeferred(BulkheadOperator.of(bulkhead))
            .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
            .transformDeferred(TimeLimiterOperator.of(timeLimiter))
            .toFuture()
            .join()
    }
}