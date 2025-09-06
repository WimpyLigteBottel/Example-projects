package nel.marco

import kotlinx.coroutines.*

class RunningService(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    private var job: Job? = null

    fun start() {
        if (job != null) {
            stop()
        }

        job = scope.launch {
            runCatching {
                delay(1000)
                println("1 scenarios configured")
                delay(1000)
                println("2 Loadtest started")
                delay(5000)
                println("3 loadtest completed")
            }
                .onSuccess {
                    println("4 SUCCESS")
                }
                .onFailure {
                    println("5. Failed because of $it")
                }
        }
    }

    fun stop() {
        println("Stopping...")
        job?.cancel(CancellationException("Stopped by user"))
        job = null
    }
}
