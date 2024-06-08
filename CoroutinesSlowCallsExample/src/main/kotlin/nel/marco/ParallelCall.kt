package nel.marco

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class ParallelCall {

    val className = this::class.java.name
    /**
     * This call is wrong because the X.await() is being wrong.
     */
    suspend fun exampleWrongSetup() = coroutineScope {
        println("$className: starting = exampleWrongSetup")
        val total = measureTimeMillis {
            val answer1 = async { longRunning() }.await() // Will cause it to do the async call then wait till done
            val answer2 = async { longRunning() }.await() // Will cause it to do the async call then wait till done

            println("answer1=${answer1}")
            println("answer2=${answer2}")
        }

        //return not necessary
        return@coroutineScope "$$className: exampleWrongSetup = $total ms"
    }

    suspend fun example() = coroutineScope {
        println("$className: starting = example")
        val total = measureTimeMillis {
            val answer1 = async { longRunning() } //This is better
            val answer2 = async { longRunning() } //

            println("answer1=${answer1.await()}")
            println("answer2=${answer2.await()}")
        }

        //return not necessary
        return@coroutineScope "$className: example = $total ms"
    }


    private suspend fun longRunning(time: Long = 3000): String {

        val totalTime = measureTimeMillis {
            delay(time)
        }
        return "took $totalTime ms"
    }
}