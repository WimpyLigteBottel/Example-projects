package org.example.repo

import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.StampedLock


typealias GlobalId = String

@Repository
class ActionRepo {

    private var internalMap = ConcurrentHashMap<GlobalId, RequestingOrder>()
    private val stampedLock = StampedLock()


    fun find(id: GlobalId): RequestingOrder? = withReadLock {
        internalMap[id]
    }

    fun findAll(): List<RequestingOrder> = withReadLock {
        internalMap.map { it.value }
    }

    fun save(requestingOrder: RequestingOrder) = withWriteLock {
        val newOrder = requestingOrder.copy(
            updated = OffsetDateTime.now(ZoneOffset.UTC)
        )
        internalMap[requestingOrder.id] = newOrder
    }

    fun remove(requestingOrder: RequestingOrder) = withWriteLock {
        internalMap.remove(requestingOrder.id)
    }


    fun ActionRepo.withWriteLock(function: () -> Unit) {
        val writeLock = stampedLock.writeLock()
        try {
            function.invoke()
        } finally {
            stampedLock.unlock(writeLock)
        }
    }

    fun <T> ActionRepo.withReadLock(function: () -> T): T {
        val writeLock = stampedLock.readLock()
        try {
            return function.invoke()
        } finally {
            stampedLock.unlock(writeLock)
        }
    }

}