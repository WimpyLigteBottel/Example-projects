package com.example

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.http.ContentDisposition
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.*

@SpringBootApplication
open class Launcher


fun main() {
    runApplication<Launcher>()
}

@RestController
open class Endpoint {

    private val log = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/upload")
    fun handleFile(
        @RequestPart("file1", required = false) file: MultipartFile? = null,
        @RequestPart("file2", required = false) file2: MultipartFile? = null
    ): String {

        file?.let {
            log.info("fileName={}", it.originalFilename)
            createCopy(it.resource, "1")
        }

        file2?.let {
            log.info("fileName={}", it.originalFilename)
            createCopy(it.resource, "2")
        }

        if (file == null && file2 == null)
            return "No file"
        else
            return "File uploaded [file1=${file?.originalFilename};file2=${file2?.originalFilename}]"
    }

    @PostMapping("/upload-multiples")
    fun handleFiles(
        @RequestPart("files", required = false) files: List<MultipartFile> = emptyList(),
    ): String {
        files.forEachIndexed { index, resource ->
            log.info("fileName={}", resource.originalFilename)
            createCopy(resource.resource, "$index")
        }

        return "Files uploaded [count=${files.size}]"
    }

    private fun createCopy(file: Resource, version: String) {
        log.info("Going to create copy now")
        Files.write(
            Path.of("C:\\code\\Example-projects\\upload-file-example\\target\\$version-${UUID.randomUUID()}-${file.filename}"),
            file.contentAsByteArray,
            StandardOpenOption.CREATE_NEW,
            StandardOpenOption.TRUNCATE_EXISTING
        )
    }
}

@Component
open class UploadOnRunTime : CommandLineRunner {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun run(vararg args: String?) {
        kotlin.runCatching {
            singleUpload()
            mutlipleUpload()
        }
    }

    private fun mutlipleUpload() {
        val builder = MultipartBodyBuilder()
        val file = ClassPathResource("picture.jpg").file

        for (x in 1..10) {
            builder
                .part("files", file.readBytes())
                .header(
                    "Content-Disposition",
                    ContentDisposition.formData().name("files").filename(x.toString()).build().toString()
                )
        }

        val response = WebClient.create()
            .post()
            .uri("http://localhost:8080/upload-multiples")
            .body(BodyInserters.fromMultipartData(builder.build()))
            .retrieve()
            .toEntity(String::class.java)
            .block()

        log.info("${response?.statusCode};${response?.body}")
    }

    private fun singleUpload() {
        val builder = MultipartBodyBuilder()

        val file = ClassPathResource("picture.jpg").file
        builder
            .part("file1", file.readBytes())
            .header(
                "Content-Disposition",
                ContentDisposition.formData().name("file1").filename(file.name).build().toString()
            )

        builder
            .part("file2", file.readBytes())
            .header(
                "Content-Disposition",
                ContentDisposition.formData().name("file2").filename(file.name).build().toString()
            )

        val response = WebClient.create().post().uri("http://localhost:8080/upload")
            .bodyValue(builder.build())
            .retrieve()
            .toEntity(String::class.java)
            .block()

        log.info("${response?.statusCode};${response?.body}")
    }
}