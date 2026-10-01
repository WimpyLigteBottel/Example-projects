package nel.intro

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors


val cpuWorkers = Executors.newFixedThreadPool(1).asCoroutineDispatcher()
val ioWorkers = Executors.newFixedThreadPool(64).asCoroutineDispatcher()


/*
What is going to print?

1.
Worker A: Tell me when I can start!
Worker B: YOU CAN START!
Worker A: DONE!


2.

Worker A: Tell me when I can start!
Worker A: DONE!
Worker B: YOU CAN START!

 */


fun main(): Unit {
    runBlocking {

        println("Launching tasks!")

        launch(cpuWorkers) {
            println("Worker A: Tell me when i can start!")
            Thread.sleep(2000)
            println("Worker A: DONE!")
        }

        // Say when ready :)
        launch(cpuWorkers) {
            println("Worker B: YOU CAN START!")
        }

    }
}
