package me.marco.order.api

import me.marco.order.OrderItem

// ============= DTOs =============
data class CreateOrderRequest(val orderId: String? = null)

data class AddItemRequest(
    val itemId: String,
    val name: String,
    val price: Double,
    val quantity: Int
)

data class MarkAsPaidRequest(val paymentMethod: String)

data class OrderResponse(
    val orderId: String,
    val items: List<OrderItem>,
    val totalAmount: Double,
    val isPaid: Boolean,
    val version: Long,
    val error: String? = null
)