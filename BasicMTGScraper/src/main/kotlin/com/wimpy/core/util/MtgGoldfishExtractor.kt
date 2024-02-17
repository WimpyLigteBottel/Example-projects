package com.wimpy.core.util

import com.wimpy.core.ScraperUtil
import com.wimpy.db.dao.entity.MtgCard
import com.wimpy.db.dao.entity.MtgHistory
import org.jsoup.nodes.Document
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.math.BigDecimal


@Configuration
@ConfigurationProperties(prefix = "mtg.gold.fish.extract")
open class MtgGoldfishProperties {
    lateinit var price: String
    lateinit var name: String
    lateinit var edition: String
}

@Component
open class MtgGoldfishExtractor(
    private val mtgGoldfishProperties: MtgGoldfishProperties,
    private val scraperUtil: ScraperUtil
) {


    fun getLatestCardHistory(link: String): MtgHistory {
        val document = scraperUtil.retrieveOnline(link)

        val mtgHistory = MtgHistory()
        mtgHistory.mtgCard = MtgCard(name = extractName(document))
        mtgHistory.price = extractPrice(document)
        mtgHistory.link = link
        return mtgHistory;
    }

    private fun extractPrice(document: Document): BigDecimal {
        return BigDecimal.valueOf(
            document
                .select(mtgGoldfishProperties.price)
                .html()
                .replace("&nbsp;".toRegex(), "").substring(1).toDouble()
        )
    }

    private fun extractName(document: Document): String {
        return document
            .select(mtgGoldfishProperties.name)
            .text()
            .replace("&nbsp;".toRegex(), "")
    }

    fun editionSelector(document: Document): String {
        return document
            .select(mtgGoldfishProperties.edition)
            .text()
            .replace("&nbsp;".toRegex(), "")
    }
}