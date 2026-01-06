package me.marco.event

import me.marco.event.models.*
import me.marco.order.Order
import me.marco.order.OrderItem
import me.marco.order.RulesHandler.applyRule
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class OrderCommandHandler(
    private val eventStore: EventStore, private val applicationEventPublisher: ApplicationEventPublisher
) {
    private val logger = LoggerFactory.getLogger(OrderCommandHandler::class.java)

    fun handle(command: Command) {
        val order = getOrder(command.aggregateId)

        val event = order.applyRule(command)

        event.onSuccess { evt ->
            eventStore.save(evt)
            applicationEventPublisher.publishEvent(evt)
        }
            .onFailure {
                logger.warn("FAILED {}", it.message)
            }

    }

    fun getOrder(orderId: String): Order {
        val events = eventStore.getEvents(orderId)
        return events.fold(Order(orderId)) { acc, event -> acc.apply(event) }
    }

    fun Order.apply(event: Event): Order = when (event) {
        is OrderCreatedEvent -> copy()
        is ItemAddedEvent -> copy(
            items = items + OrderItem(event.itemId, event.name, event.price, event.quantity)
        )

        is OrderMarkedAsPaidEvent -> copy(isPaid = true)
        is OrderClearedEvent -> copy(items = emptyList())
    }.incrementVersion()
}