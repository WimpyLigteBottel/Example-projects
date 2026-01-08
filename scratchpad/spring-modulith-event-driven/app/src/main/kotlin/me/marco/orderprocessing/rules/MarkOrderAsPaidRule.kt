package me.marco.orderprocessing.rules

import me.marco.common.Order
import me.marco.orderprocessing.models.Command
import me.marco.orderprocessing.models.Event
import me.marco.orderprocessing.models.OrderMarkedAsPaidEvent

fun handleMarkAsPaid(command: Command.MarkOrderAsPaidCommand, order: Order): Event {
    return when {
        order.deleted -> throw IllegalStateException("Order already deleted")
        order.isPaid -> throw Exception("Order is already paid")
        order.items.isEmpty() -> throw Exception("Cannot mark empty order as paid")
        else ->
            OrderMarkedAsPaidEvent(
                aggregateId = command.aggregateId,
                paymentMethod = command.paymentMethod,
                amount = order.totalAmount
            )
    }
}