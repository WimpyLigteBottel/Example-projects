package me.marco.order.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.service.annotation.DeleteExchange
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange


@HttpExchange("/api/orders", accept = ["application/json"])
interface OrderClient {

    @PostExchange
    fun createOrder(
        @RequestBody request: CreateOrderRequest,
    ): ResponseEntity<OrderResponse>

    @GetExchange("/{orderId}")
    fun getOrder(
        @PathVariable orderId: String,
    ): ResponseEntity<OrderResponse>

    @DeleteExchange("/{orderId}")
    fun deleteOrder(@PathVariable orderId: String): ResponseEntity<*>
}