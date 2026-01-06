package me.marco.order


import me.marco.event.models.*
import org.springframework.stereotype.Service

@Service
object RulesHandler {

    fun Order.applyRule(command: Command): Result<Event> {
        return runCatching {
            when (command) {
                is Command.CreateOrderCommand -> handleCreateOrder(command, this)
                is Command.AddItemCommand -> handleAddItem(command, this)
                is Command.MarkOrderAsPaidCommand -> handleMarkAsPaid(command, this)
                is Command.ClearOrderCommand -> handleClearOrder(command, this)
            }
        }
    }

    private fun handleCreateOrder(command: Command.CreateOrderCommand, order: Order): OrderCreatedEvent {
        return when {
            order.version > 0 -> throw IllegalStateException("Order already been created")
            else -> OrderCreatedEvent(aggregateId = command.aggregateId)
        }
    }


    private fun handleClearOrder(command: Command.ClearOrderCommand, order: Order): Event {
        return when {
            order.isPaid -> throw Exception("Can't clear an paid order")
            order.items.isEmpty() -> throw Exception("There is no items to clear")
            else -> OrderClearedEvent(
                aggregateId = command.aggregateId,
            )
        }
    }


    private fun handleAddItem(command: Command.AddItemCommand, order: Order): Event {
        if (order.isPaid) {
            throw Exception("Cannot add items to a paid order")
        }
        return ItemAddedEvent(
            aggregateId = command.aggregateId,
            itemId = command.itemId,
            name = command.name,
            price = command.price,
            quantity = command.quantity
        )
    }

    private fun handleMarkAsPaid(command: Command.MarkOrderAsPaidCommand, order: Order): Event {
        return when {
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
}