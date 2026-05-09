package org.example.cron

import org.example.api.State
import org.example.repo.ActionRepo
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class CleanupCronJob(val actionRepo: ActionRepo) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedRate = 5000, timeUnit = TimeUnit.MILLISECONDS)
    fun cleanupAllFullyRolledBackTransactions() {
        val actions = actionRepo.findAll()
            .filter { it.isState(State.ROLLBACK) }

        actions.forEach { request ->
            actionRepo.remove(request)
        }

        if (!actions.isEmpty()) {
            log.info("removed rollback transactions [rollbackCount=${actions.size}]")
        }
    }

}


