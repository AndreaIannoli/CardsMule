package com.sweng.cardsmule.client.handlers;

import com.sweng.cardsmule.shared.models.OwnedCardFetched;

public interface HandleOwnedCardEdit {
    void onConfirmCardEdit(String deckName, OwnedCardFetched editedPcard);
}
