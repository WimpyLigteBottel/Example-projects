package me.marco.order.events

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class EventStore {
    private val events = ConcurrentHashMap<String, MutableList<Event>>()

    fun save(event: Event) {
        events.getOrPut(event.orderId) { mutableListOf() }.add(event)
    }

    fun deleteEvents(event: Event) {
        events[event.orderId] = mutableListOf()
    }

    fun getEvents(aggregateId: String): List<Event> = events[aggregateId] ?: emptyList()

    fun getUnpaidOrders(): List<Pair<String, List<Event>>> = events.entries.map {
        val events: List<Pair<String, List<Event>>> = events.entries.mapNotNull { (key, values) ->
            val hasPaidOrder = values.filter { it -> it is Event.OrderMarkedAsPaidEvent }

            if (hasPaidOrder.isNotEmpty())
                return@mapNotNull null

            if (values.isEmpty())
                return@mapNotNull null

            key to values.toList()
        }
        return events
    }

    fun getEventsByCustomerId(customerId: String): List<Pair<String, List<Event>>> {
        val events: List<Pair<String, List<Event>>> = events.entries.map { (key, values) ->
            val orderCreatedEvents = values.filter { it ->
                if (it !is Event.OrderCreatedEvent)
                    return@filter false
                return@filter it.customerId == customerId
            }

            if (orderCreatedEvents.isEmpty())
                return emptyList()


            key to values.toList()
        }
        return events
    }
}