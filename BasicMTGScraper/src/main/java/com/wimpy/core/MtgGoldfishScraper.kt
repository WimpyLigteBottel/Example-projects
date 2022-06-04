package com.wimpy.core

import com.google.gson.Gson
import com.wimpy.core.MtgGoldfishScraper
import com.wimpy.core.util.MtgGoldfishExtractor
import com.wimpy.dao.MtgCardCrudDao
import com.wimpy.dao.MtgHistoryCrudDao
import com.wimpy.dao.entity.MtgCard
import com.wimpy.dao.entity.MtgHistory
import com.wimpy.rest.v1.model.MtgQuery
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * This class contains the core logic to retrieve the card price
 */
@Component
open class MtgGoldfishScraper @Autowired constructor(
    private val mtgGoldfishExtractor: MtgGoldfishExtractor,
    private val mtgHistoryCrudDao: MtgHistoryCrudDao,
    private val mtgCardCrudDao: MtgCardCrudDao,
    private val scraperUtil: ScraperUtil
) {


    @Transactional
    open fun retrieveCardPrice(mtgGoldFishQuery: MtgQuery): MtgHistory {
        var link = mtgGoldFishQuery.link

        //https://www.mtggoldfish.com/price/Commander 2017/Alms Collector#paper
        if(mtgGoldFishQuery.cardName.isBlank() && link.endsWith("#paper"))
            mtgGoldFishQuery.cardName = link.substringAfterLast("/").substringBefore("#paper")

        // If link is empty construct it yourself
        if (mtgGoldFishQuery.link.length == 0) {
            link = constructLinkOfCard(mtgGoldFishQuery.cardName, mtgGoldFishQuery.edition)
        }

        return try {
            val document = scraperUtil.retrieveOnline(link)
            val mtgHistory = MtgHistory()
            mtgHistory.name = mtgGoldfishExtractor.extractName(document)
            mtgHistory.price = mtgGoldfishExtractor.extractPrice(document)
            mtgHistory.link = link
            mtgHistory
        } catch (e: Exception) {
            log.error("Failed to retrieve card details from website [link={}]", link, e)
            throw RuntimeException("Could not retrieve card details", e)
        }
    }

    @Transactional
    open fun saveCardPrice(mtgHistory: MtgHistory): MtgHistory {
        val optionalMtgCard = mtgCardCrudDao.findByName(mtgHistory.name)


        //if empty creates the card and links it up to the history
        optionalMtgCard.ifPresentOrElse({ mtgCard: MtgCard? -> mtgHistory.mtgCard = mtgCard }) {
            val entity = MtgCard()
                .setName(mtgHistory.name)
            mtgHistory.mtgCard = mtgCardCrudDao.save(entity)
        }
        val card = mtgHistoryCrudDao.save(mtgHistory)
        log.info("saved [entityJson={}]", Gson().toJson(card))
        return card
    }

    /**
     * Takes the cardName and edition and construct the link find the card
     *
     * @param cardName
     * @param edition
     * @return
     */
    private fun constructLinkOfCard(cardName: String, edition: String): String {
        val formattedEdition = specialFormatting(edition)
        val cardNameEdition = specialFormatting(cardName)
        return "https://www.mtggoldfish.com/price/${formattedEdition}/${cardNameEdition}#paper";
    }

    private fun specialFormatting(text: String): String {
        return text.replace("[\\s]+".toRegex(), "+")
    }

    companion object {
        private val log = LoggerFactory.getLogger(MtgGoldfishScraper::class.java)
    }
}