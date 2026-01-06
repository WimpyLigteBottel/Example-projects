package me.marco.rules

import me.marco.common.Order
import me.marco.event.models.Command
import me.marco.event.models.Event
import me.marco.event.models.ItemAddedEvent

fun handleAddItem(command: Command.AddItemCommand, order: Order): Event {
    if (order.isPaid) {
        throw Exception("Cannot add items to a paid order")
    }
    return ItemAddedEvent(
        aggregateId = command.aggregateId,
        itemId = command.itemId,
        name = command.name,
        price = command.price,
        quantity = command.quantity,
        orderId = order.id
    )
}