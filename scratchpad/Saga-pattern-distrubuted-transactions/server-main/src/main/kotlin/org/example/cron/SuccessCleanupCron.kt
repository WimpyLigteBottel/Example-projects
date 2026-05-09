package org.example.cron

import org.example.api.MainServerController
import org.example.api.State
import org.example.repo.ActionRepo
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class SuccessCleanupCron(
    val actionRepo: ActionRepo,
    val mainServerController: MainServerController,
) {
    private val log = LoggerFactory.getLogger(this::class.java)


    @Scheduled(fixedRate = 5000, timeUnit = TimeUnit.MILLISECONDS)
    fun processActions() {
        log.info("Starting event process")
        mainServerController.startProcess(100)
    }


    @Scheduled(fixedRate = 5000, timeUnit = TimeUnit.MILLISECONDS)
    fun successfulTransactions() {
        val actions = actionRepo.findAll()
            .filter { it.isState(State.SUCCESS) }

        if (!actions.isEmpty()) {
            log.info("success transactions and cleaning up [count=${actions.size}]")
        }

        actions.forEach { request ->
            actionRepo.remove(request)
        }

    }
}


