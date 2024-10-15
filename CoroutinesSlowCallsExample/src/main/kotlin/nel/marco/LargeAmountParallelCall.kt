package nel.marco

import kotlinx.coroutines.*
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.lang.Thread.currentThread
import java.lang.Thread.sleep
import kotlin.system.measureTimeMillis

@Component
class LargeAmountParallelCall {

    private val log = LoggerFactory.getLogger(this::class.java)
    val className = this::class.java.name

    val client = WebClient.create("http://localhost:8080")

    suspend fun example(dispatcher: CoroutineDispatcher, callAmount: Int) = withContext(dispatcher) {
        val total = measureTimeMillis {
            val jobs = List(callAmount) {
                async { retrieveUsers() }
            }
            jobs.awaitAll()
        }

        return@withContext "${className}: example ($dispatcher) = $total ms"
    }

    /**
     * This is still running in main thread and one by one
     */
    fun exampleWrongSetup(callAmount:Int): String {
        val total = measureTimeMillis {
            runBlocking(Dispatchers.Unconfined) {
                val jobs = List(callAmount) {
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
    fun exampleWrongSetup2(callAmount:Int): String {
        val total = measureTimeMillis {
            runBlocking {
                val jobs = List(callAmount) {
                    async { retrieveUsers() }
                }

                jobs.awaitAll()
            }

        }
        return "${className}: exampleWrongSetup2 = $total ms"
    }

    private suspend fun retrieveUsers(): String = coroutineScope {
//        println("${currentThread().name}")
        sleep(3000)

        client
            .get()
            .uri("/users")
            .retrieve()
            .bodyToMono<String>()
            .awaitSingle()
    }
}
