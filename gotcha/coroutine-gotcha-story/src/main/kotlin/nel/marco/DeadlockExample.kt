package nel.marco

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors


val workers = Executors.newFixedThreadPool(2).asCoroutineDispatcher()
val countDownLatch = CountDownLatch(1)


/*
What is going to print?

1.
Worker A: Tell me when I can start!
Worker B: YOU CAN START!
Worker B: DONE!
Worker A: DONE!


2.

Launching tasks!
Worker A: Tell me when i can start!

 */


fun main(): Unit {
    runBlocking {

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
            println("Worker B: DONE!")
        }
    }
}
