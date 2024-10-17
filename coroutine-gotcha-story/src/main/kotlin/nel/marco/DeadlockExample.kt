package nel.marco

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors


val workers = Executors.newFixedThreadPool(2).asCoroutineDispatcher()

fun main(): Unit {
    runBlocking(workers) {


        val countDownLatch = CountDownLatch(1)

        println("Launching tasks!")

        launch(workers) {
            println("Worker A: Tell me when i can start!")
            countDownLatch.await()
            println("Worker A: DONE!")
        }

        // Say when ready :)
        launch(workers) {
            println("Worker B: YOU CAN START!")
            countDownLatch.countDown()
        }
    }
}
