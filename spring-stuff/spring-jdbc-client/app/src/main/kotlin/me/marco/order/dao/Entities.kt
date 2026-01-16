package me.marco.order.dao

import com.fasterxml.jackson.annotation.JsonProperty

data class OrderEntity(
    val orderId: String? = null,
    val totalAmount: Double = 0.0,
    @get:JsonProperty("isPaid")
    val isPaid: Boolean = false,
    val version: Long = 0,
    val items: List<OrderItemEntity> = emptyList()
)


data class OrderItemEntity(
    val id: Long? = null,
    val orderId: String,
    val item: String
)
