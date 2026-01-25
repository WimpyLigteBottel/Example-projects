package me.marco.manual

import java.time.Duration
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.Executors

class SchedulerCleanup {

    val cleanup = Executors.newVirtualThreadPerTaskExecutor()
    val resetInflight = Executors.newVirtualThreadPerTaskExecutor()


    /**
     * Cleans up the message after a bit
     */
    fun cleanup(
        id: UUID,
        subscriber: Subscriber,
        subscribersQueueMap: ConcurrentHashMap<Subscriber, ConcurrentLinkedDeque<Envelop>>
    ) {
        cleanup.submit {
            Thread.sleep(Duration.ofSeconds(1))
            subscribersQueueMap[subscriber]?.removeIf { it.id == id && it.state == Envelop.EnvelopState.COMPLETED }
        }
    }

    /**
     * Aka if the message is still in flight after 10s then fix it back to ready to be pulled
     */
    fun resetInflight(
        e: UUID?,
        subscriber: Subscriber,
        subscribersQueueMap: ConcurrentHashMap<Subscriber, ConcurrentLinkedDeque<Envelop>>
    ) {
        resetInflight.submit {
            Thread.sleep(Duration.ofSeconds(10))

            val envelop =
                subscribersQueueMap[subscriber]?.find { e == it.id && it.state == Envelop.EnvelopState.INFLIGHT }

            envelop?.let {
                it.state = Envelop.EnvelopState.READY
            }
        }
    }

}