package nel.marco.decorator

import io.github.resilience4j.bulkhead.BulkheadRegistry
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.kotlin.bulkhead.bulkhead
import io.github.resilience4j.kotlin.bulkhead.executeSuspendFunction
import io.github.resilience4j.kotlin.circuitbreaker.circuitBreaker
import io.github.resilience4j.kotlin.circuitbreaker.executeSuspendFunction
import io.github.resilience4j.kotlin.retry.executeSuspendFunction
import io.github.resilience4j.kotlin.retry.retry
import io.github.resilience4j.kotlin.timelimiter.decorateSuspendFunction
import io.github.resilience4j.kotlin.timelimiter.timeLimiter
import io.github.resilience4j.retry.RetryRegistry
import io.github.resilience4j.timelimiter.TimeLimiterRegistry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random

@RestController
class ExampleDecoratorRestController(
    private val exampleService: ExampleServiceDecorator
) {

    @GetMapping("/decorator")
    suspend fun retry(): String {
        println("before: ${Thread.currentThread().name}")
        return exampleService.simple()
    }
}

@Service
class ExampleServiceDecorator(
    private val config: Resilience4jConfig
) {
    suspend fun simple(): String {
        return config.decorateSuspend(
            name = "backendA",
            fallbackFunction = { fallbackFunction() },
            block = { simulateService() }
        )
    }

    suspend fun flow() = runBlocking { // HAD TO ADD runblocking here to get it to work
        config.decorateWithFlow(
            name = "backendA",
            fallbackFunction = { fallbackFunction() },
            block = { simulateService() }
        ).first()
    }

    private suspend fun simulateService(): String {
        // Simulate some processing time
        kotlinx.coroutines.delay(100)

        if (Random.nextDouble() < 0.5) { // Increased failure rate for testing
            throw RuntimeException("Service failed!")
        }
        return "Service executed successfully"
    }

    private suspend fun fallbackFunction(): String {
        return "Fallback executed!"
    }
}

@Component
class Resilience4jConfig(
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

    suspend fun <T : Any> decorateWithFlow(
        name: String,
        fallbackFunction: suspend () -> T,
        block: suspend () -> T
    ): Flow<T> {
        val circuitBreaker = circuitBreakerRegistry.circuitBreaker(name)
        val retry = retryRegistry.retry(name)
        val timeLimiter = timeLimiterRegistry.timeLimiter(name)
        val bulkhead = bulkheadRegistry.bulkhead(name)

        return flow {
            try {
                val result = block.invoke()
                emit(result)
            } catch (e: Exception) {
                throw e // Let the decorators handle the exception
            }
        }
            .bulkhead(bulkhead)
            .retry(retry)
            .circuitBreaker(circuitBreaker)
            .timeLimiter(timeLimiter)
            .catch { throwable ->
                println("Exception caught in flow decorator: ${throwable.message}")
                emit(fallbackFunction())
            }
    }
}