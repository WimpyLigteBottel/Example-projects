package me.marco.order.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.DeleteExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange


@HttpExchange("/api/orders/{orderId}/items", accept = ["application/json"])
interface OrderItemClient {

    @PostExchange()
    fun addItemToOrder(
        @PathVariable orderId: Long,
        @RequestParam item: String
    ): ResponseEntity<OrderResponse>

    @DeleteExchange("/{itemId}")
    fun deleteItem(
        @PathVariable orderId: Long,
        @PathVariable itemId: Long,
    ): ResponseEntity<*>
}