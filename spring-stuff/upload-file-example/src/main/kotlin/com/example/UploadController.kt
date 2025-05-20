package com.example

import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.*

@RestController
class UploadController {

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
            Path.of("target\\$version-${UUID.randomUUID()}-${file.filename}"),
            file.contentAsByteArray,
            StandardOpenOption.CREATE_NEW,
            StandardOpenOption.TRUNCATE_EXISTING
        )
    }
}