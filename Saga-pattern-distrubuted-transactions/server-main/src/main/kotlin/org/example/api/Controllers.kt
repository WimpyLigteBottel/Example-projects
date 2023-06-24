package org.example.api

import org.example.Action
import org.example.repo.RequestingOrder
import org.example.service.ProcessingService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class MainServerController(
    private val processingService: ProcessingService
) {


    @GetMapping("/start")
    fun startProcess(@RequestParam(required = false, defaultValue = "1") repeat: Int) {
        for (x in 0..repeat)
            processingService.startProcess(RequestingOrder())

    }

}

@RestController
class ListenerController(
    private val processingService: ProcessingService
) {
    @PostMapping("/listen")
    fun handleOrder(
        @RequestBody action: Action
    ) {
        processingService.handleResponse(action)
    }
}