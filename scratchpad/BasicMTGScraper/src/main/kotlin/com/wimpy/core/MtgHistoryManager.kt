package com.wimpy.core

import com.wimpy.core.util.MtgGoldfishExtractor
import com.wimpy.db.dao.MtgCardCrudDao
import com.wimpy.db.dao.MtgHistoryCrudDao
import com.wimpy.db.dao.MtgHistorySpecification
import com.wimpy.db.dao.entity.MtgCard
import com.wimpy.db.dao.entity.MtgHistory
import com.wimpy.rest.v1.models.CardHistoryModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import kotlin.jvm.optionals.getOrElse

@Component
open class MtgHistoryManager(
    private val mtgHistoryCrudDao: MtgHistoryCrudDao,
    private val mtgCardCrudDao: MtgCardCrudDao,
    private val mtgGoldfishExtractor: MtgGoldfishExtractor,
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Transactional
    open fun saveCardAndHistoryToDb(link: String): MtgCard {
        val latestCardHistory = mtgGoldfishExtractor.getLatestCardHistory(link)

        //save card first
        val card = mtgCardCrudDao.findByName(latestCardHistory.mtgCard.name).getOrElse {
            mtgCardCrudDao.save(latestCardHistory.mtgCard).let {
                log.info("new card saved name=${it.name} id=${it.id}")
                it
            }
        }


        // save history
        mtgHistoryCrudDao.save(latestCardHistory.copy(mtgCard = card))

        log.info("Got the latest history for name=${card.name}")


        return card
    }


    @Transactional
    open fun retrieveLatestHistory(link: String): List<CardHistoryModel> {
        val card = saveCardAndHistoryToDb(link)


        return mtgHistoryCrudDao.findAllByMtgCard(card)
            .map { it.convert() }

    }

    @Transactional
    open fun retrieveHistoriesOfCard(name: String): List<CardHistoryModel> {
        val mtgCard = mtgCardCrudDao.getByName(name)


        return mtgHistoryCrudDao.findAllByMtgCard(mtgCard)
            .map { it.convert() }

    }

    open fun retrieveHistoriesOfAllCards(): List<List<MtgHistory>> {
        val cards = mtgCardCrudDao.findAll()

        return cards.map {
            mtgHistoryCrudDao.findAllByMtgCard(it)
        }

    }


    suspend fun updateAllCardHistories(olderThan: OffsetDateTime) {
        coroutineScope {
            val cards = mtgCardCrudDao.findAll()
            val firstHistoryOfAllCards = cards.mapNotNull {
                val history = mtgHistoryCrudDao.findAllByMtgCard(it).maxByOrNull { it.created }!!


                if(history.updated.isAfter(olderThan))
                    return@mapNotNull null

                 history
            }


            val deferred = firstHistoryOfAllCards.map {
                async {
                    val latestCardHistory = mtgGoldfishExtractor.getLatestCardHistory(it.link)
                        .copy(mtgCard = it.mtgCard)

                    mtgHistoryCrudDao.save(latestCardHistory)
                    log.info("updated card $latestCardHistory")
                }
            }


            deferred.awaitAll()

            log.info("Updated ${deferred.size} cards")
        }
    }

    @Transactional
    open fun clearHistory() {
        mtgCardCrudDao.deleteAll()
        mtgHistoryCrudDao.deleteAll()
    }

    private fun MtgHistory.convert(): CardHistoryModel {
        return CardHistoryModel(
            id = this.id!!,
            cardName = this.mtgCard.name,
            price = this.price,
            timeCreated = this.created
        )
    }

}

