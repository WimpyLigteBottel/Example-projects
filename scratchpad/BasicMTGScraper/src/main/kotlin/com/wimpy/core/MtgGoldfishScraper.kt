package com.wimpy.core

import com.wimpy.core.util.MtgGoldfishExtractor
import com.wimpy.db.dao.entity.MtgHistory
import com.wimpy.rest.v1.models.MtgQueryRequest
import org.springframework.stereotype.Component

@Component
open class MtgGoldfishScraper(
    private val mtgGoldfishExtractor: MtgGoldfishExtractor,
) {


    /**
     * Gets the latest card history from goldfish
     */
    fun retrieveCardHistory(mtgQuery: MtgQueryRequest): MtgHistory = mtgGoldfishExtractor.getLatestCardHistory(mtgQuery.link)


}