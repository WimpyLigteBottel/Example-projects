package me.marco.orderprocessing.service.dto

// ============= DTOs =============
data class CreateOrderRequestDTO(val orderId: String? = null)

data class AddItemRequestDTO(
    val itemId: String,
    val name: String,
    val price: Double,
    val quantity: Int
)

data class MarkAsPaidRequestDTO(val paymentMethod: String)
