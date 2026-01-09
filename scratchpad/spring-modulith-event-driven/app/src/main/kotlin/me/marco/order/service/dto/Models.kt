package me.marco.order.service.dto

import java.time.OffsetDateTime

// ============= DTOs =============
data class CreateOrderRequestDTO(val customerId: String)

data class AddItemRequestDTO(
    val itemId: String,
    val name: String,
    val price: Double,
    val quantity: Int
)

data class MarkAsPaidRequestDTO(val paymentMethod: String)

data class OrderItem(
    val itemId: String,
    val name: String,
    val price: Double,
    val quantity: Int,
    val timeAdded: OffsetDateTime = OffsetDateTime.now(),
)

data class Order(
    val id: String,
    val customerId: String = "",
    val items: List<OrderItem> = emptyList(),
    val isPaid: Boolean = false,
    val totalAmount: Double = items.sumOf { it.price * it.quantity },
    val version: Long = 0,
    val started: OffsetDateTime = OffsetDateTime.now(),
    val lastUpdated: OffsetDateTime = OffsetDateTime.now(),
    val deleted: Boolean = false
) {
    fun incrementVersion(): Order =
        copy(
            version = version + 1,
            totalAmount = items.sumOf { it.price * it.quantity },
            lastUpdated = OffsetDateTime.now(),
        )
}
