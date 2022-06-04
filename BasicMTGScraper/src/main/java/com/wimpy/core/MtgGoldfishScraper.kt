package com.wimpy.core;

import com.google.gson.Gson;
import com.wimpy.core.util.MtgGoldfishExtractor;
import com.wimpy.dao.MtgCardCrudDao;
import com.wimpy.dao.MtgHistoryCrudDao;
import com.wimpy.dao.entity.MtgCard;
import com.wimpy.dao.entity.MtgHistory;
import com.wimpy.rest.v1.model.MtgQuery;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * This class contains the core logic to retrieve the card price
 */
@Component
public class MtgGoldfishScraper {


    private static final Logger log = LoggerFactory.getLogger(MtgGoldfishScraper.class);

    private final MtgGoldfishExtractor mtgGoldfishExtractor;
    private final MtgHistoryCrudDao mtgHistoryCrudDao;
    private final MtgCardCrudDao mtgCardCrudDao;
    private final ScraperUtil scraperUtil;

    @Autowired
    public MtgGoldfishScraper(MtgGoldfishExtractor mtgGoldfishExtractor, MtgHistoryCrudDao mtgHistoryCrudDao, MtgCardCrudDao mtgCardCrudDao, ScraperUtil scraperUtil) {
        this.mtgGoldfishExtractor = mtgGoldfishExtractor;
        this.mtgHistoryCrudDao = mtgHistoryCrudDao;
        this.mtgCardCrudDao = mtgCardCrudDao;
        this.scraperUtil = scraperUtil;
    }

    @Transactional
    public MtgHistory retrieveCardPrice(MtgQuery mtgGoldFishQuery) {

        String link = mtgGoldFishQuery.link();

        // If link is empty construct it yourself
        if (mtgGoldFishQuery.link().length() == 0) {
            link = constructLinkOfCard(mtgGoldFishQuery.cardName(), mtgGoldFishQuery.edition());
        }

        try {
            Document document = scraperUtil.retrieveOnline(link);

            MtgHistory mtgHistory = new MtgHistory();
            mtgHistory.setName(mtgGoldfishExtractor.extractName(document));
            mtgHistory.setPrice(mtgGoldfishExtractor.extractPrice(document));
            mtgHistory.setLink(link);

            return mtgHistory;

        } catch (Exception e) {
            log.error("Failed to retrieve card details from website [link={}]", link, e);
            throw new RuntimeException("Could not retrieve card details");
        }

    }

    @Transactional
    public MtgHistory saveCardPrice(MtgHistory mtgHistory) {

        Optional<MtgCard> optionalMtgCard = mtgCardCrudDao.findByName(mtgHistory.getName());


        //if empty creates the card and links it up to the history
        optionalMtgCard.ifPresentOrElse(mtgHistory::setMtgCard, () -> {
            MtgCard entity = new MtgCard();
            entity.setName(mtgHistory.getName());
            mtgHistory.setMtgCard(mtgCardCrudDao.save(entity));
        });


        MtgHistory card = mtgHistoryCrudDao.save(mtgHistory);
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
