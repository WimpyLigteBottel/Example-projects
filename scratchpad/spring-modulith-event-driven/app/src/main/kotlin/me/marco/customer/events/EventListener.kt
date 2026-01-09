package me.marco.customer.events

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
open class CustomerEventListeners() {


    private val logger = LoggerFactory.getLogger(this::class.java)

    @Async
    @EventListener
    open fun onCreated(event: Event.CustomerCreatedEvent) {
        logger.info("📧 Sending welcome email ${event.aggregateId}")
    }

    @Async
    @EventListener
    open fun onDeleted(event: Event.CustomerDeletedEvent) {
        logger.info("❌ Customer has been deleted ${event.aggregateId}")
    }
}
