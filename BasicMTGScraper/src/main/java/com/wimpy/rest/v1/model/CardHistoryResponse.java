package com.wimpy.rest.v1.model;

import java.util.List;

public class CardHistoryResponse {

    private String cardName;
    private List<CardHistoryModel> cards;

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public List<CardHistoryModel> getCards() {
        return cards;
    }

    public void setCards(List<CardHistoryModel> cards) {
        this.cards = cards;
    }
}
