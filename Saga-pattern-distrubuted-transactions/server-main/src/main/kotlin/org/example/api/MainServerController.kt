package org.example.api

import org.example.dto.ActionAndState
import org.example.dto.RequestingOrder
import org.example.service.ProcessingService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.StampedLock
import java.util.stream.IntStream

@RestController
class MainServerController(
    private val processingService: ProcessingService
) {

    private val log = LoggerFactory.getLogger(this::class.java)
    private val stampedLock = StampedLock()


    @Scheduled(fixedRate = 5000, timeUnit = TimeUnit.MILLISECONDS)
    fun processActions() {
        startProcess(10)
    }


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