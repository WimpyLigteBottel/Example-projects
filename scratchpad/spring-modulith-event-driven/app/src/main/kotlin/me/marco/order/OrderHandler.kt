package me.marco.order


import me.marco.actions.models.*
import org.springframework.stereotype.Service

@Service
object OrderHandler {

    fun Order.handle(command: Command): Result<Event> {
        return when (command) {
            is Command.CreateOrderCommand -> handleCreateOrder(command)
            is Command.AddItemCommand -> handleAddItem(command, this)
            is Command.MarkOrderAsPaidCommand -> handleMarkAsPaid(command, this)
            is Command.ClearOrderCommand -> handleClearOrder(command, this)
        }
    }

    private fun handleCreateOrder(command: Command.CreateOrderCommand): Result<Event> {
        return when {
            else -> Result.success(OrderCreatedEvent(aggregateId = command.aggregateId))
        }
    }


    private fun handleClearOrder(command: Command.ClearOrderCommand, order: Order): Result<Event> {
        return when {
            order.isPaid -> Result.failure(Exception("Can't clear an paid order"))
            order.items.isEmpty() -> Result.failure(Exception("There is no items to clear"))
            else -> Result.success(
                OrderClearedEvent(
                    aggregateId = command.aggregateId,
                )
            )
        }
    }


    private fun handleAddItem(command: Command.AddItemCommand, order: Order): Result<Event> {
        if (order.isPaid) {
            return Result.failure(Exception("Cannot add items to a paid order"))
        }
        return Result.success(
            ItemAddedEvent(
                aggregateId = command.aggregateId,
                itemId = command.itemId,
                name = command.name,
                price = command.price,
                quantity = command.quantity
            )
        )
    }

    private fun handleMarkAsPaid(command: Command.MarkOrderAsPaidCommand, order: Order): Result<Event> {
        return when {
            order.isPaid -> Result.failure(Exception("Order is already paid"))
            order.items.isEmpty() -> Result.failure(Exception("Cannot mark empty order as paid"))
            else -> Result.success(
                OrderMarkedAsPaidEvent(
                    aggregateId = command.aggregateId,
                    paymentMethod = command.paymentMethod,
                    amount = order.totalAmount
                )
            )
        }
    }
}