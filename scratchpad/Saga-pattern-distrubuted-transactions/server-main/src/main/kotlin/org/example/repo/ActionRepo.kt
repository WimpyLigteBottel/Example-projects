package org.example.repo

import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.concurrent.ConcurrentHashMap


typealias GlobalId = String

@Repository
class ActionRepo {

    private var internalMap = ConcurrentHashMap<GlobalId, RequestingOrder>()

    fun find(id: GlobalId): RequestingOrder? = internalMap[id]

    fun findAll(): List<RequestingOrder> = internalMap.map { it.value }

    fun save(requestingOrder: RequestingOrder) {
        val newOrder = requestingOrder.copy(updated = OffsetDateTime.now(ZoneOffset.UTC))
        internalMap[requestingOrder.id] = newOrder
    }

    fun remove(requestingOrder: RequestingOrder) = internalMap.remove(requestingOrder.id)

}