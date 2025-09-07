package me.marco

import java.io.File
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption
import java.util.*
import kotlin.io.path.Path
import kotlin.system.measureTimeMillis

val largeFile = File("./target/largefile.txt")
val mediumFile = File("./target/mediumFile.txt")
val smallFile = File("./target/smallFile.txt")


fun main() {

    largeFile.parentFile?.delete()
    mediumFile.parentFile?.delete()
    smallFile.parentFile?.delete()

    largeFile.parentFile?.mkdirs()
    mediumFile.parentFile?.mkdirs()
    smallFile.parentFile?.mkdirs()

    val numLines = 400_000_000 // 10M UUIDs for testing

    largeFile(numLines, largeFile)
    largeFile(numLines / 40, mediumFile)
    largeFile(numLines / 400, smallFile)
}

private fun largeFile(numLines: Int, file: File) {
    val elapsed3 = measureTimeMillis {
        val buffer = ByteBuffer.allocateDirect(655360) // 64KB buffer
        FileChannel.open(
            Path(file.path),
            StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND
        )
            .use { channel ->

                repeat(numLines) {
                    val line = "${UUID.randomUUID()}\n"
                    val bytes = line.toByteArray()
                    // If buffer can't fit more data, write and clear
                    if (buffer.remaining() < bytes.size) {
                        buffer.flip()
                        channel.write(buffer)
                        buffer.clear()
                    }

                    buffer.put(bytes)
                }

                // Write remaining data
                if (buffer.position() > 0) {
                    buffer.flip()
                    channel.write(buffer)
                }
            }
    }
    println("FileChannel with buffer: $elapsed3 ms")
}