package me.marco.orderprocessing.rules

import me.marco.common.Order
import me.marco.orderprocessing.models.Command
import me.marco.orderprocessing.models.Event
import me.marco.orderprocessing.models.OrderDeletedEvent


fun handleOrderDeletion(command: Command.DeleteOrderCommand, order: Order): Event {
    return OrderDeletedEvent(
        aggregateId = command.aggregateId
    )
}
