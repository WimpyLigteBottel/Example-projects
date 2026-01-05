package me.marco.order

import me.marco.actions.OrderCommandHandler
import me.marco.actions.models.Command.AddItemCommand
import me.marco.actions.models.Command.CreateOrderCommand
import me.marco.actions.models.Command.MarkOrderAsPaidCommand
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


// ============= DTOs =============
data class CreateOrderRequest(val orderId: String? = null)

data class AddItemRequest(
    val itemId: String,
    val name: String,
    val price: Double,
    val quantity: Int
)

data class MarkAsPaidRequest(val paymentMethod: String)

data class OrderResponse(
    val orderId: String,
    val items: List<OrderItem>,
    val totalAmount: Double,
    val isPaid: Boolean,
    val version: Long,
    val error: String? = null
)

fun Order.toResponse() = OrderResponse(
    orderId = id,
    items = items,
    totalAmount = totalAmount,
    isPaid = isPaid,
    version = version
)


@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val commandHandler: OrderCommandHandler
) {
    @PostMapping
    fun createOrder(@RequestBody request: CreateOrderRequest): ResponseEntity<OrderResponse> {
        val command = CreateOrderCommand(aggregateId = request.orderId ?: UUID.randomUUID().toString())

        return commandHandler.handle(command).fold(
            onSuccess = {
                val order = commandHandler.getOrder(command.aggregateId)
                ResponseEntity.ok(order.toResponse())
            },
            onFailure = { ResponseEntity.status(HttpStatus.BAD_REQUEST).build() }
        )
    }

    @PostMapping("/{orderId}/items")
    fun addItem(
        @PathVariable orderId: String,
        @RequestBody request: AddItemRequest
    ): ResponseEntity<OrderResponse> {
        val command = AddItemCommand(
            aggregateId = orderId,
            itemId = request.itemId,
            name = request.name,
            price = request.price,
            quantity = request.quantity
        )

        return commandHandler.handle(command).fold(
            onSuccess = {
                val order = commandHandler.getOrder(orderId)
                ResponseEntity.ok(order.toResponse())
            },
            onFailure = { error ->
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(OrderResponse(orderId, emptyList(), 0.0, false, -1, error.message))
            }
        )
    }

    @PostMapping("/{orderId}/pay")
    fun markAsPaid(
        @PathVariable orderId: String,
        @RequestBody request: MarkAsPaidRequest
    ): ResponseEntity<OrderResponse> {
        val command = MarkOrderAsPaidCommand(
            aggregateId = orderId,
            paymentMethod = request.paymentMethod
        )

        return commandHandler.handle(command).fold(
            onSuccess = {
                val order = commandHandler.getOrder(orderId)
                ResponseEntity.ok(order.toResponse())
            },
            onFailure = { error ->
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(OrderResponse(orderId, emptyList(), 0.0, false, -1, error.message))
            }
        )
    }

    @GetMapping("/{orderId}")
    fun getOrder(@PathVariable orderId: String): ResponseEntity<OrderResponse> {
        val order = commandHandler.getOrder(orderId)
        return if (order.items.isEmpty() && !order.isPaid) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(order.toResponse())
        }
    }
}
