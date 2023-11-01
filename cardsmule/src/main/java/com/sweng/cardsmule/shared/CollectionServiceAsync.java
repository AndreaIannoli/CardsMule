package com.sweng.cardsmule.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.Grade;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.OwnedCardFetched;
import com.sweng.cardsmule.shared.models.WishedCard;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;

public interface CollectionServiceAsync {
    void addCollection(String token, String collectionName, AsyncCallback<Boolean> callback);

    void removeDeck(String token, String deckName, AsyncCallback<Boolean> callback);

    void addOwnedCardToCollection(String token, CardsmuleGame game, String collectionName, int cardId, Grade grade, String description, AsyncCallback<Boolean> callback);

    void removeOwnedCardFromCollection(String token, String collectionName, OwnedCard ownedCard, AsyncCallback<List<CollectionVariationPayload>> callback);

    void editOwnedCard(String token, String collectionName, OwnedCard ownedCard, AsyncCallback<List<CollectionVariationPayload>> callback);

    void getUserCollectionNames(String token, AsyncCallback<List<String>> callback);

    void getDeck(String token, String deckName, AsyncCallback<List<OwnedCardFetched>> callback);

    void getUserCollection(String email, AsyncCallback<List<OwnedCardFetched>> callback);

    void getOwnedCardsByCardId(int cardId, AsyncCallback<List<OwnedCard>> callback);

    void getWishedCardsByCardId(int cardId, AsyncCallback<List<WishedCard>> callback);

    void addOwnedCardsToDeck(String token, String deckName, List<OwnedCard> ownedCards, AsyncCallback<List<OwnedCardFetched>> callback);
    
    void isOwnerOfACard(String token, int cardId, AsyncCallback<Boolean> callback);
}
