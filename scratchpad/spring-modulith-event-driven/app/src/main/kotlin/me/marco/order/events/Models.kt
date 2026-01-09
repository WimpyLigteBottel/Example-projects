@file:Suppress("ktlint:standard:no-wildcard-imports")

package me.marco.order.events

import java.time.Instant
import java.util.*

sealed interface Event {
    val eventId: String
    val aggregateId: String
    val timestamp: Instant

    data class OrderCreatedEvent(
        override val eventId: String = UUID.randomUUID().toString(),
        override val aggregateId: String,
        override val timestamp: Instant = Instant.now(),
    ) : Event

    data class OrderClearedEvent(
        override val aggregateId: String,
        override val eventId: String = UUID.randomUUID().toString(),
        override val timestamp: Instant = Instant.now(),
    ) : Event

    data class OrderDeletedEvent(
        override val aggregateId: String,
        override val eventId: String = UUID.randomUUID().toString(),
        override val timestamp: Instant = Instant.now(),
    ) : Event

    data class ItemAddedEvent(
        override val aggregateId: String,
        override val eventId: String = UUID.randomUUID().toString(),
        override val timestamp: Instant = Instant.now(),
        val orderId: String,
        val itemId: String,
        val name: String,
        val price: Double,
        val quantity: Int,
    ) : Event

    data class RemoveItemEvent(
        override val aggregateId: String,
        override val eventId: String = UUID.randomUUID().toString(),
        override val timestamp: Instant = Instant.now(),
        val itemId: String,
    ) : Event

    data class OrderMarkedAsPaidEvent(
        override val eventId: String = UUID.randomUUID().toString(),
        override val aggregateId: String,
        override val timestamp: Instant = Instant.now(),
        val paymentMethod: String,
        val amount: Double,
    ) : Event
}
