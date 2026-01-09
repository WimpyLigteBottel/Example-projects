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
}