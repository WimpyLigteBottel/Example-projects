package me.marco.order

import me.marco.orderprocessing.service.OrderService
import me.marco.order.api.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService,
) : OrderClient {
    @PostMapping
    override fun createOrder(
        @RequestBody request: CreateOrderRequest,
    ): ResponseEntity<OrderResponse> {

        try {
            val orderId = orderService.createOrder(request.internalize())
            val order = orderService.getOrder(orderId)
            return ResponseEntity.ok(order.toResponse())
        } catch (_: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @PostMapping("/{orderId}/items")
    override fun addItem(
        @PathVariable orderId: String,
        @RequestBody request: AddItemRequest,
    ): ResponseEntity<OrderResponse> {
        try {
            val orderId = orderService.addItem(orderId, request.internalize())
            val order = orderService.getOrder(orderId)
            return ResponseEntity.ok(order.toResponse())
        } catch (_: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @PostMapping("/{orderId}/pay")
    override fun markAsPaid(
        @PathVariable orderId: String,
        @RequestBody request: MarkAsPaidRequest,
    ): ResponseEntity<OrderResponse> {

        try {
            val orderId = orderService.markAsPaid(orderId, request.internalize())
            val order = orderService.getOrder(orderId)
            return ResponseEntity.ok(order.toResponse())
        } catch (_: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @GetMapping("/{orderId}")
    override fun getOrder(
        @PathVariable orderId: String,
    ): ResponseEntity<OrderResponse> {
        val order = orderService.getOrder(orderId)

        if (order.version == 0L || order.deleted)
            return ResponseEntity.notFound().build()

        return ResponseEntity.ok(order.toResponse())
    }

    @DeleteMapping("/{orderId}")
    override fun deleteOrder(orderId: String): ResponseEntity<Any> {

        try {
            val orderId = orderService.deleteOrder(orderId)
            return ResponseEntity.accepted().build()
        } catch (_: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }
}
