package com.sweng.cardsmule.client.activities;

import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.views.TradeView;
import com.sweng.cardsmule.shared.TradeCardsServiceAsync;
import com.sweng.cardsmule.shared.models.Offer;
import com.sweng.cardsmule.client.BaseAsyncCallback;

public class TradesActivity extends AbstractActivity implements TradeView.Presenter {
	private final TradeView view;
    private final TradeCardsServiceAsync rpcService;
    private final User user;
    private final PlaceController placeController;
    
    public TradesActivity(TradeView view, TradeCardsServiceAsync rpcService, User user, PlaceController placeController) {
        this.view = view;
        this.rpcService = rpcService;
        this.user = user;
        this.placeController = placeController;
    }

	@Override
	public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
		view.setPresenter(this);
        acceptsOneWidget.setWidget(view.asWidget());
        fetchSentOffer();
        fetchReceivedOffer();
		
	}
	private void fetchSentOffer() {
        rpcService.getOfferListSent(user.getToken(), new BaseAsyncCallback<List<Offer>>() {
            @Override
            public void onSuccess(List<Offer> offers) {
                view.setFromYouOfferList(offers);
            }
        });
    }
	private void fetchReceivedOffer() {
        rpcService.getOfferListReceived(user.getToken(), new BaseAsyncCallback<List<Offer>>() {
            @Override
            public void onSuccess(List<Offer> proposals) {
                view.setToYouOfferList(proposals);
            }
        });
    }

	@Override
	public void goTo(Place place) {
        placeController.goTo(place);
		
	}
	@Override
    public void onStop() {
        view.resetOfferLists();
    }
}
