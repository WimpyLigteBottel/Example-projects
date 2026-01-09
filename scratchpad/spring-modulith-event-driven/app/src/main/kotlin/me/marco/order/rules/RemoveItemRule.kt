package me.marco.order.rules

import me.marco.order.command.Command
import me.marco.order.events.Event
import me.marco.order.service.dto.Order

fun handleRemoveItem(command: Command.RemoveItemCommand, order: Order): Event {

    when {
        !order.items.any { it.itemId == command.itemId } -> throw IllegalStateException("Item already removed")
        order.deleted -> throw IllegalStateException("Order already deleted")
        order.isPaid -> throw IllegalStateException("Order already paid")
    }

    return Event.RemoveItemEvent(
        aggregateId = command.aggregateId, itemId = command.itemId
    )
}
