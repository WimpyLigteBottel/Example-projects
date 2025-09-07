package me.marco

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.system.measureTimeMillis

/**
 * Files.lines -> 6000000 lines in 357 ms
 * Files.lines -> 60000000 lines in 2196 ms
 * Files.lines -> 1036789776 lines in 37522 ms
 */
fun main() {
    readFile(Path(smallFile.path))
    readFile(Path(mediumFile.path))
    readFile(Path(largeFile.path))
}

private fun readFile(path: Path) {
    var lineCount = 0L

    val elapsed = measureTimeMillis {
        Files.lines(path).use { stream ->
            lineCount = stream.count()
        }
    }

    println("Files.lines -> $lineCount lines in $elapsed ms")
}