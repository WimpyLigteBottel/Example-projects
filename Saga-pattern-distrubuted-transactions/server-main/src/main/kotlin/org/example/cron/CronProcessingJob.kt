package org.example.cron

import org.example.PendingActionName
import org.example.api.MainServerController
import org.example.repo.ActionRepo
import org.example.repo.RequestingOrder
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
class CronProcessingJob(
    val orderService: OrderService,
    val paymentService: PaymentService,
    val actionRepo: ActionRepo,
    val processingService: ProcessingService,
    val mainServerController: MainServerController,
    @Value("\${total-transaction-time}") val totalJourneyTimePossible: Int
) {


    private val log = LoggerFactory.getLogger(this::class.java)


    @Scheduled(fixedRate = 5000, timeUnit = TimeUnit.MILLISECONDS)
    fun processActions() {
        log.info("Starting event process")
        mainServerController.startProcess(100)
    }


    @Scheduled(fixedRate = 5000, timeUnit = TimeUnit.MILLISECONDS)
    fun successfullTransactions() {
        val actions = actionRepo.findAll()
            .map { it.value }
            .filter { it.isSuccess() }

        log.info("success transactions [count=${actions.size}]")
    }


    @Scheduled(fixedRate = 5000, timeUnit = TimeUnit.MILLISECONDS)
    fun cleanupAllFullyRolledBackTransactions() {
        val actions = actionRepo.findAll()
            .map { it.value }
            .filter { it.isRolledBacked() }


        actions.forEach { request ->
            actionRepo.remove(request)
        }

        log.info("removed rollback transactions [rollbackCount=${actions.size}]")
    }


    @Scheduled(fixedRate = 5000, timeUnit = TimeUnit.MILLISECONDS)
    fun initiateRollbackActions() {

        var counter = 0

        val actions = actionRepo.findAll()
            .map { it.value }
            .filter { isOlderThanTransactionJourney(it) }
            .filter { !it.isSuccess() }


        actions.forEach { request ->
            log.info(
                "Needs to rollback! [globalId={};payment={};order={}]",
                request.id,
                request.pendingActions[PendingActionName.PROCESS_PAYMENT]?.state,
                request.pendingActions[PendingActionName.CREATE_ORDER]?.state,
            )


            // cleaning up orders
            request.pendingActions[PendingActionName.CREATE_ORDER]
                ?.let {
                    orderService.stopProcessingBlocking(it)
                    processingService.handleResponse(it.rollback())
                    counter++
                }


            // cleaning up payemnts
            request.pendingActions[PendingActionName.PROCESS_PAYMENT]
                ?.let {
                    paymentService.stopProcessingBlocking(it)
                    processingService.handleResponse(it.rollback())
                    counter++
                }

        }

        log.info("cleaning up old actions [rollbackCountInitiated=$counter]")
    }

    private fun isOlderThanTransactionJourney(it: RequestingOrder) =
        Duration.between(it.created, OffsetDateTime.now(ZoneOffset.UTC)).toSeconds() > totalJourneyTimePossible


}


