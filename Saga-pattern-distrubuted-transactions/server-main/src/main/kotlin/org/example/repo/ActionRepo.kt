package org.example.repo

import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.StampedLock

@Repository
class ActionRepo {


    private var orderProcessMap = ConcurrentHashMap<String, RequestingOrder>()
    private val stampedLock = StampedLock()


    fun find(id: String): RequestingOrder? {
        val readLock = stampedLock.readLock()
        try {
            return orderProcessMap[id]?.copy()
        } finally {
            stampedLock.unlock(readLock)
        }

    }

    fun findAll(): MutableMap<String, RequestingOrder> {

        val readLock = stampedLock.readLock()
        try {
            return orderProcessMap.toMutableMap()

        } finally {
            stampedLock.unlock(readLock)
        }
    }

    fun save(requestingOrder: RequestingOrder) {
        val writeLock = stampedLock.writeLock()
        try {
            val newOrder = requestingOrder.copy(
                updated = OffsetDateTime.now(ZoneOffset.UTC)
            )
            orderProcessMap[newOrder.id] = newOrder
        } finally {
            stampedLock.unlock(writeLock)
        }

    }

    fun remove(requestingOrder: RequestingOrder) {
        val writeLock = stampedLock.writeLock()
        try {
            orderProcessMap.remove(requestingOrder.id)
        } finally {
            stampedLock.unlock(writeLock)
        }
    }

}