package org.example.service

import org.example.Action
import org.example.PendingActionName
import org.example.ProcessActionService
import org.example.repo.ActionRepo
import org.example.repo.RequestingOrder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


@Service
class OrderService(
    @Value("\${order-server-url}") private val baseUrl: String
) : ProcessActionService(baseUrl = baseUrl, name = PendingActionName.CREATE_ORDER)

@Service
class PaymentService(
    @Value("\${payment-server-url}") private val baseUrl: String
) : ProcessActionService(baseUrl = baseUrl, name = PendingActionName.PROCESS_PAYMENT)

@Service
class ProcessingService(
    private val orderService: OrderService,
    private val paymentService: PaymentService,
    private val actionRepo: ActionRepo
) {


    fun startProcess(requestingOrder: RequestingOrder) {
        val createOrder = orderService.createFireAndForget(requestingOrder.id)
        val createPayment = paymentService.createFireAndForget(requestingOrder.id)

        requestingOrder.addPendingAction(createOrder)
        requestingOrder.addPendingAction(createPayment)

        actionRepo.save(requestingOrder)
    }


    fun handleResponse(action: Action) {
        val requestingOrder = actionRepo.find(action.globalId)

        requestingOrder?.let {
            it.pendingActions[action.name] = action
            actionRepo.save(it)
        }
    }


}


