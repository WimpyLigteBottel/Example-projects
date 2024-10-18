package nel.marco

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Thread.sleep
import java.util.concurrent.Executors

/**
 * Remember block vs suspending.... What is the difference
 */

fun main(): Unit {
//    blocking()
    suspending()
}

private fun blocking() {
    val workers = Executors.newFixedThreadPool(1).asCoroutineDispatcher()

    runBlocking {
        repeat(10) {
            launch(workers) {
                sleep(1000)
                println("DONE!")
            }
        }
    }
}

private fun suspending() {
    val workers = Executors.newFixedThreadPool(1).asCoroutineDispatcher()

    runBlocking {
        repeat(10) {
            launch(workers) {
                delay(1000)
                println("DONE!")
            }
        }
    }
}
