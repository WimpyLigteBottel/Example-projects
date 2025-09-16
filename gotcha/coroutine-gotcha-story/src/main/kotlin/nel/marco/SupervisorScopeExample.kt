package nel.marco

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

data class CombinedResult(val main: String, val secondary: Any?)


fun main(): Unit = runBlocking {

    coroutineScope {
        val mainJob = async {
            delay(1000)
            "MAIN"
        }

        val secondJob =
            async {
                delay(100)
                throw RuntimeException("Failed")
            }

        runCatching {
            awaitAll(mainJob, secondJob)
        }

        val main = mainJob.await()
        val second = runCatching { secondJob.await() }.getOrNull()

        println(CombinedResult(main, second))
    }
}
