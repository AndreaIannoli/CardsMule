package com.sweng.cardsmule.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.Grade;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.OwnedCardFetched;
import com.sweng.cardsmule.shared.models.WishedCard;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;
import com.sweng.cardsmule.shared.throwables.GeneralException;
import com.sweng.cardsmule.shared.throwables.InputException;

@RemoteServiceRelativePath("collections")
public interface CollectionService extends RemoteService {
    boolean addCollection(String token, String collectionName) throws AuthenticationException;

    boolean removeDeck(String token, String deckName) throws AuthenticationException;

    boolean addOwnedCardToCollection(String token, CardsmuleGame game, String collectionName, int cardId, Grade grade, String description) throws GeneralException;

    List<CollectionVariationPayload> removeOwnedCardFromCollection(String token, String collectionName, OwnedCard ownedCard) throws GeneralException;

    List<CollectionVariationPayload> editOwnedCard(String token, String collectionName, OwnedCard ownedCard) throws GeneralException;

    List<String> getUserCollectionNames(String token) throws AuthenticationException;

    List<OwnedCardFetched> getDeck(String token, String deckName) throws AuthenticationException;

    List<OwnedCardFetched> getUserCollection(String email) throws AuthenticationException;

    List<OwnedCard> getOwnedCardsByCardId(int cardId) throws InputException;

    List<WishedCard> getWishedCardsByCardId(int cardId) throws InputException;

    List<OwnedCardFetched> addOwnedCardsToDeck(String token, String deckName, List<OwnedCard> ownedCards) throws GeneralException;
    
}
