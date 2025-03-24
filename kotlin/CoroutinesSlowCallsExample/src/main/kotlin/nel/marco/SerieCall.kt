package nel.marco

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class SerieCall {
    val className = this::class.java.name

    suspend fun example() = coroutineScope {
        println("$className starting = example")
        val total = measureTimeMillis {
            val answer1 = longRunning()
            val answer2 = longRunning()

            println("answer1=${answer1}")
            println("answer2=${answer2}")
        }

        return@coroutineScope "$className example = $total ms"
    }


    private suspend fun longRunning(time: Long = 3000): String {

        val totalTime = measureTimeMillis {
            delay(time)
        }
        return "took $totalTime ms"
    }
}