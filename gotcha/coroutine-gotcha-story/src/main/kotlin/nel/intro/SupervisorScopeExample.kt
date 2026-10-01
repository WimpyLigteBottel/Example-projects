package nel.intro

import kotlinx.coroutines.async
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

        val secondJob = runCatching {
            async {
                delay(100)
                throw RuntimeException("Failed")
            }
        }.getOrNull()


        val main = mainJob.await()
        val second = secondJob?.await()

        println(CombinedResult(main, second))
    }
}
