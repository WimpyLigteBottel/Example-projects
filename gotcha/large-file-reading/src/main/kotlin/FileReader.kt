package me.marco

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import kotlin.system.measureTimeMillis

/**
 * BufferedReader + FileReader -> 6000000 lines in 316 ms
 * BufferedReader + FileReader -> 60000000 lines in 2290 ms
 * BufferedReader + FileReader -> 1036789776 lines in 37173 ms
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
