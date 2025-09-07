package me.marco

import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path
import kotlin.system.measureTimeMillis

fun main() {
    val file = Path("./target/largefile.txt")

    repeat(5) {
        countLinesFileChannelOptimized(file, 1024 * 1024 * 16)
        countLinesFileChannelOptimized(file, "030c566a-433f-4a34-91aa-d72e756ca5fe\n".toByteArray().size * 100000)
    }
}

fun countLinesFileChannelOptimized(file: Path, bufferSize: Int = 16 * 1024 * 1024) {
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