package com.wimpy.rest.v1

import com.wimpy.core.MtgGoldfishScraper
import com.wimpy.db.dao.MtgHistory
import com.wimpy.rest.v1.model.MtgQuery
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/v1/scrape")
class ScraperRestEndpoint @Autowired constructor(private val mtgGoldfishScraper: MtgGoldfishScraper) {

    private val log = LoggerFactory.getLogger(this.javaClass)


    @PostMapping
    fun addNewCard(
        @RequestParam(required = false, defaultValue = "") name: String,
        @RequestParam link: String
    ): ResponseEntity<List<MtgHistory>> {
        val mtgHistory = mtgGoldfishScraper.retrieveCardPrice(MtgQuery(name, "", link))
        return ResponseEntity.ok(mtgGoldfishScraper.saveCardPrice(mtgHistory))
    }


}