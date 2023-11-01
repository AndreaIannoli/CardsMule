package com.sweng.cardsmule.client.views;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.sweng.cardsmule.shared.CollectionVariationPayload;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.OwnedCardFetched;

public interface DecksManagerView extends IsWidget{
    void displayAlert(String message);

    void replaceData(List<CollectionVariationPayload> data);

    void displayAddedDeck(String deckName);

    void setPresenter(Presenter presenter);
	
    void setData(List<String> data);

    void resetData();

    interface Presenter {
    	void goTo(Place place);
    	
        void createDeck(String deckName);

        void deleteDeck(String deckName, Consumer<Boolean> isRemoved);
        
        void fetchUserDeck(String deckName, BiConsumer<List<OwnedCardFetched>, String> setDeckData);

        void updateOwnedCard(String deckName, OwnedCard editedPcard);

        void removeOwnedCardFromDeck(String deckName, OwnedCard pCard);

        void addOwnedCardsToDeck(String deckName, List<OwnedCard> pCards, Consumer<List<OwnedCardFetched>> updateCustomDeck);
    }
}
