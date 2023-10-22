package com.sweng.cardsmule.client.widgets;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sweng.cardsmule.client.handlers.HandleCustomDeck;
import com.sweng.cardsmule.client.handlers.HandleDeck;
import com.sweng.cardsmule.client.handlers.HandleOwnedCardEdit;
import com.sweng.cardsmule.client.handlers.HandleOwnedCardRemove;
import com.sweng.cardsmule.client.handlers.HandleOwnedCardSelection;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.OwnedCardFetched;

public class DeckManagerWidget extends Composite implements HandleOwnedCardSelection, HandleOwnedCardRemove, HandleOwnedCardEdit {
	private static final DeckManagerUIBinder uiBinder = GWT.create(DeckManagerUIBinder.class);
    @UiField
    HeadingElement deckName;
    @UiField
    HTMLPanel cards;
    @UiField
    Button showButton;
    Button addButton;
    boolean isVisible;
    List<OwnedCard> deckSelectedCards;
    HandleDeck deckHandler;
    HandleOwnedCardSelection cardSelectionHandler;
    HandleOwnedCardEdit cardEditHandler;
    @UiField
    HTMLPanel actions;
    
    interface DeckManagerUIBinder extends UiBinder<Widget, DeckManagerWidget> {
    }
    
    @UiConstructor
    public DeckManagerWidget(HandleDeck deckHandler, HandleCustomDeck customDeckHandler, HandleOwnedCardSelection cardSelectionHandler,
                      HandleOwnedCardEdit cardEditHandler, String name) {
        this.deckHandler = deckHandler;
        this.cardSelectionHandler = cardSelectionHandler;
        this.cardEditHandler = cardEditHandler;
        initWidget(uiBinder.createAndBindUi(this));
        setDeckName(name);
        isVisible = (deckHandler == null);
        showButton.setVisible(!isVisible);
        cards.setVisible(isVisible);

        if (!isVisible) {
            showButton.addClickHandler(e -> {
                if (isVisible = !isVisible) {
                    deckHandler.onShowDeck(name, this::setData);
                }
                cards.setVisible(isVisible);
            });
        }

        if (customDeckHandler != null) {
            addButton = new Button("&#43;", (ClickHandler) e -> {
                customDeckHandler.onClickAddOwnedCardsToCustomDeck(deckName.getInnerText(),
                        pCards -> setData(pCards, null));
            });
            Button removeButton = new Button("x", (ClickHandler) e -> {
                if (Window.confirm("Are you sure you want to remove this deck?"))
                    customDeckHandler.onClickRemoveCustomDeck(deckName.getInnerText(), (Boolean isRemoved) -> {
                        if (isRemoved) {
                            removeFromParent();
                        } else {
                            Window.alert("Deck cannot be deleted. It may be a default deck or does not exist.");
                        }
                    });
            });
            addButton.setStyleName("deckButton");
            addButton.setEnabled(false);
            removeButton.setStyleName("deckButton");
            actions.add(addButton);
            actions.add(removeButton);
        }
    }
    
    public void setData(List<OwnedCardFetched> data, String selectedCardId) {
        cards.clear();
        for (OwnedCardFetched ownedCard : data) {
        	OwnedCardWidget ownedCardWidget = createOwnedCardWidget(ownedCard);
            if (ownedCard.getId().equals(selectedCardId)) {
            	ownedCardWidget.setSelected();
            }
            cards.add(ownedCardWidget);
        }
        changeSelection();
    }

	@Override
	public void onClickDeleteButton(OwnedCard ownedCard) {
        if (deckHandler != null) {
            deckHandler.onRemoveOwnedCard(deckName.getInnerText(), ownedCard);
        }
	}

	@Override
	public void changeSelection() {
        if (cardSelectionHandler != null) {
            setDeckSelectedCards();
            cardSelectionHandler.changeSelection();
        }
	}
	
    public List<OwnedCard> getDeckSelectedCards() {
        return deckSelectedCards;
    }
    
    private OwnedCardWidget createOwnedCardWidget(OwnedCardFetched ownedCard) {
        return new OwnedCardWidget(ownedCard,
                cardSelectionHandler != null ? this : null,
                deckHandler != null ? this : null,
                cardEditHandler != null ? this : null
        );
    }

    private void setDeckSelectedCards() {
        List<OwnedCard> selectedCards = new LinkedList<>();
        cards.forEach((widget) -> {
        	OwnedCardWidget ownedCardWidget = (OwnedCardWidget) widget;
            if (ownedCardWidget.getSelected())
                selectedCards.add(ownedCardWidget.getOwnedCard());
        });
        deckSelectedCards = selectedCards;
    }

	@Override
	public void onConfirmCardEdit(String deckName, OwnedCard editedOwnedCard) {
		cardEditHandler.onConfirmCardEdit(this.deckName.getInnerText(), editedOwnedCard);
	}
	
    public String getDeckName() {
        return deckName.getInnerText();
    }

    public void setDeckName(String name) {
        deckName.setInnerText(name);
    }

    public void setAddButtonEnabled(boolean isEnabled) {
        if (addButton != null) {
            addButton.setEnabled(isEnabled);
        }
    }
}
