package nel.marco

import kotlinx.coroutines.*
import java.lang.Thread.sleep
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.CoroutineContext


val message = ThreadLocal<String>()


fun main(): Unit = runBlocking() {
    val countDownLatch = CountDownLatch(1)

    //Manager says a message in the store
    managerSaySomething()

    val sharingContext = Dispatchers.Default + message.asContextElement()
    val context = Dispatchers.Default

    tellWorker("A", countDownLatch, sharingContext)
    tellWorker("B", countDownLatch, context)

    sleep(500)
    countDownLatch.countDown()

}

private fun managerSaySomething() {
    // Setting up some context
    val manager = Thread.currentThread().name
    message.set("$manager said: \"Remember RED Apples on top!\"")
    println(message.get())
}

private fun CoroutineScope.tellWorker(
    name: String,
    countDownLatch: CountDownLatch,
    contextElement: CoroutineContext,
) {
    launch(contextElement) {
        countDownLatch.await()
        val worker = Thread.currentThread().name
        println("$worker $name: Remembers what manager said -> ${message.get()}") // Will remember
    }
}

// What other places can it happen?

//  Request Scope (Web Context)
//  Hibernate Lazy Initialization (Session Context)
//  Logging Context (MDC - Mapped Diagnostic Context)
//  Transactional Context (Spring @Transactional):
//  Spring Security Context