package me.marco.order.api

import me.marco.order.dao.OrderItemJdbcClient
import me.marco.order.dao.OrderJdbcClient
import nl.wykorijnsburger.kminrandom.minRandom
import nl.wykorijnsburger.kminrandom.minRandomCached
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull

@RestController
class OrderController(
    private val orderJdbcClient: OrderJdbcClient,
    private val orderItemJdbcClient: OrderItemJdbcClient
) : OrderInterface {

    override fun createOrder(
        request: CreateOrderRequest,
    ): ResponseEntity<OrderResponse> {
        val id = orderJdbcClient.createOrder()

        val order = orderJdbcClient.getOrder(id.toString())

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
        return OrderResponse.OK(
            orderId = minRandomCached(),
            items = listOf(minRandom(), minRandom(), minRandom()),
            totalAmount = minRandomCached(),
            isPaid = minRandomCached(),
            version = minRandomCached()
        ).let {
            ResponseEntity.ok(it)
        }
    }

    override fun deleteOrder(orderId: String): ResponseEntity<Any> {
        return try {
            ResponseEntity.accepted().body(OrderResponse.Accepted())
        } catch (_: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(OrderResponse.Problem("Failed to delete order"))
        }
    }
}