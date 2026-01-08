package me.marco.orderprocessing.rules

import me.marco.common.Order
import me.marco.orderprocessing.models.Command
import me.marco.orderprocessing.models.Event
import me.marco.orderprocessing.models.RemoveItemEvent


fun handleRemoveItem(command: Command.RemoveItemCommand, order: Order): Event {
    if (!order.items.any { it.itemId == command.itemId }) {
        throw IllegalStateException("Item already removed")
    }
    if (order.deleted) {
        throw IllegalStateException("Order already deleted")
    }
    if (order.isPaid) {
        throw IllegalStateException("Order already paid")
    }

    return RemoveItemEvent(
        aggregateId = command.aggregateId, itemId = command.itemId
    )
}
