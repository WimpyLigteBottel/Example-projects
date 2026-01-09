@file:Suppress("ktlint:standard:filename", "ktlint:standard:no-wildcard-imports")

package me.marco.customer.command

import me.marco.customer.events.CustomerEventStore
import me.marco.customer.events.Event
import me.marco.customer.rules.CustomerRulesHandler.applyRule
import me.marco.customer.service.dto.Customer
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class CustomerCommandHandler(
    private val customerEventStore: CustomerEventStore,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun handle(command: Command): Result<Event> {
        val customer = getCustomer(command.customerId)

        val event = customer.applyRule(command)

        return event
            .onSuccess { evt ->
                customerEventStore.save(evt)
                applicationEventPublisher.publishEvent(evt)
            }.onFailure {
                logger.warn("FAILED {}", it.message)
            }
    }

    fun getCustomer(customerId: String): Customer {
        val events = customerEventStore.getEvents(customerId)
        return events.fold(Customer(customerId)) { acc, event -> acc.apply(event) }
    }

    private fun Customer.apply(event: Event): Customer =
        when (event) {
            is Event.CustomerCreatedEvent -> copy(name = event.name, age = event.age, email = event.email)
            is Event.CustomerDeletedEvent -> copy(deleted = true)
        }.incrementVersion()
}
