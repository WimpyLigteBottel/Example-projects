@file:Suppress("ktlint:standard:no-wildcard-imports")

package me.marco.order.events

import java.time.Instant
import java.util.*

sealed interface Event {
    val eventId: String
    val orderId: String
    val timestamp: Instant

    data class OrderCreatedEvent(
        override val eventId: String = UUID.randomUUID().toString(),
        override val timestamp: Instant = Instant.now(),
        override val orderId: String,
        val customerId: String
    ) : Event

    data class OrderClearedEvent(
        override val orderId: String,
        override val eventId: String = UUID.randomUUID().toString(),
        override val timestamp: Instant = Instant.now(),
    ) : Event

    data class OrderDeletedEvent(
        override val orderId: String,
        override val eventId: String = UUID.randomUUID().toString(),
        override val timestamp: Instant = Instant.now(),
    ) : Event

    data class ItemAddedEvent(
        override val eventId: String = UUID.randomUUID().toString(),
        override val timestamp: Instant = Instant.now(),
        override val orderId: String,
        val itemId: String,
        val name: String,
        val price: Double,
        val quantity: Int,
    ) : Event

    data class RemoveItemEvent(
        override val eventId: String = UUID.randomUUID().toString(),
        override val timestamp: Instant = Instant.now(),
        override val orderId: String,
        val itemId: String,
    ) : Event

    data class OrderMarkedAsPaidEvent(
        override val eventId: String = UUID.randomUUID().toString(),
        override val timestamp: Instant = Instant.now(),
        override val orderId: String,
        val paymentMethod: String,
        val amount: Double,
    ) : Event

    data class OrderCancelledDueToTimeoutEvent(
        override val eventId: String = UUID.randomUUID().toString(),
        override val timestamp: Instant = Instant.now(),
        override val orderId: String,
    ) : Event

}
