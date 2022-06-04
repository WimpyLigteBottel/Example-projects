package com.wimpy.core;

import com.wimpy.dao.MtgCardCrudDao;
import com.wimpy.dao.MtgHistoryCrudDao;
import com.wimpy.dao.entity.MtgCard;
import com.wimpy.dao.entity.MtgHistory;
import com.wimpy.rest.v1.model.CardHistoryModel;
import com.wimpy.rest.v1.model.CardHistoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MtgHistoryManager {

    private final MtgHistoryCrudDao mtgHistoryCrudDao;
    private final MtgCardCrudDao mtgCardCrudDao;

    @Autowired
    public MtgHistoryManager(MtgHistoryCrudDao mtgHistoryCrudDao, MtgCardCrudDao mtgCardCrudDao) {
        this.mtgHistoryCrudDao = mtgHistoryCrudDao;
        this.mtgCardCrudDao = mtgCardCrudDao;
    }


    public Optional<CardHistoryResponse> retrieveHistory(String cardName) {

        Optional<MtgCard> cardOptional = mtgCardCrudDao.findByName(cardName);

        if (cardOptional.isEmpty()) {
            return Optional.empty();
        }
        List<MtgHistory> histories = mtgHistoryCrudDao.findByMtgCard(cardOptional.get());


        CardHistoryResponse cardHistoryResponse = new CardHistoryResponse();
        cardHistoryResponse.setCardName(cardName);
        cardHistoryResponse.setCards(histories.stream()
                .map(mtgHistory -> new CardHistoryModel(mtgHistory.getPrice(), mtgHistory.getCreated()))
                .collect(Collectors.toList()));


        return Optional.of(cardHistoryResponse);
    }


}
