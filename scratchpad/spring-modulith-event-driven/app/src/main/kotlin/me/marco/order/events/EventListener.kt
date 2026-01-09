package me.marco.order.events

import me.marco.order.command.Command
import me.marco.order.command.OrderCommandHandler
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.concurrent.Executors

@Component
open class OrderEventListeners(
    private val orderCommandHandler: OrderCommandHandler,
    private val eventStore: EventStore,
    @Value("\${cleanup.time}") private val cleanupTime: Long,
) {

    val tasks = Executors.newFixedThreadPool(10)


    private val logger = LoggerFactory.getLogger(this::class.java)

    @Async
    @EventListener
    open fun onOrderCreated(event: Event.OrderCreatedEvent) {
        logger.info("📧 Order created ${event.aggregateId}")

        // create task to clean up order
        tasks.submit {
            Thread.sleep(cleanupTime * 2)
            orderCommandHandler.handle(Command.DeleteOrderCommand(event.aggregateId))
            eventStore.deleteEvents(event)
        }
    }

    @Async
    @EventListener
    open fun onItemAdded(event: Event.ItemAddedEvent) {
        logger.info("📊 Item added ${event.name}")

        // creates a task to clean up item
        tasks.submit {
            Thread.sleep(cleanupTime)

            orderCommandHandler.handle(
                Command.RemoveItemCommand(
                    aggregateId = event.aggregateId,
                    itemId = event.itemId,
                ),
            )
        }
    }

    @Async
    @EventListener
    open fun onOrderPaid(event: Event.OrderMarkedAsPaidEvent) {
        logger.info("💳 Processing payment ${event.amount}")
    }

    @Async
    @EventListener
    open fun itemRemovedEvent(event: Event.RemoveItemEvent) {
        logger.info("❌ Item has been removed ${event.itemId}")
    }

    @Async
    @EventListener
    open fun orderDeleted(event: Event.OrderDeletedEvent) {
        logger.info("❌ Order has been deleted ${event.aggregateId}")
    }

    @Async
    @EventListener
    open fun onDeleted(event: me.marco.customer.events.Event.CustomerDeletedEvent) {
        logger.info("❌!!! Customer has been deleted and now will be deleting all orders owned by customer ${event.aggregateId}")

        val allOrders = orderCommandHandler.findAllOrdersByCustomerId(event.aggregateId)
        allOrders.forEach { order ->
            orderCommandHandler.handle(Command.DeleteOrderCommand(order.id))
        }
    }
}
