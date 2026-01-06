package me.marco.rules

import me.marco.common.Order
import me.marco.event.models.Command
import me.marco.event.models.OrderCreatedEvent

fun handleCreateOrder(command: Command.CreateOrderCommand, order: Order): OrderCreatedEvent {
    return when {
        order.version > 0 -> throw IllegalStateException("Order already been created")
        else -> OrderCreatedEvent(aggregateId = command.aggregateId)
    }
}