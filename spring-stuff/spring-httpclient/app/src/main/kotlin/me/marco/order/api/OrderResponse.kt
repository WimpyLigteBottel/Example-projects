package me.marco.order.api

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