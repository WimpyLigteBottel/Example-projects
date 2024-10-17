package nel.marco

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asContextElement
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Thread.sleep
import java.util.concurrent.CountDownLatch


val message = ThreadLocal<String>()


fun main(): Unit = runBlocking() {

    // Setting up some context
    val manager = Thread.currentThread().name
    message.set("$manager said: \"Remember RED Apples on top!\"")

    println(message.get())


    val countDownLatch = CountDownLatch(1)
    launch(Dispatchers.Default + message.asContextElement()) {
        countDownLatch.await()
        val worker = Thread.currentThread().name
        println("$worker A: Remembers what manager said -> ${message.get()}") // Will remember
    }
    launch(Dispatchers.Default) {
        countDownLatch.await()
        val worker = Thread.currentThread().name
        println("$worker B: Remembers what manager said -> ${message.get()}") // Won't  remember
    }
    sleep(500)
    countDownLatch.countDown()

}

// What other places can it happen?

//  Request Scope (Web Context)
//  Hibernate Lazy Initialization (Session Context)
//  Logging Context (MDC - Mapped Diagnostic Context)
//  Transactional Context (Spring @Transactional):
//  Spring Security Context