package com.wimpy.core

import com.wimpy.db.dao.MtgCardCrudDao
import com.wimpy.db.dao.MtgHistory
import com.wimpy.db.dao.MtgHistoryCrudDao
import com.wimpy.rest.v1.model.CardHistoryModel
import com.wimpy.rest.v1.model.CardHistoryResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
open class MtgHistoryManager @Autowired constructor(
    private val mtgHistoryCrudDao: MtgHistoryCrudDao,
    private val mtgCardCrudDao: MtgCardCrudDao
) {
    fun retrieveHistory(cardName: String): CardHistoryResponse {
        val cardOptional = mtgCardCrudDao.findByName(cardName)

        val histories = cardOptional.map {
            val findByMtgCard = mtgHistoryCrudDao.findByMtgCard(it)

            return@map findByMtgCard
                .map { mtgHistory: MtgHistory ->
                    CardHistoryModel(mtgHistory.price, mtgHistory.created)
                }
        }.orElse(listOf())



        return CardHistoryResponse(cardName, histories);
    }

    fun clearHistory() {
        mtgHistoryCrudDao.deleteAll()
    }
}