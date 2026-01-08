package me.marco.order.api

import com.fasterxml.jackson.annotation.JsonProperty
import me.marco.common.OrderItem
import me.marco.orderprocessing.service.dto.AddItemRequestDTO
import me.marco.orderprocessing.service.dto.CreateOrderRequestDTO
import me.marco.orderprocessing.service.dto.MarkAsPaidRequestDTO

// ============= DTOs =============
data class CreateOrderRequest(val orderId: String? = null) {
    fun internalize(): CreateOrderRequestDTO {
        return CreateOrderRequestDTO(
            orderId
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


data class OrderResponse(
    val orderId: String,
    val items: List<OrderItem>,
    val totalAmount: Double,
    @get:JsonProperty("isPaid")
    val isPaid: Boolean,
    val version: Long,
    val error: String? = null
)