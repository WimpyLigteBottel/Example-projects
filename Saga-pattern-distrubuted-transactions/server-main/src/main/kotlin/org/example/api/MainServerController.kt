package org.example.api

import org.example.dto.ActionAndState
import org.example.dto.RequestingOrder
import org.example.service.ProcessingService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.stream.IntStream

@RestController
class MainServerController(
    private val processingService: ProcessingService
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/start")
    fun startProcess(@RequestParam(required = false, defaultValue = "1") repeat: Int) {

        IntStream.rangeClosed(0, repeat).boxed().forEach {
            processingService.startProcess(RequestingOrder())
        }
    }


    @GetMapping("/listen")
    fun handleOrder(@RequestParam id: String, @RequestBody actionAndState: ActionAndState) {
        processingService.handleResponse(id, actionAndState)
    }
}