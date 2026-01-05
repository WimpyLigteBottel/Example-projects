package me.marco.order


import me.marco.actions.models.*


data class OrderItem(
    val itemId: String,
    val name: String,
    val price: Double,
    val quantity: Int
)

data class Order(
    val id: String,
    val items: List<OrderItem> = emptyList(),
    val isPaid: Boolean = false,
    val totalAmount: Double = 0.0,
    val version: Long = 0
) {

    fun apply(event: Event): Order = when (event) {
        is OrderCreatedEvent -> copy(version = version + 1)
        is ItemAddedEvent -> copy(
            items = items + OrderItem(event.itemId, event.name, event.price, event.quantity),
            totalAmount = totalAmount + (event.price * event.quantity),
            version = version + 1
        )
        is OrderMarkedAsPaidEvent -> copy(isPaid = true, version = version + 1)
        is OrderClearedEvent -> copy(
            items = emptyList(),
            totalAmount = 0.0,
            version = version + 1,
        )
    }

}