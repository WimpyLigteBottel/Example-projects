package nel.marco

import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import kotlin.system.measureTimeMillis

@Component
class LargeAmountParallelCall {

    private val log = LoggerFactory.getLogger(this::class.java)
    val className = this::class.java.name

    val client = WebClient.create("http://localhost:8080")

    suspend fun example() = coroutineScope {
        val total = measureTimeMillis {
            val jobs = List(20) {
                async(Dispatchers.IO) { retrieveUsers() }

            }
            jobs.awaitAll()
        }

        return@coroutineScope "${className}: example = $total ms"
    }

    /**
     * This is still running in main thread and one by one
     */
    fun exampleWrongSetup(): String {
        val total = measureTimeMillis {
            runBlocking(Dispatchers.Default) {
                val jobs = List(20) {
                    async { retrieveUsers() }
                }

                jobs.awaitAll()
            }

        }
        return "${className}: exampleWrongSetup = $total ms"
    }

    /**
     * This is still running in main thread and one by one
     */
    fun exampleWrongSetup2(): String {
        val total = measureTimeMillis {
            runBlocking {
                val jobs = List(20) {
                    async { retrieveUsers() }
                }

                jobs.awaitAll()
            }

        }
        return "${className}: exampleWrongSetup2 = $total ms"
    }

    private suspend fun retrieveUsers(): String {
        log.info(Thread.currentThread().name)

        return client
            .get()
            .uri("/users")
            .retrieve()
            .bodyToMono<String>()
            .block()!!
    }
}
