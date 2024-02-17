package com.wimpy.cron

import com.wimpy.core.MtgHistoryManager
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.time.OffsetDateTime

@Component
@EnableScheduling
open class PricerUpdateCron(
    private val mtgHistoryManager: MtgHistoryManager
) {

    private val log = LoggerFactory.getLogger(this.javaClass)


    @Scheduled(fixedRate = 5_000L)
    fun scheduleJob() {
        time { runBlocking {
            val olderThan = OffsetDateTime.now().minusSeconds(30)
            mtgHistoryManager.updateAllCardHistories(olderThan) } }
    }


    private fun time(action: () -> Unit) {
        val before = Instant.now()
        action.invoke();
        val after = Duration.between(before, Instant.now())
        log.info("updateAllPrices took [total=${after.toMillis()}ms]")
    }
}