package com.sweng.cardsmule.client.views;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sweng.cardsmule.shared.CollectionVariationPayload;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.OwnedCardFetched;
import com.sweng.cardsmule.client.handlers.HandleCustomDeck;
import com.sweng.cardsmule.client.handlers.HandleDeck;
import com.sweng.cardsmule.client.handlers.HandleNavBar;
import com.sweng.cardsmule.client.handlers.HandleOwnedCardEdit;
import com.sweng.cardsmule.client.handlers.HandleOwnedCardSelection;
import com.sweng.cardsmule.client.place.DecksManagerPlace;
import com.sweng.cardsmule.client.place.HomePlace;
import com.sweng.cardsmule.client.place.PreAuthenticationPlace;
import com.sweng.cardsmule.client.place.TradesPlace;
import com.sweng.cardsmule.client.views.DecksManagerView;
import com.sweng.cardsmule.client.widgets.DeckManagerWidget;

public class DecksManagerViewImpl extends Composite implements DecksManagerView, HandleDeck, HandleCustomDeck, HandleOwnedCardEdit, HandleOwnedCardSelection, HandleNavBar {
    private static final DecksManagerViewImpl.DecksManagerViewImplUIBinder uiBinder = GWT.create(DecksManagerViewImpl.DecksManagerViewImplUIBinder.class);
    private static final String DEFAULT_CUSTOM_DECK_TEXT = "Write here your custom deck name";
    Presenter presenter;
    @UiField
    HTMLPanel collectionContainer;
    @UiField
    HTMLPanel decksContainer;
    @UiField
    HeadingElement newDeckName;
    DeckManagerWidget ownedDeck;
    
    interface DecksManagerViewImplUIBinder extends UiBinder<Widget, DecksManagerViewImpl> {
    }
    
    public DecksManagerViewImpl() {
    	initWidget(uiBinder.createAndBindUi(this));
    }

	@Override
	public void displayAlert(String message) {
		Window.alert(message);
	}

	@Override
	public void replaceData(List<CollectionVariationPayload> data) {
        Map<String, CollectionVariationPayload> lookup = data.stream()
                .collect(Collectors.toMap(CollectionVariationPayload::getCollectionName, Function.identity()));
        decksContainer.forEach(w -> {
        	DeckManagerWidget deckManagerWidget = ((DeckManagerWidget) w);
            CollectionVariationPayload modifiedDeck = lookup.get(deckManagerWidget.getDeckName());
            if (modifiedDeck != null) deckManagerWidget.setData(modifiedDeck.getOwnedCards(), null);
        });
        collectionContainer.forEach(w -> {
        	DeckManagerWidget deckManagerWidget = ((DeckManagerWidget) w);
            CollectionVariationPayload modifiedDeck = lookup.get(deckManagerWidget.getDeckName());
            if (modifiedDeck != null) deckManagerWidget.setData(modifiedDeck.getOwnedCards(), null);
        });
		
	}
	
    private DeckManagerWidget createDeck(String deckName) {
        if (deckName.equals("Owned")) {
            return new DeckManagerWidget(this, null, this, this, deckName);
        } else if (deckName.equals("Wished")) {
            return new DeckManagerWidget(this, null, null, this, deckName);
        } else {
            return new DeckManagerWidget(this, this, null, null, deckName);
        }
    }

	@Override
	public void displayAddedDeck(String deckName) {
        decksContainer.add(createDeck(deckName));
        newDeckName.setInnerText(DEFAULT_CUSTOM_DECK_TEXT);
        changeSelection();
	}

	@Override
	public void setPresenter(DecksManagerView.Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setData(List<String> data) {
        for (String deckName : data) {
            boolean isOwned = deckName.equals("Owned");
            DeckManagerWidget deck = createDeck(deckName);
            if (isOwned) ownedDeck = deck;
            if(deckName.equals("Owned") || deckName.equals("Wished")) {
            	collectionContainer.add(deck);
            } else {
            	decksContainer.add(deck);
            }
            
        }
		
	}

	@Override
	public void resetData() {
		collectionContainer.clear();
		decksContainer.clear();
	}

	@Override
	public void changeSelection() {
        for (Widget w : decksContainer)
            ((DeckManagerWidget) w).setAddButtonEnabled(ownedDeck.getDeckSelectedCards() != null && !ownedDeck.getDeckSelectedCards().isEmpty());
	}

	@Override
	public void onConfirmCardEdit(String deckName, OwnedCard editedOwnedCard) {
		presenter.updateOwnedCard(deckName, editedOwnedCard);
	}

	@Override
	public void onClickRemoveCustomDeck(String deck, Consumer<Boolean> isRemoved) {
		presenter.deleteDeck(deck, isRemoved);
	}

	@Override
	public void onClickAddOwnedCardsToCustomDeck(String deckName,
			Consumer<List<OwnedCardFetched>> updatedDeck) {
		presenter.addOwnedCardsToDeck(deckName, ownedDeck.getDeckSelectedCards(), updatedDeck);
	}

	@Override
	public void onShowDeck(String deckName, BiConsumer<List<OwnedCardFetched>, String> setDeckData) {
		presenter.fetchUserDeck(deckName, setDeckData);
	}

	@Override
	public void onRemoveOwnedCard(String deckName, OwnedCard ownedCard) {
		presenter.removeOwnedCardFromDeck(deckName, ownedCard);
	}
	
	 @UiHandler(value = "newDeckButton")
    public void onClickCustomDeckAdd(ClickEvent e) {
        presenter.createDeck(newDeckName.getInnerText());
    }
 
	@Override
	public void onClickLogout() {
		presenter.goTo(new PreAuthenticationPlace());	
	}

	@Override
	public void onClickHome() {
		presenter.goTo(new HomePlace());		
	}

	@Override
	public void onClickDeck() {
		presenter.goTo(new DecksManagerPlace());
	}
	@Override
	public void onClickTrades() {
		presenter.goTo(new TradesPlace());
	}
}
