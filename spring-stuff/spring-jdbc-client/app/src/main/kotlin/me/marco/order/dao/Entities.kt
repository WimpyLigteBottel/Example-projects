package me.marco.order.dao

import com.fasterxml.jackson.annotation.JsonProperty

data class OrderEntity(
    val orderId: Long,
    val totalAmount: Double,
    @get:JsonProperty("isPaid")
    val isPaid: Boolean,
    val version: Long,
    val items: List<OrderItemEntity>
)


data class OrderItemEntity(
    val id: Long,
    val orderId: Long,
    val item: String
)
