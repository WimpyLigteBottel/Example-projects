package me.marco.events

import me.marco.order.Order


interface Event
data class AddItemEvent(val orderID: String, val orderItemId: String) : Event
data class CreateOrderEvent(val orderID: String) : Event
data class UpdateOrderEvent(val orderID: String, val order: Order) : Event
data class PaidOrderEvent(val orderID: String) : Event