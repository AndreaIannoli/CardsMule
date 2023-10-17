package com.sweng.cardsmule.client.activities;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mapdb.Serializer;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.place.GameCardDetailsPlace;
import com.sweng.cardsmule.client.views.GameCardDetailsView;
import com.sweng.cardsmule.server.gsonserializer.GsonSerializer;
import com.sweng.cardsmule.shared.AuthenticationServiceAsync;
import com.sweng.cardsmule.shared.CardServiceAsync;
import com.sweng.cardsmule.shared.CollectionService;
import com.sweng.cardsmule.shared.CollectionServiceAsync;
import com.sweng.cardsmule.shared.models.Account;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.SwengCard;
import com.sweng.cardsmule.shared.models.WishedCard;
import com.sweng.cardsmule.shared.throwables.InputException;
import com.sweng.cardsmule.client.BaseAsyncCallback;

public class GameCardDetailsActivity extends AbstractActivity implements GameCardDetailsView.Presenter{
    private final GameCardDetailsPlace place;
    private final GameCardDetailsView view;
    private final CardServiceAsync cardService;
    private final CollectionServiceAsync collectionService;
    private final AuthenticationServiceAsync authenticationService;
    private final User user;
    private final PlaceController placeController;
	
    public GameCardDetailsActivity(GameCardDetailsView view, GameCardDetailsPlace place, CardServiceAsync cardService, CollectionServiceAsync collectionService, AuthenticationServiceAsync authenticationService, User user, PlaceController placeController) {
        this.view = view;
        this.place = place;
        this.cardService = cardService;
        this.collectionService = collectionService;
        this.authenticationService = authenticationService;
        this.user = user;
        this.placeController = placeController;
        //user.attach(this);
    }
    
    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        view.setPresenter(this);
        containerWidget.setWidget(view.asWidget());
        fetchCard();
        view.createUserWidgets(user.isLoggedIn());
        fetchOwnedPhysicalCards();
        fetchWishedPhysicalCards();
    }
    
    
    public void fetchCard() {
        try {
			cardService.getGameCard(place.getGame(), place.getIdCard(), new BaseAsyncCallback<SwengCard>() {
			    @Override
			    public void onSuccess(SwengCard result) {
			        view.setData(result);
			    }
			});
		} catch (InputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
    public void goTo(Place place) {
        placeController.goTo(place);
    }
    
    /*
    @Override
    public void update() {
        view.createUserWidgets(user.isLoggedIn());
        fetchOwnedPhysicalCards();
        fetchWishedPhysicalCards();
    }
    */
    
    private void fetchOwnedPhysicalCards() {
        collectionService.getOwnedCardsByCardId(place.getIdCard(), new BaseAsyncCallback<List<OwnedCard>>() {
            @Override
            public void onSuccess(List<OwnedCard> result) {
                view.setOwnList(result);
            }
        });
    }
    
    /*
    private List<OwnedCard> filterOwnPhysicalCards(List<? extends OwnedCard> ownedCards) {
    	authenticationService.me(user.getToken(), new BaseAsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				return ownedCards.stream().filter(ownedCard -> !ownedCard.getUserEmail().equals(result)).collect(Collectors.toList());
			}
    	});
    }
    */

    private void fetchWishedPhysicalCards() {
    	/*
        if (user.isLoggedIn()) {
        	CollectionService.getListPhysicalCardWithEmailDealing(authSubject.getToken(), place.getGame(), place.getCardId(), new BaseAsyncCallback<List<PhysicalCardWithEmailDealing>>() {
                @Override
                public void onSuccess(List<PhysicalCardWithEmailDealing> result) {
                    view.setWishedByUserList(filterOwnPhysicalCards(result));
                }
            });
        } else {
        */
        	collectionService.getWishedCardsByCardId(place.getIdCard(), new BaseAsyncCallback<List<WishedCard>>() {
                @Override
                public void onSuccess(List<WishedCard> result) {
                    view.setWishList(result);
                }
            });
        /*}*/
    }
}
