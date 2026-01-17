package me.marco.order.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange

@RequestMapping("/api/orders")
interface OrderInterface {
    @PostMapping
    fun createOrder(
        @RequestBody request: CreateOrderRequest,
    ): ResponseEntity<OrderResponse>

    @GetMapping("/{orderId}")
    fun getOrder(
        @PathVariable orderId: String,
    ): ResponseEntity<OrderResponse>


    @GetMapping
    fun getOrders(
        @RequestParam orderId: List<String>,
    ): ResponseEntity<List<OrderResponse>>

    @DeleteMapping("/{orderId}")
    fun deleteOrder(@PathVariable orderId: String): ResponseEntity<*>
}