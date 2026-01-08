package me.marco.orderprocessing.rules

import me.marco.common.Order
import me.marco.orderprocessing.models.Command
import me.marco.orderprocessing.models.OrderCreatedEvent

fun handleCreateOrder(command: Command.CreateOrderCommand, order: Order): OrderCreatedEvent {
    return when {
        order.deleted -> throw IllegalStateException("Order already deleted")
        order.version > 0 -> throw IllegalStateException("Order already been created")
        else -> OrderCreatedEvent(aggregateId = command.aggregateId)
    }
}