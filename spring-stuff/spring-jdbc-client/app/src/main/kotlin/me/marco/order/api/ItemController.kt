package me.marco.order.api

import me.marco.order.dao.OrderItemJdbcClient
import me.marco.order.dao.OrderJdbcClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/orders/{orderId}/items")
class ItemController(
    private val orderJdbcClient: OrderJdbcClient,
    private val orderItemJdbcClient: OrderItemJdbcClient
) : OrderItemClient {

    @PostMapping
    override fun addItemToOrder(
        orderId: Long,
        item: String
    ): ResponseEntity<Long> {
        val item = orderItemJdbcClient.addItem(orderId, item)

        return ResponseEntity
            .ok(item)
    }

    override fun deleteItem(orderId: Long, itemId: Long): ResponseEntity<*> {
        orderItemJdbcClient.removeItem(itemId)

        return ResponseEntity.noContent().build<Any>()
    }
}