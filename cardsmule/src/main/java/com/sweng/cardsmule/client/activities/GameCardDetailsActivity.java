package com.sweng.cardsmule.client.activities;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mapdb.Serializer;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.place.GameCardDetailsPlace;
import com.sweng.cardsmule.client.place.PreAuthenticationPlace;
import com.sweng.cardsmule.client.views.GameCardDetailsView;
import com.sweng.cardsmule.server.gsonserializer.GsonSerializer;
import com.sweng.cardsmule.shared.AuthenticationServiceAsync;
import com.sweng.cardsmule.shared.CardServiceAsync;
import com.sweng.cardsmule.shared.CollectionService;
import com.sweng.cardsmule.shared.CollectionServiceAsync;
import com.sweng.cardsmule.shared.models.Account;
import com.sweng.cardsmule.shared.models.Collection;
import com.sweng.cardsmule.shared.models.Grade;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.SwengCard;
import com.sweng.cardsmule.shared.models.WishedCard;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;
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
        
        fetchCollectionCards();
        fetchWishedCollectionCards();
        isOwnerCheck();
    }
    
    public void isOwnerCheck() { 
    	collectionService.isOwnerOfACard(user.getToken(), place.getIdCard(), new BaseAsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				view.createUserWidgets(user.isLoggedIn(), result);
			}
    	});
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
    
    @Override
    public void update() {
        isOwnerCheck();
        fetchCollectionCards();
        fetchWishedCollectionCards();
    }
    
    private List<OwnedCard> filterYourOwnedCollectionCards(List<? extends OwnedCard> ownedCards) {
        return ownedCards.stream().filter(ownedCard -> !ownedCard.getUserEmail().equals(user.getEmail())).collect(Collectors.toList());
    }
    
    
    private void fetchCollectionCards() {
        collectionService.getOwnedCardsByCardId(place.getIdCard(), new BaseAsyncCallback<List<OwnedCard>>() {
            @Override
            public void onSuccess(List<OwnedCard> result) {
                view.setOwnList(filterYourOwnedCollectionCards(result));
            }
        });
    }

    private void fetchWishedCollectionCards() {    	
        	collectionService.getWishedCardsByCardId(place.getIdCard(), new BaseAsyncCallback<List<WishedCard>>() {
                @Override
                public void onSuccess(List<WishedCard> result) {                	
                		view.setWishList(result);
                }
            });
    }
    
    public void logout() {
    	user.resetToken();
    	RootPanel.get("nav").clear();
    	//goTo(new PreAuthenticationPlace());
    	
    }

	@Override
	public void addCardToDeck(String deckName, String grade, String description) {
		// TODO Auto-generated method stub
		collectionService.addOwnedCardToCollection(
                user.getToken(),
                place.getGame(),
                deckName,
                place.getIdCard(),
                Grade.getGrade(Integer.parseInt(grade)),
                description,
                new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof AuthenticationException) {
                            view.displayAlert(((AuthenticationException) caught).getExceptionText());
                        } else if (caught instanceof InputException) {
                            view.displayAlert(((InputException) caught).getExceptionText());
                        } else {
                            view.displayAlert("Internal server error");
                        }
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            view.displayAlert("Success! Card added to " + view.getDeckSelected() + " deck");
                            view.hideModal();
                        } else {
                            view.displayAlert("Deck not found");
                        }
                    }
                }
        );
	}
}
