package me.marco.event

import me.marco.event.models.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.concurrent.Executors

@Component
open class OrderEventListeners {
    @Async
    @EventListener
    open fun onOrderCreated(event: OrderCreatedEvent) {
        println("📧 Sending welcome email for order ${event.aggregateId}")
    }

    @Async
    @EventListener
    open fun onItemAdded(event: ItemAddedEvent) {
        println("📊 Item added ${event.name}")
    }

    @Async
    @EventListener
    open fun onOrderPaid(event: OrderMarkedAsPaidEvent) {
        println("💳 Processing payment ${event.amount}")
    }

    @Async
    @EventListener
    open fun itemRemovedEvent(event: RemoveItemEvent) {
        println("❌ Item has been removed ${event.itemId}")
    }
}

@Component
class OrderItemExpirationSaga(
    private val orderRepository: OrderCommandHandler,
    @Value("\${cleanup.time}") private val cleanupTime: Long,
) {
    val tasks = Executors.newFixedThreadPool(10)

    @EventListener
    fun on(event: ItemAddedEvent) {
        tasks.submit {
            Thread.sleep(cleanupTime)

            orderRepository.handle(
                Command.RemoveItemCommand(
                    aggregateId = event.aggregateId,
                    itemId = event.itemId,
                ),
            )
        }
    }
}
