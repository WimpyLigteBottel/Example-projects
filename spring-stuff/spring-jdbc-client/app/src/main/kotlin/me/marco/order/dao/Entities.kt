package me.marco.order.dao

import me.marco.order.api.models.Item
import me.marco.order.api.models.Order

data class OrderEntity(
    val orderId: Long,
    val totalAmount: Double,
    val isPaid: Boolean,
    val version: Long,
    val items: List<OrderItemEntity>
)

data class OrderItemEntity(
    val id: Long,
    val orderId: Long,
    val item: String
)


fun OrderEntity.transform(): Order {
    return Order(
        orderId = orderId.toString(),
        items = items.map {
            Item(
                id = it.id,
                name = it.item
            )
        },
        totalAmount = totalAmount,
        isPaid = isPaid,
        version = version
    )
}