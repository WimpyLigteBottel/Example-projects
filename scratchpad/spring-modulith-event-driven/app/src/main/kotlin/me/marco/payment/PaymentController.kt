package me.marco.payment

import me.marco.events.FireEventService
import me.marco.events.PaidOrderEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PaymentController(
    val fireEventService: FireEventService
) {

    var logger: Logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/pay/{id}")
    fun payOrder(@PathVariable id: String): ResponseEntity<String> {
        logger.info("paid!")
        fireEventService.fireEvent(PaidOrderEvent(id))
        return ResponseEntity.ok("Success")
    }
}