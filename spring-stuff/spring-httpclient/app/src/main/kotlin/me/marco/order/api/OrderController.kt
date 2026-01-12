package me.marco.order.api

import nl.wykorijnsburger.kminrandom.minRandom
import nl.wykorijnsburger.kminrandom.minRandomCached
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController() : OrderClient, OrderInterface {

    override fun createOrder(
        request: CreateOrderRequest,
    ): ResponseEntity<OrderResponse> {
        return try {
            ResponseEntity.ok(
                OrderResponse.OK(
                    orderId = minRandomCached(),
                    items = listOf(minRandom(), minRandom(), minRandom()),
                    totalAmount = minRandomCached(),
                    isPaid = minRandomCached(),
                    version = minRandomCached()
                )
            )
        } catch (_: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(OrderResponse.Problem("Failed to create order"))
        }
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