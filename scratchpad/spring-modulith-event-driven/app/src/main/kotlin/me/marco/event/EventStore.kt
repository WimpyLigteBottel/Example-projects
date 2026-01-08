package me.marco.event

import me.marco.event.models.Event
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class EventStore {
    private val events = ConcurrentHashMap<String, MutableList<Event>>()

    fun save(event: Event) {
        events.getOrPut(event.aggregateId) { mutableListOf() }.add(event)
    }

    fun getEvents(aggregateId: String): List<Event> = events[aggregateId] ?: emptyList()
}
