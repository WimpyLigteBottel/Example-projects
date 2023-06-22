package org.example.service

import org.example.dto.ActionAndState
import org.example.dto.RequestingOrder
import org.example.dto.State
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.StampedLock

@Service
class ProcessingService(
    val orderPaymentService: OrderPaymentService
) {


    private val log = LoggerFactory.getLogger(this::class.java)

    private val stampedLock = StampedLock()
    private var orderProcessMap = ConcurrentHashMap<String, RequestingOrder>()


    fun startProcess(requestingOrder: RequestingOrder) {
        val id = stampedLock.writeLock()
        try {
            //Sending request creating order
            val createOrder = orderPaymentService.createOrder(requestingOrder.id)
            val createPayment = orderPaymentService.createPayment(requestingOrder.id)


            save(requestingOrder.copy(pendingActions = mutableListOf(createOrder, createPayment)))
        } finally {
            stampedLock.unlock(id)
        }
    }


    fun handleResponse(id: String, actionAndState: ActionAndState) {
        val requestingOrder = find(id)


        val stamp = stampedLock.writeLock()
        try {
            requestingOrder?.pendingActions?.removeIf { it.name == actionAndState.name }
            requestingOrder?.pendingActions?.add(actionAndState)

            requestingOrder?.let { save(it) }

        } finally {
            stampedLock.unlock(stamp)
        }
    }


    /**
     * This will at a fix rate try to process all successful and failed actions and ignore the pending
     */
    @Scheduled(fixedRate = 5000, timeUnit = TimeUnit.MILLISECONDS)
    fun processActions() {

        var toBeProcessed = mutableMapOf<String, RequestingOrder>()

        val stamp = stampedLock.readLock()
        try {
            toBeProcessed.putAll(orderProcessMap.filter {
                return@filter it.value.created.isAfter(OffsetDateTime.now().minusSeconds(10))
            })
        } finally {
            stampedLock.unlock(stamp)
        }


        toBeProcessed.values.forEach { request ->
            if (request.hasFailure()) {
                request.pendingActions
                    .filter { it.state == State.SUCCESS }
                    .forEach {
                        val response = orderPaymentService.stopProcessing(it) // should rollback the success action


                        response?.let {
                            handleResponse(request.id, response) // save / update
                        }
                    }
            }

            if (request.isSuccess()) {
                log.info("request process is success! [request=$request]")
                orderProcessMap.remove(request.id)
            } else if (request.isFailure()) {
                log.info("request process has failed! [request=$request]")
                orderProcessMap.remove(request.id)
            }
        }


    }

    private fun save(requestingOrder: RequestingOrder) {
        val newOrder = requestingOrder.copy(
            updated = OffsetDateTime.now(ZoneOffset.UTC)
        )
        val previousValue = orderProcessMap.put(newOrder.id, newOrder)
        log.debug("saving request [new={};old={}]", requestingOrder, previousValue)

    }


    private fun find(id: String): RequestingOrder? {
        val stamp = stampedLock.readLock()
        try {
            return orderProcessMap[id]
        } finally {
            stampedLock.unlock(stamp)
        }
    }

}


