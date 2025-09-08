package me.marco

import java.io.File
import kotlin.system.measureTimeMillis

/**
BufferedReader -> 4000000 lines in 210 ms
BufferedReader -> 40000000 lines in 1387 ms
BufferedReader -> 400000000 lines in 13689 ms
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
