package com.wimpy.core.util;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MtgGoldfishExtractor {

    @Value("${mtg.gold.fish.extract.price}")
    private String priceSelector;

    @Value("${mtg.gold.fish.extract.name}")
    private String nameSelector;

    @Value("${mtg.gold.fish.extract.edition}")
    private String editionSelector;

    public BigDecimal extractPrice(Document document) {
        return BigDecimal.valueOf(Double.parseDouble(document
                .select(priceSelector)
                .html()
                .replaceAll("&nbsp;", "").substring(1)));
    }

    public String extractName(Document document) {
        return document
                .select(nameSelector)
                .text()
                .replaceAll("&nbsp;", "");
    }

}
