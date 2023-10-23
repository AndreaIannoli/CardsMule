package com.sweng.cardsmule.client.activities;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.place.TradePlace;
import com.sweng.cardsmule.client.views.NewTradeView;
import com.sweng.cardsmule.shared.OfferPayload;
import com.sweng.cardsmule.shared.TradeCardsServiceAsync;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;
import com.sweng.cardsmule.shared.throwables.InputException;
import com.sweng.cardsmule.shared.throwables.OfferNotFoundException;

public class TradeActivity extends AbstractActivity implements NewTradeView.TradePresenter {
	 	private final TradePlace place;
	    private final NewTradeView view;
	    private final TradeCardsServiceAsync tradeService;
	    private final User user;
	    private final PlaceController placeController;
	    public TradeActivity(TradePlace place, NewTradeView view, TradeCardsServiceAsync tradeService, User user, PlaceController placeController) {
	        this.place = place;
	        this.view = view;
	        this.tradeService = tradeService;
	        this.user = user;
	        this.placeController = placeController;
	    }

	@Override
	public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
		view.setPresenter(this);
        acceptsOneWidget.setWidget(view.asWidget());
        view.setTradeButtons();
        view.setData("Trade proposal Page", "Cancel Proposal", "Accept Proposal");
        fetchOfferData();
		
	}
	private void fetchOfferData() {
		tradeService.getOffer(user.getToken(), place.getOfferId(), new AsyncCallback<OfferPayload>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof AuthenticationException) {
                    view.showAlert(((AuthenticationException) caught).getExceptionText());
                } else if (caught instanceof InputException) {
                    view.showAlert(((InputException) caught).getExceptionText());
                } else if (caught instanceof OfferNotFoundException) {
                    view.showAlert(((OfferNotFoundException) caught).getExceptionText());
                } else {
                    view.showAlert("Internal server error: " + caught.getMessage());
                }
            }

            @Override
            public void onSuccess(OfferPayload payload) {
                view.setSenderDeck(false, payload.getSenderCards(), null, payload.getSenderEmail());
                view.setReceiverDeck(false, payload.getReceiverCards(), null, payload.getReceiverEmail());
                view.setAcceptButtonEnabled(!payload.getSenderEmail().equals(user.getEmail()));
            }
        });
    }

	@Override
	public void acceptTradeOffer() {
		tradeService.acceptOffer(user.getToken(), place.getOfferId(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof AuthenticationException)
                    view.showAlert(((AuthenticationException) caught).getExceptionText());
                else if (caught instanceof OfferNotFoundException)
                    view.showAlert(((OfferNotFoundException) caught).getExceptionText());
                else
                    view.showAlert("Internal server error: " + caught.getMessage());
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    view.showAlert("Successfully accepted offer: " + place.getOfferId());
                } else
                    view.showAlert("It seems this offer doesn't exist anymore");
            }
        });
		
	}

	@Override
	public void refuseOrWithdrawOffer() {
		tradeService.refuseOrWithdrawOffer(user.getToken(), place.getOfferId(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof AuthenticationException)
                    view.showAlert(((AuthenticationException) caught).getExceptionText());
                else if (caught instanceof OfferNotFoundException)
                    view.showAlert(((OfferNotFoundException) caught).getExceptionText());
                else
                    view.showAlert("Internal server error: " + caught.getMessage());
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    view.showAlert("Successfully remove offer: " + place.getOfferId());
                } else
                    view.showAlert("It seems this offer doesn't exist anymore");
            }
        });
		
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
		
	}
	 @Override
	    public void onStop() {
	        view.resetHandlers();
	    }

}
