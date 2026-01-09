package me.marco.customer.rules


import me.marco.customer.command.Command
import me.marco.customer.events.Event
import me.marco.customer.service.dto.Customer

object CustomerRulesHandler {
    fun Customer.applyRule(command: Command): Result<Event> {
        return runCatching {
            when (command) {
                is Command.CreateCustomerCommand -> handleCreate(command, this)
                is Command.DeleteCustomerCommand -> handleDelete(command, this)
            }
        }
    }
}