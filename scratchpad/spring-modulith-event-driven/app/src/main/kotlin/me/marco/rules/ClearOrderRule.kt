package me.marco.rules

import me.marco.common.Order
import me.marco.event.models.Command
import me.marco.event.models.Event
import me.marco.event.models.OrderClearedEvent

fun handleClearOrder(command: Command.ClearOrderCommand, order: Order): Event {
    return when {
        order.isPaid -> throw Exception("Can't clear an paid order")
        order.items.isEmpty() -> throw Exception("There is no items to clear")
        else -> OrderClearedEvent(
            aggregateId = command.aggregateId,
        )
    }
}