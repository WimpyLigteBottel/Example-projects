package me.marco.order.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
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

    @DeleteMapping("/{orderId}")
    fun deleteOrder(@PathVariable orderId: String): ResponseEntity<*>
}

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = OrderResponse.OK::class, name = "200"),
    JsonSubTypes.Type(value = OrderResponse.Problem::class, name = "400")
)
sealed class OrderResponse(type: Int) {

    data class OK(
        val orderId: String,
        val items: List<String>,
        val totalAmount: Double,
        @get:JsonProperty("isPaid") // What happens if this not here?
        val isPaid: Boolean,
        val version: Long,
    ) : OrderResponse(200)

    class Accepted() : OrderResponse(202)

    data class Problem(
        val message: String
    ) : OrderResponse(400)
}

data class CreateOrderRequest(val customerId: String)