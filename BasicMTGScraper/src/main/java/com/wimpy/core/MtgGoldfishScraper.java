package com.wimpy.core;

import com.google.gson.Gson;
import com.wimpy.dao.CardDatabaseCrudDao;
import com.wimpy.dao.entity.MtgCard;
import com.wimpy.dao.entity.MtgHistory;
import com.wimpy.model.MtgQuery;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains the core logic to retrieve the card price
 */
@Component
public class MtgGoldfishScraper {


    private static final Logger log = LoggerFactory.getLogger(MtgGoldfishScraper.class);
    private final Map<String, String> cookies = new HashMap<>();

    @Value("${mtg.gold.fish.extract.price}")
    private String priceSelector;

    @Value("${mtg.gold.fish.extract.name}")
    private String nameSelector;

    @Value("${mtg.gold.fish.extract.edition}")
    private String editionSelector;

    private final CardDatabaseCrudDao cardDatabaseCrudDao;

    @Autowired
    public MtgGoldfishScraper(CardDatabaseCrudDao cardDatabaseCrudDao) {
        this.cardDatabaseCrudDao = cardDatabaseCrudDao;
        setupDefaultCookies();
    }

    /**
     * Setup the default cookies to scrap future request a lot faster
     */
    private void setupDefaultCookies() {
        try {
            cookies.putAll(
                    Jsoup.connect(
                                    "https://www.mtggoldfish.com/price/Throne+of+Eldraine/Edgewall+Innkeeper#paper")
                            .cookies(cookies)
                            .execute()
                            .cookies());
        } catch (IOException e) {
            log.error("Failed to update default cookies [errorMessage={}]", e.getMessage(), e);
        }
    }


    @Transactional
    public MtgHistory retrieveCardPrice(MtgQuery mtgGoldFishQuery) {

        String link = mtgGoldFishQuery.link();

        // If link is empty construct it yourself
        if (mtgGoldFishQuery.link().length() == 0) {
            link = constructLinkOfCard(mtgGoldFishQuery.cardName(), mtgGoldFishQuery.edition());
        }

        try {
            Connection.Response execute = Jsoup.connect(link).cookies(cookies).execute();
            // Updates cookies after every request
            cookies.putAll(execute.cookies());
            Document document = Jsoup.parse(execute.body());


            MtgHistory mtgHistory = new MtgHistory();

            String name = document
                    .select(nameSelector)
                    .text()
                    .replaceAll("&nbsp;", "");
            mtgHistory.setName(name);

            BigDecimal price = BigDecimal.valueOf(Double.parseDouble(document
                    .select(priceSelector)
                    .html()
                    .replaceAll("&nbsp;", "").substring(1)));
            mtgHistory.setPrice(price);

            mtgHistory.setLink(link);

            return mtgHistory;

        } catch (Exception e) {
            log.error("Failed to retrieve card details from website [link={}]", link, e);
            throw new RuntimeException("Could not retrieve card details");
        }

    }

    @Transactional
    public MtgHistory saveCardPrice(MtgQuery query) {
        MtgHistory mtgHistory = retrieveCardPrice(query);


        MtgHistory card = cardDatabaseCrudDao.save(mtgHistory);
        log.info("saved [entityJson={}]", new Gson().toJson(card));

        return card;
    }


    /**
     * Takes the cardName and edition and construct the link find the card
     *
     * @param cardName
     * @param edition
     * @return
     */
    private String constructLinkOfCard(String cardName, String edition) {

        String formattedEdition = specialFormatting(edition);
        String cardNameEdition = specialFormatting(cardName);

        return String.format("https://www.mtggoldfish.com/price/%s/%s#paper", formattedEdition, cardNameEdition);
    }


    private String specialFormatting(String text) {
        return text.replaceAll("[\\s]+", "+");
    }
}
