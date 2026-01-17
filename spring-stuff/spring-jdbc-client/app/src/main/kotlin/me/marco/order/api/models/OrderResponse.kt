package me.marco.order.api.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = OrderResponse.OK::class, name = "200"),
    JsonSubTypes.Type(value = OrderResponse.Accepted::class, name = "202"),
    JsonSubTypes.Type(value = OrderResponse.Problem::class, name = "400")
)
sealed class OrderResponse(type: Int) {

    data class OK(
        val order: Order,
    ) : OrderResponse(200)

    class Accepted() : OrderResponse(202)

    data class Problem(
        val message: String
    ) : OrderResponse(400)

    data class ProblemNotFound(
        val message: String
    ) : OrderResponse(404)
}

data class Order(
    val orderId: String,
    val items: List<Item>,
    val totalAmount: Double,
    @get:JsonProperty("isPaid") // What happens if this not here?
    val isPaid: Boolean,
    val version: Long,
)

data class CreateOrderRequest(val name: String)
