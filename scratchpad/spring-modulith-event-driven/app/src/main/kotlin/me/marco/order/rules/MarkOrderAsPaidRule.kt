package me.marco.order.rules

import me.marco.order.service.dto.Order
import me.marco.order.command.Command
import me.marco.order.events.Event

fun handleMarkAsPaid(command: Command.MarkOrderAsPaidCommand, order: Order): Event {
    return when {
        order.deleted -> throw IllegalStateException("Order already deleted")
        order.isPaid -> throw Exception("Order is already paid")
        order.items.isEmpty() -> throw Exception("Cannot mark empty order as paid")
        else ->
            Event.OrderMarkedAsPaidEvent(
                aggregateId = command.aggregateId,
                paymentMethod = command.paymentMethod,
                amount = order.totalAmount
            )
    }
}