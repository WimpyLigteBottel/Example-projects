package org.example.service

import org.example.dto.ActionAndState
import org.example.dto.RequestingOrder
import org.example.dto.State
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Service
class ProcessingService(
    val orderPaymentService: OrderPaymentService
) {


    private val log = LoggerFactory.getLogger(this::class.java)

    private var orderProcessMap = mutableMapOf<String, RequestingOrder>()


    fun startProcess(requestingOrder: RequestingOrder) {
        //Sending request creating order
        val createOrder = orderPaymentService.createOrder(requestingOrder.id)
        val createPayment = orderPaymentService.createPayment(requestingOrder.id)

        requestingOrder.pendingActions.add(createOrder)
        requestingOrder.pendingActions.add(createPayment)

        save(requestingOrder)
    }


    fun handleResponse(id: String, actionAndState: ActionAndState) {

        val requestingOrder = find(id)

        requestingOrder?.pendingActions?.removeIf { it.name == actionAndState.name }
        requestingOrder?.pendingActions?.add(actionAndState)


        requestingOrder?.let { save(it) }
    }


    @Scheduled(fixedRate = 1000)
    fun processActions() {
        val keys = orderProcessMap.keys

        keys.forEach {
            find(it)?.let { request ->

                if (request.hasFailure()) {
                    request.pendingActions.filter { it.state == State.SUCCESS }
                        .forEach {
                            val response = orderPaymentService.stopProcessing(it) // should rollback the success action
                            handleResponse(request.id, response) // save the memory
                        }
                }

                if (request.isSuccess()) {
                    log.info("order process is success! [request=$request]")

                    orderProcessMap.remove(request.id)
                } else if (request.isFailure()) {
                    log.info("order process has failed! [request=$request]")
                    orderProcessMap.remove(request.id)
                }
            }
        }
    }

    private fun save(requestingOrder: RequestingOrder) {
        val newOrder = requestingOrder.copy(
            updated = OffsetDateTime.now(ZoneOffset.UTC)
        )
        val previousValue = orderProcessMap.put(newOrder.id, newOrder)
        log.info("saving order [new=$requestingOrder;old=$previousValue]")
    }


    private fun find(id: String): RequestingOrder? {
        return orderProcessMap[id]
    }

}


