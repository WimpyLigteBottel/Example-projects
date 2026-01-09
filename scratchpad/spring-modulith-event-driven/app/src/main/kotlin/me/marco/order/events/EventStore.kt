package me.marco.order.events

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class EventStore {
    private val events = ConcurrentHashMap<String, MutableList<Event>>()

    fun save(event: Event) {
        events.getOrPut(event.aggregateId) { mutableListOf() }.add(event)
    }

    fun deleteEvents(event: Event) {
        events[event.aggregateId] = mutableListOf()
    }

    fun getEvents(aggregateId: String): List<Event> = events[aggregateId] ?: emptyList()

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