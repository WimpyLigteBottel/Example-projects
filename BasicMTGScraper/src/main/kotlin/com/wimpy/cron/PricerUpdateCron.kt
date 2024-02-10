package com.wimpy.cron

import com.wimpy.core.MtgGoldfishScraper
import com.wimpy.db.dao.MtgHistory
import com.wimpy.db.dao.MtgHistoryCrudDao
import com.wimpy.db.filter.MtgHistoryFilter
import com.wimpy.db.filter.MtgHistorySpecification
import com.wimpy.rest.v1.model.MtgQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Component
@EnableScheduling
class PricerUpdateCron constructor(
    private val mtgGoldfishScraper: MtgGoldfishScraper,
    private val mtgHistoryCrudDao: MtgHistoryCrudDao,
) {

    private val log = LoggerFactory.getLogger(this.javaClass)


    private fun toLocalDateTime(instant: Instant): LocalDateTime =
        LocalDateTime.ofInstant(instant, ZoneId.systemDefault())


    //    @Scheduled(fixedRate = 5_000L)
    fun scheduleJob() {
        time { updateAllPricesCoroutines() };
    }

    fun updateAllPricesCoroutines() {

        val specification = MtgHistorySpecification(
            mtgHistoryFilter = MtgHistoryFilter(
                olderThan = LocalDateTime.now().minusMinutes(1)
            )
        )

        val findAll = mtgHistoryCrudDao.findAll(specification)
        val allCards = findAll.distinctBy { it.name }

        runBlocking(Dispatchers.IO) {
            for (it in allCards) {
                launch { updatePriceOfCard(it) }
            }
        }

    }

    private fun updatePriceOfCard(mtgHistory: MtgHistory) {
        val mtgGoldFishQuery = MtgQuery(mtgHistory.name, "", mtgHistory.link)
        mtgGoldfishScraper.saveCardPrice(mtgGoldfishScraper.retrieveCardPrice(mtgGoldFishQuery))
    }


    private fun time(action: () -> Unit) {
        val before = Instant.now()
        action.invoke();
        val after = Duration.between(before, Instant.now())
        log.info("updateAllPrices took [tota=${after.toMillis()}ms]")
    }
}