package me.marco

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import kotlin.system.measureTimeMillis


/**
BufferedReader + FileReader -> 4000000 lines in 217 ms
BufferedReader + FileReader -> 40000000 lines in 1541 ms
BufferedReader + FileReader -> 400000000 lines in 14225 ms
 */
fun main() {
    readFile(smallFile)
    readFile(mediumFile)
    readFile(largeFile)
}

private fun readFile(file: File) {
    var lineCount = 0L

    val elapsed = measureTimeMillis {

        val br = BufferedReader(FileReader(file))

        var line: String? = br.readLine()

        while (line != null) {
            line = br.readLine()
            lineCount++
        }
    }

    println("BufferedReader + FileReader -> $lineCount lines in $elapsed ms")
}
