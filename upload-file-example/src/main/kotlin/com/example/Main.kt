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
import org.springframework.web.reactive.function.client.WebClient
import java.nio.charset.Charset
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
        @RequestPart("file1", required = false) file: Resource? = null,
        @RequestPart("file2", required = false) file2: Resource? = null
    ): String {

        file?.let {
            log.info("fileName={}", it.filename)
            log.info("file contents={}", it.getContentAsString(Charset.defaultCharset()))
            createCopy(it, "1")
        }

        file2?.let {
            log.info("fileName={}", it.filename)
            log.info("file contents={}", it.getContentAsString(Charset.defaultCharset()))
            createCopy(it, "2")
        }

        if (file == null && file2 == null)
            return "No file"
        else
            return "File uploaded [file1=${file?.filename};file2=${file2?.filename}]"
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
        val builder = MultipartBodyBuilder()

        kotlin.runCatching {
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

            log.info("${response.statusCode};${response.body}")
        }

    }
}