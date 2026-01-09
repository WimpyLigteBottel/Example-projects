package me.marco.order.rules

import me.marco.order.command.Command
import me.marco.order.events.Event
import me.marco.order.service.dto.Order


fun handleOrderDeletion(command: Command.DeleteOrderCommand, order: Order): Event {
    when {
        order.deleted -> throw IllegalStateException("Order already deleted")
    }
    return Event.OrderDeletedEvent(
        aggregateId = command.aggregateId
    )
}
