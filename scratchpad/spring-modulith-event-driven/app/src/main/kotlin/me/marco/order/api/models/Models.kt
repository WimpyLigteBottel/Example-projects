package me.marco.order.api.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import me.marco.customer.api.models.CustomerResponse
import me.marco.order.service.dto.AddItemRequestDTO
import me.marco.order.service.dto.CreateOrderRequestDTO
import me.marco.order.service.dto.MarkAsPaidRequestDTO
import me.marco.order.service.dto.OrderItem
import org.springframework.modulith.NamedInterface

// ============= DTOs =============
data class CreateOrderRequest(val customerId: String) {
    fun internalize(): CreateOrderRequestDTO {
        return CreateOrderRequestDTO(
            customerId
        )
    }
}

data class AddItemRequest(
    val itemId: String,
    val name: String,
    val price: Double,
    val quantity: Int
) {
    fun internalize(): AddItemRequestDTO {
        return AddItemRequestDTO(
            itemId,
            name,
            price,
            quantity
        )
    }
}

data class MarkAsPaidRequest(val paymentMethod: String) {
    fun internalize(): MarkAsPaidRequestDTO {
        return MarkAsPaidRequestDTO(
            paymentMethod
        )
    }
}

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = OrderResponse.OK::class, name = "OK"),
    JsonSubTypes.Type(value = OrderResponse.Problem::class, name = "PROBLEM")
)
@NamedInterface("api")
sealed class OrderResponse(
    type: String
) {

    @NamedInterface("api")
    data class OK(
        val orderId: String,
        val items: List<OrderItem>,
        val totalAmount: Double,
        @get:JsonProperty("isPaid")
        val isPaid: Boolean,
        val version: Long,
    ) : OrderResponse("OK")

    @NamedInterface("api")
    data class Problem(
        val message: String
    ) : OrderResponse("PROBLEM")
}