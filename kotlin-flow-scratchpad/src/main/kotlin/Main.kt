package nel.marco

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    runCatching { runBlocking { sharedStateFlowExample() } }
}


/**
 * Showcases how single event can be consumed by multiple other flows and perform an action
 */
suspend fun sharedStateFlowExample() {
    val personA = MutableSharedFlow<String>()
    val personB = MutableSharedFlow<String>()

    withContext(Dispatchers.IO) {
        launch {
            personA.collect {
                println(it)
            }
        }
        launch {
            personB.collect {
                println(it)
            }
        }

        generateNumbers(10, 100)
            .collect {
                personA.emit("Person A = $it")
                personB.emit("Person B = $it")
            }

        cancel("Stop processing because flows go on forever!")
    }

}


/**
 * Generates numbers until the amount specified has been generated with delay in between based on the delayAmount in milliseconds
 */
suspend fun generateNumbers(amountOfNumber: Int, delayAmount: Long = 1000) = withContext(Dispatchers.IO) {
    flow {
        repeat(amountOfNumber) {
            delay(delayAmount)
            emit(it)
        }
    }
}