package me.marco.order.api.models

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = OrderItemResponse.OK::class, name = "200"),
    JsonSubTypes.Type(value = OrderItemResponse.Problem::class, name = "400")
)
sealed class OrderItemResponse(type: Int) {

    data class OK(
        val item: Item
    ) : OrderResponse(200)


    data class Problem(
        val message: String
    ) : OrderResponse(400)
}


data class Item(
    val id: Long,
    val name: String
)