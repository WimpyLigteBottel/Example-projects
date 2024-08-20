package nel.marco.queuesystem.service

import nel.marco.queuesystem.api.QueueNumber
import org.slf4j.LoggerFactory
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

    private val log = LoggerFactory.getLogger(this::class.java)

    //Atomic to make it thread-safe
    private val lastKnownNumber: AtomicLong = AtomicLong(0)
    private val queue = LinkedBlockingQueue<DomainQueueNumber>()
    private val stampedLock = StampedLock()


    fun createNextNumber(): DomainQueueNumber {
        return stampedLock.withWriteLock {
            DomainQueueNumber(generateUUID().toString(), lastKnownNumber.getAndIncrement())
                .also {
                    log.info("$it created")
                    queue.add(it)
                }
        }
    }

    fun getQueue(): List<DomainQueueNumber> = queue.toList()

    fun getQueueNumber(id: String): DomainQueueNumber? {
        return stampedLock.withOptimisticRead {
            queue.firstOrNull { it.id == id }
        }
    }

    fun processOldestInQueue(): DomainQueueNumber? {
        if (queue.size > 0)
            return queue.remove().also {
                log.info("$it processed")
            }

        return null
    }

    fun clearQueue() = queue.clear()

    private fun generateUUID() = UUID(Date().time, UUID.randomUUID().leastSignificantBits)


    fun <T> StampedLock.withWriteLock(function: () -> T): T {
        val writeLock = this.writeLock()
        return try {
            function.invoke()
        } finally {
            this.unlockWrite(writeLock)
        }
    }

    fun <T> StampedLock.withOptimisticRead(function: () -> T): T {
        val lock = this.readLock()
        return try {
            function.invoke()
        } finally {
            this.unlockRead(lock)
        }
    }

}