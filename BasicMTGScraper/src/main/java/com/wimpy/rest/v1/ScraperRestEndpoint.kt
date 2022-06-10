package com.wimpy.rest.v1

import com.wimpy.core.MtgGoldfishScraper
import com.wimpy.db.entity.MtgHistory
import com.wimpy.rest.v1.model.MtgQuery
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/v1/scrape")
class ScraperRestEndpoint @Autowired constructor(private val mtgGoldfishScraper: MtgGoldfishScraper) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @GetMapping
    fun getCardDetails(@RequestParam name: String, @RequestParam edition: String): ResponseEntity<String> {
        val history = mtgGoldfishScraper.retrieveCardPrice(MtgQuery(name, edition, ""))
        log.info("name:{}->{}", name, history)
        return ResponseEntity.ok(history.price.toString())
    }

    @PostMapping
    fun addNewCard(@RequestParam(required = false,defaultValue = "") name: String, @RequestParam link: String): ResponseEntity<MtgHistory> {
        val mtgHistory = mtgGoldfishScraper.retrieveCardPrice(MtgQuery(name, "", link))
        return ResponseEntity.ok(mtgGoldfishScraper.saveCardPrice(mtgHistory))
    }


}