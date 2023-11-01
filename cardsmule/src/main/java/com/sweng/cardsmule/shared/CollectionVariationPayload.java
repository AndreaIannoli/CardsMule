package com.sweng.cardsmule.shared;

import java.io.Serializable;
import java.util.List;

import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.OwnedCardFetched;

public class CollectionVariationPayload implements Serializable {
    private static final long serialVersionUID = 546524398062390416L;
	String collectionName;
    List<OwnedCardFetched> ownedCards;

    public CollectionVariationPayload(String collectionName, List<OwnedCardFetched> ownedCards) {
        this.collectionName = collectionName;
        this.ownedCards = ownedCards;
    }

    public CollectionVariationPayload() {
    }

    public String getCollectionName() {
        return collectionName;
    }

    public List<OwnedCardFetched> getOwnedCards() {
        return ownedCards;
    }
}
