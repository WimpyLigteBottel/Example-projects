package nel.marco

import java.lang.Thread.sleep
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


val message = ThreadLocal<String>()


fun main(): Unit {
    runBlocking {
        val countDownLatch = CountDownLatch(1)

        // Setting up some context
        val manager = Thread.currentThread().name
        message.set("$manager said: \"Remember RED Apples on top!\"")
        println(message.get())

        tellWorker("A", countDownLatch, this.coroutineContext)
        tellWorker("B", countDownLatch, this.coroutineContext)

        sleep(500)
        countDownLatch.countDown()

    }
}

private fun CoroutineScope.tellWorker(
    name: String,
    countDownLatch: CountDownLatch,
    contextElement: CoroutineContext? = null,
) {
    if (contextElement == null) {
        launch {
            countDownLatch.await()
            val worker = Thread.currentThread().name
            println("$worker $name: Remembers what manager said -> ${message.get()}") // Will remember
        }
    } else {
        launch(contextElement) {
            countDownLatch.await()
            val worker = Thread.currentThread().name
            println("$worker $name: Remembers what manager said -> ${message.get()}") // Will remember
        }
    }
}

// What other places can it happen?

//  Request Scope (Web Context)
//  Hibernate Lazy Initialization (Session Context)
//  Logging Context (MDC - Mapped Diagnostic Context)
//  Transactional Context (Spring @Transactional):
//  Spring Security Context