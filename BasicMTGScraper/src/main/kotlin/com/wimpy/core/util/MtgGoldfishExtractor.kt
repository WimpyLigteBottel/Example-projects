package com.wimpy.core.util

import com.wimpy.db.dao.MtgHistory
import org.jsoup.nodes.Document
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.math.BigDecimal


@Configuration
@ConfigurationProperties(prefix = "mtg.gold.fish.extract")
open class MtgGoldfishProperties {
    //    @Value("\${mtg.gold.fish.extract.price}")
    lateinit var price: String

    //    @Value("\${mtg.gold.fish.extract.name}")
    lateinit var name: String

    //    @Value("\${mtg.gold.fish.extract.edition}")
    lateinit var edition: String
}

@Component
open class MtgGoldfishExtractor(
    private val mtgGoldfishProperties: MtgGoldfishProperties
) {


    fun extractFields(document: Document, link: String): MtgHistory {
        val mtgHistory = MtgHistory()
        mtgHistory.name = extractName(document)
        mtgHistory.price = extractPrice(document)
        mtgHistory.link = link
        return mtgHistory;
    }

    fun extractPrice(document: Document): BigDecimal {
        return BigDecimal.valueOf(
            document
                .select(mtgGoldfishProperties.price)
                .html()
                .replace("&nbsp;".toRegex(), "").substring(1).toDouble()
        )
    }

    fun extractName(document: Document): String {
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