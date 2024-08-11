package nel.marco

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlin.system.measureTimeMillis

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
suspend fun main() {

    val time = measureTimeMillis {

        val fast = generateNumbers(10)
        val slow = generateNumbers(10)

        withContext(Dispatchers.IO) {
            val a = async(Dispatchers.IO) { fast.toList() }
            val b = async(Dispatchers.IO) { slow.toList() }

            awaitAll(a, b)

            println(a.await())
            println(b.await())

        }
    }
    println("$time ms")
}


suspend fun generateNumbers(amountOfNumber: Int) = withContext(Dispatchers.IO) {
    flow {
        repeat(amountOfNumber) {
            delay(1000)
            emit(it)
        }
    }
}