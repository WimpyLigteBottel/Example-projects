package me.marco.rules

import me.marco.common.Order
import me.marco.event.models.Command
import me.marco.event.models.Event
import me.marco.event.models.OrderDeletedEvent
import me.marco.event.models.RemoveItemEvent


fun handleOrderDeletion(command: Command.DeleteOrderCommand, order: Order): Event {
    return OrderDeletedEvent(
        aggregateId = command.aggregateId
    )
}
