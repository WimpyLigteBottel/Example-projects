package me.marco.order.rules

import me.marco.order.command.Command
import me.marco.order.events.Event
import me.marco.order.service.dto.Order

fun handleAddItem(
    command: Command.AddItemCommand,
    order: Order,
): Event {

    when {
        order.customerId.isBlank() -> throw IllegalStateException("Customer id is null or blank")
        order.deleted -> throw IllegalStateException("Order already deleted")
        order.isPaid -> throw Exception("Cannot add items to a paid order")
    }

    return Event.ItemAddedEvent(
        itemId = command.itemId,
        name = command.name,
        price = command.price,
        quantity = command.quantity,
        orderId = order.id,
    )
}
