package nel.marco

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

        launch {
            val threadName = Thread.currentThread().name
            println("$threadName A: Remembers what manager said -> ${message.get()}") // Will remember
        }
        launch {
            val threadName = Thread.currentThread().name
            println("$threadName B: Remembers what manager said -> ${message.get()}") // Will remember
        }
    }
}

// What other places can it happen?

//  Request Scope (Web Context)
//  Hibernate Lazy Initialization (Session Context)
//  Logging Context (MDC - Mapped Diagnostic Context)
//  Transactional Context (Spring @Transactional):
//  Spring Security Context