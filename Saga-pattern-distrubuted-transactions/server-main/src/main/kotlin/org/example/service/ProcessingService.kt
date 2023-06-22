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

@Service
class ProcessingService(
    val orderPaymentService: OrderPaymentService
) {


    private val log = LoggerFactory.getLogger(this::class.java)

    private var orderProcessMap = ConcurrentHashMap<String, RequestingOrder>()


    fun startProcess(requestingOrder: RequestingOrder) {
        //Sending request creating order
        val createOrder = orderPaymentService.createOrder(requestingOrder.id)
        val createPayment = orderPaymentService.createPayment(requestingOrder.id)


        save(requestingOrder.copy(pendingActions = mutableListOf(createOrder, createPayment)))
    }


    fun handleResponse(id: String, actionAndState: ActionAndState) {
        val requestingOrder = find(id)

        requestingOrder?.pendingActions?.removeIf { it.name == actionAndState.name }
        requestingOrder?.pendingActions?.add(actionAndState)

        requestingOrder?.let { save(it) }

    }


    /**
     * This will at a fix rate try to process all successful and failed actions and ignore the pending
     */
    @Scheduled(fixedRate = 5000, timeUnit = TimeUnit.MILLISECONDS)
    fun processActions() {

        orderProcessMap.values.forEach { request ->
            if (request.hasFailure()) {
                request.pendingActions
                    .filter { it.state == State.SUCCESS }
                    .forEach {
                        orderPaymentService.stopProcessing(request.id, it) // should rollback the success action
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
        log.info("saving request [new={};old={}]", requestingOrder, previousValue)

    }


    private fun find(id: String): RequestingOrder? {
        return orderProcessMap[id]

    }

}


