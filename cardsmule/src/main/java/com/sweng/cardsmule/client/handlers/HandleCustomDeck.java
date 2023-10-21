package com.sweng.cardsmule.client.handlers;

import com.sweng.cardsmule.shared.models.OwnedCardFetched;

import java.util.List;
import java.util.function.Consumer;

public interface HandleCustomDeck {
    void onClickRemoveCustomDeck(String deck, Consumer<Boolean> isRemoved);

    void onClickAddOwnedCardsToCustomDeck(String deckName, Consumer<List<OwnedCardFetched>> getSelectedPhysicalCards);
}
