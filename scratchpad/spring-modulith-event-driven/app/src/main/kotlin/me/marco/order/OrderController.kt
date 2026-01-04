package me.marco.order

import me.marco.events.CreateOrderEvent
import me.marco.events.FireEventService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/orders")
class OrderController(
    val fireEventService: FireEventService
) {

    var logger: Logger = LoggerFactory.getLogger(this::class.java)

    val orders = mutableMapOf<String, Order>()

    @GetMapping("/create")
    fun createOrder(): String {
        val orderID = UUID.randomUUID().toString()
        orders.put(orderID, Order(orderID, emptyList(), false))
        fireEventService.fireEvent(CreateOrderEvent(orderID))
        return orderID
    }

    @GetMapping("/{id}")
    fun retrieve(@PathVariable id: String): Order? {
        return orders[id]
    }

    @PostMapping("/{id}")
    fun update(@RequestBody order: Order): Order? {
        orders[order.id] = order

        return orders[order.id]
    }

}