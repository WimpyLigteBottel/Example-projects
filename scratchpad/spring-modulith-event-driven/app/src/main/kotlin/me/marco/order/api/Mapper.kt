package me.marco.order.api

import me.marco.common.Order

fun Order.toResponse() = OrderResponse(
    orderId = id,
    items = items,
    totalAmount = totalAmount,
    isPaid = isPaid,
    version = version
)