package me.marco.rules

import me.marco.common.Order
import me.marco.event.models.Command
import me.marco.event.models.Event
import me.marco.event.models.RemoveItemEvent


fun handleRemoveItem(command: Command.RemoveItemCommand, order: Order): Event {
    if (!order.items.any { it.itemId == command.itemId }) {
        throw IllegalStateException("Item already removed")
    }
    if (order.isPaid) {
        throw IllegalStateException("Order already paid")
    }

    return RemoveItemEvent(
        aggregateId = command.aggregateId, itemId = command.itemId
    )
}
