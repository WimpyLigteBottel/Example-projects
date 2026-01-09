package me.marco.order.rules

import me.marco.order.command.Command
import me.marco.order.events.Event
import me.marco.order.service.dto.Order

fun handleCreateOrder(command: Command.CreateOrderCommand, order: Order): Event.OrderCreatedEvent {
    return when {
        order.deleted -> throw IllegalStateException("Order already deleted")
        order.version > 0 -> throw IllegalStateException("Order already been created")
        else -> Event.OrderCreatedEvent(aggregateId = command.aggregateId, customerId = command.customerId)
    }
}