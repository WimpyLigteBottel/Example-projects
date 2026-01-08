package me.marco.rules

import me.marco.common.Order
import me.marco.event.models.Command
import me.marco.event.models.Event
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

