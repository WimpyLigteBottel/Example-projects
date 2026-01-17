package me.marco.order.api

import me.marco.order.client.OrderItemClient
import me.marco.order.dao.OrderItemJdbcClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.service.annotation.DeleteExchange

@RestController
@RequestMapping("/api/orders/{orderId}/items")
class ItemController(
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

    @DeleteExchange("/{itemId}")
    override fun deleteItem(orderId: Long, itemId: Long): ResponseEntity<*> {
        orderItemJdbcClient.removeItem(itemId)

        return ResponseEntity.noContent().build<Any>()
    }
}