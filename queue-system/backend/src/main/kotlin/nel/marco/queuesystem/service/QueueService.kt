package nel.marco.queuesystem.service

import nel.marco.queuesystem.api.QueueNumber
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.StampedLock


data class DomainQueueNumber(
    val id: String,
    val number: Long
) {

    fun convert(): QueueNumber = QueueNumber(id = id, number = number)
}


@Service
class QueueService {

    //Atomic to make it thread-safe
    private val lastKnownNumber: AtomicLong = AtomicLong(0)
    private val map = ConcurrentHashMap<String, DomainQueueNumber>()
    private val stampedLock = StampedLock()


    fun createNextNumber(): DomainQueueNumber {
        val stamp = stampedLock.writeLock()
        try {
            val uuid = generateUUID()
            val queueNumber = lastKnownNumber.getAndIncrement()
            val domainQueueNumber = DomainQueueNumber(uuid.toString(), queueNumber)
            map[uuid.toString()] = domainQueueNumber
            return domainQueueNumber
        } finally {
            stampedLock.unlock(stamp)
        }
    }

    fun getQueue(): List<DomainQueueNumber> = map.values.toList().sortedBy { x -> x.number }

    fun getQueueNumber(id: String): DomainQueueNumber? = map[id]

    fun processOldestInQueue(): DomainQueueNumber? {
        val queue = getQueue()
        val queueNumber = queue.firstOrNull {
            return map.remove(it.id)
        }

        return queueNumber
    }

    fun clearQueue() = map.clear()


    private fun generateUUID() = UUID(Date().time, UUID.randomUUID().leastSignificantBits)

}