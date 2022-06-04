package com.wimpy.core.util

import org.jsoup.nodes.Document
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
open class MtgGoldfishExtractor {
    @Value("\${mtg.gold.fish.extract.price}")
    private val priceSelector: String? = null

    @Value("\${mtg.gold.fish.extract.name}")
    private val nameSelector: String? = null

    @Value("\${mtg.gold.fish.extract.edition}")
    private val editionSelector: String? = null

    fun extractPrice(document: Document): BigDecimal {
        return BigDecimal.valueOf(
            document
                .select(priceSelector)
                .html()
                .replace("&nbsp;".toRegex(), "").substring(1).toDouble()
        )
    }

    fun extractName(document: Document): String {
        return document
            .select(nameSelector)
            .text()
            .replace("&nbsp;".toRegex(), "")
    }

    fun editionSelector(document: Document): String {
        return document
            .select(editionSelector)
            .text()
            .replace("&nbsp;".toRegex(), "")
    }
}