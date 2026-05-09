package org.example.service

import org.example.api.Action
import org.example.api.PendingActionName
import org.example.internal.ProcessActionService
import org.example.repo.ActionRepo
import org.example.repo.RequestingOrder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.Executors


@Service
class OrderService(
    @param:Value($$"${order-server-url}") private val baseUrl: String
) : ProcessActionService(baseUrl = baseUrl, name = PendingActionName.CREATE_ORDER)

@Service
class PaymentService(
    @param:Value($$"${payment-server-url}") private val baseUrl: String
) : ProcessActionService(baseUrl = baseUrl, name = PendingActionName.PROCESS_PAYMENT)

@Service
class ItemService(
    @param:Value($$"${item-server-url}") private val baseUrl: String
) : ProcessActionService(baseUrl = baseUrl, name = PendingActionName.RESERVE_ITEM)

@Service
class ProcessingService(
    private val orderService: OrderService,
    private val paymentService: PaymentService,
    private val itemService: ItemService,
    private val actionRepo: ActionRepo
) {

    fun startProcess(requestingOrder: RequestingOrder) {
        actionRepo.save(requestingOrder)
        orderService.createFireAndForget(requestingOrder.id)
        itemService.createFireAndForget(requestingOrder.id)
        paymentService.createFireAndForget(requestingOrder.id)
    }

    fun handleResponse(action: Action) {
        val requestingOrder = actionRepo.find(action.globalId)

        requestingOrder?.let {
            it.pendingActions[action.name] = action
            actionRepo.save(it)
        }
    }


}


