package me.marco.item

import me.marco.events.AddItemEvent
import me.marco.events.FireEventService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/items")
class AddItemController(
    val fireEventService: FireEventService
) {

    var logger: Logger = LoggerFactory.getLogger(this::class.java)


    @PostMapping
    fun addItem(@RequestParam orderId: String, @RequestParam orderItemId: String): ResponseEntity<String> {
        fireEventService.fireEvent(AddItemEvent(orderID = orderId, orderItemId = orderItemId))

        return ResponseEntity.ok("Success")
    }
}

