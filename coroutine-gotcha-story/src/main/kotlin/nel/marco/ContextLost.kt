package nel.marco

import java.lang.Thread.sleep
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


val message = ThreadLocal<String>()


/*
1. Will the workers remember what the manager said?
- Yes?
- No?

2. What about now?
- Yes
- No
 */

fun main(): Unit {
    runBlocking {

        // Setting up some context
        val manager = Thread.currentThread().name
        message.set("$manager said: \"Remember RED Apples on top!\"")
        println(message.get())

        tellWorker("A", this.coroutineContext)
        tellWorker("B", this.coroutineContext)
    }
}

private fun CoroutineScope.tellWorker(
    name: String,
    contextElement: CoroutineContext? = null,
) {
    if (contextElement == null) {
        launch {
            val worker = Thread.currentThread().name
            println("$worker $name: Remembers what manager said -> ${message.get()}") // Will remember
        }
    } else {
        launch(contextElement) {
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