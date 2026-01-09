package me.marco.customer.rules

import me.marco.customer.command.Command
import me.marco.customer.events.Event
import me.marco.customer.service.dto.Customer

fun handleCreate(command: Command.CreateCustomerCommand, customer: Customer): Event.CustomerCreatedEvent {
    when {
        customer.deleted -> throw IllegalStateException("Customer already deleted")
        customer.version > 0 -> throw IllegalStateException("Customer already been created")
    }
    return Event.CustomerCreatedEvent(
        aggregateId = command.customerId,
        name = command.name,
        age = command.age,
        email = command.email,
    )
}