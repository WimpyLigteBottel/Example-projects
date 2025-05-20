package com.example

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.io.ClassPathResource
import org.springframework.http.ContentDisposition
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Flux
import java.nio.file.Path
import java.util.*

@SpringBootApplication
open class Launcher


fun main() {
    runApplication<Launcher>()
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
            .bodyToMono<String>().toFuture().join()

        log.info("$response")
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
            .bodyToMono<String>(String::class.java).toFuture().join()


        log.info("${response}")
    }
}