package me.marco.order.events

import me.marco.order.command.Command
import me.marco.order.command.OrderCommandHandler
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.concurrent.TimeUnit


@Service
open class TimeBasedOrderService(
    private val eventStore: EventStore,
    private val orderCommandHandler: OrderCommandHandler,
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    // ✅ Add this ONLY if you have time-dependent rules
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS, initialDelay = 1)
    open fun checkTimeBasedRules() {
        val now = OffsetDateTime.now().minusSeconds(1)

        // Find orders that might violate time-based rules
        eventStore.getUnpaidOrders()
            .forEach { (key, events) ->
                val order = orderCommandHandler.getOrder(key)

                val isOlderThan2Seconds = now.isAfter(order.lastUpdated)

                if (!order.isPaid && !order.deleted && isOlderThan2Seconds) {
                    val event = Event.OrderCancelledDueToTimeoutEvent(orderId = order.id)
                    applicationEventPublisher.publishEvent(event)
                    orderCommandHandler.handle(Command.DeleteOrderCommand(order.id))
                }
            }
    }
}