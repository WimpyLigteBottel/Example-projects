package com.wimpy.rest;


import com.wimpy.core.MtgGoldfishScraper;
import com.wimpy.dao.entity.MtgHistory;
import com.wimpy.model.MtgQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/scrape")
public class ScraperRestEndpoint {

    private static final Logger log = LoggerFactory.getLogger(ScraperRestEndpoint.class);
    private final MtgGoldfishScraper mtgGoldfishScraper;

    @Autowired
    public ScraperRestEndpoint(MtgGoldfishScraper mtgGoldfishScraper) {
        this.mtgGoldfishScraper = mtgGoldfishScraper;
    }


    @GetMapping
    public ResponseEntity<String> getCardDetails(@RequestParam String name, @RequestParam String edition) {
        MtgQuery mtgQuery = new MtgQuery(name, edition, "");

        MtgHistory history = mtgGoldfishScraper.retrieveCardPrice(mtgQuery);

        log.info("name:{}->{}", name, history);

        return ResponseEntity.ok(history.getPrice().toString());
    }

    @PostMapping
    public ResponseEntity<MtgHistory> addNewCard(@RequestParam String name, @RequestParam String link) {
        MtgQuery mtgGoldFishQuery = new MtgQuery(name, "", link);


        return ResponseEntity.ok(mtgGoldfishScraper.saveCardPrice(mtgGoldFishQuery));
    }
}
