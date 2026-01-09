package me.marco.customer.events

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
open class CustomerEventListeners(
    val eventStore: CustomerEventStore,
) {


    private val logger = LoggerFactory.getLogger(this::class.java)

    @Async
    @EventListener
    open fun onCreated(event: Event.CustomerCreatedEvent) {
        logger.info("\uD83D\uDC64 Sending welcome email ${event.name}")
    }

    @Async
    @EventListener
    open fun onDeleted(event: Event.CustomerDeletedEvent) {
        logger.info("❌\uD83D\uDC64 Customer has been deleted ${event.aggregateId}")
        eventStore.deleteEvents(event)
    }
}
