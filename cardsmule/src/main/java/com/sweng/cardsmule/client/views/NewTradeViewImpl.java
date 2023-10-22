package com.sweng.cardsmule.client.views;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;


import com.google.gwt.user.client.ui.Composite;
import com.sweng.cardsmule.client.handlers.HandleOwnedCardSelection;
import com.sweng.cardsmule.shared.models.OwnedCardFetched;
import com.sweng.cardsmule.client.widgets.DeckManagerWidget;

import java.util.ArrayList;
import java.util.List;


public class NewTradeViewImpl extends Composite implements NewTradeView, HandleOwnedCardSelection {

	
    private static final NewTradeViewImpl.NewTradeViewImplUIBinder uiBinder = GWT.create(NewTradeViewImpl.NewTradeViewImplUIBinder.class);
    NewTradePresenter newTradePresenter;
    TradePresenter tradePresenter;
    @UiField
    HeadingElement pageTitle;
    @UiField
    SimplePanel senderDeckContainer;
    @UiField
    SimplePanel receiverDeckContainer;
    @UiField
    Button cancelButton;
    @UiField
    Button acceptButton;
    List<HandlerRegistration> handlers;
    
    
    DeckManagerWidget senderDeck;
    DeckManagerWidget receiverDeck;

    public NewTradeViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        handlers = new ArrayList<>();
    }
    
	@Override
	public void changeSelection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setData(String title, String cancelTextButton, String acceptTextButton) {
	       pageTitle.setInnerText(title);
	       cancelButton.setText(cancelTextButton);
	       acceptButton.setText(acceptTextButton);		
	}
	
	  private DeckManagerWidget createDeck(boolean clickable, String deckName) {
	        return new DeckManagerWidget(null, null, clickable ? this : null, null, deckName);
	    }

	@Override
	public void setSenderDeck(boolean isClickable, List<OwnedCardFetched> ownedCards, 
			String selectedCardId,String senderEmail) {
		
		senderDeck = createDeck(isClickable, "Offer sender: " + senderEmail);
        senderDeck.setData(ownedCards, selectedCardId);
        senderDeckContainer.add(senderDeck);		
	}

	@Override
	public void setReceiverDeck(boolean isClickable, List<OwnedCardFetched> ownedCards, String selectedCardId,
			String receiverEmail) {
		receiverDeck = createDeck(isClickable, "Offer receiver: " + receiverEmail);
        receiverDeck.setData(ownedCards, selectedCardId);
        receiverDeckContainer.add(receiverDeck);		
	}

	@Override
	public void setPresenter(NewTradePresenter newTradePresenter) {
		this.tradePresenter = null;
        this.newTradePresenter = newTradePresenter;		
	}

	@Override
	public void setPresenter(TradePresenter tradePresenter) {
		this.newTradePresenter = null;
        this.tradePresenter = tradePresenter;		
	}

	@Override
	public void setAcceptButtonEnabled(boolean enabled) {
		acceptButton.setEnabled(enabled);		
	}

	@Override
	public void showAlert(String message) {
		Window.alert(message);		
	}

	@Override
	public void setNewTradeButtons() {
		 handlers.add(cancelButton.addClickHandler(e -> History.back()));
	        handlers.add(acceptButton.addClickHandler(e -> {
	            if (Window.confirm(confirmMessage("send")))
	                newTradePresenter.createOffer(senderDeck.getDeckSelectedCards(), receiverDeck.getDeckSelectedCards());
	        }));		
	}

	@Override
	public void setTradeButtons() {
		 handlers.add(cancelButton.addClickHandler(e -> {
	            if (Window.confirm(confirmMessage("cancel"))) {
	                tradePresenter.refuseOrWithdrawOffer();
	                History.back();
	            }
	        }));
	        handlers.add(acceptButton.addClickHandler(e -> {
	            if (Window.confirm(confirmMessage("accept"))) {
	                tradePresenter.acceptTradeOffer();
	                History.back();
	            }
	        }));		
	}

	@Override
	public void resetHandlers() {
		handlers.forEach(HandlerRegistration::removeHandler);
        senderDeckContainer.clear();
        receiverDeckContainer.clear();		
	}
	
    private String confirmMessage(String str) {
        return "Do you want to " + str + " this exchange offer?";
    }
   
	
	interface NewTradeViewImplUIBinder extends UiBinder<Widget, NewTradeViewImpl> {
    }
	
	
}

