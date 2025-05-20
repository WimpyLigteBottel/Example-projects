package com.example

import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import reactor.core.publisher.Flux
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.*

@RestController
class UploadController {

    private val log = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/upload")
    fun handleFile(
        @RequestPart("file1", required = false) file: FilePart? = null,
        @RequestPart("file2", required = false) file2: FilePart? = null
    ): String {

        file?.let {
            log.info("fileName={}", it.name())
            createCopy(it)
        }

        file2?.let {
            log.info("fileName={}", it.name())
            createCopy(it)
        }

        if (file == null && file2 == null)
            return "No file"
        else
            return "File uploaded [file1=${file?.name()};file2=${file2?.name()}]"
    }

    @PostMapping("/upload-multiples")
    fun handleFiles(
        @RequestPart("files", required = false) files: Flux<FilePart> = Flux.empty(),
    ): String {
        return "Files uploaded [count=${files.map { createCopy(it) }.count().toFuture().join()}]"
    }

    private fun createCopy(file: FilePart) {
        log.info("Going to create copy now")

        val x = Path.of("C:\\code\\Example-projects\\spring-stuff\\upload-file-reactive-example\\target").resolve(file.filename())
        file.transferTo(x).toFuture().join()
    }
}