package me.marco

import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.system.measureTimeMillis

fun main() {
    val file = Path("./target/largefile.txt")
    var lineCount = 0L

    val elapsed = measureTimeMillis {
        Files.lines(file).use { stream ->
            lineCount = stream.count()
        }
    }

    println("Files.lines -> $lineCount lines in $elapsed ms")
}
