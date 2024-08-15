package nel.marco

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    runCatching { runBlocking { sharedStateFlowExample() } }
}


/**
 * Showcases how single event can be consumed by multiple other flows and perform an action
 */
suspend fun sharedStateFlowExample() {
    val people = createMutableSharedFlows()

    withContext(Dispatchers.IO) {

        //Listen to messages
        processMessagesWhenReceivingEvent(people)

        //Generate numbers and emit to each people
        generateNumbers(10, 100)
            .collect { number ->
                people.forEach {
                    launch { it.second.emit(number.toString()) }
                }
            }

        // stops the entire context when it gets here otherwise it pauses forever
        cancel("Stop processing because flows go on forever!")
    }

}

/**
 * This launches collect action ON EACH flow and will endlessly listen
 */
private fun CoroutineScope.processMessagesWhenReceivingEvent(persons: MutableList<Pair<String, MutableSharedFlow<String>>>) {
    persons.forEach { person ->
        launch {
            person.second
                .onCompletion {
                    println("${person.first}: I am done! (Think of this like finally block)")
                }
                .collect {
                    val name = person.first
                    println("$name processed $it")
                }
        }
    }
}


suspend fun createMutableSharedFlows() = mutableListOf<Pair<String, MutableSharedFlow<String>>>(
    "a" to MutableSharedFlow(),
    "b" to MutableSharedFlow()
)


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
        .onStart { println("Starting to generate numbers") }
        .onCompletion {
            println("Done generating numbers")
            cancel("Done generating numbers")
        }
}