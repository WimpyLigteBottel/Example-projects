import kotlinx.coroutines.*

private suspend fun supervisorExample() = supervisorScope {
    val jobs = (1..10).map { i ->
        async {
            delay(200L * i)
            if (i == 5) {
                throw RuntimeException("Job $i failed")
            }
            println("Job $i finished")
            i
        }
    }

    try {
        jobs.awaitAll() // will throw when job 5 fails
    } catch (e: Exception) {
        println("Caught exception: ${e.message}")
    }
}

private suspend fun coroutineExample() = coroutineScope {
    val jobs = (1..10).map { i ->
        async {
            delay(200L * i)
            if (i == 5) {
                throw RuntimeException("Job $i failed")
            }
            println("Job $i finished")
            i
        }
    }

    try {
        jobs.awaitAll()
    } catch (e: Exception) {
        println("Caught exception: ${e.message}")
    }
}




fun main(): Unit = runBlocking {
    println("=== SupervisorScope Example ===")
    supervisorExample()

    println("\n=== CoroutineScope Example ===")
    coroutineExample()
}
