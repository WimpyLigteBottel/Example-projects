package me.marco.common

import java.time.OffsetDateTime

data class OrderItem(
    val itemId: String,
    val name: String,
    val price: Double,
    val quantity: Int,
    val timeAdded: OffsetDateTime = OffsetDateTime.now()
)

data class Order(
    val id: String,
    val items: List<OrderItem> = emptyList(),
    val isPaid: Boolean = false,
    val totalAmount: Double = items.sumOf { it.price * it.quantity },
    val version: Long = 0,
    val started: OffsetDateTime = OffsetDateTime.now(),
    val lastUpdated: OffsetDateTime = OffsetDateTime.now()
) {

    fun incrementVersion(): Order {

        return copy(version = version + 1, lastUpdated = OffsetDateTime.now())
    }
}