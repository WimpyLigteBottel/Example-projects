@file:Suppress("ktlint:standard:filename", "ktlint:standard:no-wildcard-imports")

package me.marco.order.command

import me.marco.order.events.EventStore
import me.marco.order.events.Event
import me.marco.order.service.dto.Order
import me.marco.order.service.dto.OrderItem
import me.marco.order.rules.RulesHandler.applyRule
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class OrderCommandHandler(
    private val eventStore: EventStore,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    private val logger = LoggerFactory.getLogger(OrderCommandHandler::class.java)

    fun handle(command: Command): Result<Event> {
        val order = getOrder(command.aggregateId)

        val event = order.applyRule(command)

        return event
            .onSuccess { evt ->
                eventStore.save(evt)
                applicationEventPublisher.publishEvent(evt)
            }.onFailure {
                logger.warn("FAILED {}", it.message)
            }
    }

    /**
     * Gets the final order state after applying all successful events.
     */
    fun getOrder(orderId: String): Order {
        val events = eventStore.getEvents(orderId)
        return events.fold(Order(orderId)) { acc, event -> acc.apply(event) }
    }

    private fun Order.apply(event: Event): Order =
        when (event) {
            is Event.OrderCreatedEvent -> copy()
            is Event.ItemAddedEvent ->
                copy(
                    items = items + OrderItem(event.itemId, event.name, event.price, event.quantity),
                )

            is Event.OrderMarkedAsPaidEvent -> copy(isPaid = true)
            is Event.OrderClearedEvent -> copy(items = emptyList())
            is Event.RemoveItemEvent -> {
                copy(items = items.filter { it.itemId != event.itemId })
            }

            is Event.OrderDeletedEvent -> copy(deleted = true)
        }.incrementVersion()
}
