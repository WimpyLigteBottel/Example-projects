package me.marco.order

import me.marco.event.OrderCommandHandler
import me.marco.event.models.Command.*
import me.marco.order.api.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val commandHandler: OrderCommandHandler,
) : OrderClient {
    @PostMapping
    override fun createOrder(
        @RequestBody request: CreateOrderRequest,
    ): ResponseEntity<OrderResponse> {
        val command = CreateOrderCommand(aggregateId = request.orderId ?: UUID.randomUUID().toString())

        return commandHandler.handle(command).fold(
            onSuccess = {
                val order = commandHandler.getOrder(command.aggregateId)
                ResponseEntity.ok(order.toResponse())
            },
            onFailure = { ResponseEntity.status(HttpStatus.BAD_REQUEST).build() },
        )
    }

    @PostMapping("/{orderId}/items")
    override fun addItem(
        @PathVariable orderId: String,
        @RequestBody request: AddItemRequest,
    ): ResponseEntity<OrderResponse> {
        val command =
            AddItemCommand(
                aggregateId = orderId,
                itemId = request.itemId,
                name = request.name,
                price = request.price,
                quantity = request.quantity,
            )

        return commandHandler.handle(command).fold(
            onSuccess = {
                val order = commandHandler.getOrder(orderId)
                ResponseEntity.ok(order.toResponse())
            },
            onFailure = { error ->
                ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(OrderResponse(orderId, emptyList(), 0.0, false, -1, error.message))
            },
        )
    }

    @PostMapping("/{orderId}/pay")
    override fun markAsPaid(
        @PathVariable orderId: String,
        @RequestBody request: MarkAsPaidRequest,
    ): ResponseEntity<OrderResponse> {
        val command =
            MarkOrderAsPaidCommand(
                aggregateId = orderId,
                paymentMethod = request.paymentMethod,
            )

        return commandHandler.handle(command).fold(
            onSuccess = {
                val order = commandHandler.getOrder(orderId)
                ResponseEntity.ok(order.toResponse())
            },
            onFailure = { error ->
                ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(OrderResponse(orderId, emptyList(), 0.0, false, -1, error.message))
            },
        )
    }

    @GetMapping("/{orderId}")
    override fun getOrder(
        @PathVariable orderId: String,
    ): ResponseEntity<OrderResponse> {
        val order = commandHandler.getOrder(orderId)

        if (order.version == 0L || order.deleted)
            return ResponseEntity.notFound().build()

        return ResponseEntity.ok(order.toResponse())
    }

    @DeleteMapping("/{orderId}")
    override fun deleteOrder(orderId: String): ResponseEntity<Any> {
        val command = DeleteOrderCommand(orderId)
        return commandHandler.handle(command).fold(
            onSuccess = {
                ResponseEntity.accepted().build()
            },
            onFailure = { error ->
                ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build()
            },
        )
    }
}
