package me.marco

import java.io.File
import java.util.Scanner
import kotlin.system.measureTimeMillis

/**
Scanner -> 4000000 lines in 1005 ms
Scanner -> 40000000 lines in 9194 ms
Scanner -> 400000000 lines in 81187 ms
 */
fun main() {
    readFile(smallFile)
    readFile(mediumFile)
    readFile(largeFile)
}

private fun readFile(file: File) {
    var lineCount = 0L

    val elapsed = measureTimeMillis {

        Scanner(file).useDelimiter("\n").use { scanner ->
            while(scanner.hasNextLine()){
                scanner.nextLine()
                lineCount++
            }
        }
    }

    println("Scanner -> $lineCount lines in $elapsed ms")
}
