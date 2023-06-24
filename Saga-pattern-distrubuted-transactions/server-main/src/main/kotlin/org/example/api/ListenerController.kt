package org.example.api

import org.example.ActionAndState
import org.example.service.ProcessingService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class ListenerController(
    private val processingService: ProcessingService
) {
    @PostMapping("/listen")
    fun handleOrder(
        @RequestBody actionAndState: ActionAndState
    ) {
        processingService.handleResponse(actionAndState)
    }
}