@file:Suppress("ktlint:standard:no-wildcard-imports")

package me.marco.customer.events

import org.springframework.modulith.NamedInterface
import java.time.Instant
import java.util.*

sealed interface Event {
    val eventId: String
    val aggregateId: String
    val timestamp: Instant

    data class CustomerCreatedEvent(
        override val eventId: String = UUID.randomUUID().toString(),
        override val aggregateId: String,
        override val timestamp: Instant = Instant.now(),
        val name: String,
        val age: Int,
        val email: String
    ) : Event


    //Explicitly allowing another module to listen to this!!!
    @NamedInterface("api")
    data class CustomerDeletedEvent(
        override val aggregateId: String,
        override val eventId: String = UUID.randomUUID().toString(),
        override val timestamp: Instant = Instant.now(),
    ) : Event
}
