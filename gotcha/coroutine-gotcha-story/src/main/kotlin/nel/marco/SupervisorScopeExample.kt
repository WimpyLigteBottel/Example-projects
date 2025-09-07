import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

data class CombinedResult(val main: String, val secondary: String?)


fun main(): Unit = runBlocking {

    supervisorScope {
        val mainJob = async {
            delay(1000)
            return@async "MAIN"
        }

        val secondJob = async {
            delay(100)
            throw RuntimeException("Failed")
        }

        val main = mainJob.await()
        val second = runCatching { secondJob.await() }.getOrNull()

        println(CombinedResult(main, second))
    }
}
