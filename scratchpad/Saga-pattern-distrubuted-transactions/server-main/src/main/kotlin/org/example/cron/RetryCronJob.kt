package org.example.cron

import org.example.api.PendingActionName
import org.example.api.State
import org.example.repo.ActionRepo
import org.example.repo.RequestingOrder
import org.example.service.ItemService
import org.example.service.OrderService
import org.example.service.PaymentService
import org.example.service.ProcessingService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

@Service
class RetryCronJob(
    val orderService: OrderService,
    val paymentService: PaymentService,
    val itemService: ItemService,
    val actionRepo: ActionRepo,
    val processingService: ProcessingService,
    @param:Value($$"${total-transaction-time}") val totalJourneyTimePossible: Int
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedRate = 500, timeUnit = TimeUnit.MILLISECONDS)
    fun initiateRetry() {
        var counter = 0

        val actions = actionRepo.findAll()
            .filter { it.isState(State.FAILED) }
            .filter { !isOlderThanTransactionJourney(it) }

        actions.forEach { request ->


            log.info(
                "Retrying! [globalId={};actions={}]",
                request.id,
                request.pendingActions.map { it.key to it.value.state }
            )

            request.pendingActions.keys.forEach {

                val action = request.pendingActions[it] ?: return@forEach

                when (it) {
                    PendingActionName.CREATE_ORDER -> orderService.createFireAndForget(action.globalId)
                    PendingActionName.PROCESS_PAYMENT -> paymentService.createFireAndForget(action.globalId)
                    PendingActionName.RESERVE_ITEM -> itemService.createFireAndForget(action.globalId)
                    PendingActionName.UNKNOWN -> {
                        log.warn("Unknown action $action")
                        counter-- // cancels out the counter++
                    }
                }
                processingService.handleResponse(action.pending())

                counter++
            }
        }

        if (counter > 0) log.info("Actions retried [count=$counter]")
    }

    private fun isOlderThanTransactionJourney(it: RequestingOrder) =
        Duration.between(it.created, OffsetDateTime.now(ZoneOffset.UTC)).toSeconds() > totalJourneyTimePossible


}


