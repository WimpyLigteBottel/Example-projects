package me.marco.order.rules

import me.marco.order.command.Command
import me.marco.order.events.Event
import me.marco.order.service.dto.Order

fun handleClearOrder(command: Command.ClearOrderCommand, order: Order): Event {
    return when {
        order.isPaid -> throw Exception("Can't clear an paid order")
        order.deleted -> throw IllegalStateException("Order already deleted")
        order.items.isEmpty() -> throw Exception("There is no items to clear")
        else -> Event.OrderClearedEvent(
            aggregateId = command.aggregateId,
        )
    }
}