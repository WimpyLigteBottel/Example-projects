package me.marco.order.api.models

import me.marco.order.service.dto.Order

fun Order.toResponse() = OrderResponse(
    orderId = id,
    items = items,
    totalAmount = totalAmount,
    isPaid = isPaid,
    version = version
)