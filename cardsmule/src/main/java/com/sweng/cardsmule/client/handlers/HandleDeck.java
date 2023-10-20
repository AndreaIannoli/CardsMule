package com.sweng.cardsmule.client.handlers;


import java.util.List;
import java.util.function.BiConsumer;

import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.OwnedCardFetched;

public interface HandleDeck {
    void onShowDeck(String deckName, BiConsumer<List<OwnedCardFetched>, String> setDeckData);

    void onRemoveOwnedCard(String deckName, OwnedCard pCard);
}
