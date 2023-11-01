package com.sweng.cardsmule.client.handlers;

import com.sweng.cardsmule.shared.models.OwnedCard;

public interface HandleOwnedCardEdit {
    void onConfirmCardEdit(String deckName, OwnedCard editedOwnedCard);
}
