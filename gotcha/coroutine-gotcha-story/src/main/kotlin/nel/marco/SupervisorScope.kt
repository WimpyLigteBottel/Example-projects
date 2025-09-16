package nel.marco

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope


private suspend fun supervisorExample() = supervisorScope {
    executeAsyncJobs()
}

private suspend fun coroutineExample() = coroutineScope {
    executeAsyncJobs()
}

private suspend fun CoroutineScope.executeAsyncJobs(): Any {
    val job1 = async {
        delay(150)
        println("Job 1 finished")
    }

    val job2 = async {
        delay(200L)
        throw RuntimeException("Job 2 failed")
    }

    val job3 = async {
        delay(300)
        println("Job 3 finished")
    }

    return try {
        awaitAll(job1, job2, job3)
    } catch (e: Exception) {
        println("Caught exception: ${e.message}")
    }
}


fun main(): Unit = runBlocking {
    println("=== SupervisorScope Example ===")
    runCatching {
        supervisorExample()
    }

    println("\n=== CoroutineScope Example ===")
    runCatching {
        coroutineExample()
    }
}

