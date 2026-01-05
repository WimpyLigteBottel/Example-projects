package me.marco.actions

import me.marco.actions.models.Event
import me.marco.order.Order
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class EventStore {
    private val events = ConcurrentHashMap<String, MutableList<Event>>()

    fun save(event: Event) {
        events.getOrPut(event.aggregateId) { mutableListOf() }.add(event)
    }

    fun getEvents(aggregateId: String): List<Event> {
        return events[aggregateId] ?: emptyList()
    }

    fun getEventsAfterVersion(aggregateId: String, version: Long): List<Event> {
        return getEvents(aggregateId).drop(version.toInt())
    }
}

@Component
class SnapshotStore {
    private val snapshots = ConcurrentHashMap<String, Order>()

    fun save(order: Order) {
        snapshots[order.id] = order
    }

    fun get(aggregateId: String): Order? {
        return snapshots[aggregateId]
    }

    fun delete(aggregateId: String) {
        snapshots.remove(aggregateId)
    }
}