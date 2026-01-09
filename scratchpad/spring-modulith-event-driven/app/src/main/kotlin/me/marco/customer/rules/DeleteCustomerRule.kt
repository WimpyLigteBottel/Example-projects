package me.marco.customer.rules

import me.marco.customer.command.Command
import me.marco.customer.events.Event
import me.marco.customer.service.dto.Customer


fun handleDelete(command: Command.DeleteCustomerCommand, customer: Customer): Event {
    when {
        customer.deleted -> throw IllegalStateException("Customer already deleted")
    }
    return Event.CustomerDeletedEvent(
        aggregateId = command.customerId
    )
}
