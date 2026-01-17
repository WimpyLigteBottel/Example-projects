package me.marco.order.api

import me.marco.order.dao.OrderItemJdbcClient
import me.marco.order.dao.OrderJdbcClient
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull

@RestController
class OrderController(
    private val orderJdbcClient: OrderJdbcClient,
    private val orderItemJdbcClient: OrderItemJdbcClient
) : OrderInterface, OrderClient {

    override fun createOrder(
        request: CreateOrderRequest,
    ): ResponseEntity<OrderResponse> {
        val id = orderJdbcClient.createOrder()

        val order = orderJdbcClient.getOrder(id)

        order.getOrNull()?.let {
            return try {
                ResponseEntity.ok(
                    OrderResponse.OK(
                        orderId = it.orderId.toString(),
                        items = emptyList(),
                        totalAmount = it.totalAmount,
                        isPaid = it.isPaid,
                        version = it.version
                    )
                )
            } catch (_: Exception) {
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(OrderResponse.Problem("Failed to create order"))
            }
        }

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(OrderResponse.Problem("Failed to create order"))
    }

    override fun getOrder(
        orderId: String,
    ): ResponseEntity<OrderResponse> {

        val order = orderJdbcClient.getOrderWithItems(orderId.toLong())

        order.getOrNull()?.let {
            return try {
                ResponseEntity.ok(
                    OrderResponse.OK(
                        orderId = it.orderId.toString(),
                        items = it.items.map {
                            Item(
                                id = it.id,
                                name = it.item
                            )
                        },
                        totalAmount = it.totalAmount,
                        isPaid = it.isPaid,
                        version = it.version
                    )
                )
            } catch (_: Exception) {
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(OrderResponse.Problem("Failed to create order"))
            }
        }

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(OrderResponse.Problem("Failed to create order"))
    }

    override fun getOrders(orderId: List<String>): ResponseEntity<List<OrderResponse>> {
        val allOrders = orderJdbcClient.getAllOrders(orderId.map { it.toLong() })

        val ordersMapped = allOrders.map {
            OrderResponse.OK(
                orderId = it.orderId.toString(),
                items = emptyList(),
                totalAmount = it.totalAmount,
                isPaid = it.isPaid,
                version = it.version
            )
        }
        return ResponseEntity.ok(ordersMapped)
    }

    override fun deleteOrder(orderId: String): ResponseEntity<Any> {

        return try {
            orderJdbcClient.deleteOrder(orderId)
            ResponseEntity.accepted().body(OrderResponse.Accepted())
        } catch (_: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(OrderResponse.Problem("Failed to delete order"))
        }
    }

}