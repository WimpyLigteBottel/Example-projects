package me.marco

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val file = File("./target/largefile.txt")
    var lineCount = 0L

    val elapsed = measureTimeMillis {
        file.bufferedReader().useLines { lines ->
            lineCount = lines.count().toLong()
        }
    }

    println("BufferedReader -> $lineCount lines in $elapsed ms")
}
