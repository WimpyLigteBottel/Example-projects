package me.marco.orderprocessing

import me.marco.common.Order
import me.marco.orderprocessing.models.Command
import me.marco.orderprocessing.models.Event
import me.marco.orderprocessing.rules.handleAddItem
import me.marco.orderprocessing.rules.handleClearOrder
import me.marco.orderprocessing.rules.handleCreateOrder
import me.marco.orderprocessing.rules.handleMarkAsPaid
import me.marco.orderprocessing.rules.handleOrderDeletion
import me.marco.orderprocessing.rules.handleRemoveItem
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
                is Command.RemoveItemCommand -> handleRemoveItem(command, this)
                is Command.DeleteOrderCommand -> handleOrderDeletion(command, this)
            }
        }
    }
}