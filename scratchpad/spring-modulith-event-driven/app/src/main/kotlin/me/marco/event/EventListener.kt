package me.marco.event

import me.marco.event.models.ItemAddedEvent
import me.marco.event.models.OrderCreatedEvent
import me.marco.event.models.OrderMarkedAsPaidEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component


@Component
open class OrderEventListeners {

    @Async
    @EventListener
    open fun onOrderCreated(event: OrderCreatedEvent) {
        println("📧 Sending welcome email for order ${event.aggregateId}")
        Thread.sleep(1000) // Simulate slow email service
        println("✅ Email sent!")
    }

    @Async
    @EventListener
    open fun onItemAdded(event: ItemAddedEvent) {
        println("📊 Updating analytics for ${event.name}")
        Thread.sleep(1000) // Simulate slow email service
        println("📊 Updated analytics for ${event.name}")
    }

    @Async
    @EventListener
    open fun onOrderPaid(event: OrderMarkedAsPaidEvent) {
        println("💳 Processing payment ${event.amount}")
        println("📦 Creating shipment")
        println("📧 Sending receipt")
    }
}