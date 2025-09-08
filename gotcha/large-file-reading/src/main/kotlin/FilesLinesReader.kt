package me.marco

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.system.measureTimeMillis

/**
Files.lines -> 4000000 lines in 187 ms
Files.lines -> 40000000 lines in 1429 ms
Files.lines -> 400000000 lines in 15783 ms
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