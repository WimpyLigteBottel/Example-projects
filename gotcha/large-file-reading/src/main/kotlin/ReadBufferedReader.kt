package me.marco

import java.io.File
import kotlin.system.measureTimeMillis

/**
 * BufferedReader -> 6000000 lines in 408 ms
 * BufferedReader -> 60000000 lines in 2278 ms
 * BufferedReader -> 1036789776 lines in 42631 ms
 */
fun main() {
    readFile(smallFile)
    readFile(mediumFile)
    readFile(largeFile)
}

private fun readFile(file: File) {
    var lineCount = 0L

    val elapsed = measureTimeMillis {
        file.bufferedReader().useLines { lines ->
            lineCount += lines.count()
        }
    }

    println("BufferedReader -> $lineCount lines in $elapsed ms")
}
