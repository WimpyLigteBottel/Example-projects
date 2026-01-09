package me.marco.order.api

import me.marco.order.api.models.AddItemRequest
import me.marco.order.api.models.CreateOrderRequest
import me.marco.order.api.models.MarkAsPaidRequest
import me.marco.order.api.models.OrderResponse
import org.springframework.http.ResponseEntity
import org.springframework.modulith.NamedInterface
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.DeleteExchange
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange

@HttpExchange("/api/orders", accept = ["application/json"])
@NamedInterface("api")
interface OrderClient {
    @PostExchange
    fun createOrder(
        @RequestBody request: CreateOrderRequest,
    ): ResponseEntity<OrderResponse>

    @PostExchange("/{orderId}/items")
    fun addItem(
        @PathVariable orderId: String,
        @RequestBody request: AddItemRequest,
    ): ResponseEntity<OrderResponse>

    @PostExchange("/{orderId}/pay")
    fun markAsPaid(
        @PathVariable orderId: String,
        @RequestBody request: MarkAsPaidRequest,
    ): ResponseEntity<OrderResponse>

    @GetExchange("/{orderId}")
    fun getOrder(
        @PathVariable orderId: String,
    ): ResponseEntity<OrderResponse>

    @GetExchange("/find")
    fun findLinkedOrderIds(
        @RequestParam customerId: String,
    ): ResponseEntity<List<String>>


    @DeleteExchange("/{orderId}")
    fun deleteOrder(@PathVariable orderId: String): ResponseEntity<*>
}