package org.example.api

import org.example.repo.RequestingOrder
import org.example.service.ProcessingService
import org.springframework.web.bind.annotation.*

@RestController
class MainServerController(
    private val processingService: ProcessingService
) {

    @GetMapping("/start")
    fun startProcess(@RequestParam(required = false, defaultValue = "1") repeat: Int) {
        repeat(repeat) {
            processingService.startProcess(RequestingOrder())
        }
    }

    @PostMapping("/listen")
    fun handleOrder(@RequestBody action: Action) {
        processingService.handleResponse(action)
    }
}