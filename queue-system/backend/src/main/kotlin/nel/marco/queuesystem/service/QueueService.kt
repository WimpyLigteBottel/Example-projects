package nel.marco.queuesystem.service

import nel.marco.queuesystem.api.QueueNumber
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
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
    private val queue = LinkedBlockingQueue<DomainQueueNumber>()
    private val stampedLock = StampedLock()


    fun createNextNumber(): DomainQueueNumber {
        val stamp = stampedLock.writeLock()
        try {
            val uuid = generateUUID()
            val queueNumber = lastKnownNumber.getAndIncrement()
            val domainQueueNumber = DomainQueueNumber(uuid.toString(), queueNumber)
            queue.add(domainQueueNumber)
            return domainQueueNumber
        } finally {
            stampedLock.unlock(stamp)
        }
    }

    fun getQueue(): List<DomainQueueNumber> = queue.toList()

    fun getQueueNumber(id: String): DomainQueueNumber? = queue.firstOrNull { it.id == id }

    fun processOldestInQueue(): DomainQueueNumber? {
        if (queue.size > 0)
           return queue.remove()

        return null
    }

    fun clearQueue() = queue.clear()

    private fun generateUUID() = UUID(Date().time, UUID.randomUUID().leastSignificantBits)

}