package me.marco.actions

import me.marco.actions.models.Command
import me.marco.actions.models.Event
import me.marco.order.Order
import me.marco.order.OrderHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class OrderCommandHandler(
    private val eventStore: EventStore,
    private val snapshotStore: SnapshotStore,
    private val orderHandler: OrderHandler
) {
    private val logger = LoggerFactory.getLogger(OrderCommandHandler::class.java)

    fun handle(command: Command): Result<Event> {
        // Rebuild order from snapshot + events
        val order = getOrder(command.aggregateId)

        return orderHandler.handle(command,order).onSuccess { event ->
            eventStore.save(event)
            logger.info("✅ Event saved: ${event::class.simpleName} for order ${event.aggregateId}")

            val updatedOrder = order.apply(event)
            snapshotStore.save(updatedOrder)
        }.onFailure { error ->
            logger.error("❌ Command failed: ${error.message}")
        }
    }

    fun getOrder(orderId: String): Order {
        // Try to get snapshot first
        val snapshot = snapshotStore.get(orderId)

        return if (snapshot != null) {
            // Rebuild from snapshot + events after snapshot version
            val recentEvents = eventStore.getEventsAfterVersion(orderId, snapshot.version)
            logger.debug("🔄 Rebuilding order $orderId from snapshot (version ${snapshot.version}) + ${recentEvents.size} recent events")
            recentEvents.fold(snapshot) { acc, event -> acc.apply(event) }
        } else {
            // No snapshot, rebuild from all events
            val events = eventStore.getEvents(orderId)
            logger.debug("🔄 Rebuilding order $orderId from ${events.size} events (no snapshot)")
            events.fold(
                Order(
                    orderId,
                    items = emptyList(),
                    isPaid = false,
                    totalAmount = 0.0,
                    version = 0,
                )
            ) { acc, event -> acc.apply(event) }
        }
    }
}