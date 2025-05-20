package com.example

import org.slf4j.LoggerFactory
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.file.Path

@RestController
class UploadController {

    private val log = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/upload")
    fun handleFile(
        @RequestPart("file1", required = false) file: FilePart? = null,
        @RequestPart("file2", required = false) file2: FilePart? = null
    ): String {

        file?.let {
            createCopy(it)
        }

        file2?.let {
            createCopy(it)
        }

        if (file == null && file2 == null)
            return "No file"
        else
            return "File uploaded [file1=${file?.filename()};file2=${file2?.filename()}]"
    }

    @PostMapping("/upload-multiples")
    fun handleFiles(
        @RequestPart("files", required = false) files: Flux<FilePart>,
    ): Mono<Long> {
        return files.map { createCopy(it) }.count()
    }

    private fun createCopy(file: FilePart) {
        log.info("fileName={}", file.name())
        val x =
            Path.of("C:\\code\\Example-projects\\spring-stuff\\upload-file-reactive-example\\target\\${file.filename()}")
        file.transferTo(x).toFuture().join()
    }
}