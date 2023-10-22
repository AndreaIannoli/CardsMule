package com.sweng.cardsmule.client.activities;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.sweng.cardsmule.client.BaseAsyncCallback;
import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.place.HomePlace;
import com.sweng.cardsmule.client.place.NewTradePlace;
import com.sweng.cardsmule.client.views.NewTradeView;
import com.sweng.cardsmule.shared.CollectionServiceAsync;
import com.sweng.cardsmule.shared.TradeCardsServiceAsync;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.OwnedCardFetched;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;
import com.sweng.cardsmule.shared.throwables.InputException;

import java.util.List;




public class NewTradeActivity extends AbstractActivity implements NewTradeView.NewTradePresenter {
    private final NewTradePlace place;
    private final NewTradeView view;
    private final CollectionServiceAsync deckService;
    private final TradeCardsServiceAsync exchangeService;
    private final User authSubject;
    private final PlaceController placeController;

    public NewTradeActivity(NewTradePlace place, NewTradeView view, CollectionServiceAsync deckService, TradeCardsServiceAsync exchangeService,
                               User authSubject, PlaceController placeController) {
        this.place = place;
        this.view = view;
        this.deckService = deckService;
        this.exchangeService = exchangeService;
        this.authSubject = authSubject;
        this.placeController = placeController;
    }

    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        view.setPresenter(this);
        acceptsOneWidget.setWidget(view.asWidget());
        view.setNewTradeButtons();
        view.setData("New Exchange Page", "Go back", "Send proposal");
        fetchMyOwnedDeck();
        fetchUserOwnedDeck();
    }

    private void fetchMyOwnedDeck() {
        deckService.getDeck(authSubject.getToken(), "Owned", new BaseAsyncCallback<List<OwnedCardFetched>>() {
            @Override
            public void onSuccess(List<OwnedCardFetched> ownedCards) {
                view.setSenderDeck(true, ownedCards, place.getSelectedCardId(), authSubject.getEmail());
            }
        });
    }

    private void fetchUserOwnedDeck() {
        deckService.getUserCollection(place.getReceiverUserEmail(), new BaseAsyncCallback<List<OwnedCardFetched>>() {
            @Override
            public void onSuccess(List<OwnedCardFetched> ownedCards) {
                view.setReceiverDeck(true, ownedCards, place.getSelectedCardId(), place.getReceiverUserEmail());
            }
        });
    }

    @Override
    public void createOffer(List<OwnedCard> senderDeckSelectedCards, List<OwnedCard> receiverDeckSelectedCards) {
        if (senderDeckSelectedCards.isEmpty() || receiverDeckSelectedCards.isEmpty()) {
            view.showAlert("Invalid selection error:\nProvide at least one card form each deck");
        } else {
            exchangeService.addOffer(authSubject.getToken(), place.getReceiverUserEmail(), senderDeckSelectedCards, receiverDeckSelectedCards, new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable caught) {
                    if (caught instanceof AuthenticationException) {
                        view.showAlert(((AuthenticationException) caught).getExceptionText());
                    } else if (caught instanceof InputException) {
                        view.showAlert(((InputException) caught).getExceptionText());
                    } else {
                        view.showAlert("Internal server error: " + caught.getMessage());
                    }
                }

                @Override
                public void onSuccess(Boolean result) {
                    if (result) {
                        view.showAlert("Proposal successfully created");
                        placeController.goTo(new HomePlace());
                    } else {
                        view.showAlert("Something went wrong...\nThis proposal already exists");
                    }
                }
            });
        }
    }

    @Override
    public void onStop() {
        view.resetHandlers();
    }
}
