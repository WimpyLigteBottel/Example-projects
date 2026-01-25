package me.marco.manual

import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque


interface Publisher {
    fun subscribe(subscriber: Subscriber)
    fun unsubscribe(subscriber: Subscriber)
    fun publish(message: String)
    fun poll(subscriber: Subscriber, batchSize: Int): List<Envelop>
    fun ack(envelop: Envelop, subscriber: Subscriber)
}

class SimplePublisher(
    val schedulerCleanup: SchedulerCleanup
) : Publisher {
    val subscribersQueueMap = ConcurrentHashMap<Subscriber, ConcurrentLinkedDeque<Envelop>>()

    override fun subscribe(subscriber: Subscriber) {
        subscribersQueueMap[subscriber] = ConcurrentLinkedDeque()
    }

    override fun unsubscribe(subscriber: Subscriber) {
        subscribersQueueMap.remove(subscriber)
    }

    override fun publish(message: String) {
        subscribersQueueMap.forEach { entry ->
            entry.value.add(Envelop(UUID.randomUUID(), message, Envelop.EnvelopState.READY))
        }
    }

    /**
     * Tries to get the next N messages from the queue.
     *
     * Then also mark the message as inflight so that they dont get pulledd again.
     * Also create a task to reset it if its stil in flight after X time.
     */
    override fun poll(subscriber: Subscriber, batchSize: Int): List<Envelop> {
        val subscriberMessages = subscribersQueueMap[subscriber] ?: return listOf()

        return subscriberMessages
            .asSequence()
            .filter { it.state == Envelop.EnvelopState.READY }
            .take(batchSize)
            .map {
                it.state = Envelop.EnvelopState.INFLIGHT
                it
            }
            .toList()
            .also { envelops ->
                for (e in envelops) {
                    schedulerCleanup.resetInflight(e.id, subscriber, subscribersQueueMap)
                }
            }
    }


    /**
     * Acknowledge a message and create a task to clean it up.
     */
    override fun ack(envelop: Envelop, subscriber: Subscriber) {
        val subscriberMessages = subscribersQueueMap[subscriber] ?: return

        if (envelop.id == null) {
            return
        }

        subscriberMessages.find { it.id == envelop.id }?.state = Envelop.EnvelopState.COMPLETED

        schedulerCleanup.cleanup(envelop.id, subscriber, subscribersQueueMap)
    }

}