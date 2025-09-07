package me.marco

import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path
import kotlin.system.measureTimeMillis

/**
 * Line count = 6000000, took 170 ms , bufferSize = 16777216
 * Line count = 60000000, took 1412 ms , bufferSize = 16777216
 * Line count = 1036789776, took 25219 ms , bufferSize = 16777216
 */
fun main() {

    repeat(5) {
        readFile(Path(smallFile.path), 1024 * 1024 * 16)
        readFile(Path(mediumFile.path), 1024 * 1024 * 16)
        readFile(Path(largeFile.path), 1024 * 1024 * 16)
    }
}

fun readFile(file: Path, bufferSize: Int = 16 * 1024 * 1024) {
    var lineCount = 0L
    val newlineByte = '\n'.code.toByte()

    val elapsed = measureTimeMillis {
        FileChannel.open(file, StandardOpenOption.READ).use { channel ->
            val buffer = ByteBuffer.allocateDirect(bufferSize)

            while (channel.read(buffer) > 0) {
                buffer.flip()
                while (buffer.hasRemaining()) {
                    if (buffer.get() == newlineByte) {
                        lineCount++
                    }
                }
                buffer.clear()
            }
        }
    }
    println("Line count = $lineCount, took $elapsed ms , bufferSize = $bufferSize")
}