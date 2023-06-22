package org.example.api

import org.example.dto.ActionAndState
import org.example.dto.RequestingOrder
import org.example.service.ProcessingService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.locks.StampedLock
import java.util.stream.IntStream

@RestController
class MainServerController(
    private val processingService: ProcessingService
) {

    private val log = LoggerFactory.getLogger(this::class.java)
    private val stampedLock = StampedLock()


    @GetMapping("/start")
    fun startProcess(@RequestParam(required = false, defaultValue = "1") repeat: Int) {
        val stamp = stampedLock.writeLock()
        try {
            IntStream.rangeClosed(0, repeat).boxed().forEach {

                processingService.startProcess(RequestingOrder())

            }
        } finally {
            stampedLock.unlock(stamp)
        }
    }


    @PostMapping("/listen")
    fun handleOrder(@RequestParam id: String, @RequestBody actionAndState: ActionAndState) {
        val stamp = stampedLock.writeLock()
        try {
            processingService.handleResponse(id, actionAndState)
        } finally {
            stampedLock.unlock(stamp)
        }
    }
}