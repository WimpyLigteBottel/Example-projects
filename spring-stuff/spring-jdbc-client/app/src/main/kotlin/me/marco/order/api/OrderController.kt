package me.marco.order.api

import me.marco.order.api.models.CreateOrderRequest
import me.marco.order.api.models.OrderResponse
import me.marco.order.client.OrderClient
import me.marco.order.dao.OrderDao
import me.marco.order.dao.transform
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderDao: OrderDao
) : OrderClient {

    override fun createOrder(
        request: CreateOrderRequest,
    ): ResponseEntity<OrderResponse> {
        val id = orderDao.createOrder()

        val order = orderDao.getOrder(id).getOrNull() ?: return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(OrderResponse.Problem("Failed to create order"))

        return ResponseEntity.ok(
            OrderResponse.OK(
                order = order.transform()
            )
        )
    }

    override fun getOrder(
        orderId: String,
    ): ResponseEntity<OrderResponse> {

        val order = orderDao.getOrderWithItems(orderId.toLong()).getOrNull() ?: return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(OrderResponse.Problem("Failed to create order"))

        return ResponseEntity.ok(
            OrderResponse.OK(
                order = order.transform()
            )
        )
    }

    override fun getOrders(orderId: List<String>): ResponseEntity<List<OrderResponse>> {
        val allOrders = orderDao.getAllOrders(orderId.map { it.toLong() })

        val ordersMapped = allOrders.map {
            OrderResponse.OK(it.transform())
        }
        return ResponseEntity.ok(ordersMapped)
    }

    override fun deleteOrder(orderId: String): ResponseEntity<Any> {

        return try {
            orderDao.deleteOrder(orderId)
            ResponseEntity.accepted().body(OrderResponse.Accepted())
        } catch (_: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(OrderResponse.Problem("Failed to delete order"))
        }
    }

}